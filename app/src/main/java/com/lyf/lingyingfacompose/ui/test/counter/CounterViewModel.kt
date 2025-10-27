package com.lyf.lingyingfacompose.ui.test.counter

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor() : ViewModel() {
    private var _count = mutableIntStateOf(0)
    val count: State<Int> = _count

    fun increment() {
        _count.intValue++
    }


    // 定义一个 MutableStateFlow 来管理内部状态
    private val _count2 = MutableStateFlow(0)

    // 暴露一个不可变的 StateFlow
    val count2: StateFlow<Int> = _count2.asStateFlow()

    fun increment2() {
        viewModelScope.launch { _count2.value++ }
    }


    // 内部 MutableLiveData，用于管理计数状态
    private val _count3 = MutableLiveData(0)

    // 暴露不可变的 LiveData
    val count3: LiveData<Int> = _count3

    fun increment3() {
        _count3.value = _count3.value?.plus(1)
    }


}