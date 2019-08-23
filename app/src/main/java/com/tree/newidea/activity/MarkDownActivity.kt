package com.tree.newidea.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import com.mukesh.MarkdownView
import com.tree.common.ui.BaseViewModelActivity
import com.tree.newidea.R
import com.tree.newidea.event.MainUpDate
import com.tree.newidea.view.MaskFrameLayout
import com.tree.newidea.viewModel.MarkDownViewModel
import kotlinx.android.synthetic.main.app_activity_mark_down.*
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
        closeMarkMask()
        viewModel.initData(this)//这一步必须在所有初始化操作之前
        editText.addTextChangedListener(this)
        //监听视图树的布局改变(弹出/隐藏软键盘会触发)
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(viewModel.keyboardOnGlobalChangeListener)
        viewModel.listenEventSettings(this)//监听事件的设置

    }

    private fun closeMarkMask() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.app_mask_hide)
        mask_mark.animation = anim
        mask_mark.visibility = View.VISIBLE
        anim.interpolator = DecelerateInterpolator()
        anim.start()
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                mask_mark.visibility = View.GONE
                viewModel.setUpSkidTop(this@MarkDownActivity)
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
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(viewModel.keyboardOnGlobalChangeListener)
        EventBus.getDefault().post(MainUpDate())
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.app_mark_down_activity_exit)
    }

    override fun onBackPressed() {
        maskFrameLayout.visibility = View.GONE
        super.onBackPressed()
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


    companion object {
        const val KEYBOARD_TOP_VIEW_FIRST_TIP_NULL = "# "
        const val KEYBOARD_TOP_VIEW_SECOND_TIP_NULL = "* "
        const val KEYBOARD_TOP_VIEW_THIRD_TIP_NULL = "> "
        const val KEYBOARD_TOP_VIEW_FOURTH_TIP_NULL = "1. "
        const val KEYBOARD_TOP_VIEW_FIRST_TIP = "."
        const val KEYBOARD_TOP_VIEW_SECOND_TIP = "/"
        const val KEYBOARD_TOP_VIEW_THIRD_TIP = ".com"
        const val KEYBOARD_TOP_VIEW_FOURTH_TIP = ".cn"
    }
}