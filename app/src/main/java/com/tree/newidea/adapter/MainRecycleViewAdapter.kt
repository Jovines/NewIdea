package com.tree.newidea.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tree.newidea.R
import com.tree.newidea.activity.EditActivity
import com.tree.newidea.activity.MarkDownActivity

/**
 * Created by Tree on 2019/8/16 22:48
 */
class MainRecycleViewAdapter : RecyclerView.Adapter<MainRecycleViewAdapter.ViewHolder>() {
    lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_recycle_item_main, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> holder.itemView.setOnClickListener {
                val intent = Intent(context,EditActivity::class.java)
                context.startActivity(intent)
            }
            1 -> holder.itemView.setOnClickListener {
                val intent = Intent(context,MarkDownActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }



    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)


}
