package com.lyf.lingyingfacompose.ui.mastermode

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyf.lingyingfacompose.R

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


        Row(  // 音频参考标签    // 音调标签
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MasterModeDimensions.TagMarginStart,
                    end = MasterModeDimensions.TagMenuDeleteIconMarginEnd
                ), verticalAlignment = Alignment.CenterVertically
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
        }

        // 风格标签
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 4.dp,
                    start = MasterModeDimensions.TagMarginStart,
                    end = MasterModeDimensions.TagMenuDeleteIconMarginEnd
                ), verticalAlignment = Alignment.CenterVertically
        ) {
            if (styleTag != null) {
                StyleTagChip(
                    text = styleTag, iconRes = styleTagIcon, onCloseClick = onRemoveStyleTag
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 删除按钮
            Image(
                painter = painterResource(id = MasterModeResources.IconDelBlack),
                contentDescription = "删除",
                modifier = Modifier
                    .clickable { onDeleteClick() }
                    .padding(end = MasterModeDimensions.TagMenuDeleteIconMarginEnd),
            )
        }


        // 菜单，风格，音色， 音频
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MasterModeDimensions.TagMenuRecyclerViewHeight)
                .padding(top = 12.dp, start = 12.dp, end = 12.dp)
        ) {
            MenuRowContent(items = menuItems)
        }
    }
}

/**
 * 标签芯片组件
 */
@Composable
private fun TagChip(
    text: String, onCloseClick: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(MasterModeDimensions.TagCornerRadius))
            .background(MasterModeColors.TagBackground)
            .padding(
                horizontal = MasterModeDimensions.TagHorizontalPadding,
                vertical = MasterModeDimensions.TagVerticalPadding
            ), verticalAlignment = Alignment.CenterVertically
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
    text: String, iconRes: Int?, onCloseClick: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(MasterModeDimensions.TagCornerRadius))
            .background(MasterModeColors.TagBackground)
            .padding(
                horizontal = MasterModeDimensions.TagHorizontalPadding,
                vertical = MasterModeDimensions.TagVerticalPadding
            ), verticalAlignment = Alignment.CenterVertically
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
            text = text, color = MasterModeColors.TextWhite, fontSize = 12.sp, maxLines = 1
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

@Composable
private fun MenuRowContent(items: List<String>) {
    if (items.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // ← 设置 item 之间的间距
            // horizontalArrangement = Arrangement.SpaceEvenly // 或 SpaceBetween, Center 等
        ) {
            items.forEach { item ->
                MenuItemChip(
                    text = item,
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f) // 平分宽度
                )
            }
        }
    }
}


/**
 * 菜单项item
 */
@Composable
private fun MenuItemChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))//圆角
            .background(MasterModeColors.TagBackground)
            .clickable { onClick() }
            .padding(top = 4.dp, bottom = 4.dp)
//            .padding(horizontal = 12.dp, vertical = 12.dp) // ← 这是内边距，OK
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.icon_music_style),
                contentDescription = "添加",
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = text,
                color = MasterModeColors.TextWhite,
                fontSize = 12.sp,
                textAlign = TextAlign.Center, // 可选：居中对齐文字
                modifier = Modifier.fillMaxWidth() // 让文字也撑满 chip 宽度
            )
        }

    }
}
