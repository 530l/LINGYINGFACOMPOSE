package com.lyf.lingyingfacompose.ui.wx.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
/**
 * @author: njb
 * @date:   2025/8/18 17:14
 * @desc:   描述
 */
@SuppressLint("RememberInComposition")
@Composable
fun CommentInputBar(
    modifier: Modifier = Modifier,
    hint: String,
    content: String,
    onContentChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onDismiss: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f)) // 半透明黑色遮罩
            .clickable { onDismiss() } // 点击外部关闭
    ) {
        Row(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .clickable(
                    onClick = { /* 点击输入框区域不做任何操作 */ },
                    indication = null, // 移除点击反馈
                    interactionSource = MutableInteractionSource()
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                placeholder = {
                    Text(text = hint, fontSize = 14.sp)
                },
                modifier = Modifier.weight(1f),
                maxLines = 3,
                singleLine = false,
                textStyle = TextStyle(fontSize = 14.sp),
                shape = TextFieldDefaults.outlinedShape,
                minLines = 1,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onSendClick,
                enabled = content.isNotBlank(),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(text = "发送", fontSize = 14.sp)
            }
        }
    }
}