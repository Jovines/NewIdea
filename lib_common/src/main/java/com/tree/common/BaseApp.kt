package com.tree.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.tree.common.bean.User
import com.tree.common.config.SP_KEY_USER
import com.tree.common.utils.CrashHandler
import com.tree.common.utils.LogUtils
import com.tree.common.utils.encrypt.UserInfoEncryption
import com.tree.common.utils.extensions.defaultSharedPreferences
import com.tree.common.utils.extensions.editor
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig

/**
 * Created By jay68 on 2018/8/7.
 */
open class BaseApp : MultiDexApplication() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        var user: User? = null
            set(value) {
                field = value
                val encryptedJson = userInfoEncryption.encrypt(value?.toJson())
                context.defaultSharedPreferences.editor {
                    putString(SP_KEY_USER, encryptedJson)
                }
            }
            get() {
                if (field == null) {
                    val encryptedJson = context.defaultSharedPreferences.getString(SP_KEY_USER, "")
                    val json = userInfoEncryption.decrypt(encryptedJson)
                    LogUtils.d("userinfo", json)
                    try {
                        field = Gson().fromJson(json, User::class.java)
                    } catch (e: Throwable) {
                        LogUtils.d("userinfo", "parse user json failed")
                    }
                }
                return field
            }

        val isLogin get() = (user != null)
        val hasNickname get() = (user != null && user?.nickname != null)

        private lateinit var userInfoEncryption: UserInfoEncryption
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        context = base
    }

    override fun onCreate() {
        super.onCreate()
        CrashHandler.init(applicationContext)
        userInfoEncryption = UserInfoEncryption()
        initRouter()
    }

    private fun initRouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }

}