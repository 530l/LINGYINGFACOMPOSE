package com.lyf.compose.feature

import androidx.lifecycle.ViewModel
import com.lyf.compose.core.data.bean.HomeTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class HomeViewModel : ViewModel() {

    // 启动即提供默认 Tab，避免先显示 loading 再“突变成整页 UI”造成的体感卡顿/抖动
    private val _homeTabs = MutableStateFlow(
        listOf(
            HomeTab("1", "探索", "explore"),
            HomeTab("2", "创作", "create"),
            HomeTab("3", "资产", "asset"),
            HomeTab("4", "我的", "mine")
        )
    )
    val homeTabs: StateFlow<List<HomeTab>> = _homeTabs

    // 如果后续确实需要从网络/配置下发 Tab，可在这里异步刷新 _homeTabs，
    // 但不要为了“模拟加载”而人为 delay（会导致首屏抖动和卡顿感）。
}