package com.lyf.lingyingfacompose.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import com.lyf.lingyingfacompose.ui.wx.ui.MainScreen


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen(onLogout: () -> Unit) {
    Column {
        Button(onClick = onLogout) {
            Text("Wx朋友圈")
        }
        MainScreen()
    }
}

