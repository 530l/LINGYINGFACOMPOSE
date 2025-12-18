package com.lyf.lingyingfacompose.ui.explore.tab

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lyf.lingyingfacompose.data.MusicRankingItem
import com.lyf.lingyingfacompose.data.MusicRankingModule


/**
 * 音乐榜单模块组件
 */
@Composable
fun MusicRankingModuleScreen(rankingModule: MusicRankingModule) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF293329),
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp,
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左侧内容区域
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(Color.Transparent),
        ) {
            // 标题
            Text(
                text = rankingModule.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 榜单列表
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                rankingModule.items.take(3).forEach { item ->
                    RankingListItem(item = item)
                }
            }
        }

//        Spacer(modifier = Modifier.width(12.dp))

        // 右侧封面图片
        Box {

            Box(
                modifier = Modifier
                    .size(92.dp)
                    .offset(x = (-32).dp)
                    .background(
                        color = Color(0x1AFFFFFF),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .align(Alignment.Center)

            )
            //offset:当前元素整体 相对于它原本在父容器中应该摆放的位置进行偏移。
            //padding:当前元素内部 增加内边距，推开其子内容。
            Box(
                modifier = Modifier
                    .size(106.dp)
                    .offset(x = (-16).dp)
                    .background(
                        color = Color(0x33FFFFFF),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .align(Alignment.Center)
                //不懂？ 先align 再offset 没效果？？
            )

            AsyncImage(
                model = rankingModule.coverImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

/**
 * 榜单列表项组件
 */
@Composable
private fun RankingListItem(
    item: MusicRankingItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // 排名数字
        Text(
            text = "${item.rank}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.4f)
        )

        // 歌曲名称
        Text(
            text = item.title,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(0.65f).background(Color.Transparent)
        )

        // 艺术家名称
        Text(
            text = "@${item.artist}",
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.4f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.offset((-10).dp)
        )
    }
}


