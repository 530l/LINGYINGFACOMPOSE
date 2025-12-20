package com.lyf.compose.core.ui.material3

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.joker.kit.core.designsystem.theme.SpaceHorizontalMedium
import com.joker.kit.core.designsystem.theme.SpaceHorizontalSmall
import com.joker.kit.core.designsystem.theme.SpaceHorizontalXLarge
import com.joker.kit.core.designsystem.theme.SpaceHorizontalXSmall
import com.joker.kit.core.designsystem.theme.SpaceHorizontalXXLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalSmall
import com.joker.kit.core.designsystem.theme.SpaceVerticalXLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalXSmall
import com.joker.kit.core.designsystem.theme.SpaceVerticalXXLarge


/**
 * 创建一个超大垂直间距(32dp)的Spacer组件
 * 使用方式：SpaceVerticalXXLarge()
 */
@Composable
fun SpaceVerticalXXLarge() {
    Spacer(modifier = Modifier.height(SpaceVerticalXXLarge))
}

/**
 * 创建一个特大垂直间距(24dp)的Spacer组件
 * 使用方式：SpaceVerticalXLarge()
 
 */
@Composable
fun SpaceVerticalXLarge() {
    Spacer(modifier = Modifier.height(SpaceVerticalXLarge))
}

/**
 * 创建一个大垂直间距(16dp)的Spacer组件
 * 使用方式：SpaceVerticalLarge()
 
 */
@Composable
fun SpaceVerticalLarge() {
    Spacer(modifier = Modifier.height(SpaceVerticalLarge))
}

/**
 * 创建一个中等垂直间距(12dp)的Spacer组件
 * 使用方式：SpaceVerticalMedium()
 
 */
@Composable
fun SpaceVerticalMedium() {
    Spacer(modifier = Modifier.height(SpaceVerticalMedium))
}

/**
 * 创建一个小垂直间距(8dp)的Spacer组件
 * 使用方式：SpaceVerticalSmall()
 
 */
@Composable
fun SpaceVerticalSmall() {
    Spacer(modifier = Modifier.height(SpaceVerticalSmall))
}

/**
 * 创建一个超小垂直间距(4dp)的Spacer组件
 * 使用方式：SpaceVerticalXSmall()
 
 */
@Composable
fun SpaceVerticalXSmall() {
    Spacer(modifier = Modifier.height(SpaceVerticalXSmall))
}
//endregion

//region 水平间距组件
/**
 * 创建一个超大水平间距(32dp)的Spacer组件
 * 使用方式：SpaceHorizontalXXLarge()
 
 */
@Composable
fun SpaceHorizontalXXLarge() {
    Spacer(modifier = Modifier.width(SpaceHorizontalXXLarge))
}

/**
 * 创建一个特大水平间距(24dp)的Spacer组件
 * 使用方式：SpaceHorizontalXLarge()
 
 */
@Composable
fun SpaceHorizontalXLarge() {
    Spacer(modifier = Modifier.width(SpaceHorizontalXLarge))
}

/**
 * 创建一个大水平间距(16dp)的Spacer组件
 * 使用方式：SpaceHorizontalLarge()
 
 */
@Composable
fun SpaceHorizontalLarge() {
    Spacer(modifier = Modifier.width(SpaceHorizontalXLarge))
}

/**
 * 创建一个中等水平间距(12dp)的Spacer组件
 * 使用方式：SpaceHorizontalMedium()
 
 */
@Composable
fun SpaceHorizontalMedium() {
    Spacer(modifier = Modifier.width(SpaceHorizontalMedium))
}

/**
 * 创建一个小水平间距(8dp)的Spacer组件
 * 使用方式：SpaceHorizontalSmall()
 
 */
@Composable
fun SpaceHorizontalSmall() {
    Spacer(modifier = Modifier.width(SpaceHorizontalSmall))
}

/**
 * 创建一个超小水平间距(4dp)的Spacer组件
 * 使用方式：SpaceHorizontalXSmall()
 
 */
@Composable
fun SpaceHorizontalXSmall() {
    Spacer(modifier = Modifier.width(SpaceHorizontalXSmall))
}