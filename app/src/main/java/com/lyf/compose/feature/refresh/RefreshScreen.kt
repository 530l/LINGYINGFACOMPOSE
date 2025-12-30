package com.lyf.compose.feature.refresh

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lyf.compose.core.data.bean.ArticleBean
import com.lyf.compose.core.state.LoadMoreState
import com.lyf.compose.core.ui.components.scaffold.AppScaffold
import com.lyf.compose.core.ui.components.appbar.CenterTopAppBar
import com.lyf.compose.core.ui.components.text.AppText
import com.lyf.compose.feature.refresh.v.SwipeRefreshBox
import kotlinx.coroutines.launch
import timber.log.Timber


@Composable
fun RefreshScreen(viewModel: RefreshViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (uiState.articles.isEmpty()) {
            viewModel.onRefresh()
        }
    }

    var selectedTab by remember { mutableIntStateOf(0) }

    AppScaffold(
        titleText = null,
        topBar = {
            Column {
                CenterTopAppBar(titleText = "文章列表", showBackIcon = false)
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                        Text(
                            text = "List",
                            color = if (selectedTab == 0) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                        Text(
                            text = "Grid",
                            color = if (selectedTab == 1) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (selectedTab == 0) {
                NetworkListExample(viewModel = viewModel)
            } else {
                NetworkGriltExample(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun NetworkListExample(viewModel: RefreshViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val loadMoreState = when {
        uiState.isLoadingMore -> LoadMoreState.Loading
        uiState.errorMessage != null && uiState.articles.isNotEmpty() && !uiState.isRefreshing -> LoadMoreState.Error
        !uiState.hasMore -> LoadMoreState.NoMore
        else -> LoadMoreState.PullToLoad
    }

    SwipeRefreshBox(
        items = uiState.articles,
        isRefreshing = uiState.isRefreshing,
        loadMoreState = loadMoreState,
        onRefresh = {
            coroutineScope.launch { viewModel.onRefresh() }
        },
        onLoadMore = {
            coroutineScope.launch { viewModel.onLoadMore() }
        },
        modifier = Modifier.fillMaxSize(),
        listState = listState,
        shouldTriggerLoadMore = viewModel::shouldTriggerLoadMore,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
        key = { _, item -> item.id },
        columnItemContent = { index, item ->
            GoodsListItem(index = index, goods = item)
        }
    )
}


@Composable
private fun NetworkGriltExample(viewModel: RefreshViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val gridState = rememberLazyStaggeredGridState()
    val coroutineScope = rememberCoroutineScope()

    val loadMoreState = when {
        uiState.isLoadingMore -> LoadMoreState.Loading
        uiState.errorMessage != null && uiState.articles.isNotEmpty() && !uiState.isRefreshing -> LoadMoreState.Error
        !uiState.hasMore -> LoadMoreState.NoMore
        else -> LoadMoreState.PullToLoad
    }

    SwipeRefreshBox(
        items = uiState.articles,
        isRefreshing = uiState.isRefreshing,
        loadMoreState = loadMoreState,
        onRefresh = {
            coroutineScope.launch { viewModel.onRefresh() }
        },
        onLoadMore = {
            coroutineScope.launch { viewModel.onLoadMore() }
        },
        modifier = Modifier.fillMaxSize(),
        isGrid = true,
        gridState = gridState,
        contentPadding = PaddingValues(8.dp),
        shouldTriggerLoadMore = viewModel::shouldTriggerLoadMore,
        key = { _, item -> item.id },
        gridItemContent = { index, item ->
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .height(120.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                GoodsListItem(index = index, goods = item)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoodsListItem(index: Int, goods: ArticleBean) {
    androidx.compose.material3.ListItem(
        modifier = Modifier.clip(com.lyf.compose.core.theme.ShapeMedium),
        headlineContent = {
            AppText(
                text = " ${goods.title}----$index ",
            )
        },
    )
}