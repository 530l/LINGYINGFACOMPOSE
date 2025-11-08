package com.lyf.lingyingfacompose.ui.mastermode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * 大师模式主界面
 * 整合所有子组件，管理整体布局和状态
 */
@Composable
fun MasterModeScreen(
    viewModel: MasterModeViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MasterModeColors.Background)
    ) {
        // Toolbar 区域
        ToolbarSection(
            onBackClick = onBackClick,
            onTitleClick = {
                // TODO: 处理标题点击事件（如显示下拉菜单）
            }
        )

        // 复选框栏
        CheckBoxBar(
            isPureMusicMode = uiState.isPureMusicMode,
            showResetButton = uiState.showResetButton,
            onPureMusicClick = { viewModel.togglePureMusicMode() },
            onResetClick = { viewModel.resetAll() }
        )

        // 输入区域（可滚动）
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = MasterModeDimensions.InputAreaHorizontalPadding,
                    vertical = MasterModeDimensions.InputAreaTopPadding
                )
        ) {
            InputArea(
                isPureMusicMode = uiState.isPureMusicMode,
                pureMusicName = uiState.pureMusicName,
                nonPureMusicName = uiState.nonPureMusicName,
                inspirationContent = uiState.inspirationContent,
                showAddStyleButton = uiState.musicStyles.isEmpty(),
                onPureMusicNameChange = { viewModel.updatePureMusicName(it) },
                onNonPureMusicNameChange = { viewModel.updateNonPureMusicName(it) },
                onInspirationContentChange = { viewModel.updateInspirationContent(it) },
                onAddStyleClick = {
                    // TODO: 处理添加风格点击事件
                },
                onResumeAddStyleClick = {
                    // TODO: 处理继续添加风格点击事件
                },
                tagMenuContent = {
                    TagMenuSection(
                        audioReferencePercent = uiState.audioReferencePercent,
                        toneTag = uiState.toneTag,
                        styleTag = uiState.styleTag,
                        styleTagIcon = uiState.styleTagIcon,
                        menuItems = emptyList(), // TODO: 从 ViewModel 获取菜单项
                        onRemoveAudioReference = { viewModel.removeAudioReferenceTag() },
                        onRemoveToneTag = { viewModel.removeToneTag() },
                        onRemoveStyleTag = { viewModel.removeStyleTag() },
                        onDeleteClick = {
                            // TODO: 处理删除点击事件
                        }
                    )
                }
            )
        }

        // 风格推荐区域
        RememberStyleSection(
            recommendedStyles = uiState.recommendedStyles,
            onStyleClick = { style ->
                // TODO: 处理风格推荐点击事件
                viewModel.addMusicStyle(
                    MusicStyleItem(
                        id = style.id,
                        name = style.name,
                        iconRes = style.iconRes
                    )
                )
            }
        )

        // 确认按钮区域
        ConfirmButtonSection(
            isEnabled = uiState.isConfirmEnabled,
            creditCount = uiState.creditCount,
            onConfirmClick = { viewModel.confirm() }
        )
    }
}