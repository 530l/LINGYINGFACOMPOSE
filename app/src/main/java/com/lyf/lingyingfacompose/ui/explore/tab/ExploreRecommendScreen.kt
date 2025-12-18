package com.lyf.lingyingfacompose.ui.explore.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyf.lingyingfacompose.ui.explore.ExploreViewModel

@Composable
fun ExploreRecommendScreen(viewModel: ExploreViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val recommendItems = uiState.recommendItems
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF14161B))
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 2.dp,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = PaddingValues(
                start = 1.dp,
                end = 1.dp,
                top = 12.dp,
                bottom = 12.dp
            ),
        ) {
            items(
                count = recommendItems.size,
                key = { index -> recommendItems[index].id } // 👈 关键：通过 index 获取 item.id 作为 key
            ) { index ->
                ExploreRecommendItem(
                    item = recommendItems[index],
                    onItemClick = { /* 跳转 */ },
                    onFavoriteClick = { /* 切换点赞 */ }
                )
            }
        }
    }
}


