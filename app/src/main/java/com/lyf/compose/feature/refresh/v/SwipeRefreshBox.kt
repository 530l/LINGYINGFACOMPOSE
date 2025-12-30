package com.lyf.compose.feature.refresh.v

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.lyf.compose.core.state.LoadMoreState
import com.lyf.compose.core.theme.SpaceHorizontalMedium
import com.lyf.compose.core.theme.SpaceHorizontalXXLarge
import com.lyf.compose.core.theme.SpacePaddingMedium
import com.lyf.compose.core.theme.SpaceVerticalMedium

/**
 * 自定义下拉刷新 & 加载更多容器（支持列表 / 网格）
 *
 * @param items 列表数据（null 或 empty 表示无数据）
 * @param isRefreshing 是否正在下拉刷新
 * @param loadMoreState 加载更多状态
 * @param onRefresh 下拉刷新回调
 * @param onLoadMore 加载更多回调
 * @param modifier 外部修饰符
 * @param listState 列表滚动状态
 * @param isGrid 是否使用网格布局
 * @param gridState 网格滚动状态
 * @param shouldTriggerLoadMore 自定义触发加载更多的条件，默认：滑动到最后 3 项时触发
 * @param gridItemContent 网格项内容构建器（当 isGrid = true 时使用）
 * @param contentPadding 内容内边距
 * @param verticalArrangement 列表垂直排列方式
 * @param key 唯一标识生成函数（用于提升性能）     key 决定“是不是同一个东西”，contentType 决定“是不是同一类东西”。
 * @param contentType 内容类型（用于跳过 recomposition）
 * @param columnItemContent 列表项内容构建器（当 isGrid = false 时使用）
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun <T> SwipeRefreshBox(
    items: List<T>?,
    isRefreshing: Boolean,
    loadMoreState: LoadMoreState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    isGrid: Boolean = false,
    gridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    shouldTriggerLoadMore: ((lastIndex: Int, totalCount: Int) -> Boolean)? = null,
    gridItemContent: @Composable (LazyStaggeredGridScope.(index: Int, item: T) -> Unit) = { _, _ -> /* no-op */ },
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(SpaceVerticalMedium),
    key: ((index: Int, item: T) -> Any)? = null,
    contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    columnItemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit = { _, _ -> /* no-op */ }
) {
    val hasData = !items.isNullOrEmpty()
    val showLoading = isRefreshing && !hasData
    val showEmpty = !isRefreshing && !hasData && loadMoreState == LoadMoreState.NoMore

    when {
        showLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                PageLoading()
            }
        }

        showEmpty -> {
            EmptyContent(onClick = { onRefresh() })
        }

        else -> {
            // 此分支 items 一定非空（因 showLoading/showEmpty 已排除）
            val nonNullItems = items!!

            val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh)

            val actualShouldTriggerLoadMore =
                shouldTriggerLoadMore ?: { lastIndex, totalCount ->
                    totalCount > 0 && lastIndex >= totalCount - 3
                }

            Box(
                modifier = modifier
                    .pullRefresh(pullRefreshState)
                    .clipToBounds()
                    .background(MaterialTheme.colorScheme.surface), // 下拉区域背景
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background) // 内容区域背景
                        .fillMaxSize()
                ) {
                    RefreshContent(
                        isGrid = isGrid,
                        listState = listState,
                        gridState = gridState,
                        loadMoreState = loadMoreState,
                        onLoadMore = onLoadMore,
                        shouldTriggerLoadMore = actualShouldTriggerLoadMore,
                        contentPadding = contentPadding,
                        verticalArrangement = verticalArrangement,
                        gridContent = {
                            val gridScope = this
                            itemsIndexed(
                                items = nonNullItems,
                                key = key?.let { k -> { index, item -> k(index, item) } }
                            ) { index, item ->
                                gridItemContent.invoke(gridScope, index, item)
                            }
                        },
                        content = {
                            itemsIndexed(
                                items = nonNullItems,
                                key = key,
                                contentType = contentType
                            ) { index, item ->
                                columnItemContent(index, item)
                            }
                        }
                    )
                }

                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    scale = true
                )
            }
        }
    }
}

