package com.lyf.lingyingfacompose.test

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyf.lingyingfacompose.R

val cache = mutableMapOf<String, MutableList<String>>()

fun t1() {
    val list = cache.getOrPut("users") { ArrayList() }
    list.add("Alice")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
//    val rows = 3
//    val columns = 3
//    FlowRow(
//        modifier = Modifier.padding(4.dp),
//        horizontalArrangement = Arrangement.spacedBy(4.dp),
//        maxItemsInEachRow = rows
//    ) {
//        val itemModifier = Modifier
//            .padding(4.dp)
//            .height(80.dp)
//            .weight(1f)
//            .clip(RoundedCornerShape(8.dp))
//            .background(Color333)
//        repeat(rows * columns) {
//            Spacer(modifier = itemModifier)
//        }
//        Spacer(modifier = itemModifier)
//    }

    FlowRow(
        modifier = Modifier.padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = 3
    ) {
        val itemModifier = Modifier.clip(RoundedCornerShape(8.dp))
        Box(
            modifier = itemModifier
                .height(200.dp)
                .width(60.dp)
                .background(Color.Red)
        )
        Box(
            modifier = itemModifier
                .height(200.dp)
                .fillMaxWidth(0.5f)
                .background(Color.Blue)
        ) {
            Button(
                onClick = { },
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.TopEnd)

            ) { }

            Image(
                painter = painterResource(R.drawable.bg_creation_singer_dialog),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.TopStart)
            )
        }
        Box(
            modifier = itemModifier
                .height(200.dp)
                .weight(1f)
                .background(Color.Magenta)
        )
    }
}

