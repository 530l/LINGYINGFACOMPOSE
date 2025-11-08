package com.lyf.lingyingfacompose.ui.mastermode

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 输入区域
 * 根据纯音乐模式显示不同的输入界面
 */
@Composable
fun InputArea(
    isPureMusicMode: Boolean,
    pureMusicName: String,
    nonPureMusicName: String,
    inspirationContent: String,
    showAddStyleButton: Boolean,
    onPureMusicNameChange: (String) -> Unit,
    onNonPureMusicNameChange: (String) -> Unit,
    onInspirationContentChange: (String) -> Unit,
    onAddStyleClick: () -> Unit,
    onResumeAddStyleClick: () -> Unit,
    tagMenuContent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(MasterModeDimensions.InputAreaCornerRadius))
            .background(MasterModeColors.InputAreaBackground)
//            .border(
//                width = MasterModeDimensions.InputAreaStrokeWidth,
//                color = MasterModeColors.TextWhite,
//                shape = RoundedCornerShape(MasterModeDimensions.InputAreaCornerRadius)
//            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // 上方输入内容区域（可滚动）
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    //.weight(1f)的作用 在 Column或 Row中按权重分配剩余空间
                    //使用位置:只能用在 Column或 Row的直接子 Composable 上，也就是你说的“子组件”
                    //父组件要求:父组件必须是 Column或 Row，否则无法使用 .weight()
                    //实现“上方可滚动区域撑满剩余空间 + 下方固定按钮/输入栏”等布局
                    .weight(1f)
            ) {
                if (isPureMusicMode) {
                    // 纯音乐输入模式
                    PureMusicInputContent(
                        pureMusicName = pureMusicName,
                        showAddStyleButton = showAddStyleButton,
                        onNameChange = onPureMusicNameChange,
                        onAddStyleClick = onAddStyleClick,
                        onResumeAddStyleClick = onResumeAddStyleClick
                    )
                } else {
                    // 非纯音乐输入模式
                    NonPureMusicInputContent(
                        nonPureMusicName = nonPureMusicName,
                        inspirationContent = inspirationContent,
                        onNameChange = onNonPureMusicNameChange,
                        onInspirationChange = onInspirationContentChange
                    )
                }
            }

            // 标签菜单区域（固定在底部，公共区域）
            Box(modifier = Modifier.fillMaxWidth()) {
                tagMenuContent()
            }
        }
    }
}

/**
 * 纯音乐输入内容
 */
@Composable
private fun PureMusicInputContent(
    pureMusicName: String,
    showAddStyleButton: Boolean,
    onNameChange: (String) -> Unit,
    onAddStyleClick: () -> Unit,
    onResumeAddStyleClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // 歌曲名称输入框
        TextField(
            value = pureMusicName,
            onValueChange = onNameChange,
            placeholder = {
                Text(
                    text = "请输入歌曲名称",
                    color = MasterModeColors.TextWhite40,
                    fontSize = 12.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MasterModeDimensions.InputFieldHorizontalPadding,
                    vertical = MasterModeDimensions.InputFieldTopPadding
                ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MasterModeColors.TextWhite90,
                unfocusedTextColor = MasterModeColors.TextWhite90,
                focusedContainerColor = MasterModeColors.Transparent,
                unfocusedContainerColor = MasterModeColors.Transparent,
                focusedIndicatorColor = MasterModeColors.Transparent,
                unfocusedIndicatorColor = MasterModeColors.Transparent,
                disabledIndicatorColor = MasterModeColors.Transparent
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
            singleLine = true
        )

        // 分隔线
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(MasterModeDimensions.InputFieldLineHeight)
                .padding(horizontal = MasterModeDimensions.InputFieldHorizontalPadding)
                .background(MasterModeColors.DividerLine)
        )

        // 添加音乐风格按钮或继续添加按钮
        if (showAddStyleButton) {
            AddStyleButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MasterModeDimensions.AddStyleAreaHorizontalPadding,
                        vertical = MasterModeDimensions.AddStyleAreaTopPadding
                    ),
                onClick = onAddStyleClick
            )
        } else {
            ResumeAddStyleButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MasterModeDimensions.AddStyleAreaHorizontalPadding,
                        vertical = MasterModeDimensions.ResumeAddStyleTopPadding
                    ),
                onClick = onResumeAddStyleClick
            )
        }
    }
}

/**
 * 非纯音乐输入内容
 */
@Composable
private fun NonPureMusicInputContent(
    nonPureMusicName: String,
    inspirationContent: String,
    onNameChange: (String) -> Unit,
    onInspirationChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // 歌曲名称输入框
        TextField(
            value = nonPureMusicName,
            onValueChange = onNameChange,
            placeholder = {
                Text(
                    text = "请输入歌曲名称",
                    color = MasterModeColors.TextWhite40,
                    fontSize = 12.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MasterModeDimensions.InputFieldHorizontalPadding,
                    vertical = MasterModeDimensions.InputFieldTopPadding
                ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MasterModeColors.TextWhite90,
                unfocusedTextColor = MasterModeColors.TextWhite90,
                focusedContainerColor = MasterModeColors.Transparent,
                unfocusedContainerColor = MasterModeColors.Transparent,
                focusedIndicatorColor = MasterModeColors.Transparent,
                unfocusedIndicatorColor = MasterModeColors.Transparent,
                disabledIndicatorColor = MasterModeColors.Transparent
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
            singleLine = true
        )

        // 分隔线
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(MasterModeDimensions.InputFieldLineHeight)
                .padding(
                    horizontal = MasterModeDimensions.InputFieldHorizontalPadding - 4.dp,
                    vertical = MasterModeDimensions.InputFieldLineTopPadding
                )
                .background(MasterModeColors.DividerLine)
        )

        // 灵感输入区域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    horizontal = MasterModeDimensions.InputFieldHorizontalPadding,
                    vertical = MasterModeDimensions.InputFieldLineTopPadding
                )
        ) {
            val scrollState = rememberScrollState()
            TextField(
                value = inspirationContent,
                onValueChange = onInspirationChange,
                placeholder = {
                    Text(
                        text = "输入你的音乐灵感",
                        color = MasterModeColors.TextWhite40,
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MasterModeColors.TextWhite40,
                    unfocusedTextColor = MasterModeColors.TextWhite40,
                    focusedContainerColor = MasterModeColors.Transparent,
                    unfocusedContainerColor = MasterModeColors.Transparent,
                    focusedIndicatorColor = MasterModeColors.Transparent,
                    unfocusedIndicatorColor = MasterModeColors.Transparent,
                    disabledIndicatorColor = MasterModeColors.Transparent
                ),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                minLines = 1
            )
        }
    }
}

/**
 * 添加风格按钮
 */
@Composable
private fun AddStyleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(MasterModeDimensions.AddStyleAreaHeight)
            .clip(RoundedCornerShape(MasterModeDimensions.AddStyleAreaCornerRadius))
            .background(MasterModeColors.AddStyleBackground)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "+",
                color = MasterModeColors.TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "添加音乐风格",
                color = MasterModeColors.TextWhite,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * 继续添加风格按钮
 */
@Composable
private fun ResumeAddStyleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 添加图标
        Icon(
            painter = painterResource(id = MasterModeResources.IconAddBlack),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MasterModeColors.TextWhite40
        )
        Spacer(modifier = Modifier.padding(start = 4.dp))
        Text(
            text = "继续添加音乐风格",
            color = MasterModeColors.TextWhite40,
            fontSize = 12.sp
        )
    }
}

