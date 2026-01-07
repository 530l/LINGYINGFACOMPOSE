package com.lyf.compose.data.network

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.SerializationException
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 集中化错误处理器
 * 提供用户友好的错误消息和统一的错误日志记录
 */
@Singleton
class ErrorHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * 获取用户友好的错误消息
     *
     * 将各种异常类型转换为用户可理解的错误提示。
     */
    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is ApiException -> {
                when (throwable.code) {
                    401 -> "未授权，请重新登录"
                    403 -> "无权限访问"
                    404 -> "请求的资源不存在"
                    500 -> "服务器内部错误"
                    503 -> "服务暂时不可用"
                    else -> throwable.message ?: "未知错误"
                }
            }
            is IOException -> "网络连接失败，请检查网络设置"
            is SocketTimeoutException -> "连接超时，请稍后重试"
            is UnknownHostException -> "无法解析服务器地址"
            is ConnectException -> "无法连接到服务器"
            is SerializationException -> "数据解析失败"
            else -> "发生未知错误: ${throwable.message}"
        }
    }

    /**
     * 记录错误日志
     *
     * @param throwable 发生的异常
     * @param context 错误发生的上下文信息
     */
    fun logError(throwable: Throwable, context: String) {
        Timber.e(throwable, "Error in $context")
    }

    /**
     * 记录错误日志并返回用户友好的错误消息
     *
     * 组合了日志记录和错误消息获取功能。
     */
    fun logAndGetMessage(throwable: Throwable, context: String): String {
        logError(throwable, context)
        return getErrorMessage(throwable)
    }
}
