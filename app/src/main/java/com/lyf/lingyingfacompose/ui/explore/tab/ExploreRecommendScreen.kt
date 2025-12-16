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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lyf.lingyingfacompose.R
import com.lyf.lingyingfacompose.data.V3ExploreRecommendBean
import com.lyf.lingyingfacompose.ui.explore.ExploreViewModel
import com.lyf.lingyingfacompose.utils.LikeCountFormatter

@Composable
fun ExploreRecommendScreen(viewModel: ExploreViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val recommendItems = uiState.recommendItems
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 2.dp,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(horizontal = 1.dp)
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


@Composable
fun ExploreRecommendItem(
    item: V3ExploreRecommendBean,
    modifier: Modifier = Modifier,
    onFavoriteClick: () -> Unit = {},
    onItemClick: () -> Unit = {}
) {
    // 只有未删除时才允许点击整个 Item
    val clickableModifier = if (!item.isDeleted) {
        Modifier.clickable { onItemClick() }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)
            .then(clickableModifier)
    ) {
        // === 主体内容 ===
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 1.dp)
        ) {
            // 封面区域
            val imageHeightDp = when {
                item.isMv && item.isPortraitMv -> 236.dp
                item.isMv && item.isLandscapeMv -> 133.dp
                item.isSong -> 177.dp
                else -> 0.dp
            }

            if (imageHeightDp > 0.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeightDp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.icon_logo),
                        error = painterResource(R.drawable.icon_logo)
                    )

                    // MV 播放按钮
                    if (item.isMv) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .offset(x = 10.dp, y = 10.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.6f),
                                    RoundedCornerShape(4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.icon_explore_v4_play),
                                contentDescription = "Play",
                                tint = Color.Unspecified
                            )
                        }
                    }

                    // 活动标签（左下角）
                    item.activityTitle?.takeIf { it.isNotEmpty() }?.let { title ->
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .offset(x = 8.dp, y = (-8).dp)
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(vertical = 3.dp, horizontal = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.icon_activity_title_left_drawable_iv),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = title,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.widthIn(max = 100.dp)
                                )
                            }
                        }
                    }

                    // 排名标签（左下角）
                    //在 Jetpack Compose 中，Modifier.offset() 是用来调整组件相对于其原始位置的偏移量。
                    // 这个函数允许你通过指定 x 和 y 的值来移动元素，其中：
                    //x：表示水平方向上的偏移量。正值会使元素向右移动，而负值则向左移动。
                    //y：表示垂直方向上的偏移量。正值会使元素向下移动，而负值则向上移动。
                    if (item.isShowRanking) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .offset(x = 12.dp, y = (-8).dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFF3C00B3),
                                                    Color(0xFF1C014E),
                                                    Color(0xFF0A0214)
                                                )
                                            ),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color(0x995D3C7F),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .alpha(0.8f)
                                        .padding(vertical = 3.dp, horizontal = 6.dp)
                                        .padding(start = 6.dp) // 补偿左侧图标
                                ) {
                                    Text(
                                        text = "第${item.ranking}名",
                                        color = item.rankingTextColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.widthIn(max = 120.dp)
                                    )
                                }
                            }
                            Icon(
                                painter = painterResource(R.drawable.icon_ranking_title_left_drawable),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(22.dp)
                                    .offset(x = (-12).dp, y = (-2).dp)//offset 是内容的偏移量？
                            )
                        }
                    }
                }
            }

            // 标题
            item.title?.takeIf { it.isNotEmpty() }?.let { title ->
                Text(
                    text = title,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 8.dp, end = 24.dp)
                )
            }

            // 用户头像 + 昵称 + 点赞
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = if (item.title.isNullOrBlank()) 8.dp else 4.dp,
                        start = 8.dp,
                        end = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 用户信息
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = item.headImgUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape),
                        placeholder = painterResource(R.drawable.icon_logo),
                        error = painterResource(R.drawable.icon_logo)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.userName ?: "",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 80.dp)
                    )
                }

                // 点赞
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onFavoriteClick() }
                ) {
                    Icon(
                        painter = painterResource(
                            if (item.praised) R.drawable.icon_explore_v4_favorite
                            else R.drawable.icon_explore_v4_un_favorite
                        ),
                        contentDescription = "Favorite",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = LikeCountFormatter.formatLikeCount(item.amountPraise),
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // === 删除遮罩（必须放在最后！）===
        if (item.isDeleted) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "该作品已被作者删除",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }
    }
}