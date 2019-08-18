package com.tree.newidea.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tree.newidea.R

/**
 * Created by Tree on 2019/8/18 13:58
 */
class SidebarRecycleViewAdapter : RecyclerView.Adapter<SidebarRecycleViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_recycle_item_notebook,null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 20
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)

}
