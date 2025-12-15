package com.lyf.lingyingfacompose.test


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyf.lingyingfacompose.test.components.StyleableTutorialText
import com.lyf.lingyingfacompose.test.components.TutorialHeader
import com.lyf.lingyingfacompose.test.components.TutorialText2

/**
关于[Column]、[Row]、[Box]和[Modifier]的教程。
[Column]以垂直顺序排列其子元素
[Row]以水平顺序排列其子元素
[Box]将其子元素堆叠在一起
[Modifier]用于设置属性，如尺寸、内边距、背景色、
点击动作、内边距等。
注意
修饰符的顺序很重要。根据添加内边距的顺序不同，
会使UI组件(Compose)产生外边距或内边距效果。
 */

//在 Jetpack Compose 中， Column 中的 verticalArrangement  是Alignment.Verticals类型。
// horizontalAlignment  是 Horizontal 类型，这和Row 一样，上中下，左中右。
// 而Box 的是用Alignment类型

@Preview(showBackground = true)
@Composable
fun Tutorial1_1Screen() {
    TutorialContent()
}

@Composable
private fun TutorialContent() {

    LazyColumn(Modifier.fillMaxSize()) {

        item {
//            TutorialHeader(text = "Row")
//            StyleableTutorialText(text = "1-) Row\u200B 是一种布局组件，它将子元素按水平顺序排列。")
//            RowExample()

//            TutorialHeader(text = "Column")
//            StyleableTutorialText(text = "2-) Column\u200B 是一种布局组件，它将子元素按垂直顺序排列。")
//            ColumnExample()

//            StyleableTutorialText(
//                text = "3-) 修饰符的顺序很重要。根据添加内边距的顺序不同，" +
//                        "会使UI组件(Compose)产生外边距或内边距效果。"
//            )
//            ColumnsAndRowPaddingsExample()


//            StyleableTutorialText(text = "4-) 阴影可以应用于Column或Row。")
//            ShadowExample()

//            TutorialHeader(text = "Box")
//            StyleableTutorialText(text = "5-) Box\u200B 是一种布局组件，它将子元素堆叠在一起。最后声明的元素在顶部。")
//            BoxExample()


//            StyleableTutorialText(text = "6-) Box\u200B 中的元素可以使用不同的对齐方式。")
//            BoxShadowAndAlignmentExample()
//            TutorialHeader(text = "Spacer")

            StyleableTutorialText(text = "7-) Spacer\u200B 可以用于将元素对齐到屏幕的末尾或底部。")
            WeightExample()
            TutorialHeader(text = "Weight and Spacer")

            StyleableTutorialText(
                text = "8-) 权重根据总重量决定每个子组件应占据父组件尺寸的比例。" +
                        "间隔器用于在组件之间创建水平或垂直间距。"
            )
            WeightAndSpacerExample()
        }
    }
}

@Composable
fun RowExample() {

    TutorialText2(text = "Arrangement.Start")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.Green),
        //Row 水平方向对齐方式（左中右）
        horizontalArrangement = Arrangement.Start,
        // Alignment.Center
        //Argument type mismatch: actual type is 'Alignment', but 'Alignment.Vertical' was expected.
        //参数类型不匹配：实际类型为'Alignment'，但预期类型为'Alignment.Vertical'。
        //todo 当输入参数的时候，编辑器会提示是那个类型。
        // Alignment.Top
        // @Stable val Top: Vertical = BiasAlignment.Vertical(-1f)
        //Row 垂直方向对齐方式（上中下）
        verticalAlignment = Alignment.Top
    ) {
        RowTexts()
        Button(onClick = { }, modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(text = "Click me1")
        }
        Button(onClick = { }, modifier = Modifier.align(Alignment.Bottom)) {
            Text(text = "Click me2")
        }
    }

    TutorialText2(text = "Arrangement.End")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        RowTexts()
    }

    TutorialText2(text = "Arrangement.Center")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        RowTexts()
    }

    TutorialText2(text = "Arrangement.SpaceEvenly")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        RowTexts()
    }

    TutorialText2(text = "Arrangement.SpaceAround")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        RowTexts()
    }

    TutorialText2(text = "Arrangement.SpaceBetween")

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        RowTexts()
    }
}

@Composable
fun ColumnExample() {
    val modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(200.dp)
        .background(Color.LightGray)

    TutorialText2(text = "Arrangement.Top")
    // 垂直方向对齐方式（上中下） 前提下都是需要预期类型匹配成功
    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {
        ColumnTexts()
        // 垂直方向对齐方式（左中右） 前提下都是需要预期类型匹配成功
        Text("哈哈哈", modifier = Modifier.align(Alignment.End))
        Text("哈哈哈", modifier = Modifier.align(Alignment.CenterHorizontally))
    }

    TutorialText2(text = "Arrangement.Bottom")
    Column(modifier = modifier, verticalArrangement = Arrangement.Bottom) {
        ColumnTexts()
    }

    TutorialText2(text = "Arrangement.Center")
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        ColumnTexts()
    }

    TutorialText2(text = "Arrangement.SpaceEvenly")
    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceEvenly) {
        ColumnTexts()
    }

    TutorialText2(text = "Arrangement.SpaceAround")
    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceAround) {
        ColumnTexts()
    }

    TutorialText2(text = "Arrangement.SpaceBetween")
    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceBetween) {
        ColumnTexts()
    }
}


