package com.lyf.compose.feature.asset

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyf.compose.nav.LocalNavigator
import com.lyf.compose.core.ui.components.scaffold.AppScaffold
import kotlinx.coroutines.delay
import timber.log.Timber


suspend fun fetchDataFromNetwork(): String {
    // 模拟延迟2秒，例如网络请求或数据库查询
    delay(2000)
    return "Fetched data from server"
}

@Composable
fun LaunchedEffectScreen() {
    val navigator = LocalNavigator.current
    var data by remember { mutableStateOf("Loading...") }  // 用于保存加载的数据
    var isLoading by remember { mutableStateOf(true) } // 用于记录加载状态
    LaunchedEffect(Unit) {// 使用 LaunchedEffect 加载数据
        data = try {
            // 模拟网络请求
            fetchDataFromNetwork()
        } catch (e: Exception) {
            "Failed to load data: ${e.message}"
        } finally {
            isLoading = false
        }
    }
    var count1 by remember { mutableIntStateOf(0) }
    var count2 by remember { mutableIntStateOf(0) }
    var count3 by remember { mutableIntStateOf(0) }
    var count4 by remember { mutableIntStateOf(0) }

    LaunchedEffect(count1, count2, count3, count4) {
        Timber.d("LaunchedEffect 重组一次 count1 $count1")
    }

    AppScaffold(onBackClick = { navigator.onBack() }) {
      Column(modifier = Modifier.fillMaxSize()) {
          Button(onClick = { count1++ }) {
              Text(text = "count1: $count1")
          }
          Spacer(Modifier.height(16.dp))
          LazyColumn(modifier = Modifier.fillMaxSize()) {
              item {
                  Column(modifier = Modifier.fillMaxSize()) {
                      // 展示加载状态或数据
                      if (isLoading) {
                          Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
                      } else {
                          Text(text = data, style = MaterialTheme.typography.bodyLarge)
                      }
                      Text(
                          """
                        1.只执行一次： 当Composable组件首次进入组合（Composition）时，LaunchedEffect中的代码块会被触发执行
                        2.响应依赖参数key变化： 如果传递的 key依赖发生变化，LaunchedEffect 会重新启动。也就是说，它的执行是依赖于key的变化
                        3.生命周期感知： 当组件退出组合时，LaunchedEffect 中的协程会自动取消，以避免资源泄漏
                        4.协程支持： 它运行在协程上下文中，因此特别适合处理异步任务，比如网络请求、数据库查询等

            """.trimIndent(),
                          fontSize = 16.sp,
                      )

                  }
              }
          }
      }
    }
}

