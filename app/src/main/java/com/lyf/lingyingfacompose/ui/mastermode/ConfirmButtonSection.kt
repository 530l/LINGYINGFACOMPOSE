package com.lyf.lingyingfacompose.ui.mastermode

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 确认按钮区域
 * 包含确认按钮、积分显示和三个点图标
 */
@Composable
fun ConfirmButtonSection(
    isEnabled: Boolean,
    creditCount: Int,
    onConfirmClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MasterModeDimensions.ConfirmButtonHorizontalPadding,
                vertical = 0.dp
            )
            .padding(bottom = MasterModeDimensions.ConfirmButtonBottomPadding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MasterModeDimensions.ConfirmButtonHeight)
                .clip(RoundedCornerShape(MasterModeDimensions.ConfirmButtonCornerRadius))
                .background(
                    if (isEnabled) {
                        MasterModeColors.ConfirmButtonBackground
                    } else {
                        MasterModeColors.ConfirmButtonDisabled
                    }
                )
                .clickable(enabled = isEnabled) { onConfirmClick() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 确认文本
                Text(
                    text = "确认",
                    color = MasterModeColors.TextBlack,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                // 三个点图标
                Icon(
                    painter = painterResource(id = MasterModeResources.IconBlackPoints),
                    contentDescription = null,
                    modifier = Modifier.padding(start = MasterModeDimensions.ConfirmButtonIconMarginStart),
                    tint = MasterModeColors.TextBlack
                )
                
                // 积分文本
                if (creditCount > 0) {
                    Text(
                        text = creditCount.toString(),
                        color = MasterModeColors.TextBlack,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = MasterModeDimensions.ConfirmButtonCreditMarginStart)
                    )
                }
            }
        }
    }
}

