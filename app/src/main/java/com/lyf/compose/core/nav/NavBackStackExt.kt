package com.lyf.compose.core.nav

/**
 * Navigation3 backStack 的安全工具。
 *
 * 背景：NavDisplay 要求 backStack 任何时刻都不能为空。
 * 如果使用 clear() + add() 的方式切根页面，
 * 在 Compose 重组/Effect 并发的瞬间可能被 NavDisplay 观察到空栈，
 * 导致崩溃："NavDisplay backstack cannot be empty"。
 */

/**
 * 将 backStack 收敛为“单根栈”，并把 root 替换为 [key]。
 *
 * 重要：整个过程中保证 backStack 非空。
 */
fun <T> MutableList<T>.navigateRootNonEmpty(key: T) {
    // 如果当前栈顶就是目标页，直接返回（避免不必要的重组/闪烁）
    if (this.isNotEmpty() && this.last() == key) return

    // 保留一个元素，避免任何时刻出现空栈
    while (this.size > 1) {
        this.removeAt(this.lastIndex)
    }

    if (this.isEmpty()) {
        this.add(key)
    } else {
        this[0] = key
    }
}
