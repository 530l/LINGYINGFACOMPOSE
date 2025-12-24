package com.lyf.compose.test

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lyf.compose.R

//再传统的 View布局中，margin padding 一个外边距，一个内边距，主要区分是颜色。
//在 compose 中，可以先 padding 再设置颜色。再设置 padding， 就可以区分颜色。
//todo Modifier 是顺序敏感，叠加的依次应用的，不是覆盖的。
// 通用属性用 Modifier，专项属性用函数的内置的参数，
// 例如通用的 padding，背景，宽高，圆角，切边，点击事件，触摸事件 等等等等。。。
// 专项属性例如：文字大小，文字颜色等等。
@Composable
fun MarginScreen() {
    //这种效果相当于 margin ,先把 Column 推 12.dp 再设置颜色。
    Column(
        modifier = Modifier
            .padding(12.dp)
            .background(Color(0xFFF0F0F0))
    ) {
        Image(painter = painterResource(R.drawable.icon_logo), contentDescription = null)
        Text("Margin")
    }
}

@Composable
fun PaddingScreen() {
    //这种效果相当于 padding ,先设置颜色再设置 padding。
    Column(
        modifier = Modifier
            .background(Color(0xFFF0F0F0))
            .padding(12.dp)
    ) {
        Image(painter = painterResource(R.drawable.icon_logo), contentDescription = null)
        Text("Padding")
    }
}