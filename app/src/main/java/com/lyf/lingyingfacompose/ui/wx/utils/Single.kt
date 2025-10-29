package com.lyf.lingyingfacompose.ui.wx.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

/**
 * @author: njb
 * @date:   2025/8/18 1:48
 * @desc:   描述
 */
@Composable
fun rememberStoragePermissionLauncher(
    onGranted: () -> Unit,
    onDenied: () -> Unit = {}
) = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
    if (granted) onGranted() else onDenied()
}