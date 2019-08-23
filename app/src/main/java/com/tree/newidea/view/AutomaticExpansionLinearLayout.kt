package com.tree.newidea.view

import android.view.animation.Animation


import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.animation.LayoutTransition.CHANGE_APPEARING
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.tree.newidea.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AutomaticExpansionLinearLayout : LinearLayout {
    constructor(context: Context) : super(context) {
        customLayoutTransition()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        customLayoutTransition()

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        customLayoutTransition()

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        customLayoutTransition()

    }


    private val childViewHeight = mutableListOf<Int>()
    private val childViewWight = mutableListOf<Int>()
    private var totalHeight = 0
    private var totalWidth = 0

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        super.onLayout(changed, l, t, r, b)
    }

//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
//        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
//        totalWidth = MeasureSpec.getSize(widthMeasureSpec)
//        totalHeight = MeasureSpec.getSize(heightMeasureSpec)
//
//        for (i in 0 until childCount) {
//            val view = getChildAt(i)
//            val linearLayoutParams = view.layoutParams as LayoutParams
//            val h = view.measuredHeight
//            val w = view.measuredWidth
//            val l = linearLayoutParams.marginStart
//            val r = linearLayoutParams.marginEnd
//            val t = linearLayoutParams.topMargin
//            val b = linearLayoutParams.bottomMargin
//            childViewHeight.add(t + b + h)
//            childViewWight.add(l + r + w)
//        }
//
//        val isMW = widthMode == MeasureSpec.EXACTLY
//        val isMH = heightMode == MeasureSpec.EXACTLY
//
//        setMeasuredDimension(
//            if (isMW)
//                totalWidth
//            else
//                childViewWight[0],
//            if (isMH)
//                totalHeight
//            else
//                childViewHeight[0]
//        )
//    }




    //(x,y)是否在view的区域内
    private fun isTouchPointInView(view: View, x: Int, y: Int): Boolean {
//        val location = IntArray(2)
//        view.getLocationOnScreen(location)
//        val left = location[0]
//        val top = location[1]
//        val right = left + view.measuredWidth
//        val bottom = top + view.measuredHeight



        return (y in view.top..view.bottom && x >= view.left && x <= view.right)
    }

    @SuppressLint("ObjectAnimatorBinding")
    fun customLayoutTransition() {

        layoutTransition = LayoutTransition()

        /**
         * Add Button
         * LayoutTransition.APPEARING
         * 增加一个Button时，设置该Button的动画效果
         */
        val mAnimatorAppearing = ObjectAnimator.ofFloat(null, "translationY", 90.0f, 0.0f)
            .setDuration(layoutTransition.getDuration(LayoutTransition.APPEARING))
        //为LayoutTransition设置动画及动画类型
        layoutTransition.setAnimator(LayoutTransition.APPEARING, mAnimatorAppearing)

        mAnimatorAppearing.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val view = (animation as ObjectAnimator).target as View
                view.rotationY = 0.0f
            }
        })

        /**
         * Add Button
         * LayoutTransition.CHANGE_APPEARING
         * 当增加一个Button时，设置其他Button的动画效果
         */

        val pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1)
        val pvhTop = PropertyValuesHolder.ofInt("top", 0, 1)
        val pvhRight = PropertyValuesHolder.ofInt("right", 0, 1)
        val pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1)
        val mHolderScaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.0f, 1.0f)
        val mHolderScaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.0f, 1.0f)
        val mObjectAnimatorChangeAppearing = ObjectAnimator.ofPropertyValuesHolder(
            this, pvhLeft,
            pvhTop, pvhRight, pvhBottom, mHolderScaleX, mHolderScaleY
        ).setDuration(
            layoutTransition
                .getDuration(CHANGE_APPEARING)
        )
        layoutTransition.setAnimator(CHANGE_APPEARING, mObjectAnimatorChangeAppearing)
        mObjectAnimatorChangeAppearing.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // TODO Auto-generated method stub
                super.onAnimationEnd(animation)
                val view = (animation as ObjectAnimator).getTarget() as View
                view.scaleX = 1f
                view.scaleY = 1f
            }
        })

        /**
         * Delete Button
         * LayoutTransition.DISAPPEARING
         * 当删除一个Button时，设置该Button的动画效果
         */
        val mObjectAnimatorDisAppearing = ObjectAnimator.ofFloat(null, "translationY", 0.0f, 90.0f)
            .setDuration(layoutTransition.getDuration(LayoutTransition.DISAPPEARING))
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, mObjectAnimatorDisAppearing)
        mObjectAnimatorDisAppearing.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // TODO Auto-generated method stub
                super.onAnimationEnd(animation)
                val view = (animation as ObjectAnimator).getTarget() as View
                view.translationY = 0.0f
            }
        })

        /**
         * Delete Button
         * LayoutTransition.CHANGE_DISAPPEARING
         * 当删除一个Button时，设置其它Button的动画效果
         */
        //Keyframe 对象中包含了一个时间/属性值的键值对，用于定义某个时刻的动画状态。
        val mKeyframeStart = Keyframe.ofFloat(0.0f, 0.0f)
        val mKeyframeMiddle = Keyframe.ofFloat(0.5f, 180.0f)
        val mKeyframeEndBefore = Keyframe.ofFloat(0.999f, 360.0f)
        val mKeyframeEnd = Keyframe.ofFloat(1.0f, 0.0f)

        val mPropertyValuesHolder = PropertyValuesHolder.ofKeyframe(
            "translationY",
            mKeyframeStart, mKeyframeMiddle, mKeyframeEndBefore, mKeyframeEnd
        )
        val mObjectAnimatorChangeDisAppearing =
            ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, mPropertyValuesHolder)
                .setDuration(layoutTransition.getDuration(LayoutTransition.CHANGE_DISAPPEARING))
        layoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, mObjectAnimatorChangeDisAppearing)
        mObjectAnimatorChangeDisAppearing.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // TODO Auto-generated method stub
                super.onAnimationEnd(animation)
                val view = (animation as ObjectAnimator).getTarget() as View
                view.translationY = 0.0f
            }
        })
    }

    var isPressing = false

    var textView: TextView? = null

    var anim: Animation = AnimationUtils.loadAnimation(context, R.anim.app_util_selected)


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val detectedUp = event?.action === MotionEvent.ACTION_UP
        if (!GestureDetector(context, MyOnGestureListener()).onTouchEvent(event) && detectedUp) {
            onUp(event)
        }
        return true
    }




    inner class MyOnGestureListener : GestureDetector.OnGestureListener {


        override fun onShowPress(p0: MotionEvent?) {

        }


        //单击
        override fun onSingleTapUp(p0: MotionEvent?): Boolean {

            return true
        }

        @SuppressLint("CheckResult")
        override fun onDown(p0: MotionEvent?): Boolean {
            for (i in 0 until childCount) {
                p0?.let {
                    if (isTouchPointInView(getChildAt(i), p0.x.toInt(), p0.y.toInt())) {
                        if (textView !== getChildAt(i)) {
                            textView = getChildAt(i) as TextView?
                        }

                    }
                }

            }
            textView?.tag = false
            selected?.invoke(textView!!)
            textView?.animation = anim
            anim.start()
            isPressing = true
            Observable.create<View> {
                for (i in childCount - 2 downTo 0) {
                    if (getChildAt(i).visibility == View.GONE) {
                        if (!isPressing) {
                            return@create
                        }
                        it.onNext(getChildAt(i))
                    }
                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                it.visibility = View.VISIBLE
            }

            return true
        }

        override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return true
        }

        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            for (i in 0 until childCount) {
                p1?.let {
                    if (isTouchPointInView(getChildAt(i), p1.x.toInt(), p1.y.toInt())) {
                        if (textView !== getChildAt(i)) {
                            textView?.clearAnimation()
                            textView = getChildAt(i) as TextView?
                            textView?.tag = false
                            selected?.invoke(textView!!)
                            textView?.animation = anim
                            anim.start()
                        }

                    }
                }

            }

            return true

        }

        override fun onLongPress(p0: MotionEvent?) {

        }

    }

    @SuppressLint("CheckResult")
    private fun onUp(event: MotionEvent?) {
        textView?.let {
            it.tag = true
            slidingPositionMonitoring?.invoke(it)
            it.clearAnimation()
        }
        isPressing = false
        Observable.create<View> {
            for (i in 0..childCount - 2) {
                if (getChildAt(i).visibility == View.VISIBLE) {
                    if (isPressing) {
                        return@create
                    }
                    it.onNext(getChildAt(i))
                }
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            it.visibility = View.GONE
        }
    }

    //当滑动完毕，手抬起位置View的监听
    var slidingPositionMonitoring:((TextView)->Unit)? = null

    //当滑倒该view的时候的监听
    var selected:((TextView)->Unit)? = null

}
