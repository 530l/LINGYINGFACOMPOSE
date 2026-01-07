package com.lyf.compose.data.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * 封装 API 调用，返回 Flow<NetworkResult<T>>
 *
 * 适用于需要 Loading/Success/Error 状态流驱动 UI 的场景。
 */
inline fun <T> safeApiCall(
    retryPolicy: RetryPolicy = RetryPolicy(maxRetries = 0),
    crossinline block: suspend () -> NetworkResponse<T>
): Flow<NetworkResult<T>> = networkResultFlow(retryPolicy, block)

/**
 * 封装 API 调用，返回 Flow<NetworkResult<T>>
 *
 * 内部实现带重试机制的请求逻辑。
 *
 * @param retryPolicy 重试策略
 */
inline fun <T> networkResultFlow(
    retryPolicy: RetryPolicy = RetryPolicy(maxRetries = 0),
    crossinline block: suspend () -> NetworkResponse<T>
): Flow<NetworkResult<T>> = flow {
    // 首先发射 Loading 状态
    emit(NetworkResult.Loading)

    // 带重试机制的 API 调用
    var lastException: Throwable? = null

    repeat(retryPolicy.maxRetries + 1) { attempt ->
        try {
            // 在 IO 线程执行网络请求
            val response = block()

            // 调用 ensureSuccess()：检查业务逻辑是否成功（如 errorCode == 0）
            // 若失败（如 code != 0），ensureSuccess() 会抛出异常
            val successInv = response.ensureSuccess()

            // 发射 Success 状态
            emit(NetworkResult.Success(successInv))
            return@flow
        } catch (e: Throwable) {
            lastException = e
            Timber.e(e, "API call failed (attempt $attempt/${retryPolicy.maxRetries})")

            // 判断是否应该重试
            if (retryPolicy.shouldRetry(e, attempt)) {
                val delay = retryPolicy.getDelayForAttempt(attempt)
                Timber.d("Retrying in ${delay}ms...")
                kotlinx.coroutines.delay(delay)
            } else {
                // 不再重试，发射 Error 状态
                emit(NetworkResult.Error(e))
                return@flow
            }
        }
    }
}.flowOn(Dispatchers.IO) // 确保 block() 在 IO 线程执行

/**
 * unwrapApiCall（不使用 flow）
 *
 * 适合 Repository 返回 suspend 函数，ViewModel 手动处理异常。
 */
suspend inline fun <T> unwrapApiCall(
    crossinline block: suspend () -> NetworkResponse<T>
): T = block().ensureSuccess()

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
