package com.lyf.compose.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hjq.toast.Toaster
import com.lyf.compose.base.BaseScreen
import com.lyf.compose.base.UiEffect
import com.lyf.compose.nav.Navigator

@Composable
fun LoginScreen(
    navController: Navigator,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    LocalContext.current
    BaseScreen(
        viewModel = viewModel,
        onEffect = { effect ->
            when (effect) {
                is UiEffect.Navigate -> navController.navigate(effect.route)
                is UiEffect.ShowToast -> Toaster.show(effect.message)
            }
        }
    ) { state, sendIntent ->

        if (state.isLoading) {
            CircularProgressIndicator()
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 用户名输入框
            OutlinedTextField(
                value = state.username,
                onValueChange = { sendIntent(LoginIntent.UpdateUsername(it)) },
                label = { Text("用户名") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 密码输入框
            OutlinedTextField(
                value = state.password,
                onValueChange = { sendIntent(LoginIntent.UpdatePassword(it)) },
                label = { Text("密码") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { sendIntent(LoginIntent.ClickLogin) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("登录")
            }
        }
    }
}
