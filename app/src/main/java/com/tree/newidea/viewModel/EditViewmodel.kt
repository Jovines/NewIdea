package com.tree.newidea.viewModel

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.billy.android.swipe.SmartSwipeWrapper
import com.billy.android.swipe.SwipeConsumer
import com.billy.android.swipe.consumer.SlidingConsumer
import com.billy.android.swipe.listener.SimpleSwipeListener
import com.tree.common.viewmodel.BaseViewModel
import com.tree.newidea.R
import com.tree.newidea.activity.EditActivity
import com.tree.newidea.activity.MarkDownActivity
import com.tree.newidea.adapter.SkidTopAdapter
import com.tree.newidea.bean.NotepadBean
import com.tree.newidea.util.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_activity_edit.*
import kotlinx.android.synthetic.main.app_activity_mark_down.*
import kotlinx.android.synthetic.main.app_activity_undone.*
import kotlinx.android.synthetic.main.app_skid_edit_top.*
import kotlinx.android.synthetic.main.app_skid_edit_top.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.abs

/**
 * Created by Tree on 2019/8/18 20:59
 */
class EditViewmodel : BaseViewModel() {


    lateinit var keyboardOnGlobalChangeListener: KeyboardOnGlobalChangeListener

    private var mIsSoftKeyBoardShowing = false


    var isSkidTopOpen: Boolean = false


    var textBean: NotepadBean.DatesBean.TextsBean? = null


    fun initData(editActivity: EditActivity) {
        editActivity.apply {
            StatusBarUtil.setStatusBarDarkTheme(editActivity, true)
            keyboardOnGlobalChangeListener = KeyboardOnGlobalChangeListener(editActivity)

        }
    }

    fun listenEventSettings(editActivity: EditActivity) {
        editActivity.apply {
            //监听视图树的布局改变(弹出/隐藏软键盘会触发)
            window.decorView.viewTreeObserver.addOnGlobalLayoutListener(
                keyboardOnGlobalChangeListener
            )

            edit_text.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })


