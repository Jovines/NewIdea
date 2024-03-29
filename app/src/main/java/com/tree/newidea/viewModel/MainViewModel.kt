package com.tree.newidea.viewModel

import android.os.Bundle
import android.view.View
import com.tree.common.ui.BaseViewModelActivity
import com.tree.common.utils.extensions.dp2px
import com.tree.newidea.adapter.MusicViewPagerAdapter
import com.tree.newidea.api.RECOMMENDED_MUSIC_URI
import com.tree.newidea.bean.RecommendedMusicBean
import com.tree.newidea.viewModel.MainViewModel
import kotlinx.android.synthetic.main.app_activity_main.*
import retrofit2.Call
import retrofit2.Response
import android.animation.*
import android.view.animation.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.tree.common.viewmodel.BaseViewModel
import com.tree.newidea.activity.MainActivity
import com.tree.newidea.adapter.MainRecycleViewAdapter
import kotlinx.android.synthetic.main.app_main_activity_content.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.content.Context.WINDOW_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.view.WindowManager
import android.content.Context
import android.util.DisplayMetrics
import androidx.customview.widget.ViewDragHelper
import androidx.drawerlayout.widget.DrawerLayout
import com.tree.newidea.util.*
import java.lang.reflect.Field
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.billy.android.swipe.consumer.SpaceConsumer
import com.billy.android.swipe.SmartSwipe
import com.billy.android.swipe.consumer.StretchConsumer
import com.tree.common.utils.LogUtils
import com.tree.newidea.adapter.ToDoListRecycleViewAdapter
import kotlinx.android.synthetic.main.app_main_below_layer.*
import android.app.AlertDialog
import android.content.Intent
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tree.newidea.R
import com.tree.newidea.activity.EditActivity
import com.tree.newidea.activity.MarkDownActivity
import com.tree.newidea.activity.UndoneActivity
import com.tree.newidea.adapter.SidebarRecycleViewAdapter
import com.tree.newidea.bean.NotepadBean
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_main_dialog_view.view.*
import kotlinx.android.synthetic.main.app_main_sidebar.*
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormat


/**
 * Created by Tree on 2019/8/15 19:38
 */
class MainViewModel : BaseViewModel() {


    /**
     * 是否正在收缩
     */

    var isShrinking = false



    fun hide(activity: MainActivity) {
        activity.apply {

            //收缩 动画
            val anim = headExpansionAnimation(false)

            //bar出现动画
            val barShowAnim = AnimationUtils.loadAnimation(this, com.tree.newidea.R.anim.app_bar_show)

            val itemHideAnim = AnimationUtils.loadAnimation(this, com.tree.newidea.R.anim.app_item_hide)


            //透明度变化动画
            val argbAnim = headColorReduction(false)

            anim?.addListener(object : AnimatorListenerAdapter() {
                //便利类，只要实现需要的方法
                override fun onAnimationEnd(animation: Animator) {
                    isTopOpen = false
                    cl_interactive.apply {
                        layoutParams.height = bottom - top
                    }
                    ll_bar.visibility = View.VISIBLE
                    ll_bar.startAnimation(barShowAnim)
                    StatusBarUtil.setStatusBarDarkTheme(activity, true)

                }
            })


            barShowAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    isShrinking = false
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(anim, argbAnim)
            animatorSet.start()
            adjust_ll_main.startAnimation(itemHideAnim)
            isShrinking = true
        }

    }


    fun show(activity: MainActivity) {

        activity.apply {

            val anim = headExpansionAnimation()

            //bar隐藏动画
            val barHideAnim = AnimationUtils.loadAnimation(this, com.tree.newidea.R.anim.app_bar_hide)

            //item出现的动画
            val itemShowAnim = AnimationUtils.loadAnimation(this, com.tree.newidea.R.anim.app_item_show)


            val argbAnim = headColorReduction()//对背景色颜色进行改变，操作的属性为"backgroundColor",此处必须这样写，不能全小写,后面的颜色为在对应颜色间进行渐变



            anim?.addListener(object : AnimatorListenerAdapter() {
                //便利类，只要实现需要的方法
                override fun onAnimationEnd(animation: Animator) {
                    isTopOpen = true
                    cl_interactive.apply {
                        layoutParams.height = bottom - top
                    }

                    StatusBarUtil.setStatusBarDarkTheme(activity, false)
                    isShrinking = false
                }
            })

            argbAnim?.addListener(object : AnimatorListenerAdapter() {
                //便利类，只要实现需要的方法
                override fun onAnimationEnd(animation: Animator) {
                    adjust_ll_main.visibility = View.VISIBLE
                    adjust_ll_main.startAnimation(itemShowAnim)
                }
            })

            barHideAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    ll_bar.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(argbAnim, anim)
            adjust_ll_main.visibility = View.GONE
            animatorSet.start()
            ll_bar.startAnimation(barHideAnim)
            isShrinking = true
        }

    }

