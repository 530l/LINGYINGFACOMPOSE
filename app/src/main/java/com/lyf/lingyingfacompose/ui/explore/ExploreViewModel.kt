package com.lyf.lingyingfacompose.ui.explore

import androidx.lifecycle.ViewModel
import com.lyf.lingyingfacompose.data.ExploreBannerItem
import com.lyf.lingyingfacompose.data.ExploreUiState
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
                bannerImageUrl = "https://wanx.alicdn.com/wanx/1086121689920057/text_to_image_lite_v2/aac02ce82ac246c4a672735b03249a6c_0_visibleWatermark.png",
                detailImageUrl = "https://wanx.alicdn.com/wanx/1086121689920057/text_to_image_lite_v2/aac02ce82ac246c4a672735b03249a6c_0_visibleWatermark.png",
            ),
            ExploreBannerItem(
                id = "2",
                title = "城市夜色 · Lo-Fi",
                subtitle = "适合夜读与写作的节奏",
                bannerImageUrl = "https://wanx.alicdn.com/wanx/1086121689920057/text_to_image_lite_v2/717664582e5e40baa64a306978e88b9c_0_visibleWatermark.png",
                detailImageUrl = "https://wanx.alicdn.com/wanx/1086121689920057/text_to_image_lite_v2/717664582e5e40baa64a306978e88b9c_0_visibleWatermark.png",
            ),
            ExploreBannerItem(
                id = "3",
                title = "专注 · 纯音乐",
                subtitle = "帮助你进入心流状态",
                bannerImageUrl = "https://wanx.alicdn.com/wanx/1086121689920057/text_to_image_lite_v2/724a4bca69094d089cf1566798f4517d_0_visibleWatermark.png",
                detailImageUrl = "https://wanx.alicdn.com/wanx/1086121689920057/text_to_image_lite_v2/724a4bca69094d089cf1566798f4517d_0_visibleWatermark.png",
            ),
        )
        _uiState.value = _uiState.value.copy(
            bannerItems = items,
            currentIndex = 0,
        )
    }


}


