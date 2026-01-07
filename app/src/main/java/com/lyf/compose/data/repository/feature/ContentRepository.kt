package com.lyf.compose.data.repository.feature


import com.lyf.compose.data.api.AtmobApi
import com.lyf.compose.data.bean.Article
import com.lyf.compose.data.bean.Banner
import com.lyf.compose.data.bean.Hotkey
import com.lyf.compose.data.network.NetworkResult
import com.lyf.compose.data.network.networkResultFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 内容仓库
 *
 * 处理 Banner、文章、热词等内容相关的网络请求。
 */
@Singleton
class ContentRepository @Inject constructor(
    private val atmobApi: AtmobApi
) {

    /**
     * 获取 Banner 列表
     */
    fun requestBanner(): Flow<NetworkResult<List<Banner>>> =
        networkResultFlow { atmobApi.getBanner() }

    /**
     * 获取文章列表（支持分页）
     */
    fun requestArticleList(page: Int): Flow<NetworkResult<Article>> =
        networkResultFlow { atmobApi.getArticleList(page) }

    /**
     * 获取热词列表
     */
    fun requestHotKey(): Flow<NetworkResult<List<Hotkey>>> =
        networkResultFlow { atmobApi.getHotKey() }
}
