package com.tree.newidea.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import com.tree.common.ui.BaseViewModelActivity
import com.tree.newidea.R
import com.tree.newidea.bean.NotepadBean
import com.tree.newidea.util.note
import com.tree.newidea.view.MaskFrameLayout
import com.tree.newidea.viewModel.EditViewmodel
import kotlinx.android.synthetic.main.app_activity_edit.*
import kotlinx.android.synthetic.main.app_activity_mark_down.*
import kotlinx.android.synthetic.main.app_activity_preview.*
import kotlinx.android.synthetic.main.app_skid_edit_top.view.*
import org.greenrobot.eventbus.Subscribe

class EditActivity : BaseViewModelActivity<EditViewmodel>() {
    override val viewModelClass: Class<EditViewmodel>
        get() = EditViewmodel::class.java
    override val isFragmentActivity: Boolean
        get() = false

     var maskFrameLayout: MaskFrameLayout? = null

    var bean:NotepadBean.DatesBean.TextsBean? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.app_no_anim, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_edit)
        viewModel.initData(this)
        viewModel.listenEventSettings(this)//监听事件的设置
        viewModel.setSkipTop(this@EditActivity)
        closeMarkMask()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.app_mark_down_activity_exit)

    }


    private fun closeMarkMask() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.app_mask_hide)
        edit_mark.animation = anim
        edit_mark.visibility = View.VISIBLE
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
        if (viewModel.isSkidTopOpen) {
            smart_swipe_edit.apply {
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
            maskFrameLayout?.visibility = View.GONE
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(viewModel.keyboardOnGlobalChangeListener)
    }

    @Subscribe(sticky = true)
    fun reTextBean(bean: NotepadBean.DatesBean.TextsBean) {
        note?.dates?.forEach {
            it?.texts?.forEach {
                if (bean.text == it.text && bean.title == it.title || bean == it) {
                    viewModel.textBean = it
                    edit_text.setText(it.text)
                    edit_activity_top_search_normal_tilte.setText(it.title)
                }
            }
        }
    }

    @Subscribe(sticky = true)
    fun closeMainMask(m: MaskFrameLayout) {
        maskFrameLayout = m
    }


}