package com.tree.newidea.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tree.newidea.api.OrdinaryApi

/**
 * Created by Tree on 2019/8/15 13:54
 */


lateinit var ordinaryApi: OrdinaryApi

@BindingAdapter(value = ["imageUrl", "error"], requireAll = false)
fun loadImage(view: ImageView, url: String?,error: Drawable?) {
    if (url != ""&&url != null) {
        Glide.with(view.context).load(url).apply {
        }.into(view)
    }
}
