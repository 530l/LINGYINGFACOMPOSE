package com.lyf.lingyingfacompose.ui.wx.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author: njb
 * @date:   2025/8/18 17:08
 * @desc:   描述
 */
@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 2,
    style: TextStyle = TextStyle(fontSize = 14.sp)
){
    var isExpanded by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val hasMoreLines = textLayoutResult?.lineCount ?: 0 > maxLines

    Column(modifier = modifier) {
        Text(
            text = text,
            style = style,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
            onTextLayout = { textLayoutResult = it },
            modifier = Modifier.fillMaxWidth()
        )

        if (hasMoreLines) {
            Text(
                text = if (isExpanded) "收起" else "展开",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Blue
                ),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { isExpanded = !isExpanded }
            )
        }
    }
}