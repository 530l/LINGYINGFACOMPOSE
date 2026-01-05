package com.lyf.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lyf.compose.core.theme.AppTheme
import com.lyf.compose.router.RouterRegistrations
import dagger.hilt.android.AndroidEntryPoint

// Compose 使用 Kotlin 编译器插件，首次运行需要：
// 编译 Compose 运行时代码
// 生成和缓存 Compose 类
// 预热 Compose 运行环境 ,延迟1-2s，回到主页，后debug模式就不会太卡了
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 注册应用内所有模块路由
        RouterRegistrations.registerAll()

        enableEdgeToEdge()
        setContent {
            AppTheme { AppNavHost() }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}