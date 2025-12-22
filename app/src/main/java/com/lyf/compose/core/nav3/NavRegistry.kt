package com.lyf.compose.core.nav3

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

private const val TAG = "NavRegistry"

/**
 * 一个线程安全的注册表，用于将导航键类映射到可组合构建器。
 * 注册屏幕构建器（通常在应用启动时），
 * 这样`AppNavHost`就不需要一个庞大的when代码块。
 */
object NavRegistry {
    private val map = ConcurrentHashMap<KClass<out NavKey>, @Composable (NavKey) -> Unit>()

    /**
     * 注册 builder。如果已存在相同 keyClass 的注册，记录警告并保留先前注册（不覆盖）。
     */
    fun register(
        keyClass: KClass<out NavKey>,
        builder: @Composable (NavKey) -> Unit
    ) {
        //如果不存在则放入
        val prev = map.putIfAbsent(keyClass, builder)
        if (prev != null) {
            Timber.tag(TAG).w(
                "%s ignoring new registration", "NavRegistry: " +
                        "duplicate registration for ${keyClass.simpleName},"
            )
        }
    }

    fun createEntry(key: NavKey): NavEntry<NavKey> {
        val builder = map[key::class] ?: error("No Nav entry registered for ${key::class}")
        return NavEntry(key) { builder(key) }
    }
}
