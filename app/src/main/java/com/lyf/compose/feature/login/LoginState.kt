package com.lyf.compose.feature.login

import com.lyf.compose.base.IUiIntent
import com.lyf.compose.base.IUiState

data class LoginState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val username: String = "",
    val password: String = "",
    val isLoginSuccess: Boolean = false
) : IUiState

sealed class LoginIntent : IUiIntent {
    data class UpdateUsername(val name: String) : LoginIntent()
    data class UpdatePassword(val password: String) : LoginIntent()
    object ClickLogin : LoginIntent()
}