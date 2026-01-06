package com.lyf.compose.feature.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyf.compose.R
import com.lyf.compose.nav.LocalNavigator
import com.lyf.compose.core.ui.components.scaffold.AppScaffold
import com.lyf.compose.newNav3.AnimatableRouter
import com.lyf.compose.newNav3.AnimateAsStateNavKey
import com.lyf.compose.newNav3.AnimateContentSizeNavKey
import com.lyf.compose.newNav3.AnimatedContentNavKey
import com.lyf.compose.newNav3.AnimatedVisibilityNavKey
import com.lyf.compose.test.randomColorBg

@Composable
fun CreateScreen() {
    val navigator = LocalNavigator.current
    AppScaffold {
        var position by remember { mutableIntStateOf(0) }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            items(30) { index ->
                Box(modifier = Modifier.clickable {
                    position = index
                    if (index == 0) {
                        navigator.navigate(AnimatedVisibilityNavKey)
                    }
                    if (index == 1) {
                        navigator.navigate(AnimateContentSizeNavKey)
                    }
                    if (index == 2) {
                        navigator.navigate(AnimatedContentNavKey)
                    }
                    if (index == 3) {
                        navigator.navigate(AnimateAsStateNavKey)
                    }
                    if (index == 4) {
                        navigator.navigate(AnimatableRouter)
                    }
                }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .randomColorBg(),//todo 如果这里引用了position，则每次position变化，整个列表都会重组
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Item $index: count = $index",
                            style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center)
                        )
                    }
                    HorizontalDivider()

                    //todo 目前只有这一处用到了position，所以只有这一行会重组
                    if (position == index) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_mv_character_manage_checked),
                            contentDescription = "check",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 16.dp)
                                .randomColorBg(position)
                        )
                    }
                }
            }
        }
    }

}