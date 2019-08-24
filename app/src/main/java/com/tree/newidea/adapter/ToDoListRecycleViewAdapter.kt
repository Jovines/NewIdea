package com.tree.newidea.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tree.newidea.R
import com.tree.newidea.bean.NotepadBean
import com.tree.newidea.util.note
import kotlinx.android.synthetic.main.app_recycle_item_to_do_list.view.*

/**
 * Created by Tree on 2019/8/18 16:31
 */
class ToDoListRecycleViewAdapter(val bean: MutableList<NotepadBean.DatesBean.TextsBean>?) : RecyclerView.Adapter<ToDoListRecycleViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_recycle_item_to_do_list, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bean?.let {
            holder.itemView.todo_list_tilte.text = bean[it.size-position-1].title
        }
    }
    override fun getItemCount(): Int {
        return bean?.size?:0
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)

}
