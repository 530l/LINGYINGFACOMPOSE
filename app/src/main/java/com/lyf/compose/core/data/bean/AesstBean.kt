package com.lyf.compose.core.data.bean

import androidx.navigation3.runtime.NavKey

data class AsstBean(
    val id: Int,
    val title: String,
    val subTitle: String,
    val navKey: NavKey
)