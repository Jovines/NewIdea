package com.tree.newidea.adapter

import android.animation.ObjectAnimator
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tree.newidea.R
import com.tree.newidea.activity.MainActivity
import com.tree.newidea.bean.RecommendedMusicBean
import com.tree.newidea.util.Music
import com.tree.newidea.util.dip2px
import com.tree.newidea.view.AdjustableLinearLayout
import com.tree.newidea.view.AdjustableViewPager
import kotlinx.android.synthetic.main.app_view_pager_page_music.view.*
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.imageBitmap

/**
 * Created by Tree on 2019/8/15 19:30
 */
class MusicViewPagerAdapter(
    val activity: MainActivity,
    val viewPager: AdjustableViewPager,
    linearLayout: AdjustableLinearLayout,
    bean: RecommendedMusicBean
) : PagerAdapter() {

    val songmidList = bean.playlist.tracks.map { it.id }
    val songname = bean.playlist.tracks.map { it.name }
    val hashMap = mutableMapOf<Int, View>()

    init {
        var isFirst = true
        var mState = 1
        var mPosition=0
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == 0) {
                    hashMap[mPosition - 1]?.visibility = View.GONE
                    hashMap[mPosition + 1]?.visibility = View.GONE
                    hashMap[mPosition]?.apply{
                        visibility = View.VISIBLE
                        startAnimation(AnimationUtils.loadAnimation(activity,R.anim.app_view_show))

                    }
                }else if (state == 1) {
                    viewPager.requestLayout()
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                hashMap[position - 1]?.visibility = View.GONE
                hashMap[position + 1]?.visibility = View.GONE
                mPosition = position
                if (isFirst) {
                    isFirst = !isFirst
                    return
                }
                hashMap[position]?.visibility = View.GONE

            }
            override fun onPageSelected(position: Int) {

            }
        })
    }

    fun coutLinearWide(str: String) = dip2px(35f)


    override fun getCount(): Int {
        return songname.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        hashMap[position] = View.inflate(container.context, R.layout.app_view_pager_page_music, null).apply {
            tv_music_tilte.text = songname[position]
            if (position != viewPager.currentItem) {
                visibility = View.GONE
            }
        }
        container.addView(hashMap[position]?.apply {
            id = position
        })
        return hashMap[position]!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(hashMap[position])
        hashMap.remove(position)
    }

}
