package com.lyf.compose.feature.refresh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyf.compose.core.data.bean.Article
import com.lyf.compose.core.data.bean.ArticleBean
import com.lyf.compose.core.data.network.NetworkResult
import com.lyf.compose.core.data.network.call
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
        val page: Int = 1,
        val hasMore: Boolean = true,
        val errorMessage: String? = null,
        val articles: List<ArticleBean> = emptyList(),
    )

    private val _uiState = MutableStateFlow(RefreshUiState())
    val uiState: StateFlow<RefreshUiState> = _uiState.asStateFlow()

    fun onRefresh() {
        if (_uiState.value.isRefreshing || _uiState.value.isLoadingMore) return
        _uiState.update {
            it.copy(
                page = 1,
                isRefreshing = true,
                isLoadingMore = false,
                errorMessage = null,
            )
        }
        Timber.d("QQQ  Trigger refresh, current page: ${_uiState.value.page}")
        load()
    }

    fun onLoadMore() {
        val state = _uiState.value
        // 只有在可加载且未加载中时才触发
        if (!state.hasMore || state.isLoadingMore || state.isRefreshing) return
        _uiState.update {
            it.copy(
                isLoadingMore = true,
                isRefreshing = false,
                page = state.page + 1,
                errorMessage = null
            )
        }
        Timber.d("QQQ Trigger load more,  page: ${_uiState.value.page}")
        load()
    }


    /**
     * 判断是否应该触发加载更多
     *
     * @param lastIndex 当前可见的最后一项索引（从0开始）
     * @param totalCount 列表总项数
     * @return true 表示应触发 onLoadMore()
     */
    fun shouldTriggerLoadMore(lastIndex: Int, totalCount: Int): Boolean {
        val state = _uiState.value
        // 必须满足：有更多数据可加载 + 当前未在刷新/加载中 + 列表非空 + 滑动接近底部
        return totalCount > 0 &&
                state.hasMore &&
                !state.isRefreshing &&
                !state.isLoadingMore &&
                lastIndex >= totalCount - 3
    }


    private fun load() {
        viewModelScope.launch {
            val currentPage = _uiState.value.page
            val isRefresh = _uiState.value.isRefreshing
            updateLoading(isRefreshing = isRefresh)
            repository.requestArticleList(currentPage)
                .filter { it !is NetworkResult.Loading }
                .first().call(
                    onSuccess = { response ->
                        handleSuccess(response, isRefresh)
                    },
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
        val newArticles = if (isRefresh) {
            response.datas
        } else {
            _uiState.value.articles + response.datas
        }
        val computedHasMore = response.pageCount > response.datas.size
        _uiState.update {
            it.copy(
                isRefreshing = false,
                isLoadingMore = false,
                articles = newArticles,
                hasMore = computedHasMore
            )
        }
    }

    private fun handleError(error: Throwable, isRefresh: Boolean) {
        _uiState.update {
            it.copy(
                isRefreshing = false,
                isLoadingMore = false,
                articles = emptyList(),
                hasMore = false,
                errorMessage = error.message ?: run {
                    if (isRefresh) "刷新失败" else "加载更多失败"
                }
            )
        }
    }
}