package com.lyf.compose.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyf.compose.core.data.bean.ArticleBean
import com.lyf.compose.core.data.bean.HomeTab
import com.lyf.compose.core.data.network.NetworkResult
import com.lyf.compose.core.data.network.call
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


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

}