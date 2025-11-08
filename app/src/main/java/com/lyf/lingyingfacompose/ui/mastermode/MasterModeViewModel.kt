package com.lyf.lingyingfacompose.ui.mastermode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 大师模式 UI 状态
 */
data class MasterModeUiState(
    // 纯音乐开关状态
    val isPureMusicMode: Boolean = false,
    // 纯音乐歌曲名称
    val pureMusicName: String = "",
    // 非纯音乐歌曲名称
    val nonPureMusicName: String = "",
    // 非纯音乐灵感内容
    val inspirationContent: String = "",
    // 是否显示重置按钮
    val showResetButton: Boolean = false,
    // 音频参考百分比（weirdness tag）
    val audioReferencePercent: Int? = null,
    // 音调标签（tone tag）
    val toneTag: String? = null,
    // 风格标签（style tag）
    val styleTag: String? = null,
    // 风格标签图标资源
    val styleTagIcon: Int? = null,
    // 已添加的音乐风格列表
    val musicStyles: List<MusicStyleItem> = emptyList(),
    // 风格推荐列表
    val recommendedStyles: List<RecommendedStyleItem> = emptyList(),
    // 积分数量
    val creditCount: Int = 0,
    // 确认按钮是否可用
    val isConfirmEnabled: Boolean = false
)

/**
 * 音乐风格项
 */
data class MusicStyleItem(
    val id: String,
    val name: String,
    val iconRes: Int? = null
)

/**
 * 风格推荐项
 */
data class RecommendedStyleItem(
    val id: String,
    val name: String,
    val iconRes: Int? = null
)

@HiltViewModel
class MasterModeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(
        MasterModeUiState(
            // 初始化一些测试数据
            recommendedStyles = getDefaultRecommendedStyles(),
            creditCount = 100
        )
    )
    val uiState: StateFlow<MasterModeUiState> = _uiState.asStateFlow()
    
    init {
        // 初始化时加载风格推荐数据
        loadRecommendedStyles()
    }
    
    /**
     * 加载风格推荐数据
     */
    private fun loadRecommendedStyles() {
        // TODO: 从网络或本地加载数据
        // 这里使用默认数据
        _uiState.value = _uiState.value.copy(
            recommendedStyles = getDefaultRecommendedStyles()
        )
    }
    
    /**
     * 获取默认的风格推荐数据
     */
    private fun getDefaultRecommendedStyles(): List<RecommendedStyleItem> {
        return listOf(
            RecommendedStyleItem(id = "1", name = "流行"),
            RecommendedStyleItem(id = "2", name = "摇滚"),
            RecommendedStyleItem(id = "3", name = "电子"),
            RecommendedStyleItem(id = "4", name = "爵士"),
            RecommendedStyleItem(id = "5", name = "民谣"),
            RecommendedStyleItem(id = "6", name = "R&B"),
            RecommendedStyleItem(id = "7", name = "嘻哈"),
            RecommendedStyleItem(id = "8", name = "古典")
        )
    }

    /**
     * 切换纯音乐模式
     */
    fun togglePureMusicMode() {
        _uiState.value = _uiState.value.copy(
            isPureMusicMode = !_uiState.value.isPureMusicMode
        )
        updateConfirmButtonState()
    }

    /**
     * 更新纯音乐歌曲名称
     */
    fun updatePureMusicName(name: String) {
        _uiState.value = _uiState.value.copy(pureMusicName = name)
        updateConfirmButtonState()
    }

    /**
     * 更新非纯音乐歌曲名称
     */
    fun updateNonPureMusicName(name: String) {
        _uiState.value = _uiState.value.copy(nonPureMusicName = name)
        updateConfirmButtonState()
    }

    /**
     * 更新灵感内容
     */
    fun updateInspirationContent(content: String) {
        _uiState.value = _uiState.value.copy(inspirationContent = content)
        updateConfirmButtonState()
    }

    /**
     * 重置所有输入
     */
    fun resetAll() {
        _uiState.value = MasterModeUiState()
    }

    /**
     * 添加音频参考标签
     */
    fun addAudioReferenceTag(percent: Int) {
        _uiState.value = _uiState.value.copy(
            audioReferencePercent = percent,
            showResetButton = true
        )
    }

    /**
     * 移除音频参考标签
     */
    fun removeAudioReferenceTag() {
        _uiState.value = _uiState.value.copy(audioReferencePercent = null)
        checkResetButtonVisibility()
    }

    /**
     * 添加音调标签
     */
    fun addToneTag(tone: String) {
        _uiState.value = _uiState.value.copy(
            toneTag = tone,
            showResetButton = true
        )
    }

    /**
     * 移除音调标签
     */
    fun removeToneTag() {
        _uiState.value = _uiState.value.copy(toneTag = null)
        checkResetButtonVisibility()
    }

    /**
     * 添加风格标签
     */
    fun addStyleTag(style: String, iconRes: Int? = null) {
        _uiState.value = _uiState.value.copy(
            styleTag = style,
            styleTagIcon = iconRes,
            showResetButton = true
        )
    }

    /**
     * 移除风格标签
     */
    fun removeStyleTag() {
        _uiState.value = _uiState.value.copy(
            styleTag = null,
            styleTagIcon = null
        )
        checkResetButtonVisibility()
    }

    /**
     * 添加音乐风格
     */
    fun addMusicStyle(style: MusicStyleItem) {
        val currentStyles = _uiState.value.musicStyles.toMutableList()
        if (!currentStyles.any { it.id == style.id }) {
            currentStyles.add(style)
            _uiState.value = _uiState.value.copy(musicStyles = currentStyles)
            updateConfirmButtonState()
        }
    }

    /**
     * 移除音乐风格
     */
    fun removeMusicStyle(styleId: String) {
        val currentStyles = _uiState.value.musicStyles.toMutableList()
        currentStyles.removeAll { it.id == styleId }
        _uiState.value = _uiState.value.copy(musicStyles = currentStyles)
        updateConfirmButtonState()
    }

    /**
     * 设置风格推荐列表
     */
    fun setRecommendedStyles(styles: List<RecommendedStyleItem>) {
        _uiState.value = _uiState.value.copy(recommendedStyles = styles)
    }

    /**
     * 更新积分数量
     */
    fun updateCreditCount(count: Int) {
        _uiState.value = _uiState.value.copy(creditCount = count)
    }

    /**
     * 确认提交
     */
    fun confirm() {
        // TODO: 实现提交逻辑
    }

    /**
     * 检查重置按钮显示状态
     */
    private fun checkResetButtonVisibility() {
        val state = _uiState.value
        val hasAnyTag = state.audioReferencePercent != null ||
                state.toneTag != null ||
                state.styleTag != null
        _uiState.value = _uiState.value.copy(showResetButton = hasAnyTag)
    }

    /**
     * 更新确认按钮状态
     */
    private fun updateConfirmButtonState() {
        val state = _uiState.value
        val isEnabled = if (state.isPureMusicMode) {
            state.pureMusicName.isNotBlank()
        } else {
            state.nonPureMusicName.isNotBlank() || state.inspirationContent.isNotBlank()
        }
        _uiState.value = _uiState.value.copy(isConfirmEnabled = isEnabled)
    }
}


