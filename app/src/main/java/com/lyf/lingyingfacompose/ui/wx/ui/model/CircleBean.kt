package com.lyf.lingyingfacompose.ui.wx.ui.model

/**
 * @author: njb
 * @date:   2025/8/18 1:42
 * @desc:   描述
 */
data class CircleBean(
    val data: List<DataBean>
) {
    data class DataBean(
        var id: String,
        var user_id: String,
        var type: Int = 0,
        var content: String,
        var share_title: String,
        var share_image: String,
        var share_url: String,
        var longitude: String,
        var latitude: String,
        var position: String,
        var is_del: String,
        var deleteon: String,
        var createon: String,
        var user_name: String,
        var head_img: String,
        var is_like: Int = 0,
        var files: MutableList<String>,
        var comments_list: MutableList<CommentListBean>,
        var like_list: MutableList<LikeListBean>,
        var video: String,
        var isShowAll: Boolean = false,
        var isExpanded: Boolean = false,
        var isShowCheckAll: Boolean = false,
    )
}

data class LikeListBean(
    var user_id: String?,    // 必须有此参数（与 JSON 对应）
    var circle_id: String?,  // 必须有此参数（与 JSON 对应）
    var user_name: String?
)

data class CommentListBean(
    val id: String,
    val circle_id: String,
    val user_id: String,
    val to_user_id: String,
    val type: String,
    val content: String,
    val to_user_name: String,
    val user_name: String,
    val createon: String,
    val replyUser: CommentsUser,
    val commentsUser: CommentsUser,
    val is_del: String,
    val deleteon: String,
) {
    data class CommentsUser(
        val userId: String,
        val userName: String
    )
}
