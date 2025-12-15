package com.lyf.lingyingfacompose.ui.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lt.compose_views.banner.Banner
import com.lt.compose_views.banner.rememberBannerState
import com.lt.compose_views.compose_pager.rememberScalePagerContentTransformation
import com.lt.compose_views.pager_indicator.PagerIndicator
import com.lyf.lingyingfacompose.R
import com.lyf.lingyingfacompose.components.TabBar
import com.lyf.lingyingfacompose.data.ExploreBannerItem
import com.lyf.lingyingfacompose.data.ExploreMenuItem
import com.lyf.lingyingfacompose.data.ExploreTabItem
import kotlinx.coroutines.launch


@Composable
fun ExplorerScreen(viewModel: ExploreViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState().value
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. 背景图（全屏）
        Image(
            painter = painterResource(R.drawable.explorev4_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        // 2. 内容层：Column 填满屏幕
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp) // 留出状态栏或顶部广告位置
        ) {
            // 1. 顶部横幅广告
            BannerItem(uiState.bannerItems, onBannerIndexChange = {})
            // 2. 导航菜单栏
            HorizontalMenuItem(uiState.menuItems, onMenuIndexChange = {})
            // 3. Tab 栏 + 内容分页
            TabLayoutItem(
                tabItems = uiState.tabItems,
                currentIndex = 0,
                onTabIndexChange = {}
            )
        }
    }
}


@Composable
private fun BannerItem(
    list: List<ExploreBannerItem>,
    onBannerIndexChange: (Int) -> Unit,
) {
    val bannerState = rememberBannerState()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(start = 12.dp, end = 12.dp)
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
                            Color(0x40FFFFFF), CircleShape
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


@Composable
fun HorizontalMenuItem(
    menuItems: List<ExploreMenuItem>,
    onMenuIndexChange: (Int) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        // 内容头尾 12dp 留白，相当于 12.dp padding
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
        // 每个item之间间隔12.dp,不包含头尾的12.dp
        horizontalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        items(menuItems.size) { index ->
            val item = menuItems[index]
            Box(
                modifier = Modifier.clickable(onClick = { onMenuIndexChange.invoke(index) })
            ) {
                Column(
                    modifier =
                        Modifier
                            .padding(top = 12.dp)
                            // 将Column居中
                            .align(Alignment.Center),
                    //水平居中
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(24.dp),
                        model = item.imageUrl,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.title,
                        fontSize = 12.sp, color = Color(0x90FFFFFF)
                    )
                }

                if (item.isComingSoon) {
                    Text(
                        text = "即将开放",
                        modifier = Modifier
                            .align(Alignment.TopEnd)  // 将文本对齐到Box的右上角
                            .offset(x = (20).dp, y = (6).dp)  // 微调位置：向左移一点，向上一点
                            .background(
                                Color(0x20FFFFFF),
                                RoundedCornerShape(
                                    topStart = 6.dp,
                                    topEnd = 6.dp,
                                    bottomEnd = 6.dp,
                                    bottomStart = 0.dp
                                )
                            )
                            // 调整内边距使背景更紧凑
                            .padding(horizontal = 2.dp, vertical = 1.dp),
                        fontSize = 8.sp,
                        lineHeight = 10.sp,
                        color = Color(0x90FFFFFF)
                    )
                }
            }
        }
    }
}


@Composable
fun TabLayoutItem(
    tabItems: List<ExploreTabItem>,
    currentIndex: Int,
    onTabIndexChange: (Int) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { tabItems.size })
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        TabBar(
            data = tabItems,
            dataMapping = { it.title },
            pagerState = pagerState,
            backgroundColor = Color.Black,
            selectedContentColor = Color.White,
            unselectedContentColor = Color(0x90FFFFFF),
            indicatorColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = (-8).dp)
                .wrapContentHeight(),
            onClick = {
                scope.launch { //点击tab时，切换到对应的页面
                    pagerState.animateScrollToPage(it)
                    onTabIndexChange.invoke(it)
                }
            },
        )

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.weight(1f) // 👈 占满剩余空间
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {

            }
        }
    }
}

