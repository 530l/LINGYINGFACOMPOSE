package com.lyf.compose.feature.refresh

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lyf.compose.core.data.bean.ArticleBean
import com.lyf.compose.core.ui.components.scaffold.AppScaffold
import com.lyf.compose.feature.refresh.v.SwipeRefreshBox
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * 使用项目内统一的 SwipeRefreshBox 组件替换原有的 UltraSwipeRefresh 实现
 */
@Composable
fun RefreshScreen(viewModel: RefreshViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (uiState.articles.isEmpty()) {
            viewModel.refresh()
        }
    }

    // 如果后续需要可复用 loadMore 状态映射，
    // 可在此处添加；当前 SwipeRefreshBox 直接使用 isLoading/isFinishing
    AppScaffold(titleText = "文章列表") {
        // 直接根据状态渲染，避免 AnimatedContent 等容器拦截手势
        // 使用 SwipeRefreshBox（与 HomeScreen 一致的调用方式）
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        SwipeRefreshBox(
            items = uiState.articles as List<ArticleBean>?,
            isRefreshing = uiState.isRefreshing,
            isLoading = uiState.isLoadingMore,
            isFinishing = !uiState.hasMore,
            onRefresh = {
                Timber.d("RefreshScreen: onRefresh invoked from UI")
                coroutineScope.launch { viewModel.refresh() }
            },
            onLoad = {
                Timber.d("RefreshScreen: onLoad invoked from UI")
                coroutineScope.launch { viewModel.loadMore() }
            },
            modifier = Modifier.fillMaxSize(),
            listState = listState,
            contentPadding = remember { androidx.compose.foundation.layout.PaddingValues(0.dp) },
            key = { _, item -> item.id },
            contentType = { _, _ -> null }
        ) { _, item ->
            // item content: 和原来保持一致的展示
            GoodsListItem(goods = item)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoodsListItem(goods: ArticleBean) {
    // 复用原来的展示
    androidx.compose.material3.ListItem(
        modifier = Modifier.clip(com.lyf.compose.core.theme.ShapeMedium),
        headlineContent = { com.lyf.compose.core.ui.components.text.AppText(text = goods.title.ifBlank { "未命名商品" }) },
    )
}