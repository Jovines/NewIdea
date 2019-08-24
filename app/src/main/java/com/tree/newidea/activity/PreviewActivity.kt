package com.tree.newidea.activity

import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.tree.common.ui.BaseActivity
import com.tree.newidea.R
import com.tree.newidea.bean.NotepadBean
import com.tree.newidea.event.PreviewEvent
import kotlinx.android.synthetic.main.app_activity_preview.*
import org.greenrobot.eventbus.Subscribe

class PreviewActivity : BaseActivity() {
    override val isFragmentActivity: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_preview)
    }


    @Subscribe(sticky = true)
    fun receiveIndex(previewEvent: PreviewEvent) {
        previewEvent.textBean.apply {
            val anim =
                AnimationUtils.loadAnimation(this@PreviewActivity, R.anim.app_preview_show)
            if (isIsMardown) {
                preview_activity_top_search_m.animation = anim
                preview_activity_top_search_m.visibility = View.VISIBLE
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        preview_activity_top_search_m.setMarkDownText(previewEvent.textBean.text)
                    }

                    override fun onAnimationStart(p0: Animation?) {
                    }

                })
                anim.start()

            } else {
                preview_activity_top_search_normal.animation = anim
                preview_activity_top_search_normal.visibility = View.VISIBLE
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        preview_activity_top_search_normal.apply {
                            preview_activity_top_search_normal_tilte.text = previewEvent.textBean.title
                            preview_activity_top_search_normal_date.text = previewEvent.textBean.date
                            preview_activity_top_search_normal_text.text = previewEvent.textBean.text
                        }
                    }

                    override fun onAnimationStart(p0: Animation?) {
                    }

                })
                anim.start()
            }
        }

    }
}
