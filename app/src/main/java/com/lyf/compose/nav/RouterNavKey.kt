package com.lyf.compose.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
data object SplashRouter : NavKey

@Serializable
data object HomeScreenRouter : NavKey

@Serializable
data object LoginRouter : NavKey

@Serializable
data object SideEffectRouter : NavKey

@Serializable
data object LaunchedEffectRouter : NavKey

@Serializable
data object DisposableEffectRouter : NavKey

@Serializable
data object RememberUpdatedStateRouter : NavKey

@Serializable
data object ProduceStateRouter : NavKey

@Serializable
data object RememberCoroutineScopeRouter : NavKey

@Serializable
data object SnapshotFlowRouter : NavKey


@Serializable
data object RefreshRouter : NavKey


@Serializable
data object AnimatedVisibilityRouter : NavKey


@Serializable
data object AnimateContentSizeRouter : NavKey


@Serializable
data object AnimatedContentRouter : NavKey

@Serializable
data object AnimateAsStateRouter : NavKey


@Serializable
data object AnimatableRouter : NavKey

fun NavKey.requiresLogin(): Boolean {
    return this is HomeScreenRouter
}
