package com.lyf.compose.feature.asset

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.lyf.compose.router.LocalNavigator
import com.lyf.compose.core.ui.components.scaffold.AppScaffold
import timber.log.Timber

//模拟下外部状态
val externalState = mutableListOf<String>()

@Composable
fun SideEffectScreen() {
    val navigator = LocalNavigator.current
    AppScaffold(onBackClick = { navigator.pop() }) {
        var count by remember { mutableIntStateOf(0) }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text("Counter: $count")
                    Button(onClick = { count++ }) {
                        Text("Increment")
                    }
                    //使用 SideEffect 在每次重组后同步外部状态
                    SideEffect {
                        externalState.add("Count updated to $count")
                        Timber.d("External State Synced: $externalState")
                    }
                    Text(
                        """
SideEffect会在每次重组后同步最新的 count 到外部的 externalState。
通过这个例子，不难发现，SideEffect非常适合以下场景:
-------------------------------------------------------------------
1.日志记录：在每次状态更新时记录调试日志，方便排查问题。
2.调试信息同步：将最新的 Compose 状态同步到外部工具、分析平台，或非 Compose 系统。
3.轻量级任务：处理与 UI 状态无关的简单任务，例如统计点击次数等等。
-------------------------------------------------------------------
此外，我们再思考一个问题，为什么不要在SideEffect中处理大量繁重或耗时的操作？
阻塞主线程：SideEffect始终在主线程上运行，如果在其中执行耗时的操作（如网络请求、文件读写、大量计算等），
会阻塞 UI 更新，导致界面卡顿或掉帧，直接影响用户体验
-------------------------------------------------------------------
违反职责单一原则：SideEffect 的职责是执行副作用操作，而繁重任务应交由其他专用 API（如 LaunchedEffect 或后台线程）处理。
-------------------------------------------------------------------
滥用 SideEffect 会导致代码难以维护，甚至可能引入线程安全问题
-------------------------------------------------------------------
触发重组的潜在问题：如果繁重操作间接修改了Compose状态（例如改变一个 mutableState），
可能触发额外的重组，甚至造成无限循环或性能问题
-------------------------------------------------------------------
            """.trimIndent(),
                        fontSize = 16.sp,
                    )

                }
            }
        }
    }
}

