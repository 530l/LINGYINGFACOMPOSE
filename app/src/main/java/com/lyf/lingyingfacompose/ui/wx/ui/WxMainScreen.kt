package com.lyf.lingyingfacompose.ui.wx.ui

import android.Manifest
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.ImageLoader
import com.lyf.lingyingfacompose.ui.wx.PlayVideoActivity
import com.lyf.lingyingfacompose.ui.wx.ui.model.CircleBean
import com.lyf.lingyingfacompose.ui.wx.ui.model.CommentListBean
import com.lyf.lingyingfacompose.ui.wx.ui.model.LikeListBean
import com.lyf.lingyingfacompose.ui.wx.ui.model.WeatherEvent
import com.lyf.lingyingfacompose.ui.wx.utils.Constants
import com.lyf.lingyingfacompose.ui.wx.utils.ImageSaveUtil
import com.lyf.lingyingfacompose.ui.wx.utils.RxBus
import com.lyf.lingyingfacompose.ui.wx.utils.ToastUtil
import com.lyf.lingyingfacompose.ui.wx.utils.rememberStoragePermissionLauncher
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

/**
 * @author: njb
 * @date:   2025/8/18 1:49
 * @desc:   描述
 */
@Composable
fun WxMainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyListState = rememberLazyListState()
    val compositeDisposable = remember { CompositeDisposable() }

    // 全局状态管理
    var circleData by remember { mutableStateOf<List<CircleBean.DataBean>>(emptyList()) }
    var isCommentVisible by remember { mutableStateOf(false) }
    var commentContent by remember { mutableStateOf("") }
    var currentReplyPos by remember { mutableStateOf(-1) }
    var currentToUserId by remember { mutableStateOf("") }
    var currentToUserName by remember { mutableStateOf("") }
    var isImagePreviewVisible by remember { mutableStateOf(false) }
    var previewImages by remember { mutableStateOf(emptyList<String>()) }
    var previewIndex by remember { mutableStateOf(0) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deletePos by remember { mutableStateOf(-1) }
    var showActionDialog by remember { mutableStateOf(false) } // 操作弹框状态
    var currentActionPos by remember { mutableStateOf(-1) }    // 当前操作的动态位置

    // 存储权限 launcher
    val storagePermissionLauncher = rememberStoragePermissionLauncher(
        onGranted = {
            scope.launch {
                // 修复：避免预览列表为空时崩溃
                val targetImg = previewImages.getOrNull(previewIndex) ?: return@launch
                val success = ImageSaveUtil.saveImageToGallery(
                    context, targetImg, ImageLoader(context)
                )
                ToastUtil.show(context, if (success) "图片保存成功" else "图片保存失败")
            }
        },
        onDenied = { ToastUtil.show(context, "缺少存储权限，无法保存图片") }
    )

    // 1. 初始化数据
    LaunchedEffect(Unit) {
        circleData = loadMockCircleData()
    }

    // 2. 修复 RxBus 重复订阅问题（原代码重复订阅+提前取消，导致事件接收失败）
    LaunchedEffect(Unit) {
        val disposable = RxBus.toObservable<WeatherEvent>()
            .subscribe(
                { event ->
                    println("Weather: ${event.cityName} ${event.temperature}℃")
                },
                { error -> error.printStackTrace() }
            )
        compositeDisposable.add(disposable) // 仅添加，不提前取消
    }

    // 3. 布局结构：列表 + 操作弹框 + 评论栏 + 图片预览
    Box(modifier = Modifier.fillMaxSize()) {
        // 朋友圈列表
        LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
            item { CircleHeader() }
            itemsIndexed(circleData, key = { _, item -> item.id ?: "default_key" }) { index, item ->
                CircleItem(
                    data = item,
                    // 关键：确保“操作按钮”（如右上角三个点）绑定此回调
                    onActionClick = {
                        currentActionPos = index // 记录当前操作的动态位置
                        showActionDialog = true  // 打开弹框
                    },
                    onReplyClick = { comment ->
                        currentReplyPos = index
                        currentToUserId = comment.user_id ?: ""
                        currentToUserName = comment.user_name ?: ""
                        isCommentVisible = true
                        commentContent = ""
                        keyboardController?.show()
                        scope.launch {
                            lazyListState.scrollToItem(index + 1)
                        }
                    },
                    onDeleteClick = {
                        deletePos = index
                        showDeleteDialog = true
                    },
                    onVideoClick = { videoUrl ->
                        val intent = Intent(context, PlayVideoActivity::class.java)
                        intent.putExtra("url", videoUrl)
                        startActivity(context, intent, null)
                    },
                    onImageClick = { images, imgIndex ->
                        previewImages = images
                        previewIndex = imgIndex
                        isImagePreviewVisible = true
                    },
                    onCommentLongClick = { comment ->
                        ToastUtil.show(context, "已复制：${comment.content ?: ""}")
                    }
                )
            }
        }

        // 4. 新增：点赞/评论操作弹框（底部弹出，点击操作按钮显示）
        if (showActionDialog && currentActionPos != -1) {
            // 弹框背景（点击空白关闭）
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { showActionDialog = false }
            )
            // 弹框内容（底部显示，圆角设计）
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                // 获取当前操作的动态数据（安全判断）
                val currentItem = circleData.getOrNull(currentActionPos) ?: run {
                    showActionDialog = false
                    return@Column
                }

                // 点赞按钮
                Text(
                    text = if (currentItem.is_like == 1) "取消点赞" else "点赞",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            // 执行点赞/取消点赞逻辑
                            val updatedLikeList =
                                currentItem.like_list?.toMutableList() ?: mutableListOf()
                            if (currentItem.is_like == 1) {
                                // 取消点赞：移除当前用户
                                updatedLikeList.removeAll { it.user_id == "current_user_id" }
                            } else {
                                // 点赞：添加当前用户
                                updatedLikeList.add(
                                    LikeListBean(
                                        user_id = "current_user_id",
                                        user_name = "当前用户",
                                        circle_id = currentItem.id ?: ""
                                    )
                                )
                            }
                            // 更新列表数据
                            circleData = circleData.toMutableList().apply {
                                this[currentActionPos] = currentItem.copy(
                                    is_like = if (currentItem.is_like == 1) 0 else 1,
                                    like_list = updatedLikeList
                                )
                            }
                            ToastUtil.show(
                                context,
                                if (currentItem.is_like == 1) "取消点赞成功" else "点赞成功"
                            )
                            showActionDialog = false // 关闭弹框
                        }
                )

                // 分割线
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray.copy(alpha = 0.5f))
                )

                // 评论按钮
                Text(
                    text = "评论",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            // 触发评论输入栏
                            currentReplyPos = currentActionPos
                            currentToUserId = ""
                            currentToUserName = ""
                            isCommentVisible = true
                            commentContent = ""
                            keyboardController?.show()
                            // 滚动到当前动态，避免遮挡
                            scope.launch {
                                lazyListState.scrollToItem(currentActionPos + 1)
                            }
                            showActionDialog = false // 关闭弹框
                        }
                )

                // 分割线
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray.copy(alpha = 0.5f))
                )

                // 取消按钮
                Text(
                    text = "取消",
                    fontSize = 18.sp,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable {
                            showActionDialog = false
                        }
                )
            }
        }

        // 5. 底部评论输入栏
        if (isCommentVisible && currentReplyPos != -1) { // 修复：增加currentReplyPos判断，避免空指针
            CommentInputBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp),
                hint = if (currentToUserId.isBlank()) "说点什么..." else "回复 $currentToUserName...",
                content = commentContent,
                onContentChange = { commentContent = it },
                onSendClick = {
                    if (commentContent.isBlank()) {
                        ToastUtil.show(context, "请输入评论内容")
                        return@CommentInputBar
                    }
                    val newComment = CommentListBean(
                        id = System.currentTimeMillis().toString(),
                        user_id = "current_user_id",
                        user_name = "当前用户",
                        to_user_id = currentToUserId,
                        to_user_name = currentToUserName,
                        circle_id = circleData[currentReplyPos].id ?: "",
                        content = commentContent,
                        type = "0",
                        createon = System.currentTimeMillis().toString(),
                        replyUser = CommentListBean.CommentsUser(
                            userId = currentToUserId.takeIf { it.isNotEmpty() } ?: "0",
                            userName = currentToUserName.takeIf { it.isNotEmpty() } ?: ""
                        ),
                        commentsUser = CommentListBean.CommentsUser(
                            userId = "current_user_id",
                            userName = "当前用户"
                        ),
                        is_del = "0",
                        deleteon = ""
                    )
                    // 更新评论列表（修复：确保集合可变）
                    circleData = circleData.toMutableList().apply {
                        val targetItem = this[currentReplyPos]
                        val newComments =
                            targetItem.comments_list?.toMutableList() ?: mutableListOf()
                        newComments.add(newComment)
                        this[currentReplyPos] = targetItem.copy(comments_list = newComments)
                    }
                    // 重置状态
                    isCommentVisible = false
                    commentContent = ""
                    keyboardController?.hide()
                    ToastUtil.show(
                        context,
                        if (currentToUserId.isBlank()) "评论成功" else "回复成功"
                    )
                },
                onDismiss = {
                    isCommentVisible = false
                    keyboardController?.hide()
                }
            )
        }

        // 6. 图片预览弹窗
        if (isImagePreviewVisible && previewImages.isNotEmpty()) { // 修复：判断预览列表非空
            ImagePreview(
                images = previewImages,
                currentIndex = previewIndex,
                onDismiss = { isImagePreviewVisible = false },
                onLongClick = {
                    storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            )
        }

        // 7. 删除动态对话框
        if (showDeleteDialog && deletePos != -1) {
            AlertDialog(
                title = { Text("提示") },
                text = { Text("你确定要删除这条动态吗？") },
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    Button(onClick = {
                        circleData = circleData.toMutableList().apply { removeAt(deletePos) }
                        showDeleteDialog = false
                        deletePos = -1
                        ToastUtil.show(context, "删除成功")
                    }) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }
    }

    // 8. 清理资源（页面销毁时取消RxBus订阅）
    DisposableEffect(Unit) {
        onDispose {
            compositeDisposable.dispose()
        }
    }
}

