package com.tree.newidea.activity

import android.os.Bundle
import com.tree.common.ui.BaseActivity
import com.tree.newidea.R

class UndoneActivity : BaseActivity() {
    override val isFragmentActivity: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_undone)
    }
}
