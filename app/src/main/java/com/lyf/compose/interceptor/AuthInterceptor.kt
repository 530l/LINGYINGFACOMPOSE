package com.lyf.compose.interceptor

import com.lyf.compose.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 认证拦截器
 *
 * 统一处理请求和响应的认证逻辑：
 *
 * **请求拦截**：
 * - 如果本地有 token，自动添加到请求头（Authorization: Bearer <token>）
 *
 * **响应拦截**：
 * - 如果收到 401 状态码，清空本地 token
 * - SessionManager 会触发导航跳转到登录页
 *
 * 注意：
 * - Header 名称需要根据后端规范调整
 * - token 清空发生在网络线程
 * - SessionManager 使用 StateFlow，更新后会自动驱动 UI 层
 */
class AuthInterceptor : Interceptor {

    /**
     * 拦截并处理请求和响应
     *
     * 1. 检查本地是否有 token
     * 2. 有 token 则添加到 Authorization 请求头
     * 3. 执行请求
     * 4. 检查响应状态码
     * 5. 如果是 401，清空 token 触发重新登录
     *
     * @param chain 拦截器链
     * @return HTTP 响应
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val token = SessionManager.getToken()
        val authedRequest = if (token.isNotBlank()) {
            original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }

        val response = chain.proceed(authedRequest)

        if (response.code == 401) {
            // 401 未授权：清空 token，触发跳转到登录页
            SessionManager.clearToken()
        }

        return response
    }
}

