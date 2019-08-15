package com.tree.newidea.api

import com.tree.newidea.bean.BingPictureBean
import com.tree.newidea.bean.RecommendedMusicBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by Tree on 2019/8/15 14:49
 * 网络中收集的api
 */

const val BING_BASE_URI = "https://cn.bing.com"

const val RECOMMENDED_MUSIC_URI = "https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg?g_tk=5381&uin=0&format=json&inCharset=utf-8&outCharset=utf-8¬ice=0&platform=h5&needNewCode=1&tpl=3&page=detail&type=top&topid=36&_=1520777874472"

fun recommendedSongUri(songmid: String) = "http://ws.stream.qqmusic.qq.com/C100'$songmid'.m4a"


interface OrdinaryApi {

    @GET
    fun getSplashPicture(@Url uri:String):Call<BingPictureBean>

    @GET
    fun getRecommendedSong(@Url uri: String): Call<RecommendedMusicBean>

}