package com.lyf.compose.test

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyf.compose.R

@Preview
@Composable
fun ComposableScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.Gray, CircleShape) // 背景
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Transparent,
                shadowElevation = 2.dp
            ) {
                Image(
                    contentScale = ContentScale.Crop,
                    painter = painterResource(R.drawable.bg_asset_main_top_image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        //todo 别一上来就“想要啥效果就多包一层”，
        //     先问问自己：这个效果能不能直接用 Modifier组合拼出来？

        Image(
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.bg_asset_main_top_image),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Yellow, CircleShape)
                .background(Color.Red, CircleShape)       // 背景
                .shadow(2.dp, CircleShape)                 // 阴影
        )

        //todo 聊天气泡 类似这种，嵌套了 row colum
        Row(Modifier.fillMaxWidth()) {
            Image(
                painterResource(R.drawable.bg_asset_main_top_image),
                null, Modifier.size(40.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text("你好，这是一条消息")
                Text("10:24", fontSize = 12.sp, color = Color.Gray)
            }
        }
        //todo 自定义 Layout
        // 从Row + Column 两层 → 自定义 Layout 一层,
        // 对于高频场景（聊天、Feed 流），效果特别明显。
        MessageBubble(
            "hello",
            "12:00", R.drawable.bg_asset_main_top_image
        )

        //todo 利用 SubcomposeLayout 延迟布局 + 复用资源
        val hasDiscount = false
        Box {
            Image(painterResource(
                R.drawable.bg_asset_main_top_image),null,
                modifier = Modifier.size(40.dp))
            //即使 if 条件不成立，Compose 还是会走一次测量逻辑，
            // 哪怕只是“跳过绘制”，我们依然付出了性能开销。
            // 在一个电商首页有几十几百个商品卡片时，这就是“白干活”。
            if (hasDiscount) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(Color.Red,
                            RoundedCornerShape(bottomStart = 6.dp))
                ) {
                    Text("-30%", color = Color.White)
                }
            }
        }
        ProductCard(R.drawable.bg_asset_main_top_image, hasDiscount)

    }
}

@Composable
fun ProductCard(image: Int, hasDiscount: Boolean) {
    SubcomposeLayout { constraints ->
        val main = subcompose("main") {
            Image(
                painterResource(image), null,
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }.map { it.measure(constraints) }

        val badge = if (hasDiscount) {
            subcompose("badge") {
                Box(
                    Modifier
                        .background(Color.Red, RoundedCornerShape(bottomStart = 6.dp))
                        .padding(4.dp)
                ) { Text("-30%", color = Color.White) }
            }.map { it.measure(constraints) }
        } else emptyList()

        val w = main.maxOf { it.width }
        val h = main.maxOf { it.height }
        layout(w, h) {
            main.forEach { it.place(0, 0) }
            badge.forEach { it.place(w - it.width, 0) }
        }
    }
}


@Composable
fun MessageBubble(text: String, time: String, avatarImg: Int) {
    Layout(
        content = {
            Image(painterResource(avatarImg), null, Modifier.size(40.dp))
            Text(text)
            Text(time, fontSize = 12.sp, color = Color.Gray)
        }
    ) { measurables, constraints ->
        val avatar = measurables[0].measure(constraints)
        val textM = measurables[1].measure(constraints)
        val timeM = measurables[2].measure(constraints)

        val height = maxOf(avatar.height, textM.height + timeM.height)
        layout(constraints.maxWidth, height) {
            avatar.place(0, (height - avatar.height) / 2)
            textM.place(avatar.width + 8, 0)
            timeM.place(avatar.width + 8, textM.height)
        }
    }
}
