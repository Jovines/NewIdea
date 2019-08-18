package com.tree.newidea.viewModel

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.text.Editable
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.SeekBar
import android.widget.TextView
import com.tree.common.viewmodel.BaseViewModel
import com.tree.newidea.R
import com.tree.newidea.activity.EditActivity
import com.tree.newidea.util.dip2px
import com.tree.newidea.util.phoneHeight
import com.tree.newidea.util.phoneWidth
import kotlinx.android.synthetic.main.app_activity_edit.*
import kotlin.math.abs

/**
 * Created by Tree on 2019/8/18 20:59
 */
class EditViewmodel : BaseViewModel() {
    private lateinit var keyboardTopViewFirstTxt: TextView
    private lateinit var keyboardTopViewSecondTxt: TextView
    private lateinit var keyboardTopViewThirdTxt: TextView
    private lateinit var keyboardTopViewFourthTxt: TextView
    private lateinit var keyboardTopViewTipContainer: View
    private lateinit var keyboardTopViewSeekBar: SeekBar

    lateinit var keyboardOnGlobalChangeListener:KeyboardOnGlobalChangeListener


    private var mInputViewIsNull = true

    private var mExtendSeekBarAnimator: ValueAnimator? = null
    private var mShrinkSeekBarAnimator: ValueAnimator? = null
    var mIsCanMoveCursor = false
    private var mLastSeekBarProgress = 25

    private var mSoftKeyboardTopPopupWindow: PopupWindow? = null
    private var mIsSoftKeyBoardShowing = false


