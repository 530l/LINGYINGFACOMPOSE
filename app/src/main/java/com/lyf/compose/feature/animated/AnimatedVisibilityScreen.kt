package com.lyf.compose.feature.animated

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyf.compose.R
import com.lyf.compose.core.ui.components.scaffold.AppScaffold

//todo Compose的动画功能高度依赖Compose的State概念
@Composable
fun AnimatedVisibilityScreen(onBack: () -> Unit) {
    AppScaffold(onBackClick = onBack) {
        val state = rememberLazyListState()
        Box(modifier = Modifier.background(Color.White)) {
            ScrollableList(state)
            val isExpand by remember {
                derivedStateOf { state.firstVisibleItemIndex == 0 }
            }
            EditFab(isExpand, Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp))
        }
    }
}

@Composable
fun ScrollableList(state: LazyListState) {
    val list = remember { ('A'..'Z').map { it.toString() } }
    LazyColumn(state = state) {
        items(list) { letter ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(10.dp)
            ) {
                Text(
                    text = letter,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun EditFab(isExpand: Boolean, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = { },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_edit_music),
                contentDescription = null
            )
//            if (isExpand) {
//                Text(
//                    text = "EDIT",
//                    modifier = Modifier
//                        .padding(start = 8.dp, top = 3.dp)
//                )
//            }
            AnimatedVisibility(isExpand) {
                Text(
                    text = "EDIT",
                    modifier = Modifier
                        .padding(start = 8.dp, top = 3.dp)
                )
            }

        }
    }
}