package com.lyf.lingyingfacompose.ui.mastermode

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 复选框栏
 * 包含纯音乐复选框和重置按钮
 */
@Composable
fun CheckBoxBar(
    isPureMusicMode: Boolean,
    showResetButton: Boolean,
    onPureMusicClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MasterModeDimensions.CheckBoxBarHorizontalPadding,
                vertical = MasterModeDimensions.CheckBoxBarTopPadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 纯音乐复选框
        Row(
            modifier = Modifier.clickable { onPureMusicClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id = if (isPureMusicMode) {
                        MasterModeResources.IconMasterModeSelected
                    } else {
                        MasterModeResources.IconMasterModeNormal
                    }
                ),
                contentDescription = "纯音乐",
                modifier = Modifier.padding(end = 3.dp)
            )
            Text(
                text = "纯音乐",
                color = if (isPureMusicMode) {
                    MasterModeColors.TextWhite90
                } else {
                    MasterModeColors.TextWhite26
                },
                fontSize = 13.sp
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 重置按钮
        if (showResetButton) {
            Text(
                text = "重置",
                color = MasterModeColors.TextWhite,
                fontSize = 13.sp,
                modifier = Modifier
                    .clickable { onResetClick() }
                    .padding(end = MasterModeDimensions.CheckBoxBarResetEndPadding)
            )
        }
    }
}