package com.lyf.lingyingfacompose.ui.wx.utils

import android.content.Context
import android.widget.Toast

/**
 * @author: njb
 * @date:   2025/8/18 1:55
 * @desc:   描述
 */
object ToastUtil {
    fun show(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}