/* -------------------------------------------------------------------------- */
/*                               内容切换容器                                  */
/* -------------------------------------------------------------------------- */

@Composable
private fun RefreshContent(
    isGrid: Boolean,
    listState: LazyListState,
    gridState: LazyStaggeredGridState,
    loadMoreState: LoadMoreState,
    onLoadMore: () -> Unit,
    shouldTriggerLoadMore: (lastIndex: Int, totalCount: Int) -> Boolean,
    contentPadding: PaddingValues,
    verticalArrangement: Arrangement.Vertical,
    gridContent: LazyStaggeredGridScope.() -> Unit,
    content: LazyListScope.() -> Unit
) {
    AnimatedContent(
        targetState = isGrid,
        transitionSpec = {
            (fadeIn(tween(300, easing = LinearEasing)) +
                    scaleIn(initialScale = 0.92f, animationSpec = tween(300)))
                .togetherWith(
                    fadeOut(tween(300)) +
                            scaleOut(targetScale = 0.92f, animationSpec = tween(300))
                )
        },
        label = "layout_switch"
    ) { targetIsGrid ->
        if (targetIsGrid) {
            RefreshGridContent(
                gridState = gridState,
                loadMoreState = loadMoreState,
                onLoadMore = onLoadMore,
                shouldTriggerLoadMore = shouldTriggerLoadMore,
                content = gridContent
            )
        } else {
            RefreshListContent(
                listState = listState,
                loadMoreState = loadMoreState,
                onLoadMore = onLoadMore,
                shouldTriggerLoadMore = shouldTriggerLoadMore,
                content = content
            )
        }
    }
}

/* -------------------------------------------------------------------------- */
/*                                List 实现                                    */
/* -------------------------------------------------------------------------- */

@Composable
private fun RefreshListContent(
    listState: LazyListState?,
    loadMoreState: LoadMoreState,
    onLoadMore: () -> Unit,
    shouldTriggerLoadMore: (lastIndex: Int, totalCount: Int) -> Boolean,
    content: LazyListScope.() -> Unit
) {
    val actualListState = listState ?: rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = actualListState.layoutInfo.visibleItemsInfo.lastOrNull()
            if (lastVisibleItem != null) {
                shouldTriggerLoadMore(
                    lastVisibleItem.index,
                    actualListState.layoutInfo.totalItemsCount
                )
            } else false
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium),
        state = actualListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SpaceHorizontalMedium)
    ) {
        item { Spacer(modifier = Modifier) }
        content()
        item {
            LoadMore(
                modifier = Modifier.padding(horizontal = SpaceHorizontalXXLarge),
                state = loadMoreState,
                listState = if (loadMoreState == LoadMoreState.Loading) actualListState else null,
                onRetry = onLoadMore
            )
        }
    }
}

/* -------------------------------------------------------------------------- */
/*                                Grid 实现                                    */
/* -------------------------------------------------------------------------- */

@Composable
private fun RefreshGridContent(
    gridState: LazyStaggeredGridState? = null,
    loadMoreState: LoadMoreState,
    onLoadMore: () -> Unit,
    shouldTriggerLoadMore: (lastIndex: Int, totalCount: Int) -> Boolean,
    content: LazyStaggeredGridScope.() -> Unit
) {
    val actualGridState = gridState ?: rememberLazyStaggeredGridState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = actualGridState.layoutInfo.visibleItemsInfo.lastOrNull()
            if (lastVisibleItem != null) {
                shouldTriggerLoadMore(
                    lastVisibleItem.index,
                    actualGridState.layoutInfo.totalItemsCount
                )
            } else false
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(SpacePaddingMedium),
        horizontalArrangement = Arrangement.spacedBy(SpacePaddingMedium),
        verticalItemSpacing = SpacePaddingMedium,
        state = actualGridState
    ) {
        content()
        item(span = StaggeredGridItemSpan.FullLine) {
            LoadMore(
                modifier = Modifier.padding(horizontal = SpaceHorizontalXXLarge),
                state = loadMoreState,
                listState = null,
                onRetry = onLoadMore
            )
        }
    }
}