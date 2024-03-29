package com.tree.newidea.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.tree.common.ui.BaseActivity
import com.tree.newidea.R
import com.tree.newidea.api.BING_BASE_URI
import com.tree.newidea.api.OrdinaryApi
import com.tree.newidea.api.ApiGenerator
import com.tree.newidea.bean.BingPictureBean
import com.tree.newidea.bean.NotepadBean
import com.tree.newidea.util.*
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_activity_splash.*
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response


class SplashActivity : BaseActivity() {
    override val isFragmentActivity: Boolean
        get() = false

    private var isOpenMainActivity = true

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_splash)
        ordinaryApi = ApiGenerator.getApiService(OrdinaryApi::class.java)
        Observable.create<Any> {
            note =  (getObject(this,"note") as NotepadBean?) ?: NotepadBean()
            Thread.sleep(1500)
                startActivity<MainActivity>()
            finish()
        }.subscribeOn(Schedulers.io()).subscribe{}
//        ordinaryApi.getSplashPicture("https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN").enqueue(object : retrofit2.Callback<BingPictureBean> {
//            override fun onResponse(call: Call<BingPictureBean>, response: Response<BingPictureBean>) {
//                val a = response.body()
//                response.body()?.let {
//                    Glide.with(this@SplashActivity).load("$BING_BASE_URI${it.images?.get(0)?.url?.substringBefore("&")}")
//                        .listener(
//                        object : RequestListener<Drawable> {
//                            override fun onLoadFailed(
//                                e: GlideException?,
//                                model: Any?,
//                                target: Target<Drawable>?,
//                                isFirstResource: Boolean
//                            ): Boolean {
//                                return false
//                            }
//
//                            override fun onResourceReady(
//                                resource: Drawable?,
//                                model: Any?,
//                                target: Target<Drawable>?,
//                                dataSource: DataSource?,
//                                isFirstResource: Boolean
//                            ): Boolean {
//                                iv_splash_backgroup.startAnimation(AnimationUtils.loadAnimation(this@SplashActivity, R.anim.app_splash_image))
//                                Observable.create<Any> {
//                                    Thread.sleep(3000)
//                                    if (isOpenMainActivity) {
//                                        startActivity<MainActivity>()
//                                        Thread.sleep(activityStartTime)
//                                        finish()
//                                    }
//                                }.subscribeOn(Schedulers.io()).subscribe()
//                                return false
//                            }
//                        }).into(iv_splash_backgroup)
//                    tv_splash_tilte.text = "@${it.images[0].copyright.substringBefore("(")}"
//                }
//
//            }
//            override fun onFailure(call: Call<BingPictureBean>, t: Throwable) {
//                startActivity<MainActivity>()
//            }
//        })

    }


    override fun onDestroy() {
        super.onDestroy()
        isOpenMainActivity = false
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.app_no_anim)

    }
}
