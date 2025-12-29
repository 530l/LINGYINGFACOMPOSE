package com.lyf.compose.feature.refresh.v

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lyf.compose.core.state.LoadMoreState
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

/**
 * 自定义下拉刷新&加载更多
 * @param items         列表数据
 * @param isRefreshing  设置下拉刷新
 * @param loadMoreState 加载更多状态
 * @param onRefresh     下拉刷新回调
 * @param onLoad        加载更多回调
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun <T> SwipeRefreshBox(
    items: List<T>?,
    isRefreshing: Boolean,
    loadMoreState: LoadMoreState,
    onRefresh: () -> Unit,
    onLoad: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    key: ((index: Int, item: T) -> Any)? = null,
    contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    if (items.isNullOrEmpty()) {
        if (loadMoreState != LoadMoreState.NoMore) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier.align(Alignment.Center),
//                    color = Color(0xFFFF8B80)
//                )
                PageLoading(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            EmptyContent {
                onRefresh()
            }
        }
    } else {

        // 使用 material 的 pullRefresh state 与 modifier 保持类型一致
        val state = rememberPullRefreshState(isRefreshing, onRefresh)

        // 监听列表滚动，当接近底部且当前未在加载时触发 onLoad（避免依赖 isLoading 的反向逻辑）
        val loadRequested = remember { mutableStateOf(false) }
        // 记录加载前的列表大小，用于在加载完成时判断是否有新数据
        val prevListSize = remember { mutableIntStateOf(items.size) }

        LaunchedEffect(listState, items, loadMoreState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1 }
                .distinctUntilChanged()
                .collect { lastIndex ->
                    val itemCount = items.size
                    Timber.d("SwipeRefreshBox: lastIndex=$lastIndex size=$itemCount loadMoreState=$loadMoreState loadRequested=${loadRequested.value}")
                    // 触发条件（升级）：仅当 loadMoreState 是 PullToLoad 且未重复请求时触发自动加载
                    if (loadMoreState == LoadMoreState.PullToLoad && !loadRequested.value && itemCount >= 10 && lastIndex >= itemCount - 3) {
                        Timber.d("SwipeRefreshBox: trigger onLoad() because lastIndex=$lastIndex >= ${itemCount - 3}")
                        loadRequested.value = true
                        prevListSize.intValue = itemCount
                        onLoad()
                    }
                }
        }

        // 当 loadMoreState 变为非 Loading 时重置 loadRequested 标记并检测是否有新数据
        LaunchedEffect(loadMoreState) {
            if (loadMoreState != LoadMoreState.Loading && loadRequested.value) {
                if (items.size <= prevListSize.intValue) {
                    Timber.d("SwipeRefreshBox: load finished but no new items (size=${items.size}). prev=${prevListSize.intValue}")
                } else {
                    Timber.d("SwipeRefreshBox: load finished, new items added: ${items.size - prevListSize.intValue}")
                }
                loadRequested.value = false
            }
        }

        Box(
            modifier = modifier
                .pullRefresh(state = state)
                .clipToBounds()
                .background(Color(0xFF010101)),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = contentPadding,
                    verticalArrangement = verticalArrangement,
                ) {
                    itemsIndexed(
                        items = items,
                        key = key,
                        contentType = contentType
                    ) { index, item ->
                        itemContent(index, item)
                    }
                    item {
                        // 使用项目内通用的 LoadMore 组件，传入当前 loadMoreState 与重试回调
                        LoadMore(state = loadMoreState, listState = listState, onRetry = { onLoad() })
                    }
                }
            }
            // 把 indicator 放在内容之后，确保在最上层可见
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter),
                scale = true
            )
        }
    }
}
