package com.lyf.compose.feature.asset

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun RememberUpdatedStateScreen() {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            """
            如果说LaunchedEffect是用于执行那些短期一次性的协程任务，
            那么rememberCoroutineScope适用于需要启动协程并在 Composable 生命周期内持续存在的场景。
            简单来说， rememberCoroutineScope在Composable组件中创建和管理一个协程作用域，
            使得我们能够启动协程并在组件生命周期内保持该协程的有效性。
        """.trimIndent()
        )
        Button(onClick = {
            coroutineScope.launch {
                // 假设这是一个网络请求或耗时操作
                delay(2000)
                Timber.d("网络，耗时操作完成～～～～～～～～～")
            }
        }) {
            Text(
                text = """
                值得注意的是，我们使用rememberCoroutineScope，返回一个协程作用域，
                它会和Composeable生命周期绑定，本质上是与LifecycleOwner相关联的，
                也就是说，它会在组件离开界面的时自动取消，
                所以我们要确保UI不会因为协程未结束而引发异常或资源泄露。
            """.trimIndent()
            )
        }
    }
}