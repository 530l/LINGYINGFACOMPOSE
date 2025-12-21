package com.lyf.compose.feature.explore.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lyf.compose.R
import com.lyf.compose.core.data.bean.V3ExploreRecommendBean
import com.lyf.compose.utils.LikeCountFormatter

// 这些常量放在文件顶部，避免在每次重组时重复创建 shape/brush 等对象（小优化，但很常见）
private val CardShape = RoundedCornerShape(8.dp)
private val TagShape = RoundedCornerShape(4.dp)
private val RankingGradientBrush = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF3C00B3),
        Color(0xFF1C014E),
        Color(0xFF0A0214),
    )
)
private val RankingBorderColor = Color(0x995D3C7F)

@Composable
fun ExploreRecommendItem(
    item: V3ExploreRecommendBean,
    modifier: Modifier = Modifier,
    onFavoriteClick: (V3ExploreRecommendBean) -> Unit = {},
    onItemClick: (V3ExploreRecommendBean) -> Unit = {}
) {
    // 让 click lambda 稳定：避免父级重组时每个 item 都重新分配 lambda（列表里常见的微优化）
    val itemClick = remember(item.id, onItemClick) { { onItemClick(item) } }
    val favoriteClick = remember(item.id, onFavoriteClick) { { onFavoriteClick(item) } }

    // 只有未删除时才允许点击整个 Item
    val clickableModifier = if (!item.isDeleted) {
        Modifier.clickable(onClick = itemClick)
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
            val imageHeightDp =
                remember(item.isMv, item.isPortraitMv, item.isLandscapeMv, item.isSong) {
                    when {
                        item.isMv && item.isPortraitMv -> 236.dp
                        item.isMv && item.isLandscapeMv -> 133.dp
                        item.isSong -> 177.dp
                        else -> 0.dp
                    }
                }

            if (imageHeightDp > 0.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeightDp)
                        .clip(CardShape)
                ) {
//                    AsyncImage(
//                        model = item.imageUrl,
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Crop,
//                        placeholder = painterResource(R.drawable.icon_logo),
//                        error = painterResource(R.drawable.icon_logo)
//                    )

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Journal Image 2",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        clipToBounds = true
                    )


                    // 音乐类型标签（左上角）
                    if (item.isSong || item.isMv) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = 8.dp, y = 8.dp)
                                .size(18.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.6f),
                                    TagShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            // 这里需要音乐图标，暂时使用播放图标作为占位
                            Icon(
                                painter = painterResource(R.drawable.icon_explore_v4_play),
                                contentDescription = "Music Type",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // MV 播放按钮
                    if (item.isMv) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .offset(x = 10.dp, y = 10.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.6f),
                                    TagShape
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
                                    TagShape
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
                    //offset:当前元素整体 相对于它原本在父容器中应该摆放的位置进行偏移。
                    //padding:当前元素内部 增加内边距，推开其子内容。
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
                                            brush = RankingGradientBrush,
                                            shape = TagShape
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = RankingBorderColor,
                                            shape = TagShape
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                )
            }

            // 用户头像 + 昵称 + 点赞
            // 避免每次重组都重新格式化点赞文案（格式化一般包含除法/拼接）
            val likeText = remember(item.amountPraise) {
                LikeCountFormatter.formatLikeCount(item.amountPraise)
            }
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
//                    AsyncImage(
//                        model = item.headImgUrl,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(18.dp)
//                            .clip(CircleShape),
//                        placeholder = painterResource(R.drawable.icon_logo),
//                        error = painterResource(R.drawable.icon_logo)
//                    )

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Journal Image 2",
                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize(),
                        modifier = Modifier
                            .size(18.dp),
                        clipToBounds = true
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.userName ?: "",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                // 点赞
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = favoriteClick)
                ) {
                    Icon(
                        painter = painterResource(
                            if (item.praised) R.drawable.icon_explore_v4_favorite
                            else R.drawable.icon_explore_v4_un_favorite
                        ),
                        contentDescription = "Favorite",
                        tint = Color.Unspecified,
                        // tint = if (item.praised) Color(0xFFFF5A5F) else Color.Unspecified,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = likeText,
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
                    // 注意：clip 要放在 background 之前，才能让遮罩也被圆角裁剪
                    .clip(CardShape)
                    .background(Color.Black.copy(alpha = 0.8f)),
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