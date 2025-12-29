package com.lyf.compose.feature.refresh

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.king.ultraswiperefresh.NestedScrollMode
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.indicator.SwipeRefreshHeader
import com.king.ultraswiperefresh.indicator.lottie.LottieRefreshFooter
import com.king.ultraswiperefresh.rememberUltraSwipeRefreshState
import com.lyf.compose.R
import com.lyf.compose.core.data.bean.ArticleBean
import com.lyf.compose.core.state.BaseNetWorkListUiState
import com.lyf.compose.core.state.LoadMoreState
import com.lyf.compose.core.theme.ShapeMedium
import com.lyf.compose.core.ui.components.network.BaseNetWorkListView
import com.lyf.compose.core.ui.components.scaffold.AppScaffold
import com.lyf.compose.core.ui.components.text.AppText
import kotlinx.coroutines.launch

/**
 * 下拉刷新 + 上拉加载 示例
 * todo viewmodel 的生命周期比 composable 更长，
 *      compose函数退出了，但是 viewmodel 还在，
 */
@Composable
fun RefreshScreen(viewModel: RefreshViewModel = hiltViewModel()) {

    // 使用 collectAsStateWithLifecycle 可在生命周期可见时订阅，避免后台订阅导致的泄漏
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (uiState.articles.isEmpty()) {
            viewModel.refresh()
        }
    }


    val listUiState = when {
        uiState.articles.isEmpty() && uiState.isRefreshing -> BaseNetWorkListUiState.Loading
        uiState.articles.isEmpty() && uiState.errorMessage != null -> BaseNetWorkListUiState.Error
        uiState.articles.isEmpty() -> BaseNetWorkListUiState.Empty
        else -> BaseNetWorkListUiState.Success
    }

    val loadMoreState = when {
        uiState.isLoadingMore -> LoadMoreState.Loading
        uiState.errorMessage != null && uiState.articles.isNotEmpty() && !uiState.isRefreshing -> LoadMoreState.Error
        !uiState.hasMore -> LoadMoreState.NoMore
        else -> LoadMoreState.PullToLoad
    }

    AppScaffold(titleText = "文章列表") {
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
    // 使用 UltraSwipeRefresh，并把它的 state 和 ViewModel 的刷新/加载状态保持一致
    val state = rememberUltraSwipeRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val hasMore = loadMoreState != LoadMoreState.NoMore

    // 当 ViewModel 的 isRefreshing 改变时，更新 UltraSwipe 的 isRefreshing（同步显示）
    LaunchedEffect(isRefreshing) {
        state.isRefreshing = isRefreshing
    }
    // 当 loadMoreState 改变时，更新 UltraSwipe 的 isLoading（同步底部加载指示器）
    LaunchedEffect(loadMoreState) {
        state.isLoading = loadMoreState == LoadMoreState.Loading
    }

    // 记住一个 LazyListState，用来获取当前可见项信息
    val listState = rememberLazyListState()


    //这个 LaunchedEffect 的副作用是：监听列表滚动位置，
    // 在用户接近底部时自动触发“加载更多”，实现 无限滚动（Infinite Scroll） 的体验。
//    LaunchedEffect(listState, hasMore, loadMoreState, list.size) {
//        // 下面这行会在最后可见项索引变化时发出值
//        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1 }
//            .distinctUntilChanged() // 如果索引没变就不重复处理
//            .filter { lastIndex -> lastIndex >= 0 } // 忽略 -1（布局未就绪）
//            .collect { lastIndex ->
//                // 当满足触发条件且当前不是加载中时，调用加载更多回调
//                if (shouldTriggerLoadMore(lastIndex, list.size)
//                    && hasMore && loadMoreState != LoadMoreState.Loading) {
//                    onLoadMore()
//                }
//            }
//    }

    UltraSwipeRefresh(
        state = state,
        onRefresh = {
            // 用户下拉时的回调：把刷新请求发给外部（通常是 ViewModel），
            // 这里通过 coroutineScope.launch 在协程里调用，避免在组合阶段直接执行耗时操作
            coroutineScope.launch {
                onRefresh()
            }
        },
        onLoadMore = {
            // 用户上拉触发底部加载时的回调：同样委托给外部处理（例如 ViewModel.loadMore）
            coroutineScope.launch {
                if (hasMore) {
                    onLoadMore()
                }
            }
        },
        modifier = Modifier,
        headerScrollMode = NestedScrollMode.FixedContent,//固定内容；即：内容固定，Header或 Footer进行滚动
        footerScrollMode = NestedScrollMode.FixedBehind,//固定在背后；即：Header或 Footer固定，仅内容滚动
        loadMoreEnabled = hasMore,
        alwaysScrollable = false,
        headerIndicator = { SwipeRefreshHeader(it) },
        footerIndicator = {
            //SwipeRefreshFooter(it)
            LottieRefreshFooter(
                state = it,
                height = 80.dp,
                spec = LottieCompositionSpec.RawRes(R.raw.usr_lottie_sound_wave),
            )
        }
    ) {
        LazyColumn(Modifier, state = listState) {
            items(list) { item ->
                GoodsListItem(goods = item)
            }

            // 根据 loadMoreState 显示不同的底部状态
            when (loadMoreState) {
                LoadMoreState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.Text(
                                text = "加载中...",
                                color = Color(0xFF999999),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                LoadMoreState.NoMore -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.Text(
                                text = "没有更多数据了",
                                color = Color(0xFF999999),
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    }
                }

                LoadMoreState.Error -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onLoadMore() }, contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.Text(
                                text = "加载失败，点击重试",
                                color = Color(0xFF999999),
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    }
                }

                else -> {}
            }
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