package com.lyf.compose.feature.animated

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
    //todo 前面所学习的所有动画效果都是一次性的，即播放完成之后就自动停止了。
    //     那如果我们想要持续性地循环播放某个动画该怎么办呢？
    //     这就需要借助InfiniteTransition来创建永不停止的动画效果了，
    //     这种效果有一个专业术语，叫骨架屏（Skeleton Loading）。
    //     在Compose中实现类似的骨架屏效果其实还挺容易的，
    //     只需要用几个Box控件配合着Column和Row组合一下就能搞定了
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

        //默认情况下，AnimatedContent 函数执行的动画效果是让界面上的内容淡入淡出。
        //如果这并不是你想要的动画效果，没有关系，我们下面很快就会讲到如何定制任意你想要的动画效果。
        //todo AnimatedContent 类似共享动画

        //todo animateAsState = ValueAnimator 类似属性动画
        AnimatedContent(
            targetState = tab,
            label = "Tab",
//            transitionSpec = {
//                //todo 默认情况下，AnimatedContent 函数执行的动画效果是让界面上的内容淡入淡出。
//                // 如果这并不是你想要的动画效果，没有关系，我们下面很快就会讲到如何定制任意你想要的动画效果。
//                (slideInHorizontally())
//                    .togetherWith(slideOutHorizontally())
//            }
            //slideInHorizontally()函数表示新内容是通过横向滑动进入屏幕的，
            //slideOutHorizontally()函数表示旧内容是通过横向滑动离开屏幕的。
            //中间用togetherWith串接起来，表示两个动画是同时执行的。
            //////////////////////////////////////////////////////////////
            // 具体问题出在哪儿了呢？仔细观察一下不难发现，
            // 旧内容离开屏幕时是从右往左离开的，
            // 新内容进入屏幕是从左往右进入的。这是完全不符合我们所习惯的动画效果的，
            /////////////////////////////////////////////////////////
            //todo  通常来讲，旧内容向左离开屏幕是没有问题的，但是新内容应该是从右往左进入屏幕才对。
            /////////////////////////////////////////////////////////
            // slideInHorizontally函数支持传入一个initialOffsetX参数，
            // 用于指定新内容进入屏幕时的初始位置。
            // slideOutHorizontally函数支持传入一个targetOffsetX参数，
            // 用于指定旧内容离开屏幕后停止的位置。
            transitionSpec = {
                (slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }))
                    .togetherWith(
                        slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth })
                    )
            }

        ) { tab ->
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