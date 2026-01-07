package com.lyf.compose.data.network

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API 业务异常
 *
 * 当网络请求成功（HTTP 200），但业务层返回错误码时抛出。
 * 例如：用户密码错误（errorCode != 0）。
 */
class ApiException(
    /** 业务错误码 */
    val code: Int,

    override val message: String,

    /** 原始响应数据，便于调试 */
    val rawResponse: Any? = null,
) : RuntimeException(message)

/**
 * 网络响应封装（WanAndroid API 规范）
 *
 * 统一处理后端的响应格式，检查业务状态码并提取数据。
 *
 * API 规范：
 * - errorCode == 0 表示业务成功
 * - errorCode != 0 表示业务失败（如密码错误、参数错误）
 *
 * @param T 真实数据的类型
 */
@Keep
@Serializable
data class NetworkResponse<T>(
    /** 业务数据，可能为空 */
    @SerialName("data")
    val data: T? = null,

    /** 业务状态码，0 表示成功 */
    @SerialName("errorCode")
    val errorCode: Int = 0,

    /** 业务错误提示信息 */
    @SerialName("errorMsg")
    val errorMsg: String? = null
) {
    /** 业务是否成功（errorCode == 0） */
    val isSucceeded: Boolean get() = errorCode == 0

    /**
     * 提取数据或抛出异常
     *
     * 用于 Repository 层将 NetworkResponse 转换为具体数据。
     * 如果业务失败或数据为空，会抛出 ApiException。
     *
     * 使用示例：
     * ```kotlin
     * fun getUser(): Flow<User> = flow {
     *     val response = apiService.getUser()
     *     val user = response.ensureSuccess() // 失败时自动抛出异常
     *     emit(user)
     * }
     * ```
     *
     * @throws ApiException 当 errorCode != 0 或 data 为 null 时
     */
    fun ensureSuccess(defaultMessage: String = "获取数据失败"): T {
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