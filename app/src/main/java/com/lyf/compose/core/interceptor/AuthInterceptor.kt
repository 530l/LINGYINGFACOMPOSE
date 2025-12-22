package com.lyf.compose.core.interceptor

import com.lyf.compose.core.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 统一处理鉴权相关逻辑：
 * 1) 请求时，如果本地有 token，则自动加到 Header
 * 2) 响应如果遇到 401，则清空 token（触发 AppNavHost 自动跳转到登录页）
 *
 * 说明：
 * - Header 名称需要按你的后端规范调整（这里用 Authorization: Bearer <token> 作为常见默认）。
 * - 清 token 发生在网络线程；SessionManager 使用 StateFlow，更新后会驱动 UI 层。
 */
class AuthInterceptor : Interceptor {
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
            // 401 未授权：清 token -> AppNavHost 会自动清栈跳转到 Login
            SessionManager.clearToken()
        }

        return response
    }
}

