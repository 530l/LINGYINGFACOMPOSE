package com.lyf.lingyingfacompose.data

data class ExploreUiState(
    val bannerItems: List<ExploreBannerItem> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
)


data class ExploreBannerItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val bannerImageUrl: String,
    val detailImageUrl: String,
)

