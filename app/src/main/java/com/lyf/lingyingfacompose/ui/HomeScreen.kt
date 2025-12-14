package com.lyf.lingyingfacompose.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyf.lingyingfacompose.R
import com.lyf.lingyingfacompose.data.HomeTab
import com.lyf.lingyingfacompose.ui.asset.AssetScreen
import com.lyf.lingyingfacompose.ui.create.CreateScreen
import com.lyf.lingyingfacompose.ui.explore.ExplorerScreen
import com.lyf.lingyingfacompose.ui.mine.MineScreen
import kotlinx.coroutines.launch


@Preview
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val tabs by viewModel.homeTabs.collectAsState()

    if (tabs.isEmpty()) {
        // 如果 tabs 还没加载（理论上不会，因为有默认值），可加 loading
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { tabs.size })

    val coroutineScope = rememberCoroutineScope()

    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(pagerState.currentPage) {
        // PageView 滑动更新 selectedTab 重组更新 TabItem
        selectedTab = pagerState.currentPage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF14161B)),
    ) {
        // 页面内容区域
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (tabs[page].id) {
                "1" -> ExplorerScreen()
                "2" -> CreateScreen()
                "3" -> AssetScreen()
                "4" -> MineScreen()
            }
        }

        // 底部导航栏
        NavigationBar(
            tabs = tabs,
            selected = selectedTab,
            onSelected = { index ->
                selectedTab = index
                coroutineScope.launch { pagerState.animateScrollToPage(index) }
            }
        )
    }
}


@Composable
fun NavigationBar(
    tabs: List<HomeTab>,
    selected: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.background(Color(0xFF131313))) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selected == index
            TabItem(
                iconId = getIconRes(tab.id, isSelected),
                title = tab.title,
                tint = if (isSelected) Color.White else Color(0x66FFFFFF),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelected(index) }
            )
        }
    }
}

@Composable
private fun getIconRes(tabId: String, isSelected: Boolean): Int {
    return when (tabId) {
        "1" -> if (isSelected) R.drawable.icon_explore_selected else R.drawable.icon_explore
        "2" -> if (isSelected) R.drawable.icon_create2 else R.drawable.icon_create2
        "3" -> if (isSelected) R.drawable.icon_tab_asset_selected else R.drawable.icon_tab_asset
        "4" -> if (isSelected) R.drawable.icon_mine_selected else R.drawable.icon_mine
        else -> R.drawable.icon_create2
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
        Icon(painterResource(iconId), title, Modifier.size(24.dp), tint = Color.Unspecified)
        Text(title, fontSize = 12.sp, color = tint)
    }
}

