package com.lyf.compose.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : IUiState, I : IUiIntent>(initialState: S) : ViewModel() {

    // 1. UI State: 使用 StateFlow 保证线程安全和粘性
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    // 2. UI Effect: 使用 Channel 处理一次性事件（非粘性）
    private val _effect = Channel<UiEffect>()
    val effect: Flow<UiEffect> = _effect.receiveAsFlow()

    // 3. 供子类更新状态
    protected fun setState(reduce: S.() -> S) {
        _uiState.update { it.reduce() }
    }

    // 4. 供子类发送一次性事件
    protected fun setEffect(builder: () -> UiEffect) {
        viewModelScope.launch { _effect.send(builder()) }
    }

    // 5. 抽象方法：处理 UI 意图
    abstract fun sendIntent(intent: I)

    // 6. 统一网络请求启动器（带 Loading 和 Error 处理）
    protected fun launchNetwork(
        errorBlock: ((Throwable) -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                // 开启 Loading
                setState { this.copyUiState(isLoading = true) }
                block()
            } catch (e: Exception) {
                // 统一错误处理
                errorBlock?.invoke(e) ?: setEffect {
                    UiEffect.ShowToast(
                        e.message ?: "Unknown Error"
                    )
                }
            } finally {
                // 关闭 Loading
                setState { this.copyUiState(isLoading = false) }
            }
        }
    }

    // 辅助扩展方法，需要 S 实现 copy 方法（Data Class 默认支持）
    // 这里为了演示简单，实际项目中通常使用反射或特定接口来处理 copy
    abstract fun S.copyUiState(
        isLoading: Boolean = this.isLoading,
        errorMessage: String? = this.errorMessage
    ): S
}