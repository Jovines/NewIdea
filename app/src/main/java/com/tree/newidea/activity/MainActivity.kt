package com.tree.newidea.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.tree.common.ui.BaseViewModelActivity
import com.tree.newidea.adapter.SidebarRecycleViewAdapter
import com.tree.newidea.event.MainUpDate
import com.tree.newidea.util.note
import com.tree.newidea.viewModel.MainViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_activity_main.*
import kotlinx.android.synthetic.main.app_main_sidebar.*
import org.greenrobot.eventbus.Subscribe


class MainActivity : BaseViewModelActivity<MainViewModel>() {
    override val isFragmentActivity: Boolean
        get() = false
    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java

    var splashActivity: SplashActivity? = null


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tree.newidea.R.layout.app_activity_main)
        viewModel.getPhoneScreenData(this)//取得状态栏的高度
        viewModel.initData(this)
        viewModel.getSongData(this)//设置获取歌曲数据，并初始化歌曲viewPager
        viewModel.listenEventSettings(this)//监听事件的设置
    }


    override fun onResume() {
        super.onResume()
    }


    override fun onStart() {
        super.onStart()

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    @Subscribe
    fun update(mianUpDate: MainUpDate) {
        srv_main_sidebar.adapter?.notifyDataSetChanged()

//        srv_main_sidebar.adapter = SidebarRecycleViewAdapter(note)

    }

    @Subscribe(sticky = true)
    fun receiveSplash(acticity:SplashActivity) {
        splashActivity = acticity
    }



}
