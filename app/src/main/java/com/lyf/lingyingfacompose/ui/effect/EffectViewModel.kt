package com.lyf.lingyingfacompose.ui.effect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyf.lingyingfacompose.data.UserInfoBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EffectUiState(
    val isLoading: Boolean = true,
    val data: String = "Loading...",
    val result: List<UserInfoBean> = emptyList() // ✅ 不可变、只读
)

@HiltViewModel
class EffectViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(EffectUiState())
    val uiState: StateFlow<EffectUiState> = _uiState.asStateFlow()


    fun loadData() {
        viewModelScope.launch {
            // 开始加载
            _uiState.value = _uiState.value.copy(isLoading = true)

            // 执行网络请求
            val result = try {
                fetchDataFromNetwork()
            } catch (e: Exception) {
                "Failed to load data: ${e.message ?: "Unknown error"}"
            }

            //安全的。
//            _uiState.update {
//                it.copy(
//                    isLoading = false,
//                    data = result
//                )
//            }
            //多线程下不安全的
            // 更新数据并结束加载
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                data = result
            )
        }
    }

    fun setData(data: String) {
        _uiState.value = _uiState.value.copy(data = data)
    }

    private suspend fun fetchDataFromNetwork(): String {
        delay(4000) // 模拟网络延迟
        return "Fetched data from server"
    }
}