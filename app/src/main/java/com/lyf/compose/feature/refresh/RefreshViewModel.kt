package com.lyf.compose.feature.refresh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyf.compose.core.data.bean.Article
import com.lyf.compose.core.data.bean.ArticleBean
import com.lyf.compose.core.data.network.NetworkResult
import com.lyf.compose.core.data.network.fold
import com.lyf.compose.core.data.repositories.AtmobRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RefreshViewModel @Inject constructor(
    private val repository: AtmobRepositories
) : ViewModel() {

    data class RefreshUiState(
        val isRefreshing: Boolean = false,
        val isLoadingMore: Boolean = false,
        val page: Int = 0,
        val hasMore: Boolean = true,
        val errorMessage: String? = null,
        val articles: List<ArticleBean> = emptyList(),
    )

    private val _uiState = MutableStateFlow(RefreshUiState())
    val uiState: StateFlow<RefreshUiState> = _uiState.asStateFlow()


    fun refresh() = load(page = 0, isRefresh = true)

    fun loadMore() {
        Timber.d("loadMore called, hasMore=${_uiState.value.hasMore}, isLoadingMore=${_uiState.value.isLoadingMore}")
        if (!_uiState.value.hasMore || _uiState.value.isLoadingMore) return
        load(page = _uiState.value.page + 1, isRefresh = false)
    }

    private fun load(page: Int, isRefresh: Boolean) {
        Timber.d("load(page=$page, isRefresh=$isRefresh) start")
        viewModelScope.launch {
            ///因为：
            //safeApiCall 捕获所有异常 → 转为 Error
            //Flow 一定 emit Success 或 Error → .first() 安全
            //无需 try-catch / runCatching
            updateLoading(isRefreshing = isRefresh)
            val result = repository.requestArticleList(page)
                .filter { it !is NetworkResult.Loading }
                .first()
            // ✅ 使用 fold，无需 else，无需处理 Loading
            result.fold(
                onSuccess = { response -> handleSuccess(response, isRefresh) },
                onError = { error -> handleError(error, isRefresh) }
            )
        }
    }


    private fun updateLoading(isRefreshing: Boolean) {
        _uiState.update { state ->
            state.copy(
                isRefreshing = isRefreshing,
                isLoadingMore = !isRefreshing,
                errorMessage = null,
                hasMore = if (isRefreshing) true else state.hasMore
            )
        }
    }

    private fun handleSuccess(response: Article, isRefresh: Boolean) {
        Timber.d("load success page=${response.curPage} size=${response.datas.size}")

        val computedHasMore = when {
            response.over -> false
            response.pageCount <= 0 -> response.datas.isNotEmpty()
            else -> response.curPage < response.pageCount - 1
        }

        // 加载更多时：若服务端返回的页码 ≤ 当前页，说明无新数据
        if (!isRefresh && response.curPage <= _uiState.value.page) {
            _uiState.update {
                it.copy(
                    isRefreshing = false,
                    isLoadingMore = false,
                    hasMore = computedHasMore
                )
            }
            Timber.d("server returned same or older page, no new data")
            return
        }

        val newArticles = if (isRefresh) {
            response.datas
        } else {
            _uiState.value.articles + response.datas
        }

        _uiState.update {
            it.copy(
                isRefreshing = false,
                isLoadingMore = false,
                articles = newArticles,
                page = response.curPage,
                hasMore = computedHasMore
            )
        }
    }

    private fun handleError(error: Throwable, isRefresh: Boolean) {
        Timber.e(error, "load error")
        val errorMsg = error.message ?: run {
            if (isRefresh) "刷新失败" else "加载更多失败"
        }
        _uiState.update {
            it.copy(
                isRefreshing = false,
                isLoadingMore = false,
                errorMessage = errorMsg
            )
        }
    }


    fun loadMultiplePages() {
        viewModelScope.launch {
            updateLoading(isRefreshing = true)
            val pages = listOf(0, 1, 2)

            val results = pages.map { page ->
                async {
                    val result = repository.requestArticleList(page)
                        .filter { it !is NetworkResult.Loading }
                        .first()
                    Pair(page, result)
                }
            }.awaitAll()

            val allArticles = mutableListOf<ArticleBean>()
            var anyError: Throwable? = null

            for ((page, result) in results) {
                result.fold(
                    onSuccess = { response ->
                        allArticles.addAll(response.datas)
                    },
                    onError = { error ->
                        Timber.e(error, "Page $page failed")
                        if (anyError == null) anyError = error
                    }
                )
            }
            _uiState.update {
                it.copy(
                    isRefreshing = false,
                    articles = allArticles,
                    errorMessage = anyError?.message ?: ""
                )
            }
        }
    }
}