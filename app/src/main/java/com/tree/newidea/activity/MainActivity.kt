package com.tree.newidea.activity

import android.os.Bundle
import com.tree.common.ui.BaseViewModelActivity
import com.tree.newidea.viewModel.MainViewModel


class MainActivity : BaseViewModelActivity<MainViewModel>() {
    override val isFragmentActivity: Boolean
        get() = false
    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java



    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(com.tree.newidea.R.anim.app_activity_main_enter, 0)
        super.onCreate(savedInstanceState)
        setContentView(com.tree.newidea.R.layout.app_activity_main)
        viewModel.getPhoneScreenData(this)//取得状态栏的高度
        viewModel.initData(this)
        viewModel.getSongData(this)//设置获取歌曲数据，并初始化歌曲viewPager
        viewModel.listenEventSettings(this)//监听事件的设置
        viewModel.installRecyclerView(this)//侧边的RcycleView的设置

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }



}
