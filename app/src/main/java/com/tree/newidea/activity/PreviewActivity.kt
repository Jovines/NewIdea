package com.tree.newidea.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.tree.common.ui.BaseActivity
import com.tree.newidea.R
import com.tree.newidea.event.IndexEvent
import com.tree.newidea.util.note
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
    fun receiveIndex(indexEvent: IndexEvent) {
        note?.let {
            it.dates[indexEvent.index1].texts[indexEvent.index2].apply {
                val anim =
                    AnimationUtils.loadAnimation(this@PreviewActivity, R.anim.app_preview_show)
                if (isIsMardown) {
                    preview_activity_top_search_m.animation = anim
                    preview_activity_top_search_m.visibility = View.VISIBLE
                    anim.start()
                }else{
                    preview_activity_top_search_normal.animation = anim
                    preview_activity_top_search_normal.visibility = View.VISIBLE
                    anim.start()
                }
            }
        }

    }
}
