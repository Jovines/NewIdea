package com.tree.newidea.viewModel

import android.annotation.SuppressLint
import android.graphics.Rect
import android.text.Editable
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.PopupWindow
import android.widget.TextView
import com.tree.common.viewmodel.BaseViewModel
import com.tree.newidea.R
import com.tree.newidea.activity.MarkDownActivity
import com.tree.newidea.util.dip2px
import com.tree.newidea.util.phoneHeight
import com.tree.newidea.util.phoneWidth
import kotlinx.android.synthetic.main.app_activity_edit.container
import kotlinx.android.synthetic.main.app_activity_edit.edit_view
import kotlin.math.abs

/**
 * Created by Tree on 2019/8/18 20:59
 */
class MarkDownViewModel : BaseViewModel() {
    private lateinit var keyboardTopViewFirstTxt: TextView
    private lateinit var keyboardTopViewSecondTxt: TextView
    private lateinit var keyboardTopViewThirdTxt: TextView
    private lateinit var keyboardTopViewFourthTxt: TextView
    private lateinit var keyboardTopViewTipContainer: View

    lateinit var keyboardOnGlobalChangeListener:KeyboardOnGlobalChangeListener


    private var mInputViewIsNull = true


    private var mSoftKeyboardTopPopupWindow: PopupWindow? = null
    private var mIsSoftKeyBoardShowing = false




    fun onClick(activity: MarkDownActivity, v: View) {
        var txt = ""
        when (v.id) {
            R.id.keyboard_top_view_first_txt -> txt = keyboardTopViewFirstTxt.text.toString()
            R.id.keyboard_top_view_second_txt -> txt = keyboardTopViewSecondTxt.text.toString()
            R.id.keyboard_top_view_third_txt -> txt = keyboardTopViewThirdTxt.text.toString()
            R.id.keyboard_top_view_fourth_txt -> txt = keyboardTopViewFourthTxt.text.toString()
        }
        insertTextToEditText(activity, txt)
    }



    //弹出的popupView
    @SuppressLint("InflateParams")
    fun showKeyboardTopPopupWindow(activity: MarkDownActivity, x: Int, y: Int) {
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
            keyboardTopViewTipContainer = popupView.findViewById(R.id.keyboard_top_view_tip_container)

            keyboardTopViewFirstTxt.setOnClickListener(this)
            keyboardTopViewSecondTxt.setOnClickListener(this)
            keyboardTopViewThirdTxt.setOnClickListener(this)
            keyboardTopViewFourthTxt.setOnClickListener(this)

            mSoftKeyboardTopPopupWindow =
                PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

            mSoftKeyboardTopPopupWindow?.apply {

                isTouchable = true
                isOutsideTouchable = false
                isFocusable = false
                showAtLocation(container, Gravity.BOTTOM, x, y)
            }

            updateKeyboardTopViewTips(activity,TextUtils.isEmpty(edit_view.text))
        }
    }


    //更新popupView的位置
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
    private fun closePopupWindow(activity: MarkDownActivity) {
        if (mSoftKeyboardTopPopupWindow != null && mSoftKeyboardTopPopupWindow!!.isShowing) {
            mSoftKeyboardTopPopupWindow!!.dismiss()
            mSoftKeyboardTopPopupWindow = null
            mInputViewIsNull = true
            activity.edit_view.bottom = activity.container.bottom - dip2px(15f)
        }
    }


    //当editText中的文字发生变化
    fun afterTextChanged(activity: MarkDownActivity, s: Editable) {
        val text = s.toString()
        updateKeyboardTopViewTips(activity,TextUtils.isEmpty(text))
    }


    //更新工具栏中的显示
    private fun updateKeyboardTopViewTips(activity: MarkDownActivity, isNull: Boolean) {
        if (mInputViewIsNull == isNull) {
            return
        }

        activity.apply {
            if (isNull) {
                keyboardTopViewFirstTxt.text = MarkDownActivity.KEYBOARD_TOP_VIEW_FIRST_TIP_NULL
                keyboardTopViewSecondTxt.text = MarkDownActivity.KEYBOARD_TOP_VIEW_SECOND_TIP_NULL
                keyboardTopViewThirdTxt.text = MarkDownActivity.KEYBOARD_TOP_VIEW_THIRD_TIP_NULL
                keyboardTopViewFourthTxt.text = MarkDownActivity.KEYBOARD_TOP_VIEW_FOURTH_TIP_NULL
                mInputViewIsNull = true
            } else {
                keyboardTopViewFirstTxt.text = MarkDownActivity.KEYBOARD_TOP_VIEW_FIRST_TIP
                keyboardTopViewSecondTxt.text = MarkDownActivity.KEYBOARD_TOP_VIEW_SECOND_TIP
                keyboardTopViewThirdTxt.text = MarkDownActivity.KEYBOARD_TOP_VIEW_THIRD_TIP
                keyboardTopViewFourthTxt.text = MarkDownActivity.KEYBOARD_TOP_VIEW_FOURTH_TIP
                mInputViewIsNull = false
            }
        }
    }



//    向editText中插入文本
    private fun insertTextToEditText(activity: MarkDownActivity, txt: String) {
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



    fun listenEventSettings(editActivity: MarkDownActivity) {
        editActivity.apply {



        }
    }

    fun initData(editActivity: MarkDownActivity) {
        editActivity.apply {
            keyboardOnGlobalChangeListener =  KeyboardOnGlobalChangeListener(editActivity)

        }
    }



    inner class KeyboardOnGlobalChangeListener(val activity: MarkDownActivity) : ViewTreeObserver.OnGlobalLayoutListener {
        //监听视图树的变化
        override fun onGlobalLayout() {
            val rect = Rect()
            // 获取当前页面窗口的显示范围
            activity.window.decorView.getWindowVisibleDisplayFrame(rect)
            val keyboardHeight = phoneHeight - (rect.bottom - rect.top) // 输入法的高度
            if (abs(keyboardHeight) > phoneHeight / 5) {
                mIsSoftKeyBoardShowing = true // 超过屏幕五分之一则表示弹出了输入法
                activity.edit_view.bottom = phoneHeight- abs(keyboardHeight) - dip2px(40f)
                showKeyboardTopPopupWindow(activity, phoneWidth / 2, keyboardHeight + dip2px(40f))
            } else {
                if (mIsSoftKeyBoardShowing) {
                    closePopupWindow(activity)
                }
                mIsSoftKeyBoardShowing = false
            }
        }
    }


}