    private fun MainActivity.headColorReduction(isReduction: Boolean = true): ObjectAnimator? {
        //透明度变化动画
        val argbAnim = ObjectAnimator.ofInt(
            cl_interactive,
            "backgroud",
            if (isReduction) resources.getColor(
                com.tree.newidea.R.color.windowBackground,
                null
            ) else resources.getColor(
                com.tree.newidea.R.color.homeHeadColor, null
            ),
            if (isReduction) resources.getColor(com.tree.newidea.R.color.homeHeadColor, null) else resources.getColor(
                com.tree.newidea.R.color.windowBackground,
                null
            )
        )//对背景色颜色进行改变，操作的属性为"backgroundColor",此处必须这样写，不能全小写,后面的颜色为在对应颜色间进行渐变
        argbAnim.setEvaluator(ArgbEvaluator())//如果要颜色渐变必须要ArgbEvaluator，来实现颜色之间的平滑变化，否则会出现颜色不规则跳动
        argbAnim.duration = headTransparencyChangeTime
        return argbAnim
    }

    private fun MainActivity.headExpansionAnimation(isOPen: Boolean = true): ObjectAnimator? {
        //展开 动画
        val anim =
            ObjectAnimator.ofInt(
                cl_interactive,
                "heightTransform",
                if (isOPen) dip2px(60f) + statusBarHeight else dip2px(300f),
                if (isOPen) dip2px(300f) else dip2px(60f) + statusBarHeight
            )
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = headExpansionAnimationTime
        return anim
    }


    fun initData(activity: MainActivity) {
        activity.apply {
            if (isTopOpen) {
                ll_bar.visibility = View.GONE
                val headAnim = headExpansionAnimation(true)
                headAnim?.start()
                StatusBarUtil.setStatusBarDarkTheme(activity, false)
                StatusBarUtil.setStatusBarColor(
                    activity,
                    activity.resources.getColor(com.tree.newidea.R.color.homeHeadColor, null)
                )
            } else {
                ll_bar.visibility = View.VISIBLE
                StatusBarUtil.setStatusBarDarkTheme(activity, true)
            }
            rv_to_do_list.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            srv_main_sidebar.layoutManager = LinearLayoutManager(this)
            note?.let {
                srv_main_sidebar.adapter = SidebarRecycleViewAdapter(this,it,srv_main_sidebar)
            }
            //SlidingPaneLayout阴影设置
            dl_main.sliderFadeColor = 0x00ffffff
            rc_main.layoutManager = LinearLayoutManager(this)

            Observable.create<MutableList<NotepadBean.DatesBean.TextsBean>?> {
                timelineList = getObject(this,"timelineList") as MutableList<NotepadBean.DatesBean.TextsBean>?
                timelineList?.forEach { textBean ->
                    if (textBean.isUndo) {
                        todoList?.add(textBean)
                    }
                }
                if (timelineList == null) {
                    timelineList = mutableListOf()
                }

                if (todoList == null) {
                    todoList = mutableListOf()
                }


                it.onNext(timelineList!!)
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                rc_main.adapter = MainRecycleViewAdapter(timelineList,lottie_main)
                rv_to_do_list.adapter = ToDoListRecycleViewAdapter(todoList)

            }

//            momeList = listOf(R.drawable.anger)

        }

    }


    fun getSongData(activity: MainActivity) {
        activity.apply {
            ordinaryApi.getRecommendedSong(RECOMMENDED_MUSIC_URI)
                .enqueue(object : retrofit2.Callback<RecommendedMusicBean> {
                    override fun onFailure(call: Call<RecommendedMusicBean>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<RecommendedMusicBean>,
                        response: Response<RecommendedMusicBean>
                    ) {
                        val a = response.body()
                        response.body()?.let {
                            vp_music.adapter = MusicViewPagerAdapter(activity, vp_music, adjust_ll_main, it)
                        }
                    }

                })
        }
    }


