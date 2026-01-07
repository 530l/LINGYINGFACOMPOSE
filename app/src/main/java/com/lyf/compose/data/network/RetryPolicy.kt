package com.lyf.compose.data.network

import kotlin.math.pow
import kotlin.reflect.KClass
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 可配置的重试策略
 *
 * 支持指数退避和可重试异常类型配置。默认配置：
 * - 最大重试 3 次
 * - 初始延迟 1 秒
 * - 最大延迟 10 秒
 * - 指数退避系数 2.0
 * - 可重试异常：网络 IO 相关异常
 */
data class RetryPolicy(
    val maxRetries: Int = 3,
    val initialDelayMs: Long = 1000,
    val maxDelayMs: Long = 10000,
    val backoffMultiplier: Double = 2.0,
    val retryableErrors: Set<KClass<out Throwable>> = setOf(
        IOException::class,
        SocketTimeoutException::class,
        UnknownHostException::class,
        ConnectException::class
    )
) {
    /**
     * 计算第 n 次尝试的延迟时间（指数退避）
     *
     * 延迟时间 = initialDelayMs × (backoffMultiplier ^ attempt)
     * 结果不超过 maxDelayMs。
     */
    fun getDelayForAttempt(attempt: Int): Long {
        val delay = initialDelayMs * backoffMultiplier.pow(attempt.toDouble())
        return delay.toLong().coerceAtMost(maxDelayMs)
    }

    /**
     * 判断是否应该重试
     *
     * 检查重试次数是否用尽，以及异常类型是否可重试。
     */
    fun shouldRetry(error: Throwable, attempt: Int): Boolean {
        return attempt < maxRetries && retryableErrors.any {
            it.isInstance(error)
        }
    }
}
