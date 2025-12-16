package com.lyf.lingyingfacompose.utils

import java.math.BigDecimal
import java.math.RoundingMode



object LikeCountFormatter {

    /**
    【安卓端-榜单】首页-榜单详情页，榜单歌曲点赞数>=10000万，未以万为单位显示，
    预期：以万做为单位，四舍五入保留1位小数
    1. 显示：N.N万，如：1.5万;
    2. 小数点后为0，预期：不显示小数位，直接显示N万

    四舍五入有问题，5被舍去了，5预期是进位，如：1990500，预期：199.1万，实际：199万
     */
    fun formatLikeCount(count: Long): String {
//        val count = 10000.0.toLong()
//        val count = 1990500.toLong()
//        val count =100500.toLong()
        return formatCount(count)
    }

    fun formatWorkCount(count: Long): String {
        return formatCount(count)
    }


    /**
     * 通用格式化方法：数字>=10000时以"万"为单位显示，四舍五入保留1位小数
     */
    private fun formatCount(count: Long): String {
        return if (count >= 10000) {
            // 使用 BigDecimal 以避免浮点数精度问题，并应用 HALF_UP 舍入
            val wan = BigDecimal.valueOf(count)
                .divide(BigDecimal.valueOf(10000L), 1, RoundingMode.HALF_UP)
            // 如果小数部分为零，则显示为整数（例如，“2万”而不是“2.0万”）
            val isInteger = wan.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0
            if (isInteger) {
                "${wan.setScale(0).toPlainString()}万"
            } else {
                // 确保一位小数
                "${wan.toPlainString()}万"
            }
        } else {
            count.toString()
        }
    }
}