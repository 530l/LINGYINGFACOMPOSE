package com.lyf.compose.ui.components.appbar

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.lyf.compose.R

/**
 * 回退按钮组件
 *
 * @param modifier 修饰符，用于自定义组件样式
 * @param tint 图标颜色，默认使用Unspecified颜色
 * @param onClick 点击回调函数
 */
@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    onClick: () -> Unit = {}
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_left),
            contentDescription = "Back",
            tint = tint
        )
    }
}
