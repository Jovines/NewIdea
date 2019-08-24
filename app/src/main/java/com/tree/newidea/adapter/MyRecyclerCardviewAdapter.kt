//package com.tree.newidea.adapter
//
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.tree.newidea.R
//
//class MyRecyclerCardviewAdapter(
//    var mdatas: List<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    private var themeTitle: TextView? = null
//
//    enum class ITEM_TYPE {
//        ITEM_TYPE_Theme,
//        ITEM_TYPE_Video
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//
//        if (viewType == ITEM_TYPE.ITEM_TYPE_Theme.ordinal) {
//
//            val view = LayoutInflater.from(parent.mainActivity).inflate(R.layout.videothemelist, parent, false)
//
//            return ThemeVideoHolder(view)
//
//        } else if (viewType == ITEM_TYPE.ITEM_TYPE_Video.ordinal) {
//
//            val view = LayoutInflater.from(parent.mainActivity).inflate(R.layout.videocardview, parent, false)
//            return VideoViewHolder(view)
//
//        }
//        return null
//    }
//
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        if (holder is ThemeVideoHolder) {
//
//            themeTitle!!.text = "励志"
//
//        } else if (holder is VideoViewHolder) {
//            holder.videologo.setImageResource(R.drawable.lianzai_02)
//            holder.videovname.text = "励志，俄小伙练习街头健身一年的体型变化，Dear Hard Work！"
//            holder.videoviewed.text = "2780次"
//            holder.videocomment.text = "209条"
//
//        }
//
//    }
//
//
//    override fun getItemViewType(position: Int): Int {
//
//        return if (position % 5 == 0) ITEM_TYPE.ITEM_TYPE_Theme.ordinal else ITEM_TYPE.ITEM_TYPE_Video.ordinal
//    }
//
//
//    override fun getItemCount(): Int {
//        return mdatas.size
//    }
//
//
//    inner class ThemeVideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        init {
//            themeTitle = itemView.findViewById<View>(R.id.hometab1_theme_title) as TextView
//        }
//    }
//
//    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var videologo: ImageView
//        var videovname: TextView
//        var videoviewed: TextView
//        var videocomment: TextView
//
//        init {
//            videologo = itemView.findViewById<View>(R.id.videologo) as ImageView
//            videoviewed = itemView.findViewById<View>(R.id.videoviewed) as TextView
//            videocomment = itemView.findViewById<View>(R.id.videocomment) as TextView
//            videovname = itemView.findViewById<View>(R.id.videoname) as TextView
//        }
//    }
//}