package com.lyf.lingyingfacompose.ui.explore.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyf.lingyingfacompose.data.MusicRankingItem
import com.lyf.lingyingfacompose.data.MusicRankingModule

@Composable
fun ExploreRankingScreen() {
    val list = remember { mutableStateListOf("1","2","3"/*,"4","5"*/) }
    LazyColumn(
        modifier = Modifier,
        // contentPadding 相当于rv的addItemDecoration的上下左右
        contentPadding = PaddingValues(
            start = 12.dp,
            end = 12.dp,
            top = 12.dp,
            bottom = 12.dp
        ),
        // 每个item之间间隔12.dp,不包含头尾的12.dp
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(list.size) { index ->
            MusicRankingModuleScreen(
                rankingModule = MusicRankingModule(
                    title = "周榜音乐",
                    items = listOf(
                        MusicRankingItem(1, "文字最多显示到这里", "文字最多..." ,),
                        MusicRankingItem(2, "伤心的人别停慢歌伤心的人别停慢歌", "五月天"),
                        MusicRankingItem(3, "Say you Love Me Say you Love Me", "Patti Ausetin")
                    ),
                    coverImageUrl = "https://cdn-work.muse.top//work_mv//image//2cc88719774941218479f25ec8dd6f54.jpeg"
                )
            )
        }

    }

}