            edit_fab.setOnClickListener {
                if (edit_activity_top_search_normal_tilte.editableText.toString().isNotEmpty() || edit_text.editableText.toString().isNotEmpty()) {

                    val simpleDateFormat = SimpleDateFormat(timeFormat1)// HH:mm:ss
                    //获取当前时间
                    val date = Date(System.currentTimeMillis())
                    //初始化一个textBean
                    if (textBean == null) {
                        textBean = NotepadBean.DatesBean.TextsBean()
                    }

                    textBean?.apply textBean@{
                        this.title = edit_activity_top_search_normal_tilte.editableText.toString()
                        this.text = edit_text.editableText.toString()
                        //获取当前时间
                        this.date =
                            SimpleDateFormat(timeFormat2).format(Date(System.currentTimeMillis()))


                        timelineList?.add(this@textBean)
                        timelineList?.let {
                            asynSaveSerializationObject(editActivity, "timelineList", it)
                        }
                        var isFind = false
                        note?.let { notepadBean ->
                            notepadBean.dates.forEach note@{
                                if (it.date == simpleDateFormat.format(date)) {
                                    it.texts.add(this@textBean)
                                    isFind = true
                                }
                            }
                            if (!isFind) {
                                notepadBean.dates.add(
                                    NotepadBean.DatesBean(
                                        simpleDateFormat.format(
                                            date
                                        )
                                    ).apply {
                                        texts.add(this@textBean)
                                    })
                            }
                            asynSaveSerializationObject(editActivity, "note", notepadBean)
                        }
                    }

                    lottie_edit_yes.addAnimatorListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            maskFrameLayout?.visibility = View.GONE
                            editActivity.finish()
                        }
                    })
                    lottie_edit_yes.visibility = View.VISIBLE
                    lottie_edit_yes.speed = 1.5f
                    lottie_edit_yes.playAnimation()
                }else{
                    Toast.makeText(this,"你还没有输入哦",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setSkipTop(activity: EditActivity) {
        activity.apply activity@{
            val searchNoteList = mutableListOf<NotepadBean.DatesBean.TextsBean>()
            var view: View? = null
            var isOpenKey = true
            smart_swipe_edit.addConsumer(SlidingConsumer())
                .setTopDrawerView(View.inflate(this, R.layout.app_skid_edit_top, null).apply View@{
                    view = this
                    vp_skid_edit_top.layoutManager = LinearLayoutManager(this@activity)
                    vp_skid_edit_top.adapter = SkidTopAdapter(
                        searchNoteList,
                        mark_activity_top_search_normal,
                        mark_activity_top_search_m,
                        vp_skid_edit_top,
                        skid_top_container,
                        et_search_mark
                    )
                    et_search_mark.setOnEditorActionListener { v, actionId, _ ->
                        edit_activity_top_search_normal_tilte.visibility = View.GONE
                        edit_text.visibility = View.GONE
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            searchNoteList.clear()
                            vp_skid_edit_top.visibility = View.INVISIBLE
                            lottie_mark_loading.visibility = View.VISIBLE
                            lottie_mark_loading.playAnimation()
                            lottie_no_find.visibility = View.GONE
                            lottie_no_find.pauseAnimation()
                            Observable.create<Any> {
                                if (note != null) {
                                    note?.let {
                                        it.dates.forEach { it2 ->
                                            it2.texts.forEach { it3 ->
                                                val ma =
                                                    Pattern.compile("${this@View.et_search_mark.editableText}")
                                                        .matcher(it3.text)
                                                if (ma.find()) {
                                                    searchNoteList.add(it3)
                                                }
                                            }
                                        }
                                    }
                                    Thread.sleep(1000)
                                    it.onNext(searchNoteList)
                                } else {
                                    Toast.makeText(
                                        this@activity,
                                        "哎呀，好像发生了意料之外的错误",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    lottie_no_find.visibility = View.VISIBLE
                                    lottie_no_find.playAnimation()
                                }
                            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    (vp_skid_edit_top.adapter as SkidTopAdapter).notifyDataSetChanged()
                                    edit_activity_top_search_normal_tilte.visibility = View.VISIBLE
                                    edit_text.visibility = View.VISIBLE
                                    vp_skid_edit_top.visibility = View.VISIBLE
                                    lottie_mark_loading.pauseAnimation()
                                    lottie_mark_loading.visibility = View.GONE
                                    if (searchNoteList.size == 0) {
                                        lottie_no_find.visibility = View.VISIBLE
                                        lottie_no_find.playAnimation()
                                        openKeybord(et_search_mark, this@activity)
                                    }
                                }
                        }
                        false
                    }
                    et_search_mark.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(p0: Editable?) {

                        }

                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            if (et_search_mark.editableText.isEmpty()) {
                                (vp_skid_edit_top.adapter as SkidTopAdapter).notifyDataSetChanged()
                            }
                        }

                    })
                })
                .setScrimColor(0x2F000000)
                .addListener(object : SimpleSwipeListener() {
                    override fun onSwipeOpened(
                        wrapper: SmartSwipeWrapper?,
                        consumer: SwipeConsumer?,
                        direction: Int
                    ) {
                        isSkidTopOpen = true
                        if (isOpenKey) {
                            isOpenKey = false
                            view?.let {
                                if (mark_activity_top_search_normal.visibility == View.GONE && mark_activity_top_search_m.visibility == View.GONE) {
                                    openKeybord(it.et_search_mark, this@activity)
                                }
                            }
                        }
                    }

                    override fun onSwipeStateChanged(
                        wrapper: SmartSwipeWrapper?,
                        consumer: SwipeConsumer?,
                        state: Int,
                        direction: Int,
                        progress: Float
                    ) {
                        isSkidTopOpen = true
                        view?.let {
                            closeKeybord(it.et_search_mark, this@activity)
                            closeKeybord(edit_text, this@activity)
                            closeKeybord(edit_activity_top_search_normal_tilte, this@activity)
                        }
                    }

                    override fun onSwipeClosed(
                        wrapper: SmartSwipeWrapper?,
                        consumer: SwipeConsumer?,
                        direction: Int
                    ) {
                        isOpenKey = true
                        isSkidTopOpen = false

                    }
                })
        }

    }


    inner class KeyboardOnGlobalChangeListener(val activity: EditActivity) :
        ViewTreeObserver.OnGlobalLayoutListener {
        //监听视图树的变化
        override fun onGlobalLayout() {
            val rect = Rect()
            // 获取当前页面窗口的显示范围
            activity.window.decorView.getWindowVisibleDisplayFrame(rect)
            val keyboardHeight = phoneHeight - (rect.bottom - rect.top) // 输入法的高度
            if (abs(keyboardHeight) > phoneHeight / 5) {
                mIsSoftKeyBoardShowing = true // 超过屏幕五分之一则表示弹出了输入法
                //Todo 展开工具栏
            } else {
                if (mIsSoftKeyBoardShowing) {
                    //todo 关闭工具栏
                }
                mIsSoftKeyBoardShowing = false
            }
        }
    }

}
