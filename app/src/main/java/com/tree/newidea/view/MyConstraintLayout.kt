package com.tree.newidea.view

import android.content.Context
import android.graphics.Color
import android.text.style.LineHeightSpan
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.app_activity_main.*
import android.graphics.Color.parseColor
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import com.tree.newidea.activity.MainActivity
import com.tree.newidea.util.StatusBarUtil
import com.tree.newidea.util.dip2px


/**
 * Created by Tree on 2019/8/16 18:41
 */
class MyConstraintLayout : ConstraintLayout {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    var mBottom: Int = 0

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mBottom = bottom
    }

    fun setHeightTransform(height: Int) {
        bottom = height

    }

    fun setBackgroud(rgb:Int) {
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(0f,0f,0f,0f,0f,0f, dip2px(80f).toFloat(),dip2px(80f).toFloat())
        drawable.setColor(rgb)
        background = drawable
        StatusBarUtil.setStatusBarColor(context as MainActivity,rgb)

    }




}
