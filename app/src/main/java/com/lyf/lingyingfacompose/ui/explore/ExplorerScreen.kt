package com.lyf.lingyingfacompose.ui.explore

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lt.compose_views.banner.Banner
import com.lt.compose_views.banner.BannerState
import com.lt.compose_views.banner.rememberBannerState
import com.lt.compose_views.compose_pager.rememberScalePagerContentTransformation
import com.lt.compose_views.pager_indicator.PagerIndicator
import com.lyf.lingyingfacompose.R
import com.lyf.lingyingfacompose.data.ExploreBannerItem
import com.lyf.lingyingfacompose.data.ExploreUiState


@Composable
fun ExplorerScreen(viewModel: ExploreViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ExploreTopSection(uiState) {
                Toast.makeText(context, "点击了第 $it 个", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
private fun ExploreTopSection(
    uiState: ExploreUiState,
    onBannerIndexChange: (Int) -> Unit,
) {
    val bannerState = rememberBannerState()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.explorev4_bg),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .matchParentSize()
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(26.dp))
            BannerItem(bannerState, uiState.bannerItems, onBannerIndexChange)
            //横向的 Menu
            //Tablayout
            //  推荐tab ，活动 tab, 榜单 tab
        }
    }
}


@Composable
private fun BannerItem(
    bannerState: BannerState,
    list: List<ExploreBannerItem>,
    onBannerIndexChange: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Banner(
            pageCount = list.size,
            modifier = Modifier.fillMaxSize(),
            bannerState = bannerState,
            autoScrollTime = 5000,
            clip = false,//是对这个Banner组件是否裁剪
            contentTransformation = rememberScalePagerContentTransformation(1f, 0.9f),
            orientation = Orientation.Horizontal,
            bannerKey = { index -> list[index].id },

            ) {
            AsyncImage(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = {
                        onBannerIndexChange.invoke(index)
                    }),
                model = list[index].bannerImageUrl,
                contentDescription = "图片描述",
            )
        }

        PagerIndicator(
            size = list.size,
            offsetPercentWithSelectFlow = remember { bannerState.createChildOffsetPercentFlow() },
            selectIndexFlow = remember { bannerState.createCurrSelectIndexFlow() },
            indicatorItem = {
                Spacer(
                    modifier = Modifier
                        .size(5.dp)
                        .background(
                            Color(0x40FFFFFF),
                            CircleShape
                        )
                )
            },
            selectIndicatorItem = {
                Spacer(
                    modifier = Modifier
                        .width(10.dp)
                        .height(5.dp)
                        .background(Color(0x90FFFFFF), RoundedCornerShape(3.dp))
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 10.dp, end = 10.dp),
            orientation = Orientation.Horizontal,
        )
    }
}