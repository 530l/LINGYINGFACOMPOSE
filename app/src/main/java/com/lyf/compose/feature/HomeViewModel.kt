package com.lyf.compose.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyf.compose.core.data.bean.ArticleBean
import com.lyf.compose.core.data.bean.HomeTab
import com.lyf.compose.core.data.network.NetworkResult
import com.lyf.compose.core.data.network.call
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


class HomeViewModel : ViewModel() {

    // 启动即提供默认 Tab，避免先显示 loading 再“突变成整页 UI”造成的体感卡顿/抖动
    private val _homeTabs = MutableStateFlow(
        listOf(
            HomeTab("1", "探索", "explore"),
            HomeTab("2", "创作", "create"),
            HomeTab("3", "资产", "asset"),
            HomeTab("4", "我的", "mine")
        )
    )
    val homeTabs: StateFlow<List<HomeTab>> = _homeTabs

    // 如果后续确实需要从网络/配置下发 Tab，可在这里异步刷新 _homeTabs，
    // 但不要为了“模拟加载”而人为 delay（会导致首屏抖动和卡顿感）。

    ///因为：
    //safeApiCall 捕获所有异常 → 转为 Error  ///Flow 一定 emit Success 或 Error → .first() 安全  ////无需 try-catch / runCatching

//    fun loadMultiplePages() {
//        viewModelScope.launch {
//            updateLoading(isRefreshing = true)
//            val pages = listOf(0, 1, 2)
//
//            val results = pages.map { page ->
//                async {
//                    val result = repository.requestArticleList(page)
//                        .filter { it !is NetworkResult.Loading }
//                        .first()
//                    Pair(page, result)
//                }
//            }.awaitAll()
//
//            val allArticles = mutableListOf<ArticleBean>()
//            var anyError: Throwable? = null
//
//            for ((page, result) in results) {
//                result.call(
//                    onSuccess = { response ->
//                        allArticles.addAll(response.datas)
//                    },
//                    onError = { error ->
//                        Timber.e(error, "Page $page failed")
//                        if (anyError == null) anyError = error
//                    }
//                )
//            }
//            _uiState.update {
//                it.copy(
//                    isRefreshing = false,
//                    articles = allArticles,
//                    errorMessage = anyError?.message ?: ""
//                )
//            }
//        }
//    }
}