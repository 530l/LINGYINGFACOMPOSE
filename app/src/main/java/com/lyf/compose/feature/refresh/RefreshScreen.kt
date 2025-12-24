package com.lyf.compose.feature.refresh

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.lyf.compose.core.data.bean.ArticleBean
import com.lyf.compose.core.state.BaseNetWorkListUiState
import com.lyf.compose.core.state.LoadMoreState
import com.lyf.compose.core.theme.ShapeMedium
import com.lyf.compose.core.ui.components.network.BaseNetWorkListView
import com.lyf.compose.core.ui.components.refresh.RefreshLayout
import com.lyf.compose.core.ui.components.scaffold.AppScaffold
import com.lyf.compose.core.ui.components.text.AppText

/**
 * 刷新列表页面 UI
 * - 从 ViewModel 收集状态
 * - 显示加载/空/错误/成功 状态
 * - 支持下拉刷新和上拉加载更多
 */
@Composable
fun RefreshScreen(viewModel: RefreshViewModel = hiltViewModel()) {
    // 收集 ui 状态
    val uiState by viewModel.uiState.collectAsState()

    // 首次进入时触发一次刷新（加载第一页）
    LaunchedEffect(Unit) {
        if (uiState.articles.isEmpty()) {
            viewModel.refresh()
        }
    }

    // 根据 ViewModel 状态映射到 BaseNetWorkListUiState
    val listUiState = when {
        uiState.articles.isEmpty() && uiState.isRefreshing -> BaseNetWorkListUiState.Loading
        uiState.articles.isEmpty() && uiState.errorMessage != null -> BaseNetWorkListUiState.Error
        uiState.articles.isEmpty() -> BaseNetWorkListUiState.Empty
        else -> BaseNetWorkListUiState.Success
    }

    // 根据 ViewModel 状态映射到 LoadMoreState
    val loadMoreState = when {
        uiState.isLoadingMore -> LoadMoreState.Loading
        uiState.errorMessage != null && uiState.articles.isNotEmpty() && !uiState.isRefreshing -> LoadMoreState.Error
        !uiState.hasMore -> LoadMoreState.NoMore
        else -> LoadMoreState.PullToLoad
    }

    AppScaffold(titleText = "文章列表") { padding ->
        BaseNetWorkListView(
            uiState = listUiState,
            modifier = Modifier,
            onRetry = { viewModel.refresh() }
        ) {
            // 成功时展示列表并接入下拉/上拉逻辑
            NetworkListDemoContent(
                list = uiState.articles,
                isRefreshing = uiState.isRefreshing,
                loadMoreState = loadMoreState,
                onRefresh = { viewModel.refresh() },
                onLoadMore = { viewModel.loadMore() },
                // 当列表快滚到尾部时触发加载更多（提前 3 项）
                shouldTriggerLoadMore = { lastIndex, totalCount ->
                    totalCount > 0 && lastIndex >= totalCount - 3
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NetworkListDemoContent(
    list: List<ArticleBean>,
    isRefreshing: Boolean,
    loadMoreState: LoadMoreState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    shouldTriggerLoadMore: (lastIndex: Int, totalCount: Int) -> Boolean
) {
    RefreshLayout(
        isRefreshing = isRefreshing,
        loadMoreState = loadMoreState,
        onRefresh = onRefresh,
        onLoadMore = onLoadMore,
        shouldTriggerLoadMore = shouldTriggerLoadMore
    ) {
        itemsIndexed(list) { _, item ->
            GoodsListItem(goods = item)
        }
    }
}

@Composable
private fun GoodsListItem(goods: ArticleBean) {
    ListItem(
        modifier = Modifier.clip(ShapeMedium),
        headlineContent = { AppText(text = goods.title.ifBlank { "未命名商品" }) },
    )
}