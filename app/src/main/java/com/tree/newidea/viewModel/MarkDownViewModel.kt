package com.tree.newidea.viewModel

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupWindow
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.tree.common.utils.extensions.dp2px
import com.tree.common.viewmodel.BaseViewModel
import com.tree.newidea.R
import com.tree.newidea.activity.MarkDownActivity
import com.tree.newidea.adapter.MarkdownViewPagerAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_activity_edit.container
import kotlinx.android.synthetic.main.app_activity_mark_down.*
import java.lang.Exception
import java.util.regex.Pattern
import kotlin.math.abs
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.tree.newidea.bean.NotepadBean
import com.tree.newidea.util.*
import kotlinx.android.synthetic.main.app_soft_keyboard_top_tool_view.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Tree on 2019/8/18 20:59
 */
class MarkDownViewModel : BaseViewModel() {

    lateinit var keyboardOnGlobalChangeListener: KeyboardOnGlobalChangeListener


    private var mInputViewIsNull = true


    private var mSoftKeyboardTopPopupWindow: PopupWindow? = null
    private var mIsSoftKeyBoardShowing = false


    var savaText: String = "编辑"

    private fun onClick(activity: MarkDownActivity, v: TextView) {
        val txt = v.text.toString()
        insertTextToEditText(activity, txt)
    }


    //弹出的popupView
    @SuppressLint("InflateParams")
    fun showUtilBar(activity: MarkDownActivity, x: Int, y: Int, view: View) {
        view.apply {

            fl_util_bar.translationY = -y.toFloat()
            fl_util_bar.visibility = View.VISIBLE

//            updateKeyboardTopViewTips(activity, TextUtils.isEmpty(editText.text))
        }
    }


    //关闭popupWindow
    private fun closeUtilBar(activity: MarkDownActivity, keyboardHeight: Int, view: View?) {
        activity.apply {
            val anim = ValueAnimator.ofFloat(keyboardHeight.toFloat(), 0f)
            view?.apply {
                fl_util_bar.visibility = View.GONE
                fl_util_bar.translationY = 0f
            }
            editText.apply {
                layoutParams.height = vp_mark_down.bottom - vp_mark_down.top
            }
            vp_mark_down.requestLayout()
        }
    }

    private var textLength = 0

