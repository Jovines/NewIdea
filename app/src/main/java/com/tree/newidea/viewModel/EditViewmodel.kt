package com.tree.newidea.viewModel

import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewTreeObserver
import com.tree.common.viewmodel.BaseViewModel
import com.tree.newidea.activity.EditActivity
import com.tree.newidea.util.StatusBarUtil
import com.tree.newidea.util.phoneHeight
import kotlinx.android.synthetic.main.app_activity_edit.*
import kotlin.math.abs

/**
 * Created by Tree on 2019/8/18 20:59
 */
class EditViewmodel : BaseViewModel() {


    lateinit var keyboardOnGlobalChangeListener:KeyboardOnGlobalChangeListener

    private var mIsSoftKeyBoardShowing = false


    fun initData(editActivity: EditActivity) {
        editActivity.apply {
            StatusBarUtil.setStatusBarDarkTheme(editActivity,true)
            keyboardOnGlobalChangeListener =  KeyboardOnGlobalChangeListener(editActivity)

        }
    }

    fun listenEventSettings(editActivity: EditActivity) {
        editActivity.apply {

            //监听视图树的布局改变(弹出/隐藏软键盘会触发)
            window.decorView.viewTreeObserver.addOnGlobalLayoutListener(keyboardOnGlobalChangeListener)

            edit_text.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })


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
                //Todo 展开工具栏
            } else {
                if (mIsSoftKeyBoardShowing) {
                    //todo 关闭工具栏
                }
                mIsSoftKeyBoardShowing = false
            }
        }
    }

}
