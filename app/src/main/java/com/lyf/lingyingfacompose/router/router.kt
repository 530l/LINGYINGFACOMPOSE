package com.lyf.lingyingfacompose.router

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
data object SplashRouter : NavKey

@Serializable
data object LoginRouter : NavKey

@Serializable
data object HomeScreenRouter: NavKey

@Serializable
data object DesignScreenRouter: NavKey