package com.tree.newidea.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.tree.newidea.R
import com.tree.newidea.activity.MainActivity
import com.tree.newidea.activity.PreviewActivity
import com.tree.newidea.bean.NotepadBean
import com.tree.newidea.event.PreviewEvent
import com.tree.newidea.util.asynSaveSerializationObject
import com.tree.newidea.util.note
import kotlinx.android.synthetic.main.app_main_sidebar.*
import kotlinx.android.synthetic.main.app_recycle_item_main_history.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Tree on 2019/8/16 22:48
 */
class MainRecycleViewAdapter(
    private val timelineList: MutableList<NotepadBean.DatesBean.TextsBean>?,
    private val lottieAnimator: LottieAnimationView
) : RecyclerView.Adapter<MainRecycleViewAdapter.ViewHolder>() {
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.app_recycle_item_main_history,
                null
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            bottom_line.visibility = View.VISIBLE
            top_line.visibility = View.VISIBLE
            timelineList?.let {

                if (position == it.size - 1) {
                    bottom_line.visibility = View.GONE

                }
                if (position == 0) {
                    top_line.visibility = View.GONE
                }
                val textBean = it[it.size - 1 - position]
                tv_main_recycle_date.text = textBean.date
                if (textBean.isUndo) {
                    tv_main_recycle_tilte.text = textBean.title.replace("\n", "")
                } else {
                    tv_main_recycle_tilte.text = textBean.title
                }
                when {
                    textBean.isIsMardown -> {
                        iv_icon_m.visibility = View.VISIBLE
                        iv_icon_undo.visibility = View.GONE
                        iv_icon_normal.visibility = View.GONE
                    }
                    textBean.isUndo -> {
                        iv_icon_m.visibility = View.GONE
                        iv_icon_undo.visibility = View.VISIBLE
                        iv_icon_normal.visibility = View.GONE
                    }
                    else -> {
                        iv_icon_m.visibility = View.GONE
                        iv_icon_undo.visibility = View.GONE
                        iv_icon_normal.visibility = View.VISIBLE
                    }
                }
            }
            setOnClickListener {
                timelineList?.apply {
                    if (this[size - 1 - position].isUndo) {
                        Toast.makeText(context, "备忘录没有详情页面哦", Toast.LENGTH_SHORT).show()
                        return@apply
                    }
                    EventBus.getDefault().postSticky(PreviewEvent(this[size - 1 - position]))
                    context.startActivity(Intent(context, PreviewActivity::class.java))
                }
            }
            setOnLongClickListener {
                AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("确认删除？")
                    .setPositiveButton("是的") { _, _ ->
                        val bean = timelineList?.get(timelineList.size - 1 - position)
                        note?.dates?.forEach forEach1@{ it1 ->
                            it1.texts.forEach {it2->
                                if (it2.text == bean?.text&&it2.title == bean?.title||it2== bean) {
//                                            recyclerView.removeItemDecorationAt(message.position)
                                    it1.texts.remove(bean)
                                    note?.let {
                                        asynSaveSerializationObject(context,"note",it)
                                    }
                                    return@forEach1
                                }
                            }
                        }
                        com.tree.newidea.util.timelineList?.forEach {

                        }

                        timelineList?.remove(bean)
                        timelineList?.let {
                            asynSaveSerializationObject(context,"timelineList",it)
                        }
                        notifyDataSetChanged()
                        (context as MainActivity).srv_main_sidebar.adapter?.notifyDataSetChanged()
                    }.show()

                false
            }
        }
    }

    override fun getItemCount(): Int {
        timelineList?.let {
            if (it.size != 0) {
                lottieAnimator.visibility = View.GONE
                return timelineList.size
            }
        }
        lottieAnimator.visibility = View.VISIBLE
        return 0
    }


    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)


}
