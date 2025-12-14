package com.lyf.lingyingfacompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyf.lingyingfacompose.data.HomeTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {

    private val _homeTabs = MutableStateFlow<List<HomeTab>>(emptyList())
    val homeTabs: StateFlow<List<HomeTab>> = _homeTabs

    init {
        loadTabs()
    }

    private fun loadTabs() {
        viewModelScope.launch {
            try {
                delay(500)
                val defaultTabs = listOf(
                    HomeTab("1", "探索", "explore"),
                    HomeTab("2", "创作", "create"),
                    HomeTab("3", "资产", "asset"),
                    HomeTab("4", "我的", "mine")
                )
                _homeTabs.value = defaultTabs
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}