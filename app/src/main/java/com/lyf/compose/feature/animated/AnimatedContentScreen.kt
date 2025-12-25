package com.lyf.compose.feature.animated

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.lyf.compose.core.ui.components.scaffold.AppScaffold

@Composable
fun AnimatedContentScreen() {
    AppScaffold {
        DynamicTabContent()
    }

}

@Composable
fun DynamicTabContent(modifier: Modifier = Modifier) {
    var tab by remember { mutableIntStateOf(0) }
    Column {
        Button(onClick = { tab = (tab + 1) % 3 }, modifier = modifier) {
            Text(text = "Next Tab")
        }
        //标签之间的切换是非常僵硬的，没有任何动画效果。
//        when (tab) {
//            0 -> TabZero()
//            1 -> TabOne()
//            2 -> TabTwo()
//        }
        //标签切换的代码整体用AnimatedContent函数包裹了起来，
        // 并把当前tab作为参数传给了AnimatedContent函数。
        // 这就意味着每当tab发生变化时，AnimatedContent函数都会执行一段动画效果。

        //默认情况下，AnimatedContent函数执行的动画效果是让界面上的内容淡入淡出。
        //如果这并不是你想要的动画效果，没有关系，我们下面很快就会讲到如何定制任意你想要的动画效果。

        //todo animateAsState = ValueAnimator 类似属性动画
        AnimatedContent(targetState = tab, label = "Tab") { tab ->
            when (tab) {
                0 -> TabZero()
                1 -> TabOne()
                2 -> TabTwo()
            }
        }
    }
}

@Composable
fun TabZero() {
    Text(
        text = "Tab 0",
        fontSize = 36.sp,
        modifier = Modifier
            .background(Color.Red)
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun TabOne() {
    Text(
        text = "Tab 1",
        fontSize = 36.sp,
        modifier = Modifier
            .background(Color.Yellow)
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun TabTwo() {
    Text(
        text = "Tab 2",
        fontSize = 36.sp,
        modifier = Modifier
            .background(Color.Blue)
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}