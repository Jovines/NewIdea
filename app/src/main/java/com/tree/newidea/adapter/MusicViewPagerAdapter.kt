package com.tree.newidea.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tree.newidea.R
import com.tree.newidea.bean.RecommendedMusicBean
import com.tree.newidea.util.Music
import kotlinx.android.synthetic.main.app_view_pager_page_music.view.*

/**
 * Created by Tree on 2019/8/15 19:30
 */
class MusicViewPagerAdapter (viewPager: ViewPager,bean:RecommendedMusicBean): PagerAdapter() {

    val songmid = bean.songlist.map { it.data.songmid }
    val songname = bean.songlist.map { it.data.songname }
    val hashMap = mutableMapOf<Int,View>()

    init {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                Music.paly(songmid[position])
            }
        })
    }

    override fun getCount(): Int {
        return songname.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }



    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        hashMap[position] = View.inflate(container.context, R.layout.app_view_pager_page_music, null).apply {
            tv_music_tilte.text = songname[position]
        }
        container.addView(hashMap[position])
        return hashMap[position]!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(hashMap[position])
        hashMap.remove(position)
    }

}
