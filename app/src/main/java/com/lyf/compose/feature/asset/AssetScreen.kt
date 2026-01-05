package com.lyf.compose.feature.asset

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lyf.compose.router.LocalNavigator
import com.lyf.compose.core.theme.BgContentLight
import com.lyf.compose.core.theme.BgWhiteLight
import com.lyf.compose.core.theme.PrimaryLight
import com.lyf.compose.core.theme.ShapeLarge
import com.lyf.compose.core.theme.TextPrimaryLight
import com.lyf.compose.core.theme.TextWhite
import com.lyf.compose.router.RefreshRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetScreen(
    viewModel: AssetViewModel = hiltViewModel(),
) {
    val navigator = LocalNavigator.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val assetList = uiState.value.assetList
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "资产中心",
                        fontSize = 20.sp,
                        color = TextPrimaryLight,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BgWhiteLight // 如果你希望背景透明
                ),
                modifier = Modifier
                    .statusBarsPadding() // 可选：额外确保避开状态栏
                    .height(50.dp)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BgContentLight)
                // 添加这行来应用Scaffold的内边距
                .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
        ) {
            items(
                items = assetList,
                key = { it.id }
            ) {
                Column(
                    modifier = Modifier
                        .background(PrimaryLight, ShapeLarge)
                        .padding(12.dp)
                        .clickable(onClick = {
//                            if (it.id <= 3) navigator.navigate(it.navKey)
                            navigator.navigate(RefreshRouter)
                        })
                ) {
                    Text(text = it.title, fontSize = 18.sp, color = TextWhite)
                    Text(text = it.subTitle, fontSize = 14.sp, color = TextWhite)
                }
            }
        }
    }
}
