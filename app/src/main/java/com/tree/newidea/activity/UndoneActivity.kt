package com.tree.newidea.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import com.tree.common.ui.BaseActivity
import com.tree.newidea.R
import com.tree.newidea.bean.NotepadBean
import com.tree.newidea.util.*
import com.tree.newidea.view.MaskFrameLayout
import kotlinx.android.synthetic.main.app_activity_edit.*
import kotlinx.android.synthetic.main.app_activity_undone.*
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.*

class UndoneActivity : BaseActivity() {
    override val isFragmentActivity: Boolean
        get() = false
     private var maskFrameLayout: MaskFrameLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.app_no_anim, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_undone)
        closeMarkMask()
        undo_fab.setOnClickListener {
            if (undo_edit.editableText.toString().isNotEmpty()) {
                val simpleDateFormat = SimpleDateFormat(timeFormat1)// HH:mm:ss
                //获取当前时间
                val date = Date(System.currentTimeMillis())
                //初始化一个textBean
                val textBean: NotepadBean.DatesBean.TextsBean = NotepadBean.DatesBean.TextsBean()

                textBean.apply textBean@{
                    val text = undo_edit.editableText.toString()
                        .replace("", "\n").substringAfter("\n").substringBeforeLast("\n")
                    this.title = text
                    //获取当前时间
                    this.date =
                        SimpleDateFormat(timeFormat2).format(Date(System.currentTimeMillis()))
                    this.isUndo = true

                    timelineList?.add(this@textBean)
                    timelineList?.let {
                        asynSaveSerializationObject(this@UndoneActivity, "timelineList", it)
                    }
                    todoList?.add(textBean)
                }
                maskFrameLayout?.visibility = View.GONE
                this@UndoneActivity.finish()
            } else {
                Toast.makeText(this, "你还没有输入哦", Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.app_mark_down_activity_exit)

    }


    override fun onBackPressed() {
        maskFrameLayout?.visibility = View.GONE
        super.onBackPressed()

    }

    private fun closeMarkMask() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.app_mask_hide)
        undo_mark.animation = anim
        undo_mark.visibility = View.VISIBLE
        anim.interpolator = DecelerateInterpolator()
        anim.start()
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                undo_mark.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })
    }

    @Subscribe(sticky = true)
    fun closeMainMask(m: MaskFrameLayout) {
        maskFrameLayout = m
    }
}
