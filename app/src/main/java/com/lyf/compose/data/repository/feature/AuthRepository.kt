package com.lyf.compose.data.repository.feature


import com.lyf.compose.data.api.AtmobApi
import com.lyf.compose.data.bean.User
import com.lyf.compose.data.network.NetworkResult
import com.lyf.compose.data.network.networkResultFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 认证仓库
 *
 * 负责处理用户登录、注册等认证相关的网络请求。
 */
@Singleton
class AuthRepository @Inject constructor(
    private val atmobApi: AtmobApi
) {

    /**
     * 使用用户名和密码登录
     *
     * 发送登录请求并返回登录结果，成功时包含用户信息。
     */
    fun loginByPassword(username: String, password: String): Flow<NetworkResult<User>> =
        networkResultFlow { atmobApi.loginByPassword(username, password) }
}
