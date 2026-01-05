//package com.lyf.compose.feature.nav3.conditional
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.Stable
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.snapshots.SnapshotStateList
//import androidx.compose.runtime.toMutableStateList
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
//import androidx.navigation3.runtime.NavBackStack
//import androidx.navigation3.runtime.NavEntry
//import androidx.navigation3.runtime.NavKey
//import androidx.navigation3.runtime.entryProvider
//import androidx.navigation3.runtime.rememberDecoratedNavEntries
//import androidx.navigation3.runtime.rememberNavBackStack
//import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
//import androidx.navigation3.ui.NavDisplay
//import kotlinx.serialization.Serializable
//
//// ==========================================
//// 1. 定义导航键 (NavKeys)
//// ==========================================
//
//// 基础接口（androidx.navigation3.runtime.NavKey 只是一个标记接口）
//@Serializable
//object HomeKey : NavKey
//
//@Serializable
//object ProfileKey : NavKey
//
//@Serializable
//data class DetailKey(val id: String) : NavKey
//
//// ==========================================
//// 2. 核心导航逻辑 (State & Navigator)
//// ==========================================
//
///**
// * 保存导航状态，支持多回退栈（例如底部导航栏的每个 tab 都有自己的栈）
// */
//@Stable
//class NavigationState(
//    val startKey: NavKey,
//    val topLevelStack: NavBackStack<NavKey>,
//    val subStacks: Map<NavKey, NavBackStack<NavKey>>,
//) {
//    // 当前选中的顶级页面（Tab）
//    val currentTopLevelKey: NavKey by derivedStateOf { topLevelStack.last() }
//
//    val topLevelKeys get() = subStacks.keys
//
//    // 当前激活的子栈
//    val currentSubStack: NavBackStack<NavKey>
//        get() = subStacks[currentTopLevelKey]
//            ?: error("Sub stack for $currentTopLevelKey does not exist")
//
//    // 当前显示的页面 Key
//    val currentKey: NavKey by derivedStateOf { currentSubStack.last() }
//}
//
///**
// * 创建并记住 NavigationState
// */
//@Composable
//fun rememberNavigationState(
//    startKey: NavKey,
//    topLevelKeys: Set<NavKey>,
//): NavigationState {
//    // 顶级栈（用于切换 Tab）
//    val topLevelStack = rememberNavBackStack(startKey)
//    // 子栈映射（每个 Tab 对应一个栈）
//    val subStacks = topLevelKeys.associateWith { key -> rememberNavBackStack(key) }
//
//    return remember(startKey, topLevelKeys) {
//        NavigationState(
//            startKey = startKey,
//            topLevelStack = topLevelStack,
//            subStacks = subStacks,
//        )
//    }
//}
//
///**
// * 导航控制器：处理跳转和返回逻辑
// */
//class Navigator(private val state: NavigationState) {
//
//    fun navigate(key: NavKey) {
//        when (key) {
//            // 如果点击的是当前 Tab，清空该 Tab 的子栈回到根部
//            state.currentTopLevelKey -> clearSubStack()
//            // 如果点击的是其他 Tab，切换 Tab
//            in state.topLevelKeys -> goToTopLevel(key)
//            // 否则是普通页面跳转，压入当前子栈
//            else -> goToKey(key)
//        }
//    }
//
//    fun goBack() {
//        when (state.currentKey) {
//            state.startKey -> { /* 处理退出应用或忽略 */ }
//            state.currentTopLevelKey -> {
//                // 如果在 Tab 的根部，回退到上一个 Tab
//                state.topLevelStack.removeLastOrNull()
//            }
//            else -> state.currentSubStack.removeLastOrNull()
//        }
//    }
//
//    private fun goToKey(key: NavKey) {
//        state.currentSubStack.apply {
//            remove(key) // 避免重复
//            add(key)
//        }
//    }
//
//    private fun goToTopLevel(key: NavKey) {
//        state.topLevelStack.apply {
//            if (key == state.startKey) clear() else remove(key)
//            add(key)
//        }
//    }
//
//    private fun clearSubStack() {
//        if (state.currentSubStack.size > 1) {
//            state.currentSubStack.subList(1, state.currentSubStack.size).clear()
//        }
//    }
//}
//
///**
// * 将 NavigationState 转换为 NavDisplay 可用的 NavEntry 列表
// * 并自动应用 SaveableState 和 ViewModelStore 装饰器
// */
//@Composable
//fun NavigationState.toEntries(
//    entryProvider: (NavKey) -> NavEntry<NavKey>,
//): SnapshotStateList<NavEntry<NavKey>> {
//    val decoratedEntries = subStacks.mapValues { (_, stack) ->
//        val decorators = listOf(
//            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
//            rememberViewModelStoreNavEntryDecorator<NavKey>(),
//        )
//        rememberDecoratedNavEntries(
//            backStack = stack,
//            entryDecorators = decorators,
//            entryProvider = entryProvider,
//        )
//    }
//
//    return topLevelStack
//        .flatMap { decoratedEntries[it] ?: emptyList() }
//        .toMutableStateList()
//}
//
//// ==========================================
//// 3. UI 入口 (App Composable)
//// ==========================================
//
//@Composable
//fun App() {
//    // 1. 定义顶级页面（Tab）
//    val topLevelKeys = setOf(HomeKey, ProfileKey)
//
//    // 2. 初始化状态和控制器
//    val navState = rememberNavigationState(startKey = HomeKey, topLevelKeys = topLevelKeys)
//    val navigator = remember(navState) { Navigator(navState) }
//
//    // 3. 定义页面内容提供者
//    val entryProvider = entryProvider {
//        entry<HomeKey> {
//            HomeScreen(
//                onGoToDetail = { id -> navigator.navigate(DetailKey(id)) },
//                onGoToProfile = { navigator.navigate(ProfileKey) }
//            )
//        }
//        entry<ProfileKey> {
//            ProfileScreen(
//                onGoBack = { navigator.goBack() }
//            )
//        }
//        entry<DetailKey> { key ->
//            DetailScreen(
//                id = key.id,
//                onGoBack = { navigator.goBack() }
//            )
//        }
//    }
//
//    // 4. 渲染导航显示
//    // 使用 rememberSingleSceneStrategy 进行简单的单页切换动画
//    // NiaApp 使用的是 rememberListDetailSceneStrategy 以支持大屏适配
//    NavDisplay(
//        entries = navState.toEntries(entryProvider),
//        //sceneStrategy = rememberSingleSceneStrategy(),
//        onBack = { navigator.goBack() }
//    )
//}
//
//// ==========================================
//// 4. 示例屏幕组件
//// ==========================================
//
//@Composable
//fun HomeScreen(onGoToDetail: (String) -> Unit, onGoToProfile: () -> Unit) {
//    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Button(onClick = { onGoToDetail("123") }) { Text("Go to Detail") }
//        Button(onClick = onGoToProfile) { Text("Go to Profile") }
//    }
//}
//
//@Composable
//fun ProfileScreen(onGoBack: () -> Unit) {
//    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Button(onClick = onGoBack) { Text("Back from Profile") }
//    }
//}
//
//@Composable
//fun DetailScreen(id: String, onGoBack: () -> Unit) {
//    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Text("Detail ID: $id")
//        Button(onClick = onGoBack) { Text("Back") }
//    }
//}
