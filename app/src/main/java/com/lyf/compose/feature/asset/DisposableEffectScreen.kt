package com.lyf.compose.feature.asset

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import timber.log.Timber

@Composable
fun DisposableEffectScreen() {
    val context = LocalContext.current
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    //当DisposableEffectScreen 入栈的时候 会执行网络监听，
    // 当DisposableEffectScreen 出栈的时候 会执行网络监听的注销
    //21:48:22.844  D  NetworkState 网络已连接
    //21:48:35.357  D  NetworkState注销网络监听器
    DisposableEffect(Unit) {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Timber.d("NetworkState 网络已连接")
            }

            override fun onLost(network: Network) {
                Timber.d("NetworkState网络已断开")
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        onDispose {
            Timber.d("NetworkState注销网络监听器")
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(60.dp))
        Text("通过onDisposable注销监听器，防止内存泄漏")
        Text(
            """
        DisposableEffect 作为另一个与生命周期相关的 API，
        它和LaunchedEffect类似，
        而LaunchedEffect大家可以理解为协程版的DisposableEffect。
        DispoableEffect主要用于在组件的生命周期执行一些需要清理的操作。
        比如说，注册监听器、打开文件、连接数据库等操作，需要在组件销毁时关闭或者释放资源，
        以防止内存泄漏。原理也很好理解，当组件首次进入界面时，
        DisposableEffect会执行一些操作（比如打开摄像头或监听事件)；当组件离开界面（销毁时），
        DisposableEffect 会自动执行清理代码（如关闭摄像头、注销监听器等）
    """.trimIndent()
        )
    }
}

