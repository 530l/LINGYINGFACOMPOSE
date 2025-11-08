package com.lyf.lingyingfacompose.ui.mastermode

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 标签菜单区域
 * 包含音频参考标签、音调标签、风格标签和删除按钮，以及菜单 RecyclerView
 */
@Composable
fun TagMenuSection(
    audioReferencePercent: Int?,
    toneTag: String?,
    styleTag: String?,
    styleTagIcon: Int?,
    menuItems: List<String> = emptyList(),
    onRemoveAudioReference: () -> Unit,
    onRemoveToneTag: () -> Unit,
    onRemoveStyleTag: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = MasterModeDimensions.TagMenuBottomPadding,
                bottom = MasterModeDimensions.TagMenuBottomPadding
            )
    ) {
        // 标签行
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MasterModeDimensions.TagMarginStart,
                    end = MasterModeDimensions.TagMenuDeleteIconMarginEnd
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 音频参考标签
            if (audioReferencePercent != null) {
                TagChip(
                    text = "音频参考 $audioReferencePercent%",
                    onCloseClick = onRemoveAudioReference,
                    modifier = Modifier.padding(end = MasterModeDimensions.TagMarginEnd)
                )
            }
            
            // 音调标签
            if (toneTag != null) {
                TagChip(
                    text = toneTag,
                    onCloseClick = onRemoveToneTag,
                    modifier = Modifier.padding(end = MasterModeDimensions.TagMarginEnd)
                )
            }
            
            // 风格标签
            if (styleTag != null) {
                StyleTagChip(
                    text = styleTag,
                    iconRes = styleTagIcon,
                    onCloseClick = onRemoveStyleTag
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 删除按钮
            Icon(
                painter = painterResource(id = MasterModeResources.IconDelBlack),
                contentDescription = "删除",
                modifier = Modifier
                    .clickable { onDeleteClick() }
                    .padding(end = MasterModeDimensions.TagMenuDeleteIconMarginEnd)
                    .padding(bottom = MasterModeDimensions.TagMenuDeleteIconMarginBottom),
                tint = MasterModeColors.TextWhite
            )
        }
        
        // 分隔线
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(MasterModeDimensions.TagMenuLineHeight)
                .padding(
                    horizontal = MasterModeDimensions.InputFieldHorizontalPadding,
                    vertical = MasterModeDimensions.TagMenuLineTopPadding
                )
                .background(MasterModeColors.DividerLine)
        )
        
        // 菜单 RecyclerView 区域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MasterModeDimensions.TagMenuRecyclerViewHeight)
        ) {
            // TODO: 实现 RecyclerView 内容
            // 这里可以使用 LazyRow 或自定义的滚动列表
            MenuRecyclerViewContent(items = menuItems)
        }
    }
}

/**
 * 标签芯片组件
 */
@Composable
private fun TagChip(
    text: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(MasterModeDimensions.TagCornerRadius))
            .background(MasterModeColors.TagBackground)
            .padding(
                horizontal = MasterModeDimensions.TagHorizontalPadding,
                vertical = MasterModeDimensions.TagVerticalPadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = MasterModeColors.TextWhite,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = MasterModeDimensions.TagTextMarginStart)
        )
        Spacer(modifier = Modifier.width(MasterModeDimensions.TagCloseMarginStart))
        Icon(
            painter = painterResource(id = MasterModeResources.IconDisableClose),
            contentDescription = "关闭",
            modifier = Modifier
                .size(MasterModeDimensions.TagIconSize)
                .clickable { onCloseClick() },
            tint = MasterModeColors.TextWhite
        )
    }
}

/**
 * 风格标签芯片组件（带图标）
 */
@Composable
private fun StyleTagChip(
    text: String,
    iconRes: Int?,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(MasterModeDimensions.TagCornerRadius))
            .background(MasterModeColors.TagBackground)
            .padding(
                horizontal = MasterModeDimensions.TagHorizontalPadding,
                vertical = MasterModeDimensions.TagVerticalPadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(MasterModeDimensions.TagStyleIconSize),
                tint = MasterModeColors.TextWhite
            )
            Spacer(modifier = Modifier.width(4.dp))
        } else {
            Spacer(modifier = Modifier.width(6.dp))
        }
        
        Text(
            text = text,
            color = MasterModeColors.TextWhite,
            fontSize = 12.sp,
            maxLines = 1
        )
        
        Spacer(modifier = Modifier.width(MasterModeDimensions.TagCloseMarginStart))
        Icon(
            painter = painterResource(id = MasterModeResources.IconDisableClose),
            contentDescription = "关闭",
            modifier = Modifier
                .size(MasterModeDimensions.TagIconSize)
                .clickable { onCloseClick() },
            tint = MasterModeColors.TextWhite
        )
    }
}

/**
 * 菜单 RecyclerView 内容
 */
@Composable
private fun MenuRecyclerViewContent(
    items: List<String>
) {
    if (items.isEmpty()) {
        // 如果没有数据，显示空状态或者默认菜单项
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(getDefaultMenuItems()) { item ->
                MenuItemChip(
                    text = item,
                    onClick = { /* TODO: 处理菜单项点击 */ }
                )
            }
        }
    } else {
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items) { item ->
                MenuItemChip(
                    text = item,
                    onClick = { /* TODO: 处理菜单项点击 */ }
                )
            }
        }
    }
}

/**
 * 获取默认菜单项
 */
private fun getDefaultMenuItems(): List<String> {
    return listOf(
        "节奏", "旋律", "和声", "音色", "速度", "调性"
    )
}

/**
 * 菜单项芯片
 */
@Composable
private fun MenuItemChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MasterModeColors.TagBackground)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = text,
            color = MasterModeColors.TextWhite,
            fontSize = 12.sp
        )
    }
}

