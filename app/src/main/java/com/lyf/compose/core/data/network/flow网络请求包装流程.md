```html
📌 整体结构说明
safeApiCall + NetworkResult + launchCollect）非常适合 单个接口请求驱动 UI 状态。
目标：从网络获取 Banner 列表，并在 UI 中展示。
技术栈：
Kotlin 协程 + Flow
自定义状态封装：NetworkResult
<T>（Loading / Success / Error）
    网络响应封装：NetworkResponse
    <T>
        工具函数：safeApiCall / networkResultFlow
        扩展函数：launchCollect
```

### 🔁 完整执行流程（带注释）

✅ 步骤 1：ViewModel 触发请求

```kotlin
fun requestBanner() {
    repository.requestBanner().launchCollect(
        scope = viewModelScope,
        onLoading = { /* 可选：显示加载中 */ },
        onSuccess = { bannerList ->
            _uiState.update { it.copy(banner = bannerList) }
        },
        onError = { e -> /* 可选：记录或提示错误 */ }
    )
}

```

💡 作用：启动一个协程，在 viewModelScope 中收集来自 Repository 的 Flow<NetworkResult<List<Banner>>>。
🔹 关键：使用 launchCollect 自动分发 Loading/Success/Error。

✅ 步骤 2：Repository 返回Flow状态流

````kotlin
fun requestBanner(): Flow<NetworkResult<List<Banner>>> = networkResultFlow { atmobApi.getBanner() }
````

💡 作用：调用 networkResultFlow 构建一个能发射 Loading → Success/Error 状态的 Flow。
🔹 调用 networkResultFlow，传入 suspend API 调用 atmobApi.getBanner()。

✅ 步骤 3：networkResultFlow 委托给 safeApiCall

```kotlin
inline fun <T> networkResultFlow(
    crossinline block: suspend () -> NetworkResponse<T>
): Flow<NetworkResult<T>> = safeApiCall(block)

```

🔹 本质是 safeApiCall 的别名，语义更清晰。

✅ 步骤 4：safeApiCall 构建带状态的 Flow（核心逻辑）

```kotlin
inline fun <T> safeApiCall(
    crossinline block: suspend () -> NetworkResponse<T>
): Flow<NetworkResult<T>> = flow {
    // ① 首先发射 Loading 状态
    emit(NetworkResult.Loading)

    try {
        // ② 在 IO 线程执行网络请求（因 .flowOn(Dispatchers.IO)）
        val response = block() // → 调用 atmobApi.getBanner()

        // ③ 调用 requireData()：检查业务逻辑是否成功（如 errorCode == 0）
        //    若失败（如 code != 0），requireData() 会抛出异常
        val data = response.requireData()

        // ④ 发射成功状态
        emit(NetworkResult.Success(data))
    } catch (t: Throwable) {
        // ⑤ 捕获任何异常（网络异常、解析异常、业务错误等）
        emit(NetworkResult.Error(t))
    }
}.flowOn(Dispatchers.IO) // 确保 block() 在 IO 线程执行

```

🔹 关键点：
先发 Loading → UI 可立即响应“正在加载”
requireData() 是业务校验的关键：通常内部会判断 errorCode == 0，否则抛出自定义异常（如 ApiException）
所有异常（包括业务失败）统一转为 NetworkResult.Error

✅ 步骤 5：API 接口执行（Retrofit）

✅ 步骤 6：launchCollect 收集状态并回调 UI

```kotlin

scope.launch {
    collectLatest { result ->
        when (result) {
            is NetworkResult.Loading -> onLoading()
            is NetworkResult.Success -> onSuccess(result.data)
            is NetworkResult.Error -> onError(result.exception)
        }
    }
}
```

🔹 collectLatest：若多次调用 requestBanner()，旧的收集会被取消，避免竞态问题。
🔹 状态分发：
Loading → 可显示骨架屏或进度条
Success → 更新 _uiState，触发 Compose 重组
Error → 显示 Toast 或错误占位符

🔄 数据流向图（精简版）

[ViewModel]
↓ 调用
requestBanner()
↓
[Repository]
↓ 返回
networkResultFlow { api.getBanner() }
↓
[safeApiCall]
→ emit(Loading)
→ block() → Retrofit → NetworkResponse
→ requireData() → 成功？→ emit(Success)
↓ 否
抛异常 → emit(Error)
↓
[launchCollect in viewModelScope]
↓ 分发
onLoading() / onSuccess() / onError()
↓
_update _uiState → Compose UI recompose_

✅ 总结优势
特性 说明
状态驱动 UI Loading/Success/Error 清晰分离
线程安全 网络在 IO，UI 回调在 Main（协程自动切换）
生命周期感知 viewModelScope 自动取消
防重复请求 collectLatest 避免旧结果干扰
统一错误处理 无论网络异常还是业务错误，都走 onError