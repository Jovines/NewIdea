package com.tree.newidea.activity

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import com.tree.common.ui.BaseViewModelActivity
import com.tree.newidea.R
import com.tree.newidea.view.MaskFrameLayout
import com.tree.newidea.viewModel.EditViewmodel
import kotlinx.android.synthetic.main.app_activity_edit.*
import kotlinx.android.synthetic.main.app_activity_mark_down.*
import org.greenrobot.eventbus.Subscribe

class EditActivity : BaseViewModelActivity<EditViewmodel>() {
    override val viewModelClass: Class<EditViewmodel>
        get() = EditViewmodel::class.java
    override val isFragmentActivity: Boolean
        get() = false

    lateinit var maskFrameLayout: MaskFrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.app_no_anim, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_edit)
        viewModel.initData(this)
        viewModel.listenEventSettings(this)//监听事件的设置
        closeMarkMask()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.app_mark_down_activity_exit)
    }


    private fun closeMarkMask() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.app_mask_hide)
        edit_mark.animation = anim
        anim.interpolator = DecelerateInterpolator()
        anim.start()
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                edit_mark.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })
    }


    override fun onBackPressed() {
        maskFrameLayout.visibility = View.GONE
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(viewModel.keyboardOnGlobalChangeListener)
    }

    @Subscribe(sticky = true)
    fun closeMainMask(m: MaskFrameLayout) {
        maskFrameLayout = m
    }


}