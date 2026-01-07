package com.lyf.compose.test

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable


///////////////////////////////////////////////////////////////////////////
// 定义用于识别内容的键
@Serializable
data object ProductList1 : NavKey
@Serializable
data class ProductDetail1(val id: String) : NavKey

@Deprecated("学习基本操作。。。。。。。。。。。。。。。")
@Composable
fun StudyNav3DemoApp() {
    //todo 创建一个返回堆栈，指定应用程序应开始的键

    //todo 不推荐使用这种
//    val backStack = remember { mutableStateListOf<Any>(ProductList) }

    //todo 推荐使用
    //rememberNavBackStack 可组合函数旨在创建一个在配置更改和进程终止后仍能保留的返回栈。
    //为了让 rememberNavBackStack 正常运行，返回堆栈中的每个键都必须符合特定要求：
    //1.实现 NavKey 接口：返回堆栈中的每个键都必须实现 NavKey 接口。此接口充当标记接口，向库发出可以保存密钥的信号。
    //2.具有 @Serializable 注释：除了实现 NavKey 之外，您的密钥类和对象还必须使用 @Serializable 注释进行标记。
    //todo 代替方案：替代方案：存储在 ViewModel 中
    val backStack = rememberNavBackStack(ProductList1)

    //todo Navigation 3 中：返回堆栈保存 Key，
    // Entry Provider 将 Key 映射为包含 UI 与元数据的 NavEntry，
    // NavDisplay 根据这些信息决定如何展示页面。

    val entryProvider = entryProvider {
        entry<ProductList1> { key ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
                    .clickable {
                        backStack.add(ProductDetail1(id = "ABC"))
                    }
            ) {

            }
        }
        entry<ProductDetail1> { key ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .clickable {
                        backStack.add(ProductDetail1(id = "ABC"))
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Product ${key.id} ")
            }
        }
    }

    NavDisplay(
        // Navigation 3 通过 NavEntryDecorator 将 ViewModel 的生命周期精确绑定到返回堆栈中的 NavEntry，
        // 实现页面级状态自动创建与自动销毁。如果不加，viewmodel的生命周期不跟随返回栈
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack, /*当用户触发返回事件时，系统会调用此方法。*/
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider
    )

}
