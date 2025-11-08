 package com.lyf.lingyingfacompose.ui.wx.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

/**
 * @author: njb
 * @date:   2025/8/18 17:04
 * @desc:   描述
 */
@Composable
fun CircleHeader(){
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.height(120.dp)) {
            Image(
                painter = rememberAsyncImagePainter("https://picsum.photos/1080/300"),
                contentDescription = "朋友圈背景",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // 用户名与头像垂直居中
                    modifier = Modifier
                        .align(Alignment.TopEnd) // 从顶部开始定位
                        .padding(end = 16.dp) // 右侧间距
                        .offset(y = 30.dp)
                ) {
                    // 用户名（在头像左侧）
                    Text(
                        text = "当前用户",
                        color = Color.White,
                        modifier = Modifier.padding(end = 12.dp). offset(x=10.dp,y = (-8).dp)
                    )

                    // 用户头像
                    Image(
                        painter = rememberAsyncImagePainter("https://picsum.photos/200/200?user"),
                        contentDescription = "用户头像",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .clickable { /* 头像点击事件 */ }
                            .align(Alignment.Bottom),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .width(220.dp)
                .height(80.dp)
                .padding(12.dp)
                .offset(x = 20.dp)
                .clickable { /* 消息点击事件 */ },
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter("https://picsum.photos/40/40?msg"),
                    contentDescription = "消息图标",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "10条新信息")
            }
        }
    }
}