// 模拟加载朋友圈数据（替代原AssetsUtil.getStates）
private fun loadMockCircleData(): List<CircleBean.DataBean> {
    // 统一空集合常量，避免重复创建且明确泛型
    val emptyStringList: MutableList<String> = mutableListOf<String>()
    val emptyCommentList: MutableList<CommentListBean> = mutableListOf<CommentListBean>()
    val emptyLikeList: MutableList<LikeListBean> = mutableListOf<LikeListBean>()

    return listOf(
        // 第1条：网页动态（修复：补充video参数、明确空集合泛型）
        CircleBean.DataBean(
            id = "190383",
            user_id = "12",
            type = Constants.TYPE_WEB, // "4"（网页动态）
            content = "热修复测试",
            files = emptyStringList,
            share_title = "茫茫的长白大山瀚的草原，浩始森林，大山脚下，原始森林环抱中散落着几十户人家的，一个小山村，茅草房，对面炕，烟筒立在屋后边。在村东头有一个独立的房子，那就是青年点，窗前有一道小溪流过。学子在这里吃饭，由这里出发每天随社员去地里干活。干的活要么上山伐，树，抬树，要么砍柳树毛子开荒种地。在山里，可听那吆呵声：“顺山倒了！”放树谨防回头棒！，树上的枯枝打到别的树上再蹦回来，这回头棒打人最厉害。",
            share_image = "http://imgsrc.baidu.com/forum/w=580/sign=852e30338435e5dd902ca5d746c7a7f5/bd3eb13533fa828b6cf24d82fc1f4134960a5afa.jpg",
            share_url = "",
            longitude = "",
            latitude = "",
            position = "",
            is_del = "0",
            deleteon = "0",
            createon = "3天前",
            user_name = "机器猫",
            head_img = "http://b167.photo.store.qq.com/psb?/V14EhGon2OmAUI/hQN450lNoDPF.dO82PVKEdFw0Qj5qyGeyN9fByKgWd0!/m/dJWKmWNZEwAAnull",
            comments_list = emptyCommentList,
            like_list = emptyLikeList,
            is_like = 0,
            video = "" // 补充非视频动态的video参数（空字符串）
        ),

        // 第2条：文本动态（修复：补充video参数、明确空集合泛型）
        CircleBean.DataBean(
            id = "190382", // 修正id（原190383与第1条重复）
            user_id = "12",
            type = Constants.TYPE_TEXT, // "1"（文本动态）
            content = "茫茫的长白大山瀚的草原，浩始森林，大山脚下，原始森林环抱中散落着几十户人家的，一个小山村，茅草房，对面炕，烟筒立在屋后边。在村东头有一个独立的房子，那就是青年点，窗前有一道小溪流过。学子在这里吃饭，由这里出发每天随社员去地里干活。干的活要么上山伐，树，抬树，要么砍柳树毛子开荒种地。在山里，可听那吆呵声：“顺山倒了！”放树谨防回头棒！，树上的枯枝打到别的树上再蹦回来，这回头棒打人最厉害。",
            files = emptyStringList,
            share_title = "事件分发的过程",
            share_image = "http://imgsrc.baidu.com/forum/w=580/sign=852e30338435e5dd902ca5d746c7a7f5/bd3eb13533fa828b6cf24d82fc1f4134960a5afa.jpg",
            share_url = "",
            longitude = "",
            latitude = "",
            position = "",
            is_del = "0",
            deleteon = "0",
            createon = "3天前",
            user_name = "机器猫",
            head_img = "http://b167.photo.store.qq.com/psb?/V14EhGon2OmAUI/hQN450lNoDPF.dO82PVKEdFw0Qj5qyGeyN9fByKgWd0!/m/dJWKmWNZEwAAnull",
            comments_list = emptyCommentList,
            like_list = emptyLikeList,
            is_like = 0,
            video = "" // 补充video参数
        ),

        // 第3条：图片动态（修复：移除强转、明确files泛型）
        CircleBean.DataBean(
            id = "190381",
            user_id = "3372840",
            type = Constants.TYPE_IMAGE, // "2"（图片动态）
            content = "",
            // 直接使用List<String>，无需强转MutableList
            files = listOf(
                "http://gips1.baidu.com/it/u=1410005327,4082018016&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280",
                "https://gips3.baidu.com/it/u=3732338995,3528391142&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f600_800",
                "http://pic.3h3.com/up/2014-3/20143314140858312456.gif",
                "http://gips2.baidu.com/it/u=1506371362,2755593126&fm=3028&app=3028&f=JPEG&fmt=auto?w=1440&h=2560",
                "http://gips1.baidu.com/it/u=2778784063,1731001818&fm=3028&app=3028&f=JPEG&fmt=auto?w=1920&h=2560",
                "http://gips2.baidu.com/it/u=1192674964,3939660937&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960",
                "http://gips1.baidu.com/it/u=2178851102,884542160&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280",
                "http://gips2.baidu.com/it/u=1757244148,2197425658&fm=3028&app=3028&f=JPEG&fmt=auto?w=1440&h=2560",
                "https://gips1.baidu.com/it/u=926030969,4240391978&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f576_1024"
            ) as MutableList<String>,
            share_title = "",
            share_image = "",
            share_url = "",
            longitude = "",
            latitude = "",
            position = "",
            is_del = "0",
            deleteon = "0",
            createon = "4天前",
            user_name = "一笑的小馆子",
            head_img = "http://b167.photo.store.qq.com/psb?/V14EhGon2OmAUI/hQN450lNoDPF.dO82PVKEdFw0Qj5qyGeyN9fByKgWd0!/m/dJWKmWNZEwAAnull",
            comments_list = emptyCommentList,
            like_list = emptyLikeList,
            is_like = 0,
            video = "" // 补充video参数
        ),

        // 第4条：网页动态（修复：补充video参数、明确空集合泛型）
        CircleBean.DataBean(
            id = "190380",
            user_id = "3372793",
            type = Constants.TYPE_WEB, // "4"（网页动态）
            content = "",
            files = emptyStringList,
            share_title = "《王者荣耀》真的很好玩,快来一起玩吧!现实里我独自面对生活的风雨，但只要进入峡谷，就有队友为我挡箭。今天天气好好，阳光懒懒地洒在石板路上，满大街的情侣和独自一人的我，让我黯然神伤。但一想到还差1颗星上王者，瞬间满血复活！来不来？",
            share_image = "http://imgsrc.baidu.com/forum/w=580/sign=852e30338435e5dd902ca5d746c7a7f5/bd3eb13533fa828b6cf24d82fc1f4134960a5afa.jpg",
            share_url = "", // 补充原缺失的share_url参数（按构造函数顺序）
            longitude = "",
            latitude = "",
            position = "",
            is_del = "0",
            deleteon = "0",
            createon = "4天前",
            user_name = "天宫一号",
            head_img = "http://b167.photo.store.qq.com/psb?/V14EhGon2OmAUI/hQN450lNoDPF.dO82PVKEdFw0Qj5qyGeyN9fByKgWd0!/m/dJWKmWNZEwAAnull",
            comments_list = listOf(
                CommentListBean(
                    id = "15563",
                    circle_id = "190380",
                    user_id = "3191031",
                    to_user_id = "0",
                    type = "0",
                    content = "巴结",
                    is_del = "0",
                    deleteon = "0",
                    createon = "1577085821",
                    to_user_name = "",
                    user_name = "一笑的小管子",
                    // 被回复者信息：对应to_user_id和to_user_name
                    replyUser = CommentListBean.CommentsUser(
                        userId = "0",
                        userName = "",
                    ),
                    // 评论者信息：对应当前评论的user_id和user_name
                    commentsUser = CommentListBean.CommentsUser(
                        userId = "0",
                        userName = ""
                    )
                )
            ) as MutableList<CommentListBean>,
            like_list = listOf(
                LikeListBean(
                    user_id = "3191031",
                    circle_id = "190380",
                    user_name = "一笑的小管子"
                )
            ) as MutableList<LikeListBean>,
            is_like = 1,
            video = "" // 补充video参数
        ),

        // 第5条：视频动态（正常，保留原代码）
        CircleBean.DataBean(
            id = "190379",
            user_id = "3372793",
            type = Constants.TYPE_VIDEO, // "3"（视频动态）
            content = "哈哈",
            files = listOf(
                "http://img.my.csdn.net/uploads/201701/17/1484647897_1978.jpg"
            ) as MutableList<String>,
            share_title = "",
            share_image = "",
            share_url = "",
            longitude = "",
            latitude = "",
            position = "",
            is_del = "0",
            deleteon = "0",
            createon = "4天前",
            user_name = "笑嘻嘻",
            head_img = "http://b167.photo.store.qq.com/psb?/V14EhGon2OmAUI/hQN450lNoDPF.dO82PVKEdFw0Qj5qyGeyN9fByKgWd0!/m/dJWKmWNZEwAAnull",
            video = "https://vjs.zencdn.net/v/oceans.mp4", // 视频链接
            comments_list = emptyCommentList,
            like_list = emptyLikeList,
            is_like = 0
        ),

        // 第6条：图片动态（修复：like_list的circle_id统一、明确泛型）
        CircleBean.DataBean(
            id = "190378",
            user_id = "3372793",
            type = Constants.TYPE_IMAGE, // "2"（图片动态）
            content = "",
            files = listOf(
                "https://inews.gtimg.com/news_bt/O_qHdht-j-Pc6-oSPkO0Nh5d2-gPK6YTa-ruo4P1aDQ5MAA/1000",
                "https://img1.baidu.com/it/u=2208277039,107339662&fm=253&fmt=auto&app=138&f=JPEG?w=219&h=332",
                "https://img1.baidu.com/it/u=3651140891,1638849478&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800",
                "https://img2.baidu.com/it/u=3143194608,2162960312&fm=253&fmt=auto&app=138&f=JPEG?w=499&h=300",
                "https://img1.baidu.com/it/u=174076537,4056466491&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=1201",
                "https://img2.baidu.com/it/u=3121773192,1157460465&fm=253&fmt=auto&app=138&f=JPEG?w=333&h=500",
                "https://img1.baidu.com/it/u=2377094960,1480051748&fm=253&fmt=auto&app=138&f=JPEG?w=759&h=389",
                "http://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960",
                "http://gips1.baidu.com/it/u=3874647369,3220417986&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280",
            ) as MutableList<String>,
            share_title = "",
            share_image = "",
            share_url = "",
            longitude = "",
            latitude = "",
            position = "",
            is_del = "0",
            deleteon = "0",
            createon = "4天前",
            user_name = "天宫2号",
            head_img = "http://b162.photo.store.qq.com/psb?/V14EhGon4cZvmh/z2WukT5EhNE76WtOcbqPIgwM2Wxz4Tb7Nub.rDpsDgo!/b/dOaanmAaKQAA",
            comments_list = emptyCommentList,
            like_list = emptyLikeList,
            is_like = 0,
            video = ""
        ),
    )
}
