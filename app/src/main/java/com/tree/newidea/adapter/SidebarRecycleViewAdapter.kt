package com.tree.newidea.adapter

import android.content.Context
import android.content.Intent
import android.service.quicksettings.Tile
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
import com.tree.newidea.activity.PreviewActivity
import com.tree.newidea.event.PreviewEvent
import org.greenrobot.eventbus.EventBus


/**
 * Created by Tree on 2019/8/18 13:58
 */
class SidebarRecycleViewAdapter(val context:Context,private val noteBean: NotepadBean) :
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
                tv_note_date.text = findStr(position)?.tile
            }
            is ViewHolder -> holder.itemView.apply {
                val textBean = findStr(position)?.textBean
                textBean?.apply {
                    tv_note_tilte.text = this.title
                    holder.itemView.sidebar_item.setOnClickListener {
                            EventBus.getDefault().postSticky(PreviewEvent(textBean))
                            context.startActivity(Intent(context,PreviewActivity::class.java))
                        }

                }

            }
        }
    }


    private fun findStr(position: Int): Message? {
        var sum = 0
        for (i in noteBean.dates.size - 1 downTo 0) {
            val lastSum = sum
            if (noteBean.dates[i].texts.size != 0) {
                sum++
                if (position + 1 == sum) {
                    return Message(noteBean.dates[i].date)
                }
                val a = sum
                sum += noteBean.dates[i].texts.size
                if (position + 1 in a..sum) {
                    return Message(textBean = noteBean.dates[i].texts[position-lastSum-1])
                }
            }
        }
        return null
    }

    class Message(val tile: String? = null, val textBean: NotepadBean.DatesBean.TextsBean? = null)

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
