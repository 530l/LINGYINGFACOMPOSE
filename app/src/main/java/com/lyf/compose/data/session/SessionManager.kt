package com.lyf.compose.data.session

import com.lyf.compose.data.constant.Constant
import com.lyf.compose.utils.storage.MMKVUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 全局会话管理（单例）。
 *
 * 目标：
 * - token 统一从这里读写（内部落地到 MMKV）。
 * - 对外暴露 StateFlow，让 Compose 可以“感知登录态变化”并自动导航。
 *
 * 使用方式：
 * - 登录成功：SessionManager.setToken(token)
 * - 退出登录/401：SessionManager.clearToken()
 * - 监听登录态：SessionManager.tokenFlow
 */
object SessionManager {

    // 启动时从本地恢复 token（同步读取；MMKV 已在 Application.onCreate() 初始化）
    private val _tokenFlow = MutableStateFlow(MMKVUtils.getString(Constant.TOKEN_KV, ""))
    val tokenFlow: StateFlow<String> = _tokenFlow.asStateFlow()

    fun isLoggedIn(): Boolean = _tokenFlow.value.isNotBlank()

    fun getToken(): String = _tokenFlow.value

    /**
     * 设置 token（同时写入本地）。
     */
    fun setToken(token: String) {
        _tokenFlow.value = token
        MMKVUtils.putString(Constant.TOKEN_KV, token)
    }

    /**
     * 清空 token（同时清本地）。
     */
    fun clearToken() {
        _tokenFlow.value = ""
        MMKVUtils.putString(Constant.TOKEN_KV, "")
    }
}
