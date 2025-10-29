package com.lyf.lingyingfacompose.ui.wx.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyf.lingyingfacompose.ui.wx.ui.model.CommentListBean

/**
 * @author: njb
 * @date:   2025/8/18 17:11
 * @desc:   描述
 */
@Composable
fun CommentItem(
    comment: CommentListBean,
    onReplyClick: () -> Unit,
    onLongClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onReplyClick() }, // 点击事件
                    onLongPress = { onLongClick() } // 长按事件
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 评论内容（区分普通评论和回复）
        val commentText = if (comment.to_user_id.isNullOrBlank()) {
            "${comment.user_name}：${comment.content}"
        } else {
            "${comment.user_name} 回复 ${comment.to_user_name}：${comment.content}"
        }

        Text(
            text = commentText,
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.DarkGray
            ),
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "回复",
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Blue
            ),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}