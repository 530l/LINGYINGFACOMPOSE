package com.lyf.compose.core.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

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
    emit(NetworkResult.Loading)
    try {
        val response = block()
        emit(NetworkResult.Success(response.requireData()))
    } catch (t: Throwable) {
        emit(NetworkResult.Error(t))
    }
}.flowOn(Dispatchers.IO)

/**
 * unwrapApiCall（不使用 flow）：
 * 适合 Repository 返回 suspend fun ...(): T，ViewModel try/catch
 */
suspend inline fun <T> unwrapApiCall(
    crossinline block: suspend () -> NetworkResponse<T>
): T = block().requireData()

/**
 *  Flow<Result<T>> 扩展
 *
 * Repository 结合 Flow 使用的帮助方法
 */
inline fun <T> networkResultFlow(
    crossinline block: suspend () -> NetworkResponse<T>
): Flow<NetworkResult<T>> = safeApiCall(block)
