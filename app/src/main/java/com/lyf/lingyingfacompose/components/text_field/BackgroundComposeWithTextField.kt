

package com.lyf.lingyingfacompose.components.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * creator: lt  2022/7/16  lt.dygzs@qq.com
 * effect : [GoodTextField]的背景
 *          Background of the [GoodTextField]
 * warning:
 */
interface BackgroundComposeWithTextField {
    /**
     * 通过[Modifier]来设置[GoodTextField]的背景
     * Set the background of the [GoodTextField] through [Modifier]
     */
    @Composable
    fun setBackground(modifier: Modifier): Modifier

    companion object {
        /**
         * 通过[Shape]和[Color]设置背景
         * Set the background through [Shape] and [Color]
         */
        fun createBackgroundCompose(shape: Shape, color: Color) =
            object : BackgroundComposeWithTextField {
                @Composable
                override fun setBackground(modifier: Modifier): Modifier {
                    return modifier.background(color, shape)
                }
            }

        /**
         * 默认[GoodTextField]的背景,灰色圆角矩形
         * Default [GoodTextField] background, gray rounded rectangle
         */
        val DEFAULT = createBackgroundCompose(RoundedCornerShape(8.dp), ColorF5)
    }
}