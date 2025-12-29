package com.lyf.compose.core.data.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

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
        Timber.d("safeApiCall ：$t")
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
