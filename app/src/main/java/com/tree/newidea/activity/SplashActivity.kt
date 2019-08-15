package com.tree.newidea.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.tree.common.ui.BaseActivity
import com.tree.newidea.R
import com.tree.newidea.api.BING_BASE_URI
import com.tree.newidea.api.OrdinaryApi
import com.tree.newidea.api.ApiGenerator
import com.tree.newidea.bean.BingPictureBean
import com.tree.newidea.util.loadImage
import com.tree.newidea.util.ordinaryApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_activity_splash.*
import retrofit2.Call
import retrofit2.Response

class SplashActivity : BaseActivity() {
    override val isFragmentActivity: Boolean
        get() = false

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_splash)

        ordinaryApi = ApiGenerator.getApiService(OrdinaryApi::class.java)
        ordinaryApi.getSplashPicture("https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN").enqueue(object : retrofit2.Callback<BingPictureBean> {
            override fun onResponse(call: Call<BingPictureBean>, response: Response<BingPictureBean>) {
                val a = response.body()
                response.body()?.let {
                    loadImage(
                        iv_splash_backgroup,
                        "$BING_BASE_URI${it.images?.get(0)?.url?.substringBefore("&")}",
                        null
                    )
                    tv_splash_tilte.text = "@${it.images[0].copyright.substringBefore("(")}"
                }
                Observable.create<Any> {
                    Thread.sleep(1000)
                    startActivity<MainActivity>()
                }.subscribeOn(Schedulers.io()).subscribe()
            }
            override fun onFailure(call: Call<BingPictureBean>, t: Throwable) {
                startActivity<MainActivity>()
            }
        })

    }
}
