package com.lyf.compose.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun <S : IUiState, I : IUiIntent, VM : BaseViewModel<S, I>> BaseScreen(
    viewModel: VM,
    onEffect: (UiEffect) -> Unit = {}, // 处理导航等副作用
    content: @Composable (S, (I) -> Unit) -> Unit
) {
    // 1. 收集状态
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // 2. 收集副作用（利用 LaunchedEffect 保证生命周期安全）
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            onEffect(effect)
        }
    }

    // 3. 渲染内容，并透传 Intent 发送函数
    content(state) { intent -> viewModel.sendIntent(intent) }
}