package com.lyf.compose.core.data.bean

// TabItem.kt
data class HomeTab(
    val id: String,          // 唯一标识，如 "explore", "create"
    val title: String,       // 显示标题
    val route: String        // 可用于导航（如 NavGraph）
)