package com.tree.newidea.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tree.newidea.R
import com.tree.newidea.bean.NotepadBean
import kotlinx.android.synthetic.main.app_recycle_item_notebook.view.*
import kotlinx.android.synthetic.main.app_recycle_item_notebook_date.view.*
import com.billy.android.swipe.consumer.SpaceConsumer
import com.billy.android.swipe.SmartSwipe


/**
 * Created by Tree on 2019/8/18 13:58
 */
class SidebarRecycleViewAdapter(private val noteBean: NotepadBean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class TYPE {
        TILTE, CONTENT, BLANK
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE.TILTE.ordinal -> TiltedViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.app_recycle_item_notebook_date,
                    null
                )
            )
            TYPE.CONTENT.ordinal -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.app_recycle_item_notebook,
                    null
                )
            )
            else -> BlankViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_blank, null))
        }
    }


    override fun getItemViewType(position: Int): Int {
        var sum = 0
        for (i in noteBean.dates.size - 1 downTo 0) {
            val lastSum = sum
            if (noteBean.dates[i].texts.size != 0) {
                sum++
                if (position + 1 == sum) {
                    return TYPE.TILTE.ordinal
                }
                val a = sum
                sum += noteBean.dates[i].texts.size
                if (position + 1 in a..sum) {
                    return TYPE.CONTENT.ordinal
                }
            }
        }


        return TYPE.BLANK.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TiltedViewHolder -> holder.itemView.apply {
                tv_note_date.text = findStr(position)
            }
            is ViewHolder -> holder.itemView.apply {
                tv_note_tilte.text = findStr(position)
                setOnClickListener {

                }
            }
        }
    }


    private fun findStr(position: Int): String {
        var sum = 0
        for (i in noteBean.dates.size - 1 downTo 0) {
            val lastSum = sum
            if (noteBean.dates[i].texts.size != 0) {
                sum++
                if (position + 1 == sum) {
                    return noteBean.dates[i].date
                }
                val a = sum
                sum += noteBean.dates[i].texts.size
                if (position + 1 in a..sum) {
                    return noteBean.dates[i].texts[position-lastSum-1].title
                }
            }
        }
        return "错误"
    }

    override fun getItemCount(): Int {
        var sum = 0
        noteBean.dates.forEach {
            if (it.texts.size != 0) {
                sum += it.texts.size
                sum++
            }
        }
        return sum
    }

    class TiltedViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        init {
//            item.apply {
//                SmartSwipe.wrap(this)
//                    .addConsumer(SpaceConsumer())
//                    .enableHorizontal() //工作方向：纵向
//            }
        }
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        init {
                SmartSwipe.wrap((item as ViewGroup).getChildAt(0))
                    .addConsumer(SpaceConsumer())
                    .enableHorizontal() //工作方向,横向
        }
    }

    class BlankViewHolder(item: View) : RecyclerView.ViewHolder(item)

}
