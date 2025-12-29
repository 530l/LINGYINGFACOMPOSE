package com.lyf.compose.feature.refresh.v

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

/**
 * 自定义下拉刷新&加载更多
 * @param items         列表数据
 * @param isRefreshing  设置下拉刷新
 * @param isLoading     设置加载更多
 * @param isFinishing   结束加载更多
 * @param onRefresh     下拉刷新回调
 * @param onLoad        加载更多回调
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun <T> SwipeRefreshBox(
    items: List<T>?,
    isRefreshing: Boolean,
    isLoading: Boolean,
    isFinishing: Boolean,
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
        if (!isFinishing) {
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

        LaunchedEffect(listState, items, isLoading, isFinishing) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1 }
                .distinctUntilChanged()
                .collect { lastIndex ->
                    val itemCount = items.size
                    Timber.d("SwipeRefreshBox: lastIndex=$lastIndex size=$itemCount isLoading=$isLoading isFinishing=$isFinishing loadRequested=${loadRequested.value}")
                    // 触发条件：至少有 10 项，并且滚动至倒数第 3 项以内；并且当前未在加载、未已请求加载、且还有更多数据
                    if (!isLoading && !loadRequested.value && !isFinishing && itemCount >= 10 && lastIndex >= itemCount - 3) {
                        Timber.d("SwipeRefreshBox: trigger onLoad() because lastIndex=$lastIndex >= ${itemCount - 3}")
                        loadRequested.value = true
                        prevListSize.intValue = itemCount
                        onLoad()
                    }
                }
        }

        // 当加载结束或出错时重置 loadRequested 标记，允许下次加载
        LaunchedEffect(isLoading) {
            if (!isLoading) {
                // 加载结束，检查是否有新增项
                if (items.size <= prevListSize.intValue) {
                    Timber.d("SwipeRefreshBox: load finished but no new items (size=${items.size}). prev=${prevListSize.intValue}")
                    Timber.d("没有更多数据或加载失败")
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
                        MoreIndicator(isLoading = isLoading, finishing = isFinishing)
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

@Composable
fun MoreIndicator(
    isLoading: Boolean,
    finishing: Boolean,
) {
    // 如果既不是 loading 也不是 finishing，则不显示任何内容，
    // 避免总是看到“正在加载中..."
    if (!isLoading && !finishing) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val text = if (finishing) "没有更多了！" else "正在加载中..."
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFF999999),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
