package com.tree.newidea.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tree.newidea.R
import com.tree.newidea.bean.NotepadBean
import com.tree.newidea.view.MyMarkdownView
import kotlinx.android.synthetic.main.app_recycle_item_notebook.view.*
import kotlinx.android.synthetic.main.app_skid_edit_top.view.*
import kotlin.math.min

class SkidTopAdapter(
    val bean: List<NotepadBean.DatesBean.TextsBean>,
    private val constraintLayout: ConstraintLayout,
    private val markdownView: MyMarkdownView,
    private val recyclerView: RecyclerView,
    private val container: ViewGroup
) : RecyclerView.Adapter<SkidTopAdapter.ViewHolder>() {

    val anim: ValueAnimator = ValueAnimator.ofFloat(0f, 1000f)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.app_recycle_item_notebook,
                null
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply item@{
            tv_note_tilte.text = bean[position].title
            setOnClickListener {
                constraintLayout
                if (bean[position].isIsMardown) {
                    markdownView.setMarkDownText(bean[position].text)
                    startAnim(this,markdownView, 1000)
                } else {
                    constraintLayout.apply {
                        mark_activity_top_search_normal_date.text = bean[position].date
                        mark_activity_top_search_normal_text.text = bean[position].text
                        mark_activity_top_search_normal_tilte.text = bean[position].title
                        startAnim(this@item,this, 1000)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return bean.size
    }

    //传入相对于屏幕的l,r,t,b
    fun startAnim(viewItem: View, showView: View, duration: Long) {
        viewItem.apply {
            val sizeH = (bottom - top) / (container.bottom - container.top)
            val sizeW = (right - left) / (container.right - container.left)
            val mSize = min(sizeH, sizeW)
            val startTranslateY = (bottom + top + recyclerView.top * 2) / 2 - (container.bottom - container.top) / 2
            val startTranslateX = (right + left + recyclerView.top * 2) / 2 - (container.right - container.left) / 2
            anim.interpolator = AccelerateDecelerateInterpolator()
            anim.duration = duration
            anim.addUpdateListener {
                val i = it.animatedValue as Float
                showView.apply {
                    scaleX = (i / 1000) * (1 - mSize) + mSize
                    scaleY = (i / 1000) * (1 - mSize) + mSize
                    translationX = (1 - i / 1000) * startTranslateX
                    translationY = (1 - i / 1000) * startTranslateY
                }
            }
            showView.visibility = View.VISIBLE
            anim.start()
        }
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)
}
