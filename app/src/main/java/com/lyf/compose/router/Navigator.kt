package com.lyf.compose.router

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation3.runtime.NavKey

interface Navigator {
    fun navigate(key: NavKey)//  入栈
    fun navigateRoot(key: NavKey)//  替换栈顶
    fun pop()// 出栈
}

// CompositionLocal 允许你在 Compose 组件树中隐式地传递数据，
// 而不需要通过每个函数参数显式传递。
// LocalNavigator 是一个 CompositionLocal，
// 用于在 Compose 组件树中传递 Navigator 对象。
// todo  那么用 hilt 注入 Navigator 对象 应该也能实现吧
val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No Navigator provided")
}
