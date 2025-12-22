package com.lyf.compose.feature.asset

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lyf.compose.core.nav3.LocalNavigator
import com.lyf.compose.core.ui.components.scaffold.AppScaffold

//模拟下外部状态
val externalState = mutableListOf<String>()

@Composable
fun SideEffectScreen() {
    val navigator = LocalNavigator.current
    AppScaffold(onBackClick = { navigator.pop() }) {
        var count by remember { mutableIntStateOf(0) }
        Column(modifier = Modifier.fillMaxSize()) {
            Text("Counter: $count")
            Button(onClick = { count++ }) {
                Text("Increment")
            }
            //使用 SideEffect 在每次重组后同步外部状态
            SideEffect {
                externalState.add("Count updated to $count")
                To
                println("External State Synced: $externalState")
            }
        }
    }
}

