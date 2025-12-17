package com.lyf.lingyingfacompose.ui.explore.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.lyf.lingyingfacompose.R
import com.lyf.lingyingfacompose.data.V3ExploreRecommendBean
import com.lyf.lingyingfacompose.utils.LikeCountFormatter





@Composable
fun ExploreRecommendScreen2(
    recommendItems: List<V3ExploreRecommendBean>,
    modifier: Modifier = Modifier,
    onItemClick: (V3ExploreRecommendBean) -> Unit = {},
    onFavoriteClick: (V3ExploreRecommendBean) -> Unit = {},
) {
    // 预取首屏附近图片：避免“滑几次才顺”（缓存热起来才顺）的体感
    // 注意：这里只预取少量（例如 10 个），避免瞬间触发过多网络/解码任务。
    val context = LocalContext.current
    val density = LocalDensity.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val itemWidthPx = remember(screenWidthDp, density) {
        with(density) { (screenWidthDp.dp / 2f).roundToPx() }
    }
    val avatarPx = remember(density) { with(density) { 18.dp.roundToPx() } }
    LaunchedEffect(recommendItems, itemWidthPx, avatarPx) {
        val imageLoader = context.imageLoader
        recommendItems.asSequence()
            .take(10)
            .forEach { item ->
                val coverHeightDp = when {
                    item.isMv && item.isPortraitMv -> 236.dp
                    item.isMv && item.isLandscapeMv -> 133.dp
                    item.isSong -> 177.dp
                    else -> 0.dp
                }
                val coverHeightPx = with(density) { coverHeightDp.roundToPx() }
                if (coverHeightPx > 0) {
                    item.imageUrl?.let { url ->
                        imageLoader.enqueue(
                            ImageRequest.Builder(context)
                                .data(url)
                                .size(itemWidthPx, coverHeightPx)
                                .build()
                        )
                    }
                }
                item.headImgUrl?.let { url ->
                    imageLoader.enqueue(
                        ImageRequest.Builder(context)
                            .data(url)
                            .size(avatarPx)
                            .build()
                    )
                }
            }
    }

    // 说明：
    // LazyVerticalStaggeredGrid(瀑布流) 在部分 Compose 版本/机型上更容易掉帧（测量与布局开销更大）。
    // 如果你更在乎滚动手感，这里用 LazyColumn + Row 做“双列”列表，性能更稳。
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 1.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        val rowCount = (recommendItems.size + 1) / 2
        items(
            count = rowCount,
            key = { rowIndex -> recommendItems[rowIndex * 2].id } // row key：用左侧 item id
        ) { rowIndex ->
            val leftIndex = rowIndex * 2
            val rightIndex = leftIndex + 1
            val leftItem = recommendItems[leftIndex]
            val rightItem = recommendItems.getOrNull(rightIndex)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                key(leftItem.id) {
                    ExploreRecommendItem(
                        item = leftItem,
                        modifier = Modifier.weight(1f),
                        onItemClick = onItemClick,
                        onFavoriteClick = onFavoriteClick
                    )
                }

                if (rightItem != null) {
                    key(rightItem.id) {
                        ExploreRecommendItem(
                            item = rightItem,
                            modifier = Modifier.weight(1f),
                            onItemClick = onItemClick,
                            onFavoriteClick = onFavoriteClick
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}






