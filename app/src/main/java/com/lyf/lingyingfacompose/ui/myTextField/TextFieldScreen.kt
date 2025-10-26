package com.lyf.lingyingfacompose.ui.myTextField

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.lyf.lingyingfacompose.components.text_field.GoodTextField
import com.lyf.lingyingfacompose.components.text_field.HintComposeWithTextField
import com.lyf.lingyingfacompose.components.text_field.PasswordTextField
import com.lyf.lingyingfacompose.components.text_field.VerticalSpace

@SuppressLint("RememberInComposition")
@Composable
fun TextFieldScreen() {
    Column(
        Modifier
            .padding(20.dp)
            .width(200.dp)
    ) {
        //进入页面的时候自动弹出键盘
        val keyboardController = LocalSoftwareKeyboardController.current//软键盘控制器
        val focusRequester = FocusRequester()//焦点控制器
        LaunchedEffect(key1 = Unit, block = {
            focusRequester.requestFocus()//使其获取焦点
            keyboardController?.show()//弹出软键盘
        })

        var text1 by remember { mutableStateOf("我是账号") }
        var text2 by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("password") }
        var passwordIsShow by remember { mutableStateOf(false) }
        var password2 by remember { mutableStateOf("123456") }
        var passwordIsShow2 by remember { mutableStateOf(true) }

        GoodTextField(
            value = text1, onValueChange = { text1 = it }, modifier = Modifier.height(40.dp)
        )
        VerticalSpace(dp = 16)
        GoodTextField(
            value = text2, onValueChange = { text2 = it }, hint = remember {
                HintComposeWithTextField.createTextHintCompose("请输入账号")
            }, modifier = Modifier.height(40.dp)
        )
        VerticalSpace(dp = 16)
        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            passwordIsShow = passwordIsShow,
            onPasswordIsShowChange = { passwordIsShow = it },
            modifier = Modifier.height(40.dp)
        )
        VerticalSpace(dp = 16)
        PasswordTextField(
            value = password2,
            onValueChange = { password2 = it },
            passwordIsShow = passwordIsShow2,
            onPasswordIsShowChange = { passwordIsShow2 = it },
            modifier = Modifier.height(40.dp)
        )
        VerticalSpace(dp = 16)
        Divider()
        VerticalSpace(dp = 16)
        Text("TextFieldValue")
        VerticalSpace(dp = 16)
        var fieldText by remember {
            mutableStateOf(
                TextFieldValue("我是账号", TextRange(4))
            )
        }
        var fieldPassword by remember {
            mutableStateOf(
                TextFieldValue("password", TextRange(6))
            )
        }
        var fieldPasswordIsShow by remember { mutableStateOf(true) }
        GoodTextField(
            value = fieldText, onValueChange = { fieldText = it }, hint = remember {
                HintComposeWithTextField.createTextHintCompose("请输入账号")
            }, modifier = Modifier
                .height(40.dp)
                .focusRequester(focusRequester)
        )
        VerticalSpace(dp = 16)
        PasswordTextField(
            value = fieldPassword,
            onValueChange = { fieldPassword = it },
            passwordIsShow = fieldPasswordIsShow,
            onPasswordIsShowChange = { fieldPasswordIsShow = it },
            modifier = Modifier.height(40.dp)
        )
    }
}