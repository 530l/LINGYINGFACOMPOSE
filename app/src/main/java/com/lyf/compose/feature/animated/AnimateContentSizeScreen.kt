package com.lyf.compose.feature.animated


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//🔑 简单说：
//animateContentSize：尺寸变 → 动画
//AnimatedVisibility：出现/消失 → 动画

//🔸 animateContentSize
//不是真正“显示/隐藏”，而是 内容一直存在，只是高度（或宽度）变了。
//动画 只有一种：平滑拉伸/收缩（尺寸插值）。
//适合：展开/收起文本、详情面板等“内容变多变少但始终在”的场景。

//🔸  AnimatedVisibility
// 是真的“出现”和“消失” —— 不可见时，内容完全不参与布局和重组。
// 动画 高度可定制：淡入淡出（fadeIn/fadeOut）、滑动（slideIn/slideOut）、缩放（scaleIn/scaleOut）等，还能组

//todo AnimatedVisibility和animateContentSize函数主要都是用于控制具体某个控件的动画效果。

@Composable
fun AnimateContentSizeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {
        ExpandableText(
            "Lorem ipsum dolor sit amet," +
                    " consectetur adipiscing elit, sed do eiusmod tempor " +
                    "incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                    "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex" +
                    " ea commodo consequat. Duis aute irure dolor in reprehenderit in" +
                    " voluptate velit esse cillum dolore eu fugiat nulla pariatur." +
                    " Excepteur sint occaecat cupidatat non proident, sunt in culpa " +
                    "qui officia deserunt mollit anim id est laborum.",
        )
    }
}

@Composable
fun ExpandableText(text: String) {
    var isExpand by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red) // 加个背景看实际大小
            .clickable {
                isExpand = !isExpand
            }
            .animateContentSize()
    ) {
//        Text(
//            text = text, modifier = Modifier
//                .padding(5.dp)
//                .background(Color.Cyan),
        //.animateContentSize()//动画内容大小,可以实现内容缩放动画
//                .clickable {
//                    isExpand = !isExpand
//                },
        //maxLines = if (isExpand) 10 else 2,
//            overflow = TextOverflow.Ellipsis,
//            fontSize = 16.sp,
//            textAlign = TextAlign.Start
//        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Green)
                .height(100.dp)

        )
//        if (isExpand)
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.Yellow)
//                    .height(200.dp)
//
//            )
        AnimatedVisibility(isExpand) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Yellow)
                    .height(200.dp)

            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .height(100.dp)

        )
    }
}