package com.lyf.compose.core.data.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed interface NetworkResult<out T> {
    data object Loading : NetworkResult<Nothing>
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Error(val exception: Throwable) : NetworkResult<Nothing>
}

inline fun <T, R> NetworkResult<T>.call(
    onSuccess: (T) -> R,
    onError: (Throwable) -> R,
    onLoading: () -> R = { error("Unexpected Loading state") }
): R = when (this) {
    is NetworkResult.Loading -> onLoading()
    is NetworkResult.Success -> onSuccess(data)
    is NetworkResult.Error -> onError(exception)
}


inline fun <T> Flow<NetworkResult<T>>.launchCollect(
    scope: CoroutineScope,
    crossinline onLoading: () -> Unit = {},
    crossinline onSuccess: (T) -> Unit,
    crossinline onError: (Throwable) -> Unit = {},
) = scope.launch {
    //ollectLatest：若多次调用 requestXXXXX()，旧的收集会被取消，避免竞态问题。
    collectLatest { result ->
        when (result) {
            is NetworkResult.Loading -> onLoading()
            is NetworkResult.Success -> onSuccess(result.data)
            is NetworkResult.Error -> onError(result.exception)
        }
    }
}