    //当editText中的文字发生变化
    fun afterTextChanged(activity: MarkDownActivity, s: Editable) {
        val text = s.toString()
        activity.apply {
            if (TextUtils.isEmpty(text)) return
            editText.apply {
                if (selectionStart != 0 && text[selectionStart - 1].toString() == "\n") {
                    try {
                        if (text.substring(selectionStart, selectionStart + 3) == "***") {
                            editableText.replace(selectionStart - 1, selectionEnd, "")
                            setSelection(selectionStart + 3)
                            return@apply
                        }
                    } catch (e: Exception) {
                    }
                    try {
                        if (text.substring(selectionStart, selectionStart + 2) == "~~"
                            || text.substring(selectionStart, selectionStart + 2) == "**"
                        ) {
                            editableText.replace(selectionStart - 1, selectionEnd, "")
                            setSelection(selectionStart + 2)
                            return@apply
                        }
                    } catch (e: Exception) {
                    }
                    try {
                        if (text.substring(selectionStart, selectionStart + 1) == "*") {
                            editableText.replace(selectionStart - 1, selectionEnd, "")
                            setSelection(selectionStart + 1)
                            return@apply

                        }
                    } catch (e: Exception) {
                    }

                    try {
                        if (text.substring(selectionStart, selectionStart + 5) == """]("")""") {
                            editableText.replace(selectionStart - 1, selectionEnd, "")
                            setSelection(selectionStart + 2)
                            return@apply
                        }
                    } catch (e: Exception) {
                    }
                    try {
                        if (text.substring(selectionStart, selectionStart + 3) == """"")""") {
                            editableText.replace(selectionStart - 1, selectionEnd, "")
                            setSelection(selectionStart + 1)
                            return@apply
                        }
                    } catch (e: Exception) {
                    }
                    try {
                        if (text.substring(selectionStart, selectionStart + 2) == """")""") {
                            editableText.replace(selectionStart - 1, selectionEnd, "")
                            setSelection(selectionStart + 2)
                            return@apply
                        }
                    } catch (e: Exception) {
                    }

                    try {
                        if (text.substring(selectionStart, selectionStart + 3) == """]()""") {
                            editableText.replace(selectionStart - 1, selectionEnd, "")
                            setSelection(selectionStart + 2)
                            return@apply
                        }
                    } catch (e: Exception) {
                    }

                    try {
                        if (text.substring(selectionStart, selectionStart + 1) == """)""") {
                            editableText.replace(selectionStart - 1, selectionEnd, "")
                            setSelection(selectionStart + 1)
                            return@apply
                        }
                    } catch (e: Exception) {
                    }
                    try {
                        if (textLength < text.length) {
                            val mText =
                                text.substring(0, selectionStart).substringBeforeLast("\n").substringAfterLast("\n")
                            if (Pattern.matches(">+ .+", mText)) {
                                val matcher = Pattern.compile(">+").matcher(mText)
                                matcher.lookingAt()
                                editableText.replace(selectionStart, selectionEnd, """${matcher.group()} """)
                                return@apply
                            } else if (Pattern.matches(">+\\s*", mText)) {
                                val matcher = Pattern.compile(">+\\s*").matcher(mText)
                                matcher.matches()
                                val size = matcher.group().length
                                editableText.replace(selectionStart - size - 1, selectionStart, "")
                                return@apply
                            }
                        }
                    } catch (e: Exception) {
                    }
                    try {
                        if (textLength < text.length) {
                            val mText =
                                text.substring(0, selectionStart).substringBeforeLast("\n").substringAfterLast("\n")
                            if (Pattern.matches("\\s*\\* .+", mText)) {
                                val ma = Pattern.compile("\\s*\\*").matcher(mText)
                                ma.lookingAt()
                                editableText.replace(selectionStart, selectionEnd, "${ma.group()} ")
                            } else if (Pattern.matches("\\*\\s*", mText)) {
                                val matcher = Pattern.compile("\\*\\s*").matcher(mText)
                                matcher.matches()
                                val size = matcher.group().length
                                editableText.replace(selectionStart - size - 1, selectionStart, "")
                            }
                        }
                    } catch (e: Exception) {
                    }

                }
            }
        }
        textLength = text.length
        if (textLength != 0) {
            savaText = "保\n存"
            activity.tv_save.text = savaText
        } else {
            savaText = "编\n辑"
            activity.tv_save.text = savaText
        }
//        updateKeyboardTopViewTips(activity, TextUtils.isEmpty(text))
    }


//    //更新工具栏中的显示
//    private fun updateKeyboardTopViewTips(activity: MarkDownActivity, isNull: Boolean) {
//        if (mInputViewIsNull == isNull) {
//            return
//        }
//
//        activity.apply {
//            if (isNull) {
//                keyboard_top_view_first_txt.text =
//                    MarkDownActivity.KEYBOARD_TOP_VIEW_FIRST_TIP_NULL
//                keyboard_top_view_second_txt1.text =
//                    MarkDownActivity.KEYBOARD_TOP_VIEW_SECOND_TIP_NULL
//                keyboard_top_view_third_txt.text =
//                    MarkDownActivity.KEYBOARD_TOP_VIEW_THIRD_TIP_NULL
//                keyboard_top_view_fourth_txt.text =
//                    MarkDownActivity.KEYBOARD_TOP_VIEW_FOURTH_TIP_NULL
//                mInputViewIsNull = true
//            } else {
//                keyboard_top_view_first_txt.text = MarkDownActivity.KEYBOARD_TOP_VIEW_FIRST_TIP
//                keyboard_top_view_second_txt1.text = MarkDownActivity.KEYBOARD_TOP_VIEW_SECOND_TIP
//                keyboard_top_view_third_txt.text = MarkDownActivity.KEYBOARD_TOP_VIEW_THIRD_TIP
//                keyboard_top_view_fourth_txt.text = MarkDownActivity.KEYBOARD_TOP_VIEW_FOURTH_TIP
//                mInputViewIsNull = false
//            }
//        }
//    }


