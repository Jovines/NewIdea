package com.tree.newidea.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import com.tree.common.ui.BaseViewModelActivity
import com.tree.newidea.R
import com.tree.newidea.viewModel.EditViewmodel
import kotlinx.android.synthetic.main.app_activity_edit.*

class EditActivity : BaseViewModelActivity<EditViewmodel>(), TextWatcher, View.OnClickListener,
    SeekBar.OnSeekBarChangeListener {
    override val viewModelClass: Class<EditViewmodel>
        get() = EditViewmodel::class.java
    override val isFragmentActivity: Boolean
        get() = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_edit)
        edit_view.addTextChangedListener(this)
        viewModel.initData(this)
        //监听视图树的布局改变(弹出/隐藏软键盘会触发)
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(viewModel.keyboardOnGlobalChangeListener)
        viewModel.listenEventSettings(this)//监听事件的设置

    }


    override fun onDestroy() {
        super.onDestroy()
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(viewModel.keyboardOnGlobalChangeListener)
    }



    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        viewModel.afterTextChanged(this,s)
    }

    override fun onClick(v: View) {
        viewModel.onClick(this,v)
    }

    //seekBar方法
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        viewModel.onProgressChanged(this,progress)
    }


    //seekBar方法
    override fun onStartTrackingTouch(seekBar: SeekBar) {
        viewModel.onStartTrackingTouch()
    }



    //seekBar方法
    override fun onStopTrackingTouch(seekBar: SeekBar) {
        viewModel.onStopTrackingTouch(this)
    }

    companion object {
        const val KEYBOARD_TOP_VIEW_FIRST_TIP_NULL = "www."
        const val KEYBOARD_TOP_VIEW_SECOND_TIP_NULL = "m."
        const val KEYBOARD_TOP_VIEW_THIRD_TIP_NULL = "wap."
        const val KEYBOARD_TOP_VIEW_FOURTH_TIP_NULL = ".cn"
        const val KEYBOARD_TOP_VIEW_FIRST_TIP = "."
        const val KEYBOARD_TOP_VIEW_SECOND_TIP = "/"
        const val KEYBOARD_TOP_VIEW_THIRD_TIP = ".com"
        const val KEYBOARD_TOP_VIEW_FOURTH_TIP = ".cn"
    }
}