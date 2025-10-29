package com.lyf.lingyingfacompose.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen(onLogout: () -> Unit) {
    Scaffold(
        topBar = { /* Top app bar */ },
        bottomBar = { /* Bottom navigation */ }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Button(onClick = onLogout) {
                Text("退出")
            }
        }
    }

}

