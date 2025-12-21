package com.lyf.compose.core.data.repositories

import com.lyf.compose.core.data.api.AtmobApi
import com.lyf.compose.core.data.bean.Banner
import com.lyf.compose.core.data.bean.User
import com.lyf.compose.core.data.network.NetworkResult
import com.lyf.compose.core.data.network.networkResultFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AtmobRepositories @Inject constructor(
    private val atmobApi: AtmobApi
) {


//    suspend fun loginByPassword(params: Map<String, String>): User =
//        unwrapApiCall { atmobApi.loginByPassword(params) }
//

    fun loginByPassword(username: String, password: String): Flow<NetworkResult<User>> =
        networkResultFlow { atmobApi.loginByPassword(username,password) }


    fun requestBanner(): Flow<NetworkResult<List<Banner>>> =
        networkResultFlow { atmobApi.getBanner() }
}