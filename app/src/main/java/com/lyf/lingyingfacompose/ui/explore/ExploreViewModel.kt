package com.lyf.lingyingfacompose.ui.explore

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.lyf.lingyingfacompose.data.ExploreBannerItem
import com.lyf.lingyingfacompose.data.ExploreMenuItem
import com.lyf.lingyingfacompose.data.ExploreTabItem
import com.lyf.lingyingfacompose.data.ExploreUiState
import com.lyf.lingyingfacompose.data.V3ExploreRecommendBean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class ExploreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        val items = listOf(
            ExploreBannerItem(
                id = "1",
                title = "今日推荐 · 氛围音乐",
                subtitle = "放松身心的精选曲目",
                bannerImageUrl = "https://cdn1.muse.top//static//banner//global_release_banner.png",
            ),
            ExploreBannerItem(
                id = "2",
                title = "城市夜色 · Lo-Fi",
                subtitle = "适合夜读与写作的节奏",
                bannerImageUrl = "https://cdn1.muse.top//static//banner//929339f4319e48c3b2110cae7ab18247.png",
            ),
            ExploreBannerItem(
                id = "3",
                title = "专注 · 纯音乐",
                subtitle = "帮助你进入心流状态",
                bannerImageUrl = "https://cdn1.muse.top//static//banner//e9fbfeb88ed14fae9932ccf10498ffd1.png",
            ),
            ExploreBannerItem(
                id = "4",
                title = "专注 · 纯音乐",
                subtitle = "帮助你进入心流状态",
                bannerImageUrl = "https://cdn1.muse.top//static//banner//a1318eefb068410d9fe25f568a3c81bb.png",
            ),
        )

        val menuItems = listOf(
            ExploreMenuItem(1, "大师写歌", "https://cdn1.muse.top/static/icon/function_cover.png"),
            ExploreMenuItem(2, "热歌改编", "https://cdn1.muse.top/static/icon/function_cover.png"),
            ExploreMenuItem(3, "AI MV", "https://cdn1.muse.top/static/icon/function_cover.png"),
            ExploreMenuItem(
                4,
                "AI翻唱",
                "https://cdn1.muse.top/static/icon/function_cover.png",
                isComingSoon = true
            ),
            ExploreMenuItem(5, "速配MV", "https://cdn1.muse.top/static/icon/function_cover.png"),
            ExploreMenuItem(6, "速配MV6", "https://cdn1.muse.top/static/icon/function_cover.png"),
            ExploreMenuItem(7, "速配MV7", "https://cdn1.muse.top/static/icon/function_cover.png"),
            ExploreMenuItem(8, "速配MV8", "https://cdn1.muse.top/static/icon/function_cover.png")

        )


        val tabItems = listOf(
            ExploreTabItem(1, "推荐"),
            ExploreTabItem(2, "活动"),
            ExploreTabItem(3, "榜单"),
            ExploreTabItem(4, "专栏"),
        )


        val tempRecommendItems = listOf(
            V3ExploreRecommendBean(
                id = 1.toString(),
                imageUrl = "https://picsum.photos/300/400?random=1",
                isMv = true,
                isPortraitMv = true,
                activityTitle = "夏日创作赛",
                isShowRanking = false,
                title = "《星夜独白》- 原创氛围电子",
                headImgUrl = "https://picsum.photos/50?random=user1",
                userName = "林深时见鹿",
                praised = false,
                amountPraise = 1289L,
                isDeleted = false
            ),
            V3ExploreRecommendBean(
                id = 2.toString(),
                imageUrl = "https://picsum.photos/500/300?random=2",
                isMv = true,
                isLandscapeMv = true,
                isShowRanking = true,
                ranking = 3,
                rankingTextColor = Color(0xFFFFD700), // 金色
                title = "《城市边缘》- Lo-Fi Chillhop",
                headImgUrl = "https://picsum.photos/50?random=user2",
                userName = "ChillMaker",
                praised = true,
                amountPraise = 5621L,
                isDeleted = false
            ),
            V3ExploreRecommendBean(
                id = 3.toString(),
                imageUrl = "https://picsum.photos/300/350?random=3",
                isSong = true,
                title = "《雨落长安》- 古风纯音乐",
                headImgUrl = "https://picsum.photos/50?random=user3",
                userName = "墨染青衣",
                praised = false,
                amountPraise = 892L,
                isDeleted = false
            ),
            V3ExploreRecommendBean(
                id = 4.toString(),
                imageUrl = "https://picsum.photos/300/500?random=4",
                isMv = true,
                isPortraitMv = true,
                title = "《梦境碎片》- AI 生成 MV",
                headImgUrl = "https://picsum.photos/50?random=user4",
                userName = "AI Dreamer",
                praised = false,
                amountPraise = 341L,
                isDeleted = true // 👈 模拟已删除作品
            ),
            V3ExploreRecommendBean(
                id = 5.toString(),
                imageUrl = "https://picsum.photos/400/250?random=5",
                isMv = true,
                isLandscapeMv = true,
                activityTitle = "AI 翻唱大赛",
                title = "《起风了》AI 女声版",
                headImgUrl = "https://picsum.photos/50?random=user5",
                userName = "VoiceSynth",
                praised = true,
                amountPraise = 12043L,
                isDeleted = false
            ),
            V3ExploreRecommendBean(
                id = 6.toString(),
                imageUrl = "https://picsum.photos/300/320?random=6",
                isSong = true,
                isShowRanking = true,
                ranking = 1,
                rankingTextColor = Color(0xFFFF6B6B),
                title = "《心跳频率》- 电音热单",
                headImgUrl = "https://picsum.photos/50?random=user6",
                userName = "BeatMaster",
                praised = false,
                amountPraise = 23456L,
                isDeleted = false
            )
        )


        val recommendItems = generateRecommendItems(
            startIndex = 0,
            count = 30,
            existingList = tempRecommendItems.toMutableList()
        )

        _uiState.value = _uiState.value.copy(
            bannerItems = items,
            menuItems = menuItems,
            tabItems = tabItems,
            recommendItems = recommendItems,
            currentIndex = 0,
        )
    }


    fun generateRecommendItems(
        startIndex: Int,
        count: Int,
        existingList: MutableList<V3ExploreRecommendBean> = mutableListOf()
    ): MutableList<V3ExploreRecommendBean> {
        val titles = listOf(
            "《银河漂流》", "《午夜咖啡馆》", "《山海之间》", "《数字梦境》", "《旧磁带》",
            "《霓虹雨》", "《风起云涌》", "《静默回声》", "《像素心跳》", "《月光代码》"
        )
        val artists = listOf(
            "Echo", "Nova", "SilentFlow", "PixelTune", "AuroraWave",
            "ZenMind", "CyberSoul", "DreamWeaver", "NeonRhythm", "StarGazer"
        )
        val activities = listOf("AI 创作赛", "夏日热单", "新声计划", "古风季", null, null, null)

        var index = startIndex
        repeat(count) {
            val isPortrait = (index % 3 == 0)
            val isLandscape = (index % 3 == 1)
            val isSongType = !isPortrait && !isLandscape

            val isMv = isPortrait || isLandscape
            val activity = if (index % 5 == 0) activities.random() else null

            val isDeleted = index % 9 == 0
            val showRanking = index % 7 == 0 && !isDeleted
            val ranking = if (showRanking) (1..10).random() else 0
            val rankingColor = when (ranking) {
                1 -> Color(0xFFFFD700) // 金牌
                2 -> Color(0xFFC0C0C0) // 银牌
                3 -> Color(0xFFCD7F32) // 铜牌
                else -> Color.White
            }

            val praised = (index % 4 != 0)
            val likes = (100L..50000L).random()

            // 固定图片 URL（来自你的日志）
            val portraitUrl = "https://cdn-work.muse.top/work/image/168a192c82864cb8992e8bfe4119a3af.jpeg"
            val landscapeUrl = "https://cdn1.muse.top/image_6d6cbf1e-5556-4f14-8aaa-9c3aa639b4eb.jpeg"
            val songUrl = "https://cdn1.muse.top/147681da-cfcd-4dde-b401-e8470a5fa8fe.jpeg"

            existingList.add(
                V3ExploreRecommendBean(
                    id = "@@$index",
                    imageUrl = when {
                        isPortrait -> portraitUrl
                        isLandscape -> landscapeUrl
                        else -> songUrl
                    },
                    isMv = isMv,
                    isPortraitMv = isPortrait,
                    isLandscapeMv = isLandscape,
                    isSong = isSongType,
                    activityTitle = activity,
                    isShowRanking = showRanking,
                    ranking = ranking,
                    rankingTextColor = rankingColor,
                    title = "${titles[index % titles.size]} - ${listOf("原创", "AI生成", "Remix", "纯音乐")[index % 4]}",
                    headImgUrl = portraitUrl, // 复用 portrait 图作为头像（或可替换为独立头像）
                    userName = artists[index % artists.size],
                    praised = praised,
                    amountPraise = likes,
                    isDeleted = isDeleted
                )
            )
            index++
        }
        return existingList
    }


}


