package com.lyf.lingyingfacompose.ui.wx.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.lyf.lingyingfacompose.ui.wx.ui.model.CircleBean
import com.lyf.lingyingfacompose.ui.wx.ui.model.CommentListBean
import com.lyf.lingyingfacompose.ui.wx.utils.Constants
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import coil.request.ImageRequest
import com.lyf.lingyingfacompose.R

/**
 * @author: njb
 * @date:   2025/8/18 17:05
 * @desc:   描述
 */
@Composable
fun CircleItem(
    data: CircleBean.DataBean,
    onActionClick: () -> Unit,
    onReplyClick: (CommentListBean) -> Unit,
    onDeleteClick: () -> Unit,
    onVideoClick: (String) -> Unit,
    onImageClick: (List<String>, Int) -> Unit,
    onCommentLongClick: (CommentListBean) -> Unit
){
    val likeList = data.like_list

    Column(modifier = Modifier.fillMaxWidth()) {
        // 1. 用户信息栏（头像+用户名+时间+删除按钮）
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 用户头像
            Image(
                painter = rememberAsyncImagePainter(data.head_img ?: "https://picsum.photos/200/200?default"),
                contentDescription = "用户头像",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { /* 头像点击 */ },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 用户名+时间
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.user_name.toString(),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = data.createon.toString(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // 删除按钮（仅自己的动态显示）
            if (data.id == "current_user_id") {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.message_bg_delete), // 需自行添加图标资源
                        contentDescription = "删除",
                        tint = Color.Gray
                    )
                }
            }
        }

        // 2. 动态内容（可展开文本）
        if (!data.content.isNullOrBlank()) {
            ExpandableText(
                text = data.content,
                modifier = Modifier.padding(horizontal = 12.dp),
                maxLines = 2
            )
        }

        // 3. 动态内容区（根据类型渲染）
        when (data.type) {
            Constants.TYPE_IMAGE -> {
                NineGridImages(
                    images = data.files,
                    onImageClick = onImageClick,
                    overallPadding = 14.dp,
                    itemSpacing = 6.dp
                )
            }
            Constants.TYPE_VIDEO -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable { data.video.let { onVideoClick(it) } }
                ) {
                    // 视频封面图
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://picsum.photos/800/450")
                                .crossfade(true)
                                .build()
                        ),
                        contentDescription = "视频封面",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )

                    // 播放图标
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_video_play),
                        contentDescription = "播放视频",
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                            .padding(8.dp),
                        tint = Color.White
                    )
                }
            }
            Constants.TYPE_WEB -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                        .clickable { /* 网页点击事件 */ },
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(data.share_image ?: "https://picsum.photos/80/80?web"),
                            contentDescription = "网页图标",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // 网页标题容器 - 使用weight确保占据剩余空间
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = data.share_title,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Black
                                ),
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis,
                                softWrap = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        // 4. 地址
        if (data.position.isNotBlank() && data.position != "该位置信息暂无") {
            Text(
                text = data.position,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        // 5. 点赞+评论区
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)) {
            // 点赞列表
            if (data.like_list.isNotEmpty()) {
                Row(modifier = Modifier.padding(bottom = 8.dp)) {
                    Icon(
                        painter = painterResource(id = R.mipmap.menu_add), // 需自行添加图标资源
                        contentDescription = "点赞",
                        modifier = Modifier.size(16.dp),
                        tint = if (data.is_like == 1) Color.Red else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        itemsIndexed(likeList) { index, item ->
                            val likeItem = item
                            Text(
                                text = likeItem.user_name ?: "未知用户",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = if (data.is_like == 1) Color.Red else Color.Gray
                                ),
                                modifier = Modifier.padding(horizontal = 2.dp)
                            )

                            if (index != likeList.lastIndex) {
                                Text(
                                    text = "、",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(horizontal = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 评论列表
            if (data.comments_list.isNotEmpty()) {
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    data.comments_list.forEach { comment ->
                        CommentItem(
                            comment = comment,
                            onReplyClick = { onReplyClick(comment) },
                            onLongClick = { onCommentLongClick(comment) }
                        )
                    }
                }
            }

            // 操作按钮（点赞+评论）
            IconButton(onClick = onActionClick, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 点赞图标+文字
                    Icon(
                        painter = painterResource(id = if (data.is_like == 1) R.mipmap.live_gift else R.mipmap.live_share),
                        contentDescription = "点赞",
                        modifier = Modifier.size(16.dp),
                        tint = if (data.is_like == 1) Color.Red else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (data.is_like == 1) "已点赞" else "点赞",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.weight(1f)) // 占位，将评论按钮推到右侧

                    // 评论图标+文字
                    Icon(
                        painter = painterResource(id = R.mipmap.live_comment),
                        contentDescription = "评论",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "评论(${data.comments_list.size})",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // 分割线
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray.copy(alpha = 0.3f))
        )
    }
}