package com.lyf.compose.core.data.repositories

import com.lyf.compose.core.data.api.AtmobApi
import com.lyf.compose.core.data.bean.Banner
import com.lyf.compose.core.data.bean.HomeInitData
import com.lyf.compose.core.data.bean.User
import com.lyf.compose.core.data.network.NetworkResult
import com.lyf.compose.core.data.network.mapSuccess
import com.lyf.compose.core.data.network.networkResultFlow
import com.lyf.compose.core.data.network.networkResultFlowFromSuspend
import com.lyf.compose.core.data.network.safeApiCallMulti
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AtmobRepositories @Inject constructor(
    private val atmobApi: AtmobApi
) {


//    suspend fun loginByPassword(params: Map<String, String>): User =
//        unwrapApiCall { atmobApi.loginByPassword(params) }
//

    fun loginByPassword(username: String, password: String): Flow<NetworkResult<User>> =
        networkResultFlow { atmobApi.loginByPassword(username, password) }


    fun requestBanner(): Flow<NetworkResult<List<Banner>>> =
        networkResultFlow { atmobApi.getBanner() }


    /**
     * 并发获取 Banner 和 HotKey
     * - 两个请求同时发起
     * - 任一失败 → 整体失败
     * - 全部成功 → 返回组合数据
     */
    suspend fun fetchHomeInitData(): NetworkResult<HomeInitData> =
        safeApiCallMulti(
            callA = { atmobApi.getBanner() },
            callB = { atmobApi.getHotKey() }
        ).mapSuccess { (banners, hotKeys) ->
            HomeInitData(banners, hotKeys)
        }

    /**
     * 提供给 ViewModel 的 Flow 版本（带 Loading 状态）
     */
    fun requestHomeInitData(): Flow<NetworkResult<HomeInitData>> =
        networkResultFlowFromSuspend { fetchHomeInitData() }
}