    //    向editText中插入文本
    private fun insertTextToEditText(activity: MarkDownActivity, txt: String) {
        activity.apply {
            if (TextUtils.isEmpty(txt)) return
            val start = editText.selectionStart
            val end = editText.selectionEnd
            val editable = editText.editableText//获取EditText的文字
            if (start < 0 || start >= editable.length) {
                editable.append(txt)
            } else {
                editable.replace(start, end, txt)//光标所在位置插入文字
            }
            when (txt) {
                "****" -> editText.setSelection(editText.selectionStart - 2, editText.selectionEnd - 2)
                "**" -> editText.setSelection(editText.selectionStart - 1, editText.selectionEnd - 1)
                "******" -> editText.setSelection(editText.selectionStart - 3, editText.selectionEnd - 3)
                "~~~~" -> editText.setSelection(editText.selectionStart - 2, editText.selectionEnd - 2)
                """![]("")""" -> editText.setSelection(editText.selectionStart - 5, editText.selectionEnd - 5)
                """[]("")""" -> editText.setSelection(editText.selectionStart - 5, editText.selectionEnd - 5)
                "[]()" -> editText.setSelection(editText.selectionStart - 3, editText.selectionEnd - 3)
                "![]()" -> editText.setSelection(editText.selectionStart - 3, editText.selectionEnd - 3)
            }
        }
    }


