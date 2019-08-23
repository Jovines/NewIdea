package com.tree.newidea.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.EditText
import com.mukesh.MarkdownView
import com.tree.common.ui.BaseViewModelActivity
import com.tree.newidea.R
import com.tree.newidea.event.MainUpDate
import com.tree.newidea.view.MaskFrameLayout
import com.tree.newidea.viewModel.MarkDownViewModel
import kotlinx.android.synthetic.main.app_activity_mark_down.*
import kotlinx.android.synthetic.main.app_skid_edit_top.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MarkDownActivity : BaseViewModelActivity<MarkDownViewModel>(), TextWatcher {
    override val viewModelClass: Class<MarkDownViewModel>
        get() = MarkDownViewModel::class.java
    override val isFragmentActivity: Boolean
        get() = false

    lateinit var editText: EditText
    lateinit var markdownView: MarkdownView

    lateinit var maskFrameLayout: MaskFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.app_no_anim, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_mark_down)
        viewModel.initData(this)//这一步必须在所有初始化操作之前
        editText.addTextChangedListener(this)
        //监听视图树的布局改变(弹出/隐藏软键盘会触发)
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(viewModel.keyboardOnGlobalChangeListener)
        viewModel.listenEventSettings(this)//监听事件的设置
        closeMarkMask()

    }

    private fun closeMarkMask() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.app_mask_hide)
        mask_mark.animation = anim
        mask_mark.visibility = View.VISIBLE
        anim.interpolator = DecelerateInterpolator() as Interpolator?
        anim.start()
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                mask_mark.visibility = View.GONE
                viewModel.setSkipTop(this@MarkDownActivity)
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })
    }

    override fun onStart() {
        super.onStart()
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.isExitLoopPrompt = true
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(viewModel.keyboardOnGlobalChangeListener)
        EventBus.getDefault().post(MainUpDate())
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.app_mark_down_activity_exit)
    }

    override fun onBackPressed() {
        if (viewModel.isSkidTopOpen) {
            smart_swipe_mark.apply {
                (mark_activity_top_search_normal.tag as? ValueAnimator)?.apply {
                    addListener(object : AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator?) {
                            mark_activity_top_search_normal.visibility = View.GONE
                        }
                    })
                }?.start()
                (mark_activity_top_search_m.tag as? ValueAnimator)?.apply {
                    addListener(object : AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator?) {
                            mark_activity_top_search_m.visibility = View.GONE
                        }
                    })
                }?.start()
            }
        }else{
            maskFrameLayout.visibility = View.GONE
            super.onBackPressed()
        }

    }


    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        viewModel.afterTextChanged(this, s)
        markdownView.setMarkDownText(s.toString())
    }

    @Subscribe(sticky = true)
    fun closeMainMask(m: MaskFrameLayout) {
       maskFrameLayout = m
    }

}