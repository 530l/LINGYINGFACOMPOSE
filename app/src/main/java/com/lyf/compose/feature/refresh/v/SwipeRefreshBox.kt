package com.lyf.compose.feature.refresh.v

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.util.Log
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clipToBounds
import com.lyf.compose.core.state.LoadMoreState
import com.lyf.compose.core.theme.SpaceHorizontalMedium
import com.lyf.compose.core.theme.SpaceHorizontalXXLarge
import com.lyf.compose.core.theme.SpaceVerticalMedium
import kotlinx.coroutines.delay

/**
 * 自定义下拉刷新 & 加载更多容器（支持列表 / 网格）
 *
 * @param items 列表数据（null 或 empty 表示无数据）
 * @param isRefreshing 是否正在下拉刷新
 * @param loadMoreState 加载更多状态
 * @param onRefresh 下拉刷新回调
 * @param onLoadMore 加载更多回调
 * @param modifier 外部修饰符
 * @param listState 列表滚动状态（用于 LazyColumn）
 * @param isGrid 是否使用网格布局（LazyVerticalStaggeredGrid）
 * @param gridState 网格滚动状态（用于 LazyVerticalStaggeredGrid）
 * @param shouldTriggerLoadMore 自定义触发加载更多的条件，默认：滑动到最后 3 项时触发
 * @param gridItemContent 网格项内容构建器（当 isGrid = true 时使用）
 * @param contentPadding 内容内边距
 * @param verticalArrangement 列表垂直排列方式（仅对 LazyColumn 生效）
 * @param key 唯一标识生成函数（用于提升性能）
 * @param contentType 内容类型（用于跳过 recomposition）
 * @param columnItemContent 列表项内容构建器（当 isGrid = false 时使用）
 * @param indicatorMinVisibleTime 指示器至少可见的毫秒数（默认 0，立即隐藏）
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun <T> SwipeRefreshBox(
    modifier: Modifier = Modifier,
    items: List<T> = emptyList(),
    isRefreshing: Boolean,
    loadMoreState: LoadMoreState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    listState: LazyListState = rememberLazyListState(),
    isGrid: Boolean = false,
    gridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    shouldTriggerLoadMore: ((lastIndex: Int, totalCount: Int) -> Boolean)? = null,
    gridItemContent: @Composable (LazyStaggeredGridScope.(index: Int, item: T) -> Unit) = { _, _ -> /* no-op */ },
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    key: ((index: Int, item: T) -> Any)? = null,
    contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    columnItemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit = { _, _ -> /* no-op */ },
    indicatorMinVisibleTime: Long = 500L
) {
    // 判断是否有数据
    val hasData = items.isNotEmpty()
    // 初始加载且无数据时显示加载中
    val showLoading = isRefreshing && !hasData
    // 非刷新、无数据且已加载完所有数据时显示空状态
    val showEmpty = !isRefreshing && !hasData && loadMoreState == LoadMoreState.NoMore


    when {
        showLoading -> {
            // 显示全局加载中界面
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                PageLoading() // 全屏加载组件
            }
        }

        showEmpty -> {
            // 显示空状态界面，点击可重新刷新
            EmptyContent(onClick = { onRefresh() })
        }

        else -> {
            // 确保下拉刷新指示器至少显示 indicatorMinVisibleTime 毫秒，
            // 避免因刷新过快导致指示器“闪现”而影响用户体验。
            var visibleRefreshing by remember { mutableStateOf(isRefreshing) }
            var indicatorShownAt by remember { mutableStateOf<Long?>(null) }

            // displayRefreshing 用于驱动 PullRefreshIndicator 和 PullRefreshState 的 refreshing 状态。
            // 它为 true 的条件是：外部正在刷新（isRefreshing == true）
            // 或者本地仍处于最小可见时间的延迟隐藏阶段（visibleRefreshing == true）。
            val displayRefreshing by remember { derivedStateOf { isRefreshing || visibleRefreshing } }

            // 监听 isRefreshing 变化，管理指示器的显示/隐藏生命周期：
            // - 当 isRefreshing 变为 true 时，记录首次显示时间并确保 visibleRefreshing = true；
            // - 当 isRefreshing 变为 false 时，计算已显示时长，若不足 indicatorMinVisibleTime，
            //   则延迟剩余时间后再将 visibleRefreshing 设为 false，从而保证最小可见时间。
            LaunchedEffect(isRefreshing, indicatorMinVisibleTime) {
                if (isRefreshing) {
                    if (indicatorShownAt == null) {
                        indicatorShownAt = System.currentTimeMillis()
                    }
                    visibleRefreshing = true
                } else {
                    val shownAt = indicatorShownAt ?: System.currentTimeMillis()
                    val elapsed = System.currentTimeMillis() - shownAt
                    val remaining = (indicatorMinVisibleTime - elapsed).coerceAtLeast(0L)
                    if (remaining > 0L) {
                        delay(remaining)

                    }
                    visibleRefreshing = false
                    indicatorShownAt = null
                }
            }

            // 包装原始 onRefresh 回调：
            // 当用户通过下拉手势触发刷新时，立即标记指示器已显示（visibleRefreshing = true），
            // 并记录显示起始时间，确保即使外部 isRefreshing 延迟更新，指示器也能及时响应。
            val wrappedOnRefresh = {
                visibleRefreshing = true
                indicatorShownAt = System.currentTimeMillis()
                onRefresh()
            }

            // 创建下拉刷新状态控制器，使用 displayRefreshing 保持 indicator 与手势状态的一致性
            val pullRefreshState = rememberPullRefreshState(displayRefreshing, wrappedOnRefresh)

            // 默认加载更多触发逻辑：滑动到倒数第3项时触发
            val actualShouldTriggerLoadMore =
                shouldTriggerLoadMore ?: { lastIndex, totalCount ->
                    totalCount > 0 && lastIndex >= totalCount - 3
                }

            // 包裹下拉刷新区域
            Box(
                modifier = modifier
                    .pullRefresh(pullRefreshState) // 启用下拉刷新手势
                    .clipToBounds()               // 裁剪超出部分
                    .background(MaterialTheme.colorScheme.surface), // 设置背景色
                contentAlignment = Alignment.TopCenter
            ) {
                // 内容区域（防止 indicator 被遮挡）
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                ) {
                    // 根据 isGrid 动态切换列表/网格布局，并处理加载更多逻辑
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
                            // 网格项遍历渲染
                            itemsIndexed(
                                items = items,
                                key = key?.let { k -> { index, item -> k(index, item) } }
                            ) { index, item ->
                                gridItemContent.invoke(gridScope, index, item)
                            }
                        },
                        content = {
                            // 列表项遍历渲染
                            itemsIndexed(
                                items = items,
                                key = key,
                                contentType = contentType
                            ) { index, item ->
                                columnItemContent(index, item)
                            }
                        }
                    )
                }

                // 下拉刷新指示器（圆形进度条）——使用 visibleRefreshing 来控制可见时长
                PullRefreshIndicator(
                    refreshing = displayRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    scale = true // 支持缩放动画
                )
            }
        }
    }
}

