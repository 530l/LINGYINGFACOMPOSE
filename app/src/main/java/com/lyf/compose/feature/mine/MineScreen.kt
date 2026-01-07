package com.lyf.compose.feature.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lyf.compose.core.data.session.SessionManager
import kotlinx.coroutines.delay

@Composable
fun MineScreen(onLogout: () -> Unit) {
    // 弹窗是否显示（普通 remember 足够；是否跨配置变化保存可后续再加）
    var showLogoutDialog by remember { mutableStateOf(false) }

    // 标记用户已确认退出（用于在对话框已从Compose中移除后再执行退出）
    var logoutConfirmed by remember { mutableStateOf(false) }

    // 使用内联遮罩层而不是平台 AlertDialog，确保一旦 showLogoutDialog=false 对话框即被移除
    if (showLogoutDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "退出登录", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "确定要退出当前账号吗？")
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.Center) {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("取消")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(onClick = {
                            // 标记确认并立即关闭对话框
                            logoutConfirmed = true
                            showLogoutDialog = false
                        }) {
                            Text("确定")
                        }
                    }
                }
            }
        }
    }

    // 监听确认标记：当用户确认退出（logoutConfirmed=true）时，等待几帧让Compose移除对话框，再执行退出
    LaunchedEffect(logoutConfirmed, showLogoutDialog) {
        if (logoutConfirmed && !showLogoutDialog) {
            // 等待 2 个帧，确保 UI 已更新并且 overlay 已移除
            withFrameNanos { /* first frame */ }
            withFrameNanos { /* second frame */ }
            // 额外短延迟，稳妥等待动画/重组
            delay(300)
            SessionManager.clearToken()
            //Toaster.show("已退出登录")
            // 重置标记，避免重复
            logoutConfirmed = false
            onLogout()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "MineScreen",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { showLogoutDialog = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "退出登录")
        }
    }
}