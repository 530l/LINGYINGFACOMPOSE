package com.lyf.compose.base

// 基础 UI 状态接口
interface IUiState {
    val isLoading: Boolean
    val errorMessage: String?
}

// 默认实现
data class BaseUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null
) : IUiState