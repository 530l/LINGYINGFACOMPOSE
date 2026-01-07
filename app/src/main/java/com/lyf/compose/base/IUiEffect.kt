package com.lyf.compose.base

// 一次性副作用（如导航、弹窗）
sealed interface UiEffect {
    data class ShowToast(val message: String) : UiEffect
    data class Navigate(val route: String) : UiEffect
}