    fun getPhoneScreenData(activity: MainActivity) {
        activity.apply {
            val resourceId = applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = applicationContext.resources.getDimensionPixelSize(resourceId)
            }

            val dm = DisplayMetrics()
            this.windowManager.defaultDisplay.getMetrics(dm)
            phoneWidth = dm.widthPixels
            phoneHeight = dm.heightPixels

        }
    }


    fun listenEventSettings(activity: MainActivity) {
        activity.apply {

            fl_button_add_main_normal.setOnClickListener {
                mask_main.setLocation(
                    it.left.toFloat(),
                    it.right.toFloat(),
                    it.top.toFloat() + resources.getDimension(com.tree.newidea.R.dimen.mainToolBarHeight),
                    it.bottom.toFloat()+ resources.getDimension(com.tree.newidea.R.dimen.mainToolBarHeight),
                    dl_main.bottom.toFloat(),
                    dl_main.right.toFloat(),
                    400
                )
                mask_main.animStart {
                    EventBus.getDefault().postSticky(mask_main)
                    val intent = Intent(this, EditActivity::class.java)
                    startActivity(intent)
                }


            }


            fl_button_add_main_undone.setOnClickListener {
                mask_main.setLocation(
                    it.left.toFloat(),
                    it.right.toFloat(),
                    it.top.toFloat() + resources.getDimension(com.tree.newidea.R.dimen.mainToolBarHeight),
                    it.bottom.toFloat()+ resources.getDimension(com.tree.newidea.R.dimen.mainToolBarHeight),
                    dl_main.bottom.toFloat(),
                    dl_main.right.toFloat(),
                    400
                )
                mask_main.animStart {
                    EventBus.getDefault().postSticky(mask_main)
                    val intent = Intent(this, UndoneActivity::class.java)
                    startActivity(intent)
                }
            }

            ll_no_notebook_tips.setOnClickListener {
                var dialog: AlertDialog? = null
                dialog = AlertDialog.Builder(activity)
                    .setView(View.inflate(activity, R.layout.app_main_dialog_view, null).apply {
                        dialog_m.setOnClickListener {
                            activity.startActivity<MarkDownActivity>()
                            dialog?.dismiss()
                        }
                        dialog_nnormal.setOnClickListener {
                            activity.startActivity<EditActivity>()
                            dialog?.dismiss()

                        }
                    }).show()

//                startActivity<>()
            }


            fl_button_add_main_m.setOnClickListener {
                mask_main.setLocation(
                    it.left.toFloat(),
                    it.right.toFloat(),
                    it.top.toFloat() + resources.getDimension(com.tree.newidea.R.dimen.mainToolBarHeight),
                    it.bottom.toFloat()+ resources.getDimension(com.tree.newidea.R.dimen.mainToolBarHeight),
                    dl_main.bottom.toFloat(),
                    dl_main.right.toFloat(),
                    400
                )
                mask_main.animStart {
                    EventBus.getDefault().postSticky(mask_main)
                    val intent = Intent(this, MarkDownActivity::class.java)
                    startActivity(intent)
                }
            }
            iv_put_away_down.setOnClickListener {
                if (!isShrinking) {
                    if (isTopOpen) {
                        hide(activity)
                    } else {
                        show(activity)
                    }
                }
            }

            cl_interactive.setOnClickListener {
                if (!isShrinking) {
                    if (isTopOpen) {
                        hide(activity)
                    }
                }
            }

            //侧滑时表现为弹性拉伸效果，结束后自动恢复
            SmartSwipe.wrap(dl_main)
                .addConsumer(StretchConsumer())
                .enableVertical() //工作方向：纵向

            //SlidingPaneLayout的滑动监听
            dl_main.setPanelSlideListener(object : SlidingPaneLayout.PanelSlideListener {

                override fun onPanelSlide(arg0: View, v: Float) {
                    LogUtils.d(msg = v.toString())
                    val a = resources.getDimension(
                        com.tree.newidea.R.dimen.sidebarWidth
                    )//这个a是dp,但是取出来直接变成px

                    //剩下的那部分宽
                    val width = phoneWidth - a
                    var size = width / phoneWidth

                    //占位效果
//                    main_content.translationX = -(phoneWidth/2 - width/2)*v
//                    main_content.translationY = ((dl_main.bottom-size* dl_main.bottom)/2f)*v
//                    val deci = DecimalFormat("#.0000")
//                    deci.format( (size-1)*v + 1)
//                    main_content.scaleX =  deci.format( (size-1)*v + 1).toFloat()
//                    main_content.scaleY =  deci.format( (size-1)*v + 1).toFloat()

                    //消失效果
                    main_content.translationX = -(phoneWidth / 2 - width / 2) * v
                    main_content.translationY =
                        ((dl_main.bottom - size * dl_main.bottom) / 2f + size * dl_main.bottom) * v
                    val deci = DecimalFormat("#.0000")
                    deci.format((size - 1) * v + 1)
                    main_content.scaleX = deci.format((size - 1) * v + 1).toFloat()
                    main_content.scaleY = deci.format((size - 1) * v + 1).toFloat()
                }

                override fun onPanelOpened(arg0: View) {
                    iv_put_away_down.pauseAnimation()
                }

                override fun onPanelClosed(arg0: View) {
                    iv_put_away_down.playAnimation()
                }
            })
        }
    }
}
