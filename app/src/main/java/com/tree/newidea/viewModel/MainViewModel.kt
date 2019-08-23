package com.tree.newidea.viewModel

import android.view.View
import com.tree.newidea.adapter.MusicViewPagerAdapter
import com.tree.newidea.api.RECOMMENDED_MUSIC_URI
import com.tree.newidea.bean.RecommendedMusicBean
import kotlinx.android.synthetic.main.app_activity_main.*
import retrofit2.Call
import retrofit2.Response
import android.animation.*
import android.view.animation.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.tree.common.viewmodel.BaseViewModel
import com.tree.newidea.activity.MainActivity
import kotlinx.android.synthetic.main.app_main_activity_content.*
import android.util.DisplayMetrics
import com.tree.newidea.util.*
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.billy.android.swipe.SmartSwipe
import com.billy.android.swipe.consumer.StretchConsumer
import com.tree.common.utils.LogUtils
import com.tree.newidea.adapter.ToDoListRecycleViewAdapter
import kotlinx.android.synthetic.main.app_main_below_layer.*
import android.content.Intent
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tree.newidea.activity.EditActivity
import com.tree.newidea.activity.MarkDownActivity
import com.tree.newidea.adapter.SidebarRecycleViewAdapter
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
                activity.ll_bar.visibility = View.GONE
                val headAnim = headExpansionAnimation(true)
                headAnim?.start()
                StatusBarUtil.setStatusBarDarkTheme(activity, false)
                StatusBarUtil.setStatusBarColor(
                    activity,
                    activity.resources.getColor(com.tree.newidea.R.color.homeHeadColor, null)
                )
            } else {
                activity.ll_bar.visibility = View.VISIBLE
                StatusBarUtil.setStatusBarDarkTheme(activity, true)
            }
            rv_to_do_list.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rv_to_do_list.adapter = ToDoListRecycleViewAdapter()

            srv_main_sidebar.layoutManager = LinearLayoutManager(this)
            note?.let {
                srv_main_sidebar.adapter = SidebarRecycleViewAdapter(it)
            }
            //SlidingPaneLayout阴影设置
            dl_main.sliderFadeColor = 0x00ffffff


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


//            ll_no_notebook_tips.setOnClickListener {
//
//            }
        }
    }
}
