package com.lyf.compose.feature.asset

import com.lyf.compose.core.data.bean.AsstBean
import com.lyf.compose.core.vm.BaseViewModel
import com.lyf.compose.nav.DisposableEffectRouter
import com.lyf.compose.nav.LaunchedEffectRouter
import com.lyf.compose.nav.ProduceStateRouter
import com.lyf.compose.nav.RememberCoroutineScopeRouter
import com.lyf.compose.nav.RememberUpdatedStateRouter
import com.lyf.compose.nav.SideEffectRouter
import com.lyf.compose.nav.SnapshotFlowRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AssetViewModel : BaseViewModel() {

    data class AssetLoginUi(
        val assetList: List<AsstBean> = emptyList()
    )
    private val _uiState = MutableStateFlow(AssetLoginUi())

    val uiState: StateFlow<AssetLoginUi> = _uiState.asStateFlow()
    
    init {
        val list = mutableListOf<AsstBean>()
        list.add(
            AsstBean(
                1, "SideEffect",
                "每次成功重组后 运行副作用，并且副作用与 UI 状态无关\n日志记录、触发外部 API 调用",
                SideEffectRouter
            )
        )
        list.add(
            AsstBean(
                2, "LaunchedEffect",
                "组合阶段 启动副作用，通常用于启动一次性任务或异步操作\n执行协程中的异步任务，监听特定键的变化并重新执行副作用",
                LaunchedEffectRouter
            )
        )
        list.add(
            AsstBean(
                3, "DisposableEffect",
                "当需要在组件 进入或退出组合 时执行逻辑，同时清理资源\n注册或解绑监听器、关闭文件流、取消订阅",
                DisposableEffectRouter
            )
        )
        list.add(
            AsstBean(
                4, "rememberUpdatedState",
                "当副作用需要访问 最新状态值，而状态可能随时间变化时\n避免使用过时的lambda或状态值",
                RememberUpdatedStateRouter
            )
        )
        list.add(
            AsstBean(
                5, "produceState",
                "当需要从 外部异步源 加载数据，并将结果存储为Compose的 State 时\n在 UI 中展示网络或数据库加载的数据",
                ProduceStateRouter
            )
        )
        list.add(
            AsstBean(
                6, "rememberCoroutineScope",
                "当需要启动协程，并希望其生命周期与当前组合保持一致时\n在用户交互事件（如点击按钮）中启动协程任务",
                RememberCoroutineScopeRouter
            )
        )
        list.add(
            AsstBean(
                7,
                "snapshotFlow",
                "将Compose的状态（State）转换为Flow，便于与非Compose的代码交互\n观察Compose状态变化并触发下游流处理",
                SnapshotFlowRouter
            )
        )
        _uiState.update {
            it.copy(
                assetList = list
            )
        }
    }
}