/* -------------------------------------------------------------------------- */
/*                               内容切换容器                                  */
/* -------------------------------------------------------------------------- */

/**
 * 根据 isGrid 动画切换列表与网格布局
 */
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
    // 使用 AnimatedContent 实现布局切换动画
    AnimatedContent(
        targetState = isGrid,
        transitionSpec = {
            // 进入动画：淡入 + 缩放
            (fadeIn(tween(300, easing = LinearEasing)) +
                    scaleIn(initialScale = 0.92f, animationSpec = tween(300)))
                .togetherWith(
                    // 退出动画：淡出 + 缩小
                    fadeOut(tween(300)) +
                            scaleOut(targetScale = 0.92f, animationSpec = tween(300))
                )
        },
        label = "layout_switch"
    ) { targetIsGrid ->
        if (targetIsGrid) {
            // 渲染网格布局（含加载更多）
            RefreshGridContent(
                gridState = gridState,
                loadMoreState = loadMoreState,
                onLoadMore = onLoadMore,
                shouldTriggerLoadMore = shouldTriggerLoadMore,
                contentPadding = contentPadding,
                content = gridContent
            )
        } else {
            // 渲染列表布局（含加载更多）
            RefreshListContent(
                listState = listState,
                loadMoreState = loadMoreState,
                onLoadMore = onLoadMore,
                shouldTriggerLoadMore = shouldTriggerLoadMore,
                contentPadding = contentPadding,
                verticalArrangement = verticalArrangement,
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
    listState: LazyListState,
    loadMoreState: LoadMoreState,
    onLoadMore: () -> Unit,
    shouldTriggerLoadMore: (lastIndex: Int, totalCount: Int) -> Boolean,
    contentPadding: PaddingValues,
    verticalArrangement: Arrangement.Vertical,
    content: LazyListScope.() -> Unit
) {
    // 监听滚动位置，判断是否应触发加载更多
    // 使用 remember + derivedStateOf 创建一个“派生状态”
    // 它会在依赖的状态（如 listState）发生变化时自动重新计算， 但只有当计算结果真正改变时，才会触发重组（recomposition）， 从而避免不必要的性能开销。
    val shouldLoadMore by remember {
        derivedStateOf {
            // 获取当前 LazyColumn 中最后一个可见项的信息
            // visibleItemsInfo 是一个包含当前屏幕上所有可见 item 的列表
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()

            // 如果存在可见项，则判断是否满足“触发加载更多”的条件
            if (lastVisibleItem != null) {
                // 调用用户传入的 shouldTriggerLoadMore 函数（或使用默认逻辑）：
                // - lastIndex：最后一个可见项的索引（从0开始）
                // - totalCount：列表中总的数据项数量
                shouldTriggerLoadMore(
                    lastVisibleItem.index,                    // 当前滚动到的最后可见项索引
                    listState.layoutInfo.totalItemsCount     // 列表总项数
                )
            } else {
                // 如果还没有任何可见项（比如刚进入页面、数据为空等），则不触发加载
                false
            }
        }
    }

    // 当 shouldLoadMore 变为 true 时触发 onLoadMore
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        modifier = Modifier.fillMaxSize()
    ) {
        // 插入用户定义的列表项
        content()
        // 在末尾添加加载更多组件（占满一行）
        item {
            LoadMore(
                modifier = Modifier.padding(horizontal = SpaceHorizontalXXLarge),
                state = loadMoreState,
                listState = if (loadMoreState == LoadMoreState.Loading) listState else null,
                onRetry = onLoadMore // 重试回调
            )
        }
    }
}

/* -------------------------------------------------------------------------- */
/*                                Grid 实现                                    */
/* -------------------------------------------------------------------------- */
@Composable
private fun RefreshGridContent(
    gridState: LazyStaggeredGridState,
    loadMoreState: LoadMoreState,
    onLoadMore: () -> Unit,
    shouldTriggerLoadMore: (lastIndex: Int, totalCount: Int) -> Boolean,
    contentPadding: PaddingValues,
    content: LazyStaggeredGridScope.() -> Unit
) {
    // 监听网格滚动位置，判断是否应触发加载更多
    // 使用 remember + derivedStateOf 创建一个派生状态 shouldLoadMore，
    // 它会根据 gridState（网格滚动状态）的变化自动、高效地重新计算，
    // 且仅在结果真正改变时才通知下游（如 LaunchedEffect），避免不必要的 recomposition。
    val shouldLoadMore by remember {
        derivedStateOf {
            // 获取当前屏幕上最后一个可见的网格项信息
            // visibleItemsInfo 是一个列表，包含当前可视区域内的所有 item 布局信息
            val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()

            // 如果存在至少一个可见项，则进一步判断是否应触发“加载更多”
            if (lastVisibleItem != null) {
                // 调用用户传入的 shouldTriggerLoadMore 条件函数（或使用默认逻辑）：
                // - 参数1：lastVisibleItem.index → 最后一个可见项在数据列表中的索引（从0开始）
                // - 参数2：gridState.layoutInfo.totalItemsCount → 网格中总的数据项数量
                shouldTriggerLoadMore(
                    lastVisibleItem.index,                    // 当前滚到的最后可见项索引
                    gridState.layoutInfo.totalItemsCount     // 总项数
                )
            } else {
                // 如果尚无可显示的项（例如刚进入页面、数据为空、尚未布局完成等），
                // 则暂时不触发加载更多
                false
            }
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2), // 固定两列
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(SpaceHorizontalMedium), // 水平间距
        verticalItemSpacing = SpaceVerticalMedium, // 垂直间距
        state = gridState
    ) {
        // 插入用户定义的网格项
        content()
        // 在末尾添加加载更多组件（跨整行）
        item(span = StaggeredGridItemSpan.FullLine) {
            LoadMore(
                modifier = Modifier.padding(horizontal = SpaceHorizontalXXLarge),
                state = loadMoreState,
                listState = null, // 网格不支持自动滚动到底部
                onRetry = onLoadMore
            )
        }
    }
}
