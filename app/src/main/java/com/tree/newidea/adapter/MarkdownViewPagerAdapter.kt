package com.tree.newidea.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.tree.newidea.R
import com.tree.newidea.activity.MarkDownActivity
import kotlinx.android.synthetic.main.app_view_pager_page_markdown.view.*
import kotlinx.android.synthetic.main.app_view_pager_page_markdown_normal.view.*

class MarkdownViewPagerAdapter(context:MarkDownActivity) : PagerAdapter() {

    private val pageList = mutableListOf<View>()

    init {
        pageList.add(View.inflate(context, R.layout.app_view_pager_page_markdown_normal, null).apply {
            context.editText = edit_text
        })
        pageList.add(View.inflate(context, R.layout.app_view_pager_page_markdown, null).apply {
            context.markdownView = mark_down_edit_text
        })
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(pageList[position])
        return  pageList[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(pageList[position])
    }




    override fun isViewFromObject(view: View, `object`: Any)= view == `object`
    override fun getCount() = pageList.size
}