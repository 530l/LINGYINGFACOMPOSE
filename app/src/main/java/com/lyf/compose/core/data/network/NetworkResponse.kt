package com.lyf.compose.core.data.network

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API 异常
 *
 * 当网络请求成功，但业务层返回错误时，抛出此异常
 */
class ApiException(
    val code: Int,
    override val message: String,
    val rawResponse: Any? = null,
) : RuntimeException(message)

/**
 * 解析网络响应
 * @param T 数据类型
 * @param data 真实数据
 * @param code 状态码 等于1000表示成功
 * @param errorMsg 出错的提示信息
 */
@Keep
@Serializable
data class NetworkResponse<T>(
    /**
     * 真实数据
     * 类型是泛型
     */
    @SerialName("data")
    val data: T? = null,

    /**
     * WanAndroid: errorCode == 0
     */
    @SerialName("errorCode")
    val errorCode: Int = 0,

    /**
     * WanAndroid: errorMsg
     */
    @SerialName("errorMsg")
    val errorMsg: String? = null,


    ) {
    /**
     * WanAndroid 是否成功
     */
    val isSucceeded: Boolean get() = errorCode == 0

    /**
     * Unwrap 真实数据
     *
     * - code != 1000: throw ApiException
     * - code == 1000 but data == null: throw ApiException (treat as error)
     */
    fun requireData(defaultMessage: String = "获取数据失败"): T {
        if (!isSucceeded) {
            throw ApiException(
                code = errorCode,
                message = errorMsg ?: defaultMessage,
                rawResponse = this
            )
        }
        return data ?: throw ApiException(
            code = errorCode,
            message = errorMsg ?: defaultMessage,
            rawResponse = this
        )
    }
}