package com.lyf.lingyingfacompose.ui.wx.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

/**
 * @author: njb
 * @date:   2025/8/18 17:10
 * @desc:   描述
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NineGridImages(
    images: List<Any?>,
    modifier: Modifier = Modifier,
    onImageClick: (List<String>, Int) -> Unit,
    overallPadding: Dp = 14.dp,
    itemSpacing: Dp = 6.dp
){
    val imageCount = images.size
/*    val itemSize = when (imageCount) {
        1 -> 200.dp
        2, 3 -> 200.dp
        else -> 80.dp
    }*/
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val totalOverallPadding = overallPadding.times(2)
    val totalItemSpacing = itemSpacing.times(2)
    val availableWidth = screenWidth.minus(totalOverallPadding).minus(totalItemSpacing)
    val itemSize = availableWidth.div(3)
    Log.d("NineGridImages", "图片总数: ${images.size}")

    FlowRow(
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth().padding(overallPadding)
    ) {
        images.take(9).forEachIndexed { index, url ->
            val imageUrl = url?.toString() ?: ""

            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .build()
                ),
                contentDescription = "朋友圈图片 $index",
                modifier = Modifier
                    .size(itemSize)
                    .aspectRatio(1f) // 确保正方形
                    .clickable {
                        // 安全转换列表类型
                        val stringList = images.filterIsInstance<String>()
                        onImageClick(stringList, index)
                    },
                contentScale = ContentScale.Crop
            )
        }
    }
}