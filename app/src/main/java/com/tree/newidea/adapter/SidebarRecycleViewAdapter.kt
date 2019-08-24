package com.tree.newidea.adapter

import android.content.Context
import android.content.Intent
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
import com.billy.android.swipe.consumer.SlidingConsumer
import com.tree.newidea.activity.MainActivity
import com.tree.newidea.event.TrashMessage
import com.tree.newidea.util.asynSaveSerializationObject
import com.tree.newidea.util.note
import com.tree.newidea.util.timelineList
import kotlinx.android.synthetic.main.app_main_activity_content.*
import kotlinx.android.synthetic.main.app_main_activity_content.view.*
import kotlinx.android.synthetic.main.app_main_sidebar.*
import kotlinx.android.synthetic.main.app_sidler_trash_can.view.*


/**
 * Created by Tree on 2019/8/18 13:58
 */
class SidebarRecycleViewAdapter(private val mainActivity:MainActivity, private val noteBean: NotepadBean, val recyclerView: RecyclerView) :
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
                ).apply itemView@{
                    SmartSwipe.wrap(sidebar_item)
                        .addConsumer(SlidingConsumer())
                        .setLeftDrawerView(View.inflate(context,R.layout.app_sidler_trash_can,null).apply {
                            trash_can.setOnClickListener {
                                val message = this@itemView.sidebar_item.tag as? TrashMessage
                                message?.bean?.let { bean ->
                                    noteBean.dates.forEach forEach1@{ it1 ->
                                        it1.texts.forEach {it2->
                                        if (it2 == bean) {
//                                            recyclerView.removeItemDecorationAt(message.position)
                                            it1.texts.remove(bean)
                                            note?.let {
                                                asynSaveSerializationObject(context,"note",it)
                                            }
                                            return@forEach1
                                        }
                                    } }
                                    timelineList?.forEach {
                                        if (bean.title == it.title && bean.text == it.text) {
                                            timelineList?.remove(it)
                                            timelineList?.let { mutableList ->
                                                asynSaveSerializationObject(context,"timelineList",mutableList)
                                            }
                                            return@forEach
                                        }
                                    }
                                }

                                notifyDataSetChanged()
                                mainActivity.rc_main?.adapter?.notifyDataSetChanged()
                            }
                        })
                        .setScrimColor(0x00000000)
                        //设置联动系数
                        //  0:不联动，视觉效果为：主体移动后显示下方的抽屉
                        //  0~1: 半联动，视觉效果为：抽屉视图按照联动系数与主体之间存在相对移动效果
                        //  1:全联动，视觉效果为：抽屉跟随主体一起移动(pixel by pixel)
                }
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
                sidebar_item.tag = TrashMessage(textBean,position)
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

        if (sum == 0) {
            mainActivity.ll_no_notebook_tips.visibility = View.VISIBLE
        }else{
            mainActivity.ll_no_notebook_tips.visibility = View.GONE

        }
        return sum
    }

    class TiltedViewHolder(item: View) : RecyclerView.ViewHolder(item)

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        init {
                SmartSwipe.wrap((item as ViewGroup).getChildAt(0))
                    .addConsumer(SpaceConsumer())
                    .enableHorizontal() //工作方向,横向
        }
    }

    class BlankViewHolder(item: View) : RecyclerView.ViewHolder(item)

}
