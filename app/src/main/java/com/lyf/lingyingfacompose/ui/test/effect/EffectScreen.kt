package com.lyf.lingyingfacompose.ui.test.effect

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

//模拟下外部状态
val externalState = mutableListOf<String>()

@Composable
fun SideEffectScreen() {
    var count by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
//            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Counter:$count", modifier = Modifier.padding(8.dp))
        Button(onClick = { count++ }) {
            Text("Increment")
        }
        // 使用 SideEffect 在每次重组后同步外部状态
        // 无论状态是否发生改变，只要重组完成，SideEffect都会被调用一次；
        // 它通常用来将Compose的内部状态同步到非Compose的外部系统。
        //SideEffect非常适合以下场景:
        //日志记录：在每次状态更新时记录调试日志，方便排查问题。
        //调试信息同步：将最新的 Compose 状态同步到外部工具、分析平台，或非 Compose 系统。
        //轻量级任务：处理与 UI 状态无关的简单任务，例如统计点击次数等等。
        SideEffect {
            externalState.add("Count updated to $count")
            println("External State Synced: $externalState")
        }
    }
}

@Composable
fun LaunchedEffectScreen(
    viewModel: EffectViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 首次进入时触发加载
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    // UI 只展示状态，不修改状态
    // 父容器的 contentAlignment 设置了一个适用于所有未明确指定自身对齐方式的子组件的默认对齐策略。
    //子组件的 Modifier.align() 提供了一种方法来覆盖父容器的默认对齐方式，使得特定子组件可以按照自己的需求进行定位。
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center //todo 父容器的默认对齐方式
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
            Text(text = "Loading...", modifier = Modifier.padding(top = 48.dp))
        } else {
            Column(
                horizontalAlignment = Alignment.Start,//todo 子容器(Column)内部子项的水平对齐方式
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "retry",
                    modifier = Modifier
                        .clickable(onClick = {
                            viewModel.setData("try net")
                        })
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer, // 背景色，可自定义
                            shape = RoundedCornerShape(8.dp) // 圆角，8dp 是常用值，可调整
                        )
                        .padding(horizontal = 16.dp, vertical = 18.dp), // 增加内边距让文字不贴边
                    color = MaterialTheme.colorScheme.onPrimaryContainer, // 文字颜色，与背景搭配
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = uiState.data,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    //todo 在Column中覆盖默认的水平起始对齐方式
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}




