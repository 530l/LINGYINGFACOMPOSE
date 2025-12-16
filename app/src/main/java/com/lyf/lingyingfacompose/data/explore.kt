package com.lyf.lingyingfacompose.data

import androidx.compose.ui.graphics.Color

data class ExploreUiState(
    val bannerItems: List<ExploreBannerItem> = emptyList(),
    val menuItems: List<ExploreMenuItem> = emptyList(),
    val tabItems: List<ExploreTabItem> = emptyList(),
    val recommendItems: List<V3ExploreRecommendBean> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
)


data class ExploreBannerItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val bannerImageUrl: String,
)


data class ExploreMenuItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val isComingSoon: Boolean = false
)

data class ExploreTabItem(
    val id: Int,
    val title: String,
)


data class V3ExploreRecommendBean(
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