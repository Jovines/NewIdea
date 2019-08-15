package com.tree.newidea.activity

import android.os.Bundle
import com.tree.common.ui.BaseViewModelActivity
import com.tree.newidea.R
import com.tree.newidea.adapter.MusicViewPagerAdapter
import com.tree.newidea.api.RECOMMENDED_MUSIC_URI
import com.tree.newidea.bean.RecommendedMusicBean
import com.tree.newidea.util.ordinaryApi
import com.tree.newidea.viewModel.MainViewModel
import kotlinx.android.synthetic.main.app_activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : BaseViewModelActivity<MainViewModel>(){
    override val isFragmentActivity: Boolean
        get() = false
    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_main)

        ordinaryApi.getRecommendedSong(RECOMMENDED_MUSIC_URI).enqueue(object : retrofit2.Callback<RecommendedMusicBean>{
            override fun onFailure(call: Call<RecommendedMusicBean>, t: Throwable) {

            }
            override fun onResponse(call: Call<RecommendedMusicBean>, response: Response<RecommendedMusicBean>) {
                response.body()?.let {
                    vp_music.adapter = MusicViewPagerAdapter(vp_music,it)
                }
            }

        })
    }
}
