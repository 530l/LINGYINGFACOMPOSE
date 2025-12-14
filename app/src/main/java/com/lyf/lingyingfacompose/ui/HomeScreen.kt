package com.lyf.lingyingfacompose.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyf.lingyingfacompose.R


@Preview
@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
            /*.background(Color.White)*/
        ) {
            Text(
                "selectedTab $selectedTab",
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        WxNavigationBar(selected = selectedTab, onSelected = {
            selectedTab = it
        })
    }
}

@Composable
private fun WxNavigationBar(selected: Int, onSelected: (Int) -> Unit) {
    Row {
        TabItem(
            iconId = if (selected == 0)
                R.drawable.icon_explore_selected else R.drawable.icon_explore,
            title = "探索",
            tint = if (selected == 0) Color.Black else Color.Gray,
            modifier = Modifier
                .weight(1f)
                .clickable { onSelected(0) }
        )
        TabItem(
            iconId = if (selected == 1)
                R.drawable.icon_create2 else R.drawable.icon_create2,
            title = "创作",
            tint = if (selected == 1) Color.Black else Color.Gray,
            modifier = Modifier
                .weight(1f)
                .clickable { onSelected(1) }
        )
        TabItem(
            iconId = if (selected == 2)
                R.drawable.icon_tab_asset_selected else R.drawable.icon_tab_asset,
            title = "资产",
            tint = if (selected == 2) Color.Black else Color.Gray,
            modifier = Modifier
                .weight(1f)
                .clickable { onSelected(2) }
        )
        TabItem(
            iconId = if (selected == 3)
                R.drawable.icon_mine_selected else R.drawable.icon_mine,
            title = "我的",
            tint = if (selected == 3) Color.Black else Color.Gray,
            modifier = Modifier
                .weight(1f)
                .clickable { onSelected(3) }
        )
    }
}

@Composable
private fun TabItem(
    @DrawableRes iconId: Int,
    title: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            top = 10.dp,
            bottom = 10.dp
        ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painterResource(iconId), title, Modifier.size(24.dp))
        Text(title, fontSize = 12.sp, color = tint)
    }
}


@Composable
fun MusicRankingItem(
    title: String = "周榜音乐",
    songs: List<String> = listOf(
        "1 文字最多显示到这里... @文字最多...",
        "2 伤心的人别停慢歌 @五月天",
        "3 Say you Love Me @Patti Ausetin"
    ),
    imageResource: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .then(Modifier.height(120.dp)),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                songs.forEach { song ->
                    Text(
                        text = song,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Image(
                modifier = Modifier.size(90.dp),
                imageVector = ImageVector.vectorResource(imageResource),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.9f
            )
        }
    }
}

