/*
 * Copyright lt 2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyf.lingyingfacompose.components.text_field

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lyf.lingyingfacompose.R


/**
 * creator: lt  2022/7/16  lt.dygzs@qq.com
 * effect : 密码输入框右侧的组件,一般用于切换密码是否可见
 *          The component to the right of the password input box
 * warning:
 */
interface PasswordTrailingWithTextField {
    /**
     * 在密码输入框右侧展示
     * The component to the right of the password input box
     */
    @Composable
    fun RowScope.Trailing(passwordIsShow: Boolean, onPasswordIsShowChange: (Boolean) -> Unit)

    companion object {
        /**
         * 默认的眼睛图标,点击后可以切换密码是否可见
         * default eye icon
         */
        val DEFAULT = object : PasswordTrailingWithTextField {
            @Composable
            override fun RowScope.Trailing(
                passwordIsShow: Boolean,
                onPasswordIsShowChange: (Boolean) -> Unit
            ) {
                Image(
                    painter = if (passwordIsShow)
                        painterResource(id = R.drawable.compose_views_password_show) // 显示密码可见状态的图标
                    else
                        painterResource(id = R.drawable.compose_views_password_hide), // 显示密码隐藏状态的图标
                    contentDescription = null, // 如果图片没有描述性内容，建议设为null
                    modifier = Modifier
                        .size(22.dp)
                        .clickable {
                            onPasswordIsShowChange(!passwordIsShow) // 切换密码显示状态
                        }
                )
            }
        }
    }
}