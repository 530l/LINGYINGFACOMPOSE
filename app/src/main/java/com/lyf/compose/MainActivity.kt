package com.lyf.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.lyf.compose.core.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
// Compose 使用 Kotlin 编译器插件，首次运行需要：
// 编译 Compose 运行时代码
// 生成和缓存 Compose 类
// 预热 Compose 运行环境 ,延迟1-2s，回到主页，后debug模式就不会太卡了
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 启动页
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // 启用边缘到边缘的显示效果
        enableEdgeToEdge()
        // 设置Compose内容
        setContent { // 应用主题包装
            AppTheme { AppNavHost() }
        }
        // 不让启动界面一直显示
        splashScreen.setKeepOnScreenCondition {
            false
        }
    }

    // 说明：返回键逻辑（包含“主页双击退出”）统一在 AppNavHost 的 NavDisplay(onBack=...) 中处理。
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}