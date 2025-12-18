package com.lyf.lingyingfacompose.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.lyf.lingyingfacompose.utils.TimeFormatUtil

@Stable
data class ExploreUiState(
    val bannerItems: List<ExploreBannerItem> = emptyList(),
    val menuItems: List<ExploreMenuItem> = emptyList(),
    val tabItems: List<ExploreTabItem> = emptyList(),
    val recommendItems: List<V3ExploreRecommendBean> = emptyList(),
    val exploreActivityItems: List<V3ExploreActivityBean> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@Stable
data class ExploreBannerItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val bannerImageUrl: String,
)

@Stable
data class ExploreMenuItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val isComingSoon: Boolean = false
)
@Stable
data class ExploreTabItem(
    val id: Int,
    val title: String,
)

@Stable
data class V3ExploreRecommendBean(
    val id: String,
    val imageUrl: String? = null,
    val isMv: Boolean = false,
    val isPortraitMv: Boolean = false,
    val isLandscapeMv: Boolean = false,
    val isSong: Boolean = false,
    val activityTitle: String? = null,
    val isShowRanking: Boolean = false,
    val ranking: Int = 0,
    val rankingTextColor: Color = Color.White,
    val title: String? = null,
    val headImgUrl: String? = null,
    val userName: String? = null,
    val praised: Boolean = false,
    val amountPraise: Long = 0L,
    val isDeleted: Boolean = false
)

/**
 * 音乐榜单项数据模型
 */
data class MusicRankingItem(
    val rank: Int,
    val title: String,
    val artist: String
)

/**
 * 音乐榜单模块数据模型
 */
data class MusicRankingModule(
    val title: String = "周榜音乐",
    val items: List<MusicRankingItem>,
    val coverImageUrl: String? = null
)



data class V3ExploreActivityBean(
    val id: String,

    val title: String,

    val subTitle: String,

    val workCount: Int = 0,

    val imageUrl: String,

    val startTimestamp: Long? = null,

    val endTimestamp: Long? = null,

    val activityStatus: Int, // 0-未开始 1-进行中 2-结束

    val awardUsers: List<AwardUser> = emptyList(),

    val enable: Boolean = true,

    val schemeUrl: String? = null,

    // Compose 侧状态（UI State）
    val isPitchUp: Boolean = false
) {

    val endTimestampStr: String
        get() = endTimestamp?.let {
            TimeFormatUtil.formatTimestampToYearMonthDay(it)
        }.orEmpty()

    data class AwardUser(
        val userName: String,
        val headImgUrl: String
    )
}