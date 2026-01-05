package com.lyf.compose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.lyf.compose.core.data.session.SessionManager

/**
 * 保存导航状态，支持多回退栈（例如底部导航栏的每个 tab 都有自己的栈）
 */
@Stable
class NavigationState(
    val startKey: NavKey,
    val topLevelStack: NavBackStack<NavKey>,
    val subStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    // 当前选中的顶级页面（Tab）
    val currentTopLevelKey: NavKey by derivedStateOf { topLevelStack.last() }

    val topLevelKeys get() = subStacks.keys

    // 当前激活的子栈
    val currentSubStack: NavBackStack<NavKey>
        get() = subStacks[currentTopLevelKey]
            ?: error("Sub stack for $currentTopLevelKey does not exist")

    // 当前显示的页面 Key
    val currentKey: NavKey by derivedStateOf { currentSubStack.last() }
}

/**
 * 创建并记住 NavigationState
 */
@Composable
fun rememberNavigationState(
    startKey: NavKey,
    topLevelKeys: Set<NavKey>,
): NavigationState {
    // 顶级栈（用于切换 Tab）
    val topLevelStack = rememberNavBackStack(startKey)
    // 子栈映射（每个 Tab 对应一个栈）
    val subStacks = topLevelKeys.associateWith { key -> rememberNavBackStack(key) }

    return remember(startKey, topLevelKeys) {
        NavigationState(
            startKey = startKey,
            topLevelStack = topLevelStack,
            subStacks = subStacks,
        )
    }
}

/**
 * 导航控制器：处理跳转和返回逻辑
 * 实现 Core Navigator 接口
 */
class MultiStackNavigator(private val state: NavigationState) : Navigator {

    override fun navigate(key: NavKey) {
        if (NavRegistry.requiresLogin(key) && !SessionManager.isLoggedIn()) {
            navigate(LoginRouter)
            return
        }

        when (key) {
            // 如果点击的是当前 Tab，清空该 Tab 的子栈回到根部
            state.currentTopLevelKey -> clearSubStack()
            // 如果点击的是其他 Tab，切换 Tab
            in state.topLevelKeys -> goToTopLevel(key)
            // 否则是普通页面跳转，压入当前子栈
            else -> goToKey(key)
        }
    }

    override fun navigateRoot(key: NavKey) {
        if (NavRegistry.requiresLogin(key) && !SessionManager.isLoggedIn()) {
            navigateRoot(LoginRouter)
            return
        }

        // 对于多栈导航，navigateRoot 通常意味着切换到某个 TopLevelKey 并清空历史
        if (key in state.topLevelKeys) {
            state.topLevelStack.apply {
                clear()
                add(key)
            }
        } else {
            // 如果 key 不是 TopLevelKey，作为普通跳转处理
            goToKey(key)
        }
    }

    override fun pop() {
        when (state.currentKey) {
            state.startKey -> { /* 处理退出应用或忽略，由 BackHandler 处理 */ }
            state.currentTopLevelKey -> {
                // 如果在 Tab 的根部，回退到上一个 Tab
                state.topLevelStack.removeLastOrNull()
            }
            else -> state.currentSubStack.removeLastOrNull()
        }
    }

    private fun goToKey(key: NavKey) {
        state.currentSubStack.apply {
            remove(key) // 避免重复
            add(key)
        }
    }

    private fun goToTopLevel(key: NavKey) {
        state.topLevelStack.apply {
            if (key == state.startKey) clear() else remove(key)
            add(key)
        }
    }

    private fun clearSubStack() {
        if (state.currentSubStack.size > 1) {
            state.currentSubStack.subList(1, state.currentSubStack.size).clear()
        }
    }
}

/**
 * 将 NavigationState 转换为 NavDisplay 可用的 NavEntry 列表
 * 并自动应用 SaveableState 和 ViewModelStore 装饰器
 */
@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries = subStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator<NavKey>(),
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider,
        )
    }

    return topLevelStack
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
