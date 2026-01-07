package com.lyf.compose.data.network


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 网络请求结果封装
 *
 * 提供统一的三态状态表示：加载中、成功、失败。适用于 UI 状态驱动场景，
 * 与 Repository 层的 Flow 配合使用，实现响应式数据流。
 *
 * 使用示例：
 * ```kotlin
 * repository.getData()
 *     .collect { result ->
 *         when (result) {
 *             is NetworkResult.Loading -> showLoading()
 *             is NetworkResult.Success -> showData(result.data)
 *             is NetworkResult.Error -> showError(result.exception)
 *         }
 *     }
 * ```
 */
sealed interface NetworkResult<out T> {
    /** 加载中状态 */
    data object Loading : NetworkResult<Nothing>

    /** 成功状态，包含返回数据 */
    data class Success<T>(val data: T) : NetworkResult<T>

    /** 失败状态，包含异常信息 */
    data class Error(val exception: Throwable) : NetworkResult<Nothing>
}

/**
 * NetworkResult 的模式匹配扩展函数
 *
 * 提供类型安全的状态处理，避免 when 表达式的样板代码。
 *
 * @param onSuccess 成功时的回调，接收数据
 * @param onError 失败时的回调，接收异常
 * @param onLoading 加载中时的回调，默认抛出异常（防止未处理）
 *
 * 使用示例：
 * ```kotlin
 * result.call(
 *     onSuccess = { data -> println("获取到数据: $data") },
 *     onError = { e -> println("请求失败: ${e.message}") }
 * )
 * ```
 */
inline fun <T, R> NetworkResult<T>.call(
    onSuccess: (T) -> R,
    onError: (Throwable) -> R,
    onLoading: () -> R = { error("Unexpected Loading state") }
): R = when (this) {
    is NetworkResult.Loading -> onLoading()
    is NetworkResult.Success -> onSuccess(data)
    is NetworkResult.Error -> onError(exception)
}

/**
 * Flow<NetworkResult> 的自动收集扩展函数
 *
 * 在指定的协程作用域内收集 Flow，并处理各种状态。
 * 使用 collectLatest 确保多次调用时取消旧的收集，避免竞态问题。
 *
 * @param scope 协程作用域
 * @param onLoading 加载中回调
 * @param onSuccess 成功回调，接收数据
 * @param onError 失败回调，接收异常
 *
 * 使用示例：
 * ```kotlin
 * repository.login(username, password)
 *     .launchCollect(
 *         scope = viewModelScope,
 *         onLoading = { _uiState.update { it.copy(isLoading = true) } },
 *         onSuccess = { user -> _uiState.update { it.copy(user = user) } },
 *         onError = { e -> _uiState.update { it.copy(error = e.message) } }
 *     )
 * ```
 */
inline fun <T> Flow<NetworkResult<T>>.launchCollect(
    scope: CoroutineScope,
    crossinline onLoading: () -> Unit = {},
    crossinline onSuccess: (T) -> Unit,
    crossinline onError: (Throwable) -> Unit = {},
) = scope.launch {
    collectLatest { result ->
        when (result) {
            is NetworkResult.Loading -> onLoading()
            is NetworkResult.Success -> onSuccess(result.data)
            is NetworkResult.Error -> onError(result.exception)
        }
    }
}