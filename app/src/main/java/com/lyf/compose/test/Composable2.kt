package com.lyf.compose.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun Modifier.randomColorBg(key: Any? = Unit): Modifier {
    val color = remember(key) {
        Color(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
    }
    return this.background(color)
}

@Preview
@Composable
fun ComposableScreen2() {
    var count by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .randomColorBg(count)
    ) {
        Text(
            modifier = Modifier
                .width(200.dp)
                .height(100.dp)
                .randomColorBg(count),
            text = "count = $count",
            style = TextStyle(fontSize = 30.sp, textAlign = TextAlign.Center)
        )
        Button(
            modifier = Modifier
                .width(200.dp)
                .height(100.dp)
                .randomColorBg(count),
            onClick = { count++ }
        ) {
            Text(
                modifier = Modifier.randomColorBg(),
                text = "ADD", style = TextStyle(fontSize = 30.sp, textAlign = TextAlign.Center)
            )
        }
    }
}