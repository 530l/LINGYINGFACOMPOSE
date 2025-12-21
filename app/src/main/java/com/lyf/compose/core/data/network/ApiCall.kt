package com.lyf.compose.core.data.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * 封装一个返回[NetworkResponse]的挂起API调用，并仅暴露实际的[T]。
 * safeApiCall / networkResultFlow（使用 Flow）：
 * 适合你要 Loading/Success/Error 状态流驱动 UI
 * 此方法集中处理：
 * - 业务代码检查（errorCode == 0）
 * - 空数据处理
 * - 将异常转换为[NetworkResult.Error]
 */
inline fun <T> safeApiCall(
    crossinline block: suspend () -> NetworkResponse<T>
): Flow<NetworkResult<T>> = flow {
    // ① 首先发射 Loading 状态
    emit(NetworkResult.Loading)
    try {
        // ② 在 IO 线程执行网络请求（因 .flowOn(Dispatchers.IO)）
        //调用挂起函数
        val response = block()
        // ③ 调用 requireData()：检查业务逻辑是否成功（如 errorCode == 0）
        //    若失败（如 code != 0），requireData() 会抛出异常
        val successInv = response.ensureSuccess()
        // ④ 发射 Success 状态
        emit(NetworkResult.Success(successInv))
    } catch (t: Throwable) {
        // ⑤ 捕获任何异常（网络异常、解析异常、业务错误等）
        emit(NetworkResult.Error(t))
    }
}.flowOn(Dispatchers.IO)// 确保 block() 在 IO 线程执行


/**
 * Flow<Result<T>> 扩展
 * Repository 结合 Flow 使用的帮助方法
 * networkResultFlow 委托给 safeApiCall
 */
inline fun <T> networkResultFlow(crossinline block: suspend () -> NetworkResponse<T>)
        : Flow<NetworkResult<T>> = safeApiCall(block)


/**
 * unwrapApiCall（不使用 flow）：
 * 适合 Repository 返回 suspend fun ...(): T，ViewModel try/catch
 */
suspend inline fun <T> unwrapApiCall(
    crossinline block: suspend () -> NetworkResponse<T>
): T = block().ensureSuccess()

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * ==============================
 * 多接口并发请求工具函数
 * ==============================
 *
 * 适用于 ViewModel 或 Repository 中需要同时发起多个独立网络请求的场景。
 * 所有请求并发执行，任一失败则整体失败，全部成功则返回组合数据。
 */

// ┌───────────────┐
// │ 二元组 (Pair) │
// └───────────────┘
suspend inline fun <A, B> safeApiCallMulti(
    crossinline callA: suspend () -> NetworkResponse<A>,
    crossinline callB: suspend () -> NetworkResponse<B>
): NetworkResult<Pair<A, B>> = coroutineScope {
    try {
        val deferredA = async { callA().ensureSuccess() }
        val deferredB = async { callB().ensureSuccess() }
        val a = deferredA.await()
        val b = deferredB.await()
        NetworkResult.Success(a to b)
    } catch (t: Throwable) {
        NetworkResult.Error(t)
    }
}

// ┌───────────────┐
// │ 三元组 (Triple) │
// └───────────────┘
suspend inline fun <A, B, C> safeApiCallMulti(
    crossinline callA: suspend () -> NetworkResponse<A>,
    crossinline callB: suspend () -> NetworkResponse<B>,
    crossinline callC: suspend () -> NetworkResponse<C>
): NetworkResult<Triple<A, B, C>> = coroutineScope {
    try {
        val a = async { callA().ensureSuccess() }
        val b = async { callB().ensureSuccess() }
        val c = async { callC().ensureSuccess() }
        NetworkResult.Success(Triple(a.await(), b.await(), c.await()))
    } catch (t: Throwable) {
        NetworkResult.Error(t)
    }
}

// ┌───────────────────────┐
// │ 四元组及以上 → 使用 List │
// └───────────────────────┘
/**
 * 并发执行多个相同类型的 API 调用（如批量获取用户信息）
 */
suspend inline fun <T> safeApiCallMultiList(
    calls: List<suspend () -> NetworkResponse<T>>
): NetworkResult<List<T>> = coroutineScope {
    try {
        val deferredList = calls.map { call ->
            async { call().ensureSuccess() }
        }
        val results = deferredList.awaitAll()
        NetworkResult.Success(results)
    } catch (t: Throwable) {
        NetworkResult.Error(t)
    }
}

/**
 * 并发执行多个不同类型的 API 调用（通用可变参数版本）
 * 注意：返回顺序与传入顺序一致
 */
suspend fun safeApiCallMultiVararg(
    vararg calls: suspend CoroutineScope.() -> NetworkResponse<*>
): NetworkResult<List<Any?>> = coroutineScope {
    try {
        val deferredList = calls.map { call ->
            async { call(this).ensureSuccess() }
        }
        val results = deferredList.awaitAll()
        NetworkResult.Success(results)
    } catch (t: Throwable) {
        NetworkResult.Error(t)
    }
}

// ┌───────────────────────────────┐
// │ 辅助：将 suspend NetworkResult<T> 包装为 Flow<NetworkResult<T>>（带 Loading）│
// └───────────────────────────────┘
/**
 * 将一个返回 [NetworkResult<T>] 的 suspend 函数包装成带 Loading 状态的 Flow
 *
 * 示例：
 *   fun requestHomeData(): Flow<NetworkResult<HomeData>> =
 *       networkResultFlowFromSuspend { fetchHomeData() }
 */
inline fun <T> networkResultFlowFromSuspend(
    crossinline block: suspend () -> NetworkResult<T>
): Flow<NetworkResult<T>> = flow {
    emit(NetworkResult.Loading)
    emit(block())
}.flowOn(Dispatchers.IO)

// ┌───────────────────────┐
// │ 辅助扩展：转换 Success 数据类型 │
// └───────────────────────┘
inline fun <T, R> NetworkResult<T>.mapSuccess(crossinline transform: (T) -> R): NetworkResult<R> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.Success(transform(data))
        is NetworkResult.Error -> this
        is NetworkResult.Loading -> error("mapSuccess should not be called on Loading state")
    }
}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////