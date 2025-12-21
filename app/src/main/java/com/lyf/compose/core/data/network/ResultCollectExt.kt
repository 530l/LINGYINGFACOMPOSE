package com.lyf.compose.core.data.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 统一收集 Flow<Result<T>>
 *
 * 适用于 ViewModel 层：
 * - Loading
 * - Success
 * - Error
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
