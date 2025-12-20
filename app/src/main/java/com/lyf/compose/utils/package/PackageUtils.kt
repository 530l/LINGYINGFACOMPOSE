package com.lyf.compose.utils.`package`
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Android 应用包信息工具类
 * 提供获取应用版本、签名、安装状态等功能的便捷方法
 */
object PackageUtils {


    fun getCurrentAppName(context: Context): String {
        return try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }


    fun getCurrentVersionName(context: Context): String {
        return try {
            val packageInfo = getPackageInfoSafely(context, context.packageName)
            packageInfo?.versionName ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }


    fun getCurrentVersionCode(context: Context): Long {
        return try {
            val packageInfo = getPackageInfoSafely(context, context.packageName)
            packageInfo?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    it.longVersionCode
                } else {
                    @Suppress("DEPRECATION")
                    it.versionCode.toLong()
                }
            } ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }


    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 获取当前应用的 MD5 签名
     * @param context 应用上下文
     * @return MD5 签名字符串，格式为小写十六进制，用冒号分隔，获取失败时返回 null
     */
    fun getAppSignatureMD5(context: Context): String? {
        return generateSignatureHash(context, "MD5")
    }

    /**
     * 获取当前应用的 SHA1 签名
     */
    fun getAppSignatureSHA1(context: Context): String? {
        return generateSignatureHash(context, "SHA1")
    }

    /**
     * 安全地获取包信息
     */
    private fun getPackageInfoSafely(context: Context, packageName: String): PackageInfo? {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 生成应用签名的哈希值
     */
    private fun generateSignatureHash(context: Context, algorithm: String): String? {
        return try {
            val packageManager = context.packageManager

            // 获取签名信息
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES
                )
            }

            // 获取证书字节数组
            val certificateBytes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo?.apkContentsSigners?.get(0)?.toByteArray()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures?.get(0)?.toByteArray()
            }

            certificateBytes?.let { bytes ->
                val messageDigest = MessageDigest.getInstance(algorithm)
                val hashBytes = messageDigest.digest(bytes)

                // 转换为十六进制字符串
                buildString {
                    hashBytes.forEachIndexed { index, byte ->
                        val hex = String.format("%02x", byte.toInt() and 0xFF)
                        append(hex)
                        if (index < hashBytes.size - 1) {
                            append(":")
                        }
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}