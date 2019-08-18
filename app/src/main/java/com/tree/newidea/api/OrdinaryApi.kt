package com.tree.newidea.api

import com.tree.common.BaseApp.Companion.context
import com.tree.newidea.bean.BingPictureBean
import com.tree.newidea.bean.RecommendedMusicBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Url

/**
 * Created by Tree on 2019/8/15 14:49
 * 网络中收集的api
 */

const val BING_BASE_URI = "https://cn.bing.com"

const val RECOMMENDED_MUSIC_URI =  "http://elf.egos.hosigus.com/music/playlist/detail?id=2336345537"


fun recommendedSongUri(songmid: String) = "http://music.163.com/song/media/outer/url?id=$songmid.mp3"


interface OrdinaryApi {

    @GET
    fun getSplashPicture(@Url uri:String):Call<BingPictureBean>

    @GET
    fun getRecommendedSong(@Url uri: String): Call<RecommendedMusicBean>


}