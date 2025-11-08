package com.lyf.lingyingfacompose.ui.mastermode

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Toolbar 区域
 * 包含返回按钮、标题和下拉图标
 */
@Composable
fun ToolbarSection(
    onBackClick: () -> Unit = {},
    onTitleClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(MasterModeDimensions.ToolbarHeight)
            .padding(horizontal = MasterModeDimensions.ToolbarHorizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 返回按钮
        Image(
            painter = painterResource(id = MasterModeResources.IconBackWhite),
            contentDescription = "返回",
            modifier = Modifier
                .clickable { onBackClick() }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 标题和下拉图标
        Row(
            modifier = Modifier.clickable { onTitleClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "大师模式",
                color = MasterModeColors.TextWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(start = 4.dp))
            Icon(
                painter = painterResource(id = MasterModeResources.IconDownArrowWhite),
                contentDescription = "下拉",
                modifier = Modifier.size(16.dp),
                tint = MasterModeColors.TextWhite
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
    }
}