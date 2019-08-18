package com.tree.newidea.util

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tree.newidea.MyApplication.context
import com.tree.newidea.api.OrdinaryApi
import org.jetbrains.anko.startActivity

/**
 * Created by Tree on 2019/8/15 13:54
 */

//网络接口生成的接口类
lateinit var ordinaryApi: OrdinaryApi


@BindingAdapter(value = ["imageUrl", "error"], requireAll = false)
fun loadImage(view: ImageView, url: String?,error: Drawable?) {
    if (url != ""&&url != null) {
        Glide.with(view.context).load(url).apply {
        }.into(view)
    }
}
/**
 * @param pxValue
 * @param scale
 * (DisplayMetrics类中属性density）
 * @return
 */
fun px2dip( pxValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * 将dip或dp值转换为px值，保证尺寸大小不变
 *
 * @param dipValue
 * @param scale
 *            （DisplayMetrics类中属性density）
 * @return
 */
fun dip2px(dipValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

/**
 * 将px值转换为sp值，保证文字大小不变
 *
 * @param pxValue
 * @param fontScale
 *            （DisplayMetrics类中属性scaledDensity）
 * @return
 */
fun px2sp(pxValue: Float): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}

/**
 * 将sp值转换为px值，保证文字大小不变
 *
 * @param spValue
 * @param fontScale
 *            （DisplayMetrics类中属性scaledDensity）
 * @return
 */
fun sp2px( spValue: Float): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

