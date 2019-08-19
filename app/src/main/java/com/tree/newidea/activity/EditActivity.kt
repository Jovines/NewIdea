package com.tree.newidea.activity

import android.os.Bundle
import com.tree.common.ui.BaseViewModelActivity
import com.tree.newidea.R
import com.tree.newidea.viewModel.EditViewmodel

class EditActivity : BaseViewModelActivity<EditViewmodel>() {
    override val viewModelClass: Class<EditViewmodel>
        get() = EditViewmodel::class.java
    override val isFragmentActivity: Boolean
        get() = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_edit)
        viewModel.initData(this)
        viewModel.listenEventSettings(this)//监听事件的设置

    }


    override fun onDestroy() {
        super.onDestroy()
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(viewModel.keyboardOnGlobalChangeListener)
    }


}