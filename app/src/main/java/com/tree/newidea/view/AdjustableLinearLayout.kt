package com.tree.newidea.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout

/**
 * Created by Tree on 2019/8/16 11:08
 */
class AdjustableLinearLayout : LinearLayout {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )




    fun setSelfWith(i: Int) {
        left =  mLeft - i
        Log.d("jijij",left.toString())

    }

    var mLeft:Int = 0

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mLeft = l
    }

}
