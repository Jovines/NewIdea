package com.tree.newidea.viewModel

import com.tree.common.viewmodel.BaseViewModel
import com.tree.newidea.activity.MarkDownActivity
import com.tree.newidea.util.StatusBarUtil

/**
 * Created by Tree on 2019/8/19 13:01
 */

class MarkDownViewModel : BaseViewModel() {
    fun initData(activity: MarkDownActivity) {
        activity.apply {
            StatusBarUtil.setStatusBarDarkTheme(activity,true)
        }
    }
}