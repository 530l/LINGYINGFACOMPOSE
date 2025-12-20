package com.lyf.compose.core.data.repositories

import com.lyf.compose.core.data.network.BaseNetworkDataSource
import com.lyf.compose.core.data.api.AtmobApi
import com.lyf.compose.core.data.api.request.GoodsSearchRequest
import com.lyf.compose.core.data.bean.Auth
import com.lyf.compose.core.data.bean.Goods
import com.lyf.compose.core.data.bean.User
import com.lyf.compose.core.data.network.NetworkPageData
import com.lyf.compose.core.data.network.NetworkResponse
import javax.inject.Inject

class AtmobRepositories @Inject constructor(
    private val atmobApi: AtmobApi
) : BaseNetworkDataSource(), AtmobApi {
    override suspend fun loginByPassword(params: Map<String, String>): NetworkResponse<Auth> {
        return atmobApi.loginByPassword(params)
    }

    override suspend fun getGoodsPage(params: GoodsSearchRequest): NetworkResponse<NetworkPageData<Goods>> {
        return atmobApi.getGoodsPage(params)

    }

    override suspend fun getGoodsInfo(id: String): NetworkResponse<Goods> {
        return atmobApi.getGoodsInfo(id)
    }

    override suspend fun getPersonInfo(): NetworkResponse<User> {
        return atmobApi.getPersonInfo()
    }
}