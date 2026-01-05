package com.lyf.compose.feature.explore.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.lyf.compose.core.data.bean.V3ExploreActivityBean
import com.lyf.compose.feature.explore.ExploreViewModel
import com.lyf.compose.utils.LikeCountFormatter

@Composable
fun ExploreActivityScreen(
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val list = uiState.exploreActivityItems

    LazyColumn {
        items(
            count = list.size,
            key = { index -> list[index].id }
        ) { index ->
            ExploreActivityItem(list[index])
        }
    }
}


@Composable
fun ExploreActivityItem(
    model: V3ExploreActivityBean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF202226))
    ) {

        // ===== 左侧封面 =====
        Box {
            AsyncImage(
                model = model.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(184.dp, 104.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            if (model.activityStatus != 0) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x80000000))
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (model.activityStatus == 1) "进行中·" else "已结束·",
                        color = if (model.activityStatus == 1)
                            Color(0xFFFFC95D)
                        else
                            Color(0xE6FFFFFF),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "作品数 ${
                            LikeCountFormatter.formatWorkCount(model.workCount.toLong())
                        }",
                        color = Color(0xB3FFFFFF),
                        fontSize = 10.sp
                    )
                }
            }
        }

        // ===== 右侧内容 =====
        Column(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = model.title,
                color = Color(0xE6FFFFFF),
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = model.subTitle.orEmpty(),
                color = if (model.activityStatus == 1)
                    Color(0xFF8489E7)
                else
                    Color(0x4DFFFFFF),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            if (model.activityStatus != 2) {
                Text(
                    text = model.endTimestampStr + " 截止",
                    color = Color(0x4DFFFFFF),
                    fontSize = 12.sp
                )
            } else if (model.awardUsers.isNotEmpty()) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = "获奖用户：",
                        color = Color(0x4DFFFFFF),
                        fontSize = 10.sp
                    )

                    LazyRow(
                        modifier = Modifier.padding(start = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            count = model.awardUsers.size,
                            key = { index -> model.awardUsers[index].userName }
                        ) { index ->
                            AsyncImage(
                                model = model.awardUsers[index].headImgUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}
