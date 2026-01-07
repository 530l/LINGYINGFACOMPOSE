package com.lyf.compose.data.repositories


import com.lyf.compose.data.api.AtmobApi
import com.lyf.compose.data.bean.Article
import com.lyf.compose.data.bean.Banner
import com.lyf.compose.data.bean.User
import com.lyf.compose.data.network.NetworkResult
import com.lyf.compose.data.network.networkResultFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AtmobRepositories @Inject constructor(
    private val atmobApi: AtmobApi
) {

    fun loginByPassword(username: String, password: String): Flow<NetworkResult<User>> =
        networkResultFlow { atmobApi.loginByPassword(username, password) }


    fun requestBanner(): Flow<NetworkResult<List<Banner>>> =
        networkResultFlow { atmobApi.getBanner() }

    fun requestArticleList(page: Int): Flow<NetworkResult<Article>> =
        networkResultFlow { atmobApi.getArticleList(page) }



}