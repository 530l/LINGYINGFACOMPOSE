package com.lyf.compose.feature.login

import androidx.lifecycle.viewModelScope
import com.lyf.compose.base.BaseViewModel
import com.lyf.compose.data.network.launchCollect
import com.lyf.compose.data.repository.feature.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseViewModel<LoginState, LoginIntent>(LoginState()) {

    override fun sendIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UpdateUsername -> setState { copy(username = intent.name) }
            is LoginIntent.ClickLogin -> login()
            is LoginIntent.UpdatePassword -> setState { copy(password = intent.password) }
        }
    }

    override fun LoginState.copyUiState(
        isLoading: Boolean,
        errorMessage: String?
    ): LoginState {
        return copy(isLoading = isLoading, errorMessage = errorMessage)
    }

    private fun login() {
        launchNetwork {
            // 这里自动处理了 isLoading = true
            val result = loginByPassword(uiState.value.username, uiState.value.password)
//            if (result.success) {
//                setEffect { UiEffect.Navigate("/home") }
//            }
            // finally 块会自动处理 isLoading = false
        }
    }

    private suspend fun loginByPassword(username: String, password: String) {
        authRepository.loginByPassword(username, password)
    }
}
