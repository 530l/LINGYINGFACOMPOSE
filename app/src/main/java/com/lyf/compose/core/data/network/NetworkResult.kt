package com.lyf.compose.core.data.network

/**
 * 网络请求结果包装类
 * @param T 数据类型
 */
sealed interface NetworkResult<out T> {
    /**
     * 加载中状态
     */
    data object Loading : NetworkResult<Nothing>

    /**
     * 成功状态，包含数据
     * @param T 数据类型
     * @param data 成功返回的数据
     */
    data class Success<T>(val data: T) : NetworkResult<T>

    /**
     * 错误状态，包含异常信息
     * @param exception 异常对象
     */
    data class Error(val exception: Throwable) : NetworkResult<Nothing>
}
