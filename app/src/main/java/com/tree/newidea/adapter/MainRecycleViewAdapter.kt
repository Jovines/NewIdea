package com.tree.newidea.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.utils.BaseLottieAnimator
import com.billy.android.swipe.SmartSwipe
import com.billy.android.swipe.consumer.SpaceConsumer
import com.tree.newidea.R
import com.tree.newidea.activity.EditActivity
import com.tree.newidea.activity.MarkDownActivity
import com.tree.newidea.bean.NotepadBean
import kotlinx.android.synthetic.main.app_recycle_item_main_history.view.*

/**
 * Created by Tree on 2019/8/16 22:48
 */
class MainRecycleViewAdapter(
    val bean: MutableList<NotepadBean.DatesBean.TextsBean>?,
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
            bean?.let {
                if (position == it.size - 1) {
                    bottom_line.visibility = View.GONE

                }
                if (position == 0) {
                    top_line.visibility = View.GONE
                }
                tv_main_recycle_date.text = it[it.size - 1 - position].date
                tv_main_recycle_tilte.text = it[it.size - 1 - position].title
            }
        }
    }

    override fun getItemCount(): Int {
        bean?.let {
            if (it.size != 0) {
                lottieAnimator.visibility = View.GONE
                return bean.size
            }
        }
        lottieAnimator.visibility = View.VISIBLE
        return 0
    }


    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)


}
