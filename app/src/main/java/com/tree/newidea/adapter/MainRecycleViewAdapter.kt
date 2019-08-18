package com.tree.newidea.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tree.newidea.R

/**
 * Created by Tree on 2019/8/16 22:48
 */
class MainRecycleViewAdapter : RecyclerView.Adapter<MainRecycleViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_recycle_item_main, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
//            0 ->
        }
    }

    override fun getItemCount(): Int {
        return 5
    }



    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)


}
