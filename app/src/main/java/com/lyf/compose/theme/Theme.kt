package com.lyf.compose.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * 深色主题配色方案
 * 定义 MaterialTheme 中深色模式下的各种颜色角色（Color Roles）
 */
private val DarkColorScheme = darkColorScheme(
    // —————— 主色（Primary） ——————
    primary = PrimaryDark,               // 主要品牌色，用于 FAB、重要按钮等
    onPrimary = TextWhite,               // 在 primary 背景上的文字/图标颜色
    primaryContainer = PrimaryDark,      // 主色容器背景（如次要卡片）
    onPrimaryContainer = TextWhite,      // 在 primaryContainer 上的前景色
    inversePrimary = PrimaryLight,       // 反转主色（用于深色背景上的浅色主色）

    // —————— 次要色（Secondary） ——————
    secondary = ColorPurpleDark,         // 辅助品牌色
    onSecondary = TextWhite,             // 在 secondary 背景上的前景色
    secondaryContainer = ColorPurpleDark,// 次要色容器背景
    onSecondaryContainer = TextWhite,    // 在 secondaryContainer 上的前景色

    // —————— 第三色（Tertiary） ——————
    tertiary = ColorSuccessDark,         // 第三品牌色（常用于强调或差异化）
    onTertiary = TextWhite,              // 在 tertiary 背景上的前景色
    tertiaryContainer = ColorSuccessDark,// 第三色容器背景
    onTertiaryContainer = TextWhite,     // 在 tertiaryContainer 上的前景色

    // —————— 背景与表面（Background & Surface） ——————
    background = BgGreyDark,             // 应用最底层背景色
    onBackground = TextPrimaryDark,      // 在 background 上的文字颜色
    surface = BgWhiteDark,               // 表面组件（Card、Dialog 等）默认背景
    onSurface = TextPrimaryDark,         // 在 surface 上的文字颜色
    surfaceVariant = BgContentDark,      // 表面变体（如菜单、侧边栏背景）
    onSurfaceVariant = TextSecondaryDark,// 在 surfaceVariant 上的文字颜色
    surfaceTint = PrimaryDark,           // 表面色调（如 TopAppBar 的 tint）

    // —————— 反转颜色（Inverse） ——————
    inverseSurface = BgGreyLight,        // 反转表面（深色主题中使用浅色表面）
    inverseOnSurface = TextPrimaryLight, // 在 inverseSurface 上的文字颜色

    // —————— 错误色（Error） ——————
    error = ColorDangerDark,             // 错误状态颜色（如删除操作）
    onError = TextWhite,                 // 在 error 背景上的文字颜色
    errorContainer = BgRedDark,          // 错误容器背景（如错误输入框）
    onErrorContainer = ColorDangerDark,  // 在 errorContainer 上的文字颜色

    // —————— 边框与遮罩（Outline & Scrim） ——————
    outline = BorderDark,                // 描边、分隔线颜色（如 TextField 边框）
    outlineVariant = BorderDark,         // 描边变体（通常更淡，用于次要分隔）
    scrim = MaskDark,                    // 遮罩层颜色（如 BottomSheet 背后覆盖）

    // —————— 表面层级（Surface Levels - M3 新增） ——————
    surfaceBright = BgWhiteDark,         // 最亮表面（高光区域）
    surfaceDim = BgGreyDark,             // 最暗表面（低光区域）
    surfaceContainer = BgWhiteDark,      // 默认容器表面
    surfaceContainerLow = BgContentDark, // 低层级容器
    surfaceContainerLowest = BgGreyDark, // 最低层级容器（贴近 background）
    surfaceContainerHigh = BgWhiteDark,  // 高层级容器
    surfaceContainerHighest = BgWhiteDark,// 最高层级容器（如 Dialog）

    // —————— 固定色（Fixed Colors - M3 新增，确保可读性） ——————
    primaryFixed = PrimaryDark,          // 固定主色（不随动态颜色变化）
    primaryFixedDim = PrimaryDark,       // 固定主色的变暗版本
    onPrimaryFixed = TextWhite,          // 在 primaryFixed 上的文字
    onPrimaryFixedVariant = TextWhite,   // 在 primaryFixedDim 上的文字

    secondaryFixed = ColorPurpleDark,    // 固定次要色
    secondaryFixedDim = ColorPurpleDark,
    onSecondaryFixed = TextWhite,
    onSecondaryFixedVariant = TextWhite,

    tertiaryFixed = ColorSuccessDark,    // 固定第三色
    tertiaryFixedDim = ColorSuccessDark,
    onTertiaryFixed = TextWhite,
    onTertiaryFixedVariant = TextWhite
)

/**
 * 浅色主题配色方案
 * 定义 MaterialTheme 中浅色模式下的各种颜色角色（Color Roles）
 */
private val LightColorScheme = lightColorScheme(
    // —————— 主色（Primary） ——————
    primary = PrimaryLight,
    onPrimary = TextWhite,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = TextWhite,
    inversePrimary = PrimaryDark,

    // —————— 次要色（Secondary） ——————
    secondary = ColorPurple,
    onSecondary = TextWhite,
    secondaryContainer = ColorPurple,
    onSecondaryContainer = TextWhite,

    // —————— 第三色（Tertiary） ——————
    tertiary = ColorSuccess,
    onTertiary = TextWhite,
    tertiaryContainer = ColorSuccess,
    onTertiaryContainer = TextWhite,

    // —————— 背景与表面（Background & Surface） ——————
    background = BgGreyLight,
    onBackground = TextPrimaryLight,
    surface = BgWhiteLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = BgContentLight,
    onSurfaceVariant = TextSecondaryLight,
    surfaceTint = PrimaryLight,

    // —————— 反转颜色（Inverse） ——————
    inverseSurface = BgGreyDark,
    inverseOnSurface = TextPrimaryDark,

    // —————— 错误色（Error） ——————
    error = ColorDanger,
    onError = TextWhite,
    errorContainer = BgRedLight,
    onErrorContainer = ColorDanger,

    // —————— 边框与遮罩（Outline & Scrim） ——————
    outline = BorderLight,
    outlineVariant = BorderLight,
    scrim = MaskLight,

    // —————— 表面层级（Surface Levels） ——————
    surfaceBright = BgWhiteLight,
    surfaceDim = BgGreyLight,
    surfaceContainer = BgWhiteLight,
    surfaceContainerLow = BgContentLight,
    surfaceContainerLowest = BgGreyLight,
    surfaceContainerHigh = BgWhiteLight,
    surfaceContainerHighest = BgWhiteLight,

    // —————— 固定色（Fixed Colors） ——————
    primaryFixed = PrimaryLight,
    primaryFixedDim = PrimaryLight,
    onPrimaryFixed = TextWhite,
    onPrimaryFixedVariant = TextWhite,

    secondaryFixed = ColorPurple,
    secondaryFixedDim = ColorPurple,
    onSecondaryFixed = TextWhite,
    onSecondaryFixedVariant = TextWhite,

    tertiaryFixed = ColorSuccess,
    tertiaryFixedDim = ColorSuccess,
    onTertiaryFixed = TextWhite,
    onTertiaryFixedVariant = TextWhite
)

/**
 * 应用主题 Composable 函数
 * 根据系统设置决定使用深色或浅色主题，并应用所有设计系统元素
 *
 * @param darkTheme 是否使用深色主题，默认跟随系统设置
 * @param dynamicColor 是否使用动态颜色（Android 12+特性），默认关闭
 * @param content 需要应用主题的内容
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 动态配色适用于 Android 12 及以上版本
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}