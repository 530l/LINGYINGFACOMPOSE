package com.lyf.lingyingfacompose.ui.mastermode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 风格推荐区域
 * 显示推荐的音乐风格列表
 */
@Composable
fun RememberStyleSection(
    recommendedStyles: List<RecommendedStyleItem>,
    onStyleClick: (RecommendedStyleItem) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MasterModeDimensions.RememberStyleHeight)
            .padding(
                top = MasterModeDimensions.RememberStyleTopPadding,
                bottom = MasterModeDimensions.RememberStyleBottomPadding,
                start = MasterModeDimensions.RememberStyleHorizontalPadding,
                end = MasterModeDimensions.RememberStyleHorizontalPadding
            )
            .clip(RoundedCornerShape(MasterModeDimensions.RememberStyleCornerRadius))
            .background(MasterModeColors.InputAreaBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 标题
            Text(
                text = "风格推荐",
                color = MasterModeColors.TextWhite90,
                fontSize = 12.sp,
                modifier = Modifier.padding(
                    start = MasterModeDimensions.RememberStyleTitleMarginStart,
                    top = MasterModeDimensions.RememberStyleTitleMarginTop
                )
            )

            // 风格推荐列表
            RecommendedStyleList(
                styles = recommendedStyles,
                onStyleClick = onStyleClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MasterModeDimensions.RememberStyleRecyclerViewTopPadding,
                        start = MasterModeDimensions.RememberStyleRecyclerViewHorizontalPadding
                    )
            )
        }
    }
}

/**
 * 风格推荐列表
 */
@Composable
private fun RecommendedStyleList(
    styles: List<RecommendedStyleItem>,
    onStyleClick: (RecommendedStyleItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
    ) {
        items(styles) { style ->
            RecommendedStyleItem(
                style = style,
                onClick = { onStyleClick(style) },
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

/**
 * 风格推荐项
 */
@Composable
private fun RecommendedStyleItem(
    style: RecommendedStyleItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO: 根据实际设计实现风格推荐项的 UI
    // 这里提供一个基础实现
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MasterModeColors.TagBackground)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = style.name,
            color = MasterModeColors.TextWhite,
            fontSize = 12.sp
        )
    }
}


