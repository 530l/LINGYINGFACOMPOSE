package com.lyf.lingyingfacompose.ui.wx.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

/**
 * @author: njb
 * @date:   2025/8/18 17:15
 * @desc:   描述
 */
@Composable
fun ImagePreview(
    images: List<String>,
    currentIndex: Int,
    onDismiss: () -> Unit,
    onLongClick: () -> Unit
){
    val pagerState = rememberPagerState(initialPage = currentIndex) { images.size }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onDismiss() }, // 点击事件
                    onLongPress = { onLongClick() } // 长按事件
                )
            },
    ) {
        // 水平分页预览
        HorizontalPager(state = pagerState) { page ->
            Image(
                // 修复：用 ImageRequest 配置 crossfade（淡入效果）
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(images[page]) // 图片地址
                        .crossfade(true) // 淡入效果（新版本正确用法）
                        .build()
                ),
                contentDescription = "预览图片 $page",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // 页码指示器
        Text(
            text = "${pagerState.currentPage + 1}/${images.size}",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        )
    }
}