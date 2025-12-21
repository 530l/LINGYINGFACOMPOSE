package com.lyf.compose.feature.login

import androidx.lifecycle.viewModelScope
import com.lyf.compose.core.data.bean.Banner
import com.lyf.compose.core.data.bean.Hotkey
import com.lyf.compose.core.data.network.launchCollect
import com.lyf.compose.core.data.repositories.AtmobRepositories
import com.lyf.compose.core.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AtmobRepositories
) : BaseViewModel() {

    //UI 状态（聚合所有需要驱动UI的数据）
    data class LoginUiState(
        val isLoading: Boolean = false,
        val errorMsg: String = "",
        val isLoginSuccess: Boolean = false,
        val token: String = "",
        val banner: List<Banner> = emptyList(),
        val hotKeys: List<Hotkey> = emptyList(),
    )

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        requestBanner()
    }

    fun loginByPassword(username: String, password: String) {
        repository.loginByPassword(username, password).launchCollect(
            scope = viewModelScope,
            onLoading = { _uiState.update { it.copy(isLoading = true, errorMsg = "") } },
            onSuccess = { user ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccess = true,
                        token = user.token
                    )
                }
            },
            onError = { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMsg = e.message ?: "未知错误"
                    )
                }
            }
        )
    }

    fun requestBanner() {
        repository.requestBanner().launchCollect(
            scope = viewModelScope,
            onLoading = { /*_uiState.update { it.copy(isLoading = true, errorMsg = "") }*/ },
            onSuccess = { bannerList ->
                _uiState.update {
                    it.copy(banner = bannerList)
                }
            },
            onError = { e -> }
        )
    }

    fun loadHomeData() {
        repository.requestHomeInitData().launchCollect(
            scope = viewModelScope,
            onLoading = {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMsg = "")
            },
            onSuccess = { data ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    banner = data.banners,
                    hotKeys = data.hotKeys
                )
            },
            onError = { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMsg = error.message ?: "加载首页数据失败"
                )
            }
        )
    }


}
