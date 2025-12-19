package com.lyf.lingyingfacompose.utils

import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * 配置 Edge-to-Edge 显示
 * 设置状态栏和导航栏为透明，并配置系统栏内容颜色
 */
fun ComponentActivity.setEdgeToEdgeConfig() {
    // 启用 Edge-to-Edge
    enableEdgeToEdge()

    // 设置状态栏和导航栏为透明
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT

    // 使用 WindowCompat 获取 WindowInsetsController
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

    // 配置状态栏和导航栏样式
    configureSystemBars(windowInsetsController)

    // Android 10 (API 29) 及以上版本，禁用导航栏对比度强制
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isNavigationBarContrastEnforced = false
    }

    // Android 11 (API 30) 及以上版本，设置系统栏外观
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
    }
}

/**
 * 配置系统栏内容颜色
 * @param isDarkBackground 是否为深色背景，true 则使用浅色图标，false 则使用深色图标
 */
fun ComponentActivity.setSystemBarContentColor(isDarkBackground: Boolean) {
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

    // 设置状态栏内容颜色
    // isAppearanceLightStatusBars: true = 深色图标（浅色背景），false = 浅色图标（深色背景）
    windowInsetsController.isAppearanceLightStatusBars = !isDarkBackground

    // 设置导航栏内容颜色
    // isAppearanceLightNavigationBars: true = 深色图标（浅色背景），false = 浅色图标（深色背景）
    windowInsetsController.isAppearanceLightNavigationBars = !isDarkBackground
}

/**
 * 默认系统栏配置（深色背景，浅色图标）
 */
private fun configureSystemBars(windowInsetsController: WindowInsetsControllerCompat) {
    // 默认配置：深色背景，使用浅色图标
    windowInsetsController.isAppearanceLightStatusBars = false
    windowInsetsController.isAppearanceLightNavigationBars = false
}