    fun shrinkSeekBarAnimator(activity: EditActivity) {
        activity.apply {
            if (mExtendSeekBarAnimator != null && mExtendSeekBarAnimator!!.isRunning) {
                mExtendSeekBarAnimator!!.cancel()
                mExtendSeekBarAnimator = null
            }

            val seekBarWidth = keyboardTopViewSeekBar.width
            val minWidth = resources.getDimensionPixelOffset(R.dimen.app_keyboard_top_view_seek_bar_min_width)
            mShrinkSeekBarAnimator = ValueAnimator.ofInt(seekBarWidth - minWidth)
            mShrinkSeekBarAnimator!!.duration = 300
            mShrinkSeekBarAnimator!!.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                val layoutParams = keyboardTopViewSeekBar.layoutParams as FrameLayout.LayoutParams
                layoutParams.width = seekBarWidth - value
                keyboardTopViewSeekBar.layoutParams = layoutParams
                val normalProgress = keyboardTopViewSeekBar.max / 2
                val progress =
                    (keyboardTopViewSeekBar.progress - normalProgress) * (value / (seekBarWidth - minWidth)) + normalProgress
                keyboardTopViewSeekBar.progress = progress
            }
            mShrinkSeekBarAnimator!!.start()

            keyboardTopViewTipContainer.visibility = View.VISIBLE
        }
    }


    fun extendSeekBarAnimator() {
        if (mShrinkSeekBarAnimator != null && mShrinkSeekBarAnimator!!.isRunning) {
            mShrinkSeekBarAnimator!!.cancel()
            mShrinkSeekBarAnimator = null
        }


        val seekBarWidth = keyboardTopViewSeekBar.width
        mExtendSeekBarAnimator = ValueAnimator.ofInt(phoneWidth - seekBarWidth)
        mExtendSeekBarAnimator!!.duration = 300
        mExtendSeekBarAnimator!!.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val layoutParams = keyboardTopViewSeekBar.layoutParams as FrameLayout.LayoutParams
            layoutParams.width = value + seekBarWidth
            keyboardTopViewSeekBar.layoutParams = layoutParams
        }
        mExtendSeekBarAnimator!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                keyboardTopViewTipContainer.visibility = View.INVISIBLE
                mIsCanMoveCursor = true
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        mExtendSeekBarAnimator!!.start()
    }


    fun onClick(activity: EditActivity, v: View) {
        var txt = ""
        when (v.id) {
            R.id.keyboard_top_view_first_txt -> txt = keyboardTopViewFirstTxt.text.toString()
            R.id.keyboard_top_view_second_txt -> txt = keyboardTopViewSecondTxt.text.toString()
            R.id.keyboard_top_view_third_txt -> txt = keyboardTopViewThirdTxt.text.toString()
            R.id.keyboard_top_view_fourth_txt -> txt = keyboardTopViewFourthTxt.text.toString()
        }
        insertTextToEditText(activity, txt)
    }



    @SuppressLint("InflateParams")
    fun showKeyboardTopPopupWindow(activity: EditActivity, x: Int, y: Int) {
        activity.apply {
            if (mSoftKeyboardTopPopupWindow != null && mSoftKeyboardTopPopupWindow!!.isShowing) {
                updateKeyboardTopPopupWindow(x, y) //可能是输入法切换了输入模式，高度会变化（比如切换为语音输入）
                return
            }

            val popupView = layoutInflater.inflate(R.layout.app_soft_keyboard_top_tool_view, null)
            keyboardTopViewFirstTxt = popupView.findViewById(R.id.keyboard_top_view_first_txt)
            keyboardTopViewSecondTxt = popupView.findViewById(R.id.keyboard_top_view_second_txt)
            keyboardTopViewThirdTxt = popupView.findViewById(R.id.keyboard_top_view_third_txt)
            keyboardTopViewFourthTxt = popupView.findViewById(R.id.keyboard_top_view_fourth_txt)
            keyboardTopViewSeekBar = popupView.findViewById(R.id.keyboard_top_view_seek_bar)
            keyboardTopViewTipContainer = popupView.findViewById(R.id.keyboard_top_view_tip_container)

            keyboardTopViewFirstTxt.setOnClickListener(this)
            keyboardTopViewSecondTxt.setOnClickListener(this)
            keyboardTopViewThirdTxt.setOnClickListener(this)
            keyboardTopViewFourthTxt.setOnClickListener(this)
            keyboardTopViewSeekBar.setOnSeekBarChangeListener(this)

            mSoftKeyboardTopPopupWindow =
                PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

            mSoftKeyboardTopPopupWindow?.apply {

                isTouchable = true
                isOutsideTouchable = false
                isFocusable = false
                //        inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED //解决遮盖输入法
                showAtLocation(container, Gravity.BOTTOM, x, y)
            }

            updateKeyboardTopViewTips(activity,TextUtils.isEmpty(edit_view.text))
        }
    }


    private fun updateKeyboardTopPopupWindow(x: Int, y: Int) {
        if (mSoftKeyboardTopPopupWindow != null && mSoftKeyboardTopPopupWindow!!.isShowing) {
            mSoftKeyboardTopPopupWindow!!.update(
                x,
                y,
                mSoftKeyboardTopPopupWindow!!.width,
                mSoftKeyboardTopPopupWindow!!.height
            )
        }
    }


    //关闭popupWindow
    private fun closePopupWindow() {
        if (mSoftKeyboardTopPopupWindow != null && mSoftKeyboardTopPopupWindow!!.isShowing) {
            mSoftKeyboardTopPopupWindow!!.dismiss()
            mSoftKeyboardTopPopupWindow = null
            mInputViewIsNull = true
        }
    }


    fun afterTextChanged(activity: EditActivity, s: Editable) {
        val text = s.toString()
        updateKeyboardTopViewTips(activity,TextUtils.isEmpty(text))
    }


    private fun updateKeyboardTopViewTips(activity: EditActivity, isNull: Boolean) {
        if (mInputViewIsNull == isNull) {
            return
        }

        activity.apply {
            if (isNull) {
                keyboardTopViewFirstTxt.text = EditActivity.KEYBOARD_TOP_VIEW_FIRST_TIP_NULL
                keyboardTopViewSecondTxt.text = EditActivity.KEYBOARD_TOP_VIEW_SECOND_TIP_NULL
                keyboardTopViewThirdTxt.text = EditActivity.KEYBOARD_TOP_VIEW_THIRD_TIP_NULL
                keyboardTopViewFourthTxt.text = EditActivity.KEYBOARD_TOP_VIEW_FOURTH_TIP_NULL
                mInputViewIsNull = true
            } else {
                keyboardTopViewFirstTxt.text = EditActivity.KEYBOARD_TOP_VIEW_FIRST_TIP
                keyboardTopViewSecondTxt.text = EditActivity.KEYBOARD_TOP_VIEW_SECOND_TIP
                keyboardTopViewThirdTxt.text = EditActivity.KEYBOARD_TOP_VIEW_THIRD_TIP
                keyboardTopViewFourthTxt.text = EditActivity.KEYBOARD_TOP_VIEW_FOURTH_TIP
                mInputViewIsNull = false
            }
        }
    }

    fun onProgressChanged(activity: EditActivity, progress: Int) {
        if (mIsCanMoveCursor) {
            moveEditViewCursor(activity,mLastSeekBarProgress > progress)
        }
        mLastSeekBarProgress = progress
    }


     fun onStartTrackingTouch() {
        mIsCanMoveCursor = false
        extendSeekBarAnimator()
    }

    fun onStopTrackingTouch(activity: EditActivity) {
        mIsCanMoveCursor = false
        shrinkSeekBarAnimator(activity)
    }

    private fun moveEditViewCursor(activity: EditActivity, isMoveLeft: Boolean) {
        activity.apply {
            val index = edit_view.selectionStart
            if (isMoveLeft) {
                if (index <= 0) return
                edit_view.setSelection(index - 1)
            } else {
                val edit = edit_view.editableText//获取EditText的文字
                if (index >= edit.length) return
                edit_view.setSelection(index + 1)
            }

        }
    }

    inner class KeyboardOnGlobalChangeListener(val activity: EditActivity) :
        ViewTreeObserver.OnGlobalLayoutListener {
        //监听视图树的变化
        override fun onGlobalLayout() {
            val rect = Rect()
            // 获取当前页面窗口的显示范围
            activity.window.decorView.getWindowVisibleDisplayFrame(rect)
            val keyboardHeight = phoneHeight - (rect.bottom - rect.top) // 输入法的高度
            if (abs(keyboardHeight) > phoneHeight / 5) {
                mIsSoftKeyBoardShowing = true // 超过屏幕五分之一则表示弹出了输入法
                showKeyboardTopPopupWindow(activity, phoneWidth / 2, keyboardHeight + dip2px(40f))
            } else {
                if (mIsSoftKeyBoardShowing) {
                    closePopupWindow()
                }
                mIsSoftKeyBoardShowing = false
            }
        }
    }


    private fun insertTextToEditText(activity: EditActivity, txt: String) {
        activity.apply {
            if (TextUtils.isEmpty(txt)) return

            val start = edit_view.selectionStart
            val end = edit_view.selectionEnd
            val editText = edit_view.editableText//获取EditText的文字
            if (start < 0 || start >= editText.length) {
                editText.append(txt)
            } else {
                editText.replace(start, end, txt)//光标所在位置插入文字
            }
        }

    }

    fun listenEventSettings(editActivity: EditActivity) {
        editActivity.apply {



        }
    }

    fun initData(editActivity: EditActivity) {
        editActivity.apply {
           keyboardOnGlobalChangeListener =  KeyboardOnGlobalChangeListener(editActivity)

        }
    }


}
