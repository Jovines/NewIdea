package com.tree.newidea.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Size
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import java.time.Duration
import kotlin.math.min

class MaskFrameLayout : FrameLayout {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
    }

    fun setShow(i: Float) {
        scaleX = (i/1000)*(1-mSize)+mSize
        scaleY = (i/1000)*(1-mSize)+mSize
        translationX = (1-i/1000)*startTranslateX
        translationY = (1-i/1000)*startTranslateY
    }

    lateinit var  anim:ObjectAnimator

    private var mSize: Float = 0f

    private var startTranslateY = 0f

    private var startTranslateX = 0f


    //传入相对于屏幕的l,r,t,b
    fun setLocation(l:Float,r:Float,t:Float,b:Float,rootViewH:Float,rootViewW:Float,duration:Long){
        val sizeH = (b-t)/rootViewH
        val sizeW = (r-l)/rootViewW
        mSize = min(sizeH,sizeW)
        startTranslateY = (b+t)/2 - rootViewH/2
        startTranslateX = (r+l)/2 - rootViewW/2
        anim = ObjectAnimator.ofFloat(this, "show", 0f, 1000f)
        anim.interpolator = AccelerateDecelerateInterpolator ()
        anim.duration = duration
    }

    fun animStart(lambda:()->Unit) {
        visibility = View.VISIBLE
        anim.start()
        anim.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(p0: Animator?) {

            }
            override fun onAnimationEnd(p0: Animator?) {
                lambda()
            }
            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

        })
    }

}
