package com.lyf.lingyingfacompose.ui.wx.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyf.lingyingfacompose.R


@Composable
fun WxHomeScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    Column {
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
