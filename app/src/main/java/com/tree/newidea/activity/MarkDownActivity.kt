package com.tree.newidea.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tree.common.ui.BaseActivity
import com.tree.common.ui.BaseViewModelActivity
import com.tree.newidea.R
import com.tree.newidea.viewModel.MarkDownViewModel
import kotlinx.android.synthetic.main.app_activity_mark_down.*

class MarkDownActivity : BaseViewModelActivity<MarkDownViewModel>() {
    override val viewModelClass: Class<MarkDownViewModel>
        get() = MarkDownViewModel::class.java
    override val isFragmentActivity: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_mark_down)
        viewModel.initData(this)
        mark_down_edit_text.setMarkDownText("# 测试")
    }
}
