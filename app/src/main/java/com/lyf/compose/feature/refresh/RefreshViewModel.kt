package com.lyf.compose.feature.refresh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyf.compose.core.data.bean.Article
import com.lyf.compose.core.data.bean.ArticleBean
import com.lyf.compose.core.data.network.launchCollect
import com.lyf.compose.core.data.repositories.AtmobRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // 用于取消上一次加载任务，避免竞态（如快速下拉两次）
    private var activeLoadJob: Job? = null

    fun refresh() = load(page = 0, isRefresh = true)

    fun loadMore() {
        Timber.d("RefreshViewModel: loadMore called, hasMore=${_uiState.value.hasMore}, isLoadingMore=${_uiState.value.isLoadingMore}")
        if (!_uiState.value.hasMore) return
        load(page = _uiState.value.page + 1, isRefresh = false)
    }

    private fun load(page: Int, isRefresh: Boolean) {
        Timber.d("RefreshViewModel: load(page=$page,isRefresh=$isRefresh) start")
        // 取消上一次未完成的请求（防止旧响应覆盖新状态）
        activeLoadJob?.cancel()
        activeLoadJob = viewModelScope.launch {
            // 更新 loading 状态 & 清除错误
            _uiState.update { state ->
                state.copy(
                    isRefreshing = isRefresh,
                    isLoadingMore = !isRefresh,
                    errorMessage = null,
                    hasMore = if (isRefresh) true else state.hasMore
                )
            }

            repository.requestArticleList(page).launchCollect(
                scope = viewModelScope,
                onLoading = { /* 若 launchCollect 内部已处理 loading，此处可留空 */ },
                onSuccess = { response: Article ->
                    Timber.d("RefreshViewModel: load success page=${response.curPage} size=${response.datas.size}")
                    // 计算是否还有更多数据
                    val computedHasMore = when {
                        response.over -> false
                        response.pageCount <= 0 -> response.datas.isNotEmpty()
                        else -> response.curPage < response.pageCount - 1
                    }

                    // 加载更多时：若服务端返回的页码 ≤ 当前页，说明无新数据，仅更新 hasMore
                    if (!isRefresh && response.curPage <= _uiState.value.page) {
                        _uiState.update {
                            it.copy(
                                isRefreshing = false,
                                isLoadingMore = false,
                                hasMore = computedHasMore
                            )
                        }
                        Timber.d("RefreshViewModel: server returned same or older page, no new data")
                        return@launchCollect
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
                },
                onError = { error ->
                    Timber.e(error, "RefreshViewModel: load error")
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            isLoadingMore = false,
                            errorMessage = error.message ?: run {
                                if (isRefresh) "刷新失败" else "加载更多失败"
                            }
                        )
                    }
                }
            )
        }
    }
}