@Composable
fun RowTexts() {
    Text(
        text = "Row1", modifier = Modifier
            .background(Color(0xFFFF9800))
            .padding(4.dp)
    )
    Text(
        text = "Row2", modifier = Modifier
            .background(Color(0xFFFFA726))
            .padding(4.dp)
    )
    Text(
        text = "Row3", modifier = Modifier
            .background(Color(0xFFFFB74D))
            .padding(4.dp)
    )
}

@Composable
fun ColumnTexts() {
    Text(
        text = "Column1", modifier = Modifier
            .background(Color(0xFF8BC34A))
            .padding(4.dp)
    )
    Text(
        text = "Column2", modifier = Modifier
            .background(Color(0xFF9CCC65))
            .padding(4.dp)
    )
    Text(
        text = "Column3", modifier = Modifier
            .background(Color(0xFFAED581))
            .padding(4.dp)
    )
}



@Composable
fun ColumnsAndRowPaddingsExample() {

    Row(
        modifier = Modifier
            .background(Color(0xFFF06292))
            .fillMaxWidth()
            .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Column(
            modifier = Modifier
                .background(Color(0xFFFFEB3B))
                .padding(15.dp)
                .background(Color(0xFFFFFFFF))
                .padding(18.dp)
        ) {
            Text(text = "Text A1")
            Text(text = "Text A2")
            Text(text = "Text A3")
        }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .background(Color(0xFF80DEEA))
                .padding(end = 15.dp)
                .background(Color(0xFF9575CD))
                .padding(top = 12.dp, bottom = 22.dp)
        ) {
            Text(text = "Text B1")
            Text(text = "Text B2")
            Text(text = "Text B3")
        }

        Column(
            modifier = Modifier
                .background(Color(0xFF607D8B))
                .padding(15.dp)
                .background(Color(0xFFB2FF59))
        ) {
            Text(text = "Text C1")
            Text(text = "Text C2")
            Text(text = "Text C3")
        }
    }
}

@Composable
fun ShadowExample() {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        RowTexts()
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        ColumnTexts()
    }
}

@Composable
fun BoxExample() {

    val modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)
        .background(Color.LightGray)

    Box(
        modifier = modifier

    ) {

        // 这是最下面的那个
        Text(
            text = "First",
            modifier = Modifier
                .background(Color(0xFF1976D2))
                .size(200.dp),
            color = Color.White,
        )

        // 这是中间的那个
        Text(
            text = "Second",
            modifier = Modifier
                .background(Color(0xFF2196F3))
                .size(150.dp),
            color = Color.White
        )

        // 这是最上面的那个
        Text(
            text = "Third ",
            modifier = Modifier
                .background(Color(0xFF64B5F6))
                .size(100.dp).align(Alignment.Center),
            color = Color.White
        )
    }
}

@Composable
fun BoxShadowAndAlignmentExample() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color.LightGray)
            .padding(8.dp)
    ) {

        Box(
            modifier = Modifier
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            // This is the one at the bottom
            Text(
                text = "First",
                modifier = Modifier
                    .background(Color(0xFFFFA000))
                    .size(200.dp),
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(8.dp)
                )
                .align(Alignment.TopEnd)

        ) {
            // This is the one in the middle
            Text(
                text = "Second",
                modifier = Modifier
                    .background(Color(0xFFFFC107))
                    .size(150.dp),
                color = Color.White
            )
        }


        val modifier = Modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .align(Alignment.BottomStart)

        Box(
            modifier = modifier

        ) {
            // This is the one on top
            Text(
                text = "Third ",
                modifier = Modifier
                    .background(Color(0xFFFFD54F))
                    .size(100.dp),
                color = Color.White
            )
        }
    }
}

@Composable
fun WeightExample() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Row {
            Text(
                text = "Row1", modifier = Modifier
                    .background(Color(0xFFFF9800))
                    .padding(4.dp)
            )

            // 🔥 This spacer fills space between Row1 and space other than Row2, and Row3
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Row2", modifier = Modifier
                    .background(Color(0xFFFFA726))
                    .padding(4.dp)
            )
            Text(
                text = "Row3", modifier = Modifier
                    .background(Color(0xFFFFB74D))
                    .padding(4.dp)
            )
        }

        Column(modifier = Modifier.height(200.dp)) {
            Text(
                text = "Column1", modifier = Modifier
                    .background(Color(0xFF8BC34A))
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Column2", modifier = Modifier
                    .background(Color(0xFF9CCC65))
                    .padding(4.dp)
            )
            Text(
                text = "Column3", modifier = Modifier
                    .background(Color(0xFFAED581))
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun WeightAndSpacerExample() {

    // This is parent modifier
    val modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(Color.LightGray)

    val rowModifier = Modifier
        .fillMaxHeight()
        .background(Color(0xFFA1887F))
        .padding(4.dp)

    Row(modifier = modifier) {

        Text(
            fontSize = 12.sp,
            text = "Weight 2",
            modifier = rowModifier.weight(2f)
        )

        // Spacer creates a space with given modifier width or height based on which scope(row/column) it exists
        Spacer(modifier = modifier.weight(1f))

        Text(
            fontSize = 12.sp,
            text = "Weight 3",
            modifier = rowModifier.weight(3f)
        )

        Spacer(modifier = modifier.weight(1f))

        Text(
            fontSize = 12.sp,
            text = "Weight 4",
            modifier = rowModifier.weight(4f)
        )
    }

    // This spacer is for column which behaves as padding below this component
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(showBackground = true)
@Preview("dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(device = Devices.PIXEL_C, showBackground = true)
@Composable
private fun Tutorial1_1Preview() {
    TutorialContent()
}