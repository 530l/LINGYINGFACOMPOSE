package com.lyf.lingyingfacompose.ui.test.counter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CounterScreen(counterViewModel: CounterViewModel = hiltViewModel()) {
    // ViewModel中获取状态
    //ViewModel 能在配置更改（如屏幕旋转）时保持状态不变。
    val count by counterViewModel.count
    // 收集 ViewModel 中的 StateFlow 并转换为 Compose 状态
    // 使用 collectAsState 是生命周期感知的，确保 Flow 只在 Composable 处于活动状态时收集
    val count2 by counterViewModel.count2.collectAsState()
    // 使用 observeAsState 将 LiveData 转换为 Compose 状态
    // LiveData 是生命周期感知的，确保数据只在 Composable 处于活动状态时被观察
    val count3 by counterViewModel.count3.observeAsState()
    //todo ===================================================

    //remember和mutableStateOf在Composable函数中几乎永远都是配套使用的。
    val count4 = remember { mutableIntStateOf(0) }

    //更加普遍的写法是借助Kotlin的委托语法对来State的用法进一步精简
    //这里的变化是，我们使用by关键字替代了之前的等号，用委托的方式来为count变量赋值。
    //by  就相关于具体的值类型了，而不是 State 类型了
    var count5 by remember { mutableIntStateOf(0) }

    //todo count4 count5 变量是定义在Composable函数中的，虽然我们用remember函数将它包裹住了，
    // 但这只能保证它在Composable函数重组的时候数据不会丢失。
    // 而手机横竖屏旋转会导致Activity重新创建，这个时候数据肯定是会丢失的。

    //todo ===================================================
    //rememberSaveable 函数是remember函数的一个增强版，
    //它唯一和remember不同的地方就是在于其包裹的数据在手机横竖屏旋转时会被保留下来。
    var count6 by rememberSaveable { mutableIntStateOf(0) }

    //todo 总结：
    // 1.在 compose 组合函数中，想要ui根基数据状态发送变化，
    // 数据必须包装为MutableState类型 ，compose函数才会识别，进行数据重组。
    // 2. remember函数的作用是让其包裹住的变量在重组的过程中得到保留，从而就不会出现变量被重新初始化的情况了。
    // 如果val count = mutableStateOf(0) 直接这样使用，每次重组都会初始化，不保留历史数据
    // 3. remember 和 by 的去区别，主要是一个类型，by是委托模式，具体值的类型，而remember是状态类型
    // 4. 屏幕旋转要保留数据，可以放到 viewmodel 中 或者使用rememberSaveable 函数。
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Count: $count")
        Text(text = "Count2: $count2")
        Text(text = "Count3: $count3")
        Text(text = "Count4: ${count4.intValue}")
        Text(text = "Count5: $count5")
        Text(text = "Count6: $count6")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            counterViewModel.increment()
            counterViewModel.increment2()
            counterViewModel.increment3()
            count4.intValue++
            count5++
            count6++
        }) {
            Text("Increment")
        }
    }
}

@Preview
@Composable
fun PreviewCounterScreen() {
    CounterScreen()
}