    fun listenEventSettings(activity: MarkDownActivity) {
        activity.apply {
            mark_down_back.setOnClickListener {
                maskFrameLayout.visibility = View.GONE
                finish()
            }

            var mPosition = 0
            vp_mark_down.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                    when (state) {
                        1 -> {
                            val anim = AnimationUtils.loadAnimation(this@apply, R.anim.app_save_button_hide)
                            tv_save.startAnimation(anim)


                        }
                        0 -> {
                            val anim = AnimationUtils.loadAnimation(this@apply, R.anim.app_save_button_show)
                            tv_save.startAnimation(anim)
                            anim.setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationRepeat(p0: Animation?) {

                                }

                                override fun onAnimationEnd(p0: Animation?) {
                                }

                                override fun onAnimationStart(p0: Animation?) {
                                    when (mPosition) {
                                        0 -> tv_save.text = "编\n辑"
                                        1 -> tv_save.text = "预\n览"
                                    }
                                }

                            })
                        }

                    }
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    mPosition = position

                }

                override fun onPageSelected(position: Int) {

                }

            })


            tv_save.setOnClickListener {
                if (savaText != "编\n辑") {
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")// HH:mm:ss
                    //获取当前时间
                    val date = Date(System.currentTimeMillis())
                    val text = NotepadBean.DatesBean.TextsBean().apply {
                        val ma = Pattern.compile(" *#+ \\w+.*").matcher(editText.editableText.toString())
                        if (ma.lookingAt()) {
                            val a = ma.group()
                            title = ma.group().replace(Regex(" *#+ "), "").replace(Regex("\\s*"), "")
                        }
                        text = editText.editableText.toString()
                        //获取当前时间
                        this.date = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Date(System.currentTimeMillis()))
                    }
                    var isFind = false
                    note.dates.forEach {
                        if (it.date == simpleDateFormat.format(date)) {
                            it.texts.add(text)
                            isFind = true
                        }
                    }
                    if (!isFind) {
                        note.dates.add(NotepadBean.DatesBean(simpleDateFormat.format(date)).apply {
                            texts.add(text)
                        })
                    }
                }
            }

        }
    }

    private var isTips = true

    fun initData(editActivity: MarkDownActivity) {
        editActivity.apply {
            keyboardOnGlobalChangeListener = KeyboardOnGlobalChangeListener(editActivity)
            StatusBarUtil.setStatusBarDarkTheme(this, true)
            vp_mark_down.adapter = MarkdownViewPagerAdapter(this)
            Observable.create<Int> {
                for (i in 0..Int.MAX_VALUE) {
                    Thread.sleep(4000)
                    if (isTips) {
                        it.onNext(i)
                    }

                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                when (it % 2) {
                    0 -> {
                        if (editText.editableText.isNotEmpty()) {
                            tv_mark_title.text = "左滑可以预览哦"
                        }
                    }
                    1 -> {
                        tv_mark_title.text = "MarkDown"
                    }
                }
            }
        }
    }


    inner class KeyboardOnGlobalChangeListener(val activity: MarkDownActivity) :
        ViewTreeObserver.OnGlobalLayoutListener {

        lateinit var view: View
        var isFirst = true

        //监听视图树的变化
        override fun onGlobalLayout() {
            val rect = Rect()
            // 获取当前页面窗口的显示范围
            activity.apply {
                window.decorView.getWindowVisibleDisplayFrame(rect)
                val keyboardHeight = container.bottom - rect.bottom// 输入法的高度
                editText.apply {
                    layoutParams.height =
                        vp_mark_down.bottom - vp_mark_down.top - keyboardHeight - dip2px(40f) - dp2px(15f)
                }
                vp_mark_down.requestLayout()
                if (abs(keyboardHeight) > phoneHeight / 5) {
                    mIsSoftKeyBoardShowing = true // 超过屏幕五分之一则表示弹出了输入法
                    if (isFirst) {
//                    初始化工具栏
                        view = View.inflate(this, R.layout.app_soft_keyboard_top_tool_view, null).apply {
                            fun sliding(textView: TextView) {
                                onClick(activity, textView)
                                tv_mark_title.text = "MarkDown"
                            }

                            keyboard_top_view_first_txt.slidingPositionMonitoring = { sliding(it) }
                            keyboard_top_view_second_txt.slidingPositionMonitoring = { sliding(it) }
                            keyboard_top_view_third_txt.slidingPositionMonitoring = { sliding(it) }
                            keyboard_top_view_fourth_txt.slidingPositionMonitoring = { sliding(it) }

                            keyboard_top_view_first_txt.selected = {
                                when (it) {
                                    keyboard_top_view_first_txt1 -> tv_mark_title.text = "一级标题"
                                    keyboard_top_view_first_txt2 -> tv_mark_title.text = "二级标题"
                                    keyboard_top_view_first_txt3 -> tv_mark_title.text = "三级标题"
                                    keyboard_top_view_first_txt4 ->
                                        tv_mark_title.text = "四级标题"
                                    keyboard_top_view_first_txt5 -> tv_mark_title.text = "五级标题"
                                    keyboard_top_view_first_txt6 -> tv_mark_title.text = "六级标题"
                                }
                            }
                            keyboard_top_view_second_txt.selected = {
                                when (it) {
                                    keyboard_top_view_second_txt1 -> tv_mark_title.text = "一级引用"
                                    keyboard_top_view_second_txt2 -> tv_mark_title.text = "二级引用"
                                    keyboard_top_view_second_txt3 -> tv_mark_title.text = "三级引用"
                                    keyboard_top_view_second_txt4 -> tv_mark_title.text = "四级引用"
                                    keyboard_top_view_second_txt5 -> tv_mark_title.text = "五级引用"
                                    keyboard_top_view_second_txt6 -> tv_mark_title.text = "六级引用"
                                }
                            }

                            keyboard_top_view_third_txt.selected = {
                                when (it) {
                                    keyboard_top_view_third_txt1 -> tv_mark_title.text = "粗体字"
                                    keyboard_top_view_third_txt2 -> tv_mark_title.text = "斜体字"
                                    keyboard_top_view_third_txt3 -> tv_mark_title.text = "斜体加粗"
                                    keyboard_top_view_third_txt4 -> tv_mark_title.text = "删除线"
                                    keyboard_top_view_third_txt5 -> tv_mark_title.text = "无序列表"
                                }
                            }
                            keyboard_top_view_fourth_txt.selected = {
                                when (it) {
                                    keyboard_top_view_fourth_txt1 -> tv_mark_title.text = "插入链接"
                                    keyboard_top_view_fourth_txt2 -> tv_mark_title.text = "插入有标题的链接"
                                    keyboard_top_view_fourth_txt3 -> tv_mark_title.text = "插入图片"
                                    keyboard_top_view_fourth_txt4 -> tv_mark_title.text = "插入有标题的图片"
                                }
                            }

                        }
                        fl_mark_container.addView(view)
                        isFirst = false
                    }

                    showUtilBar(activity, phoneWidth / 2, keyboardHeight, view)
                } else {
                    if (mIsSoftKeyBoardShowing) {
                        closeUtilBar(activity, keyboardHeight, view)
                    }
                    mIsSoftKeyBoardShowing = false
                }
            }
        }
    }
}