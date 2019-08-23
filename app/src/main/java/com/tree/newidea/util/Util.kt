package com.tree.newidea.util

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tree.newidea.MyApplication.context
import com.tree.newidea.activity.MainActivity
import com.tree.newidea.api.OrdinaryApi
import com.tree.newidea.bean.NotepadBean
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.startActivity
import java.io.*
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.RESULT_SHOWN
import android.widget.EditText


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



/**
 * 将序列化的类保存在本地
 */
fun saveObject(context: Context,name: String, `object`: Any) {

    var fos: FileOutputStream? = null

    var oos: ObjectOutputStream? = null

    try {

        fos = context.openFileOutput(name, Context.MODE_PRIVATE)

        oos = ObjectOutputStream(fos)

        oos.writeObject(`object`)

    } catch (e: Exception) {

        e.printStackTrace()

        //这里是保存文件产生异常

    } finally {

        if (fos != null) {

            try {

                fos.close()

            } catch (e: IOException) {

                //fos流关闭异常

                e.printStackTrace()

            }

        }

        if (oos != null) {

            try {

                oos.close()

            } catch (e: IOException) {

                //oos流关闭异常

                e.printStackTrace()

            }

        }

    }

}


fun asynSaveSerializationObject(context: Context,name: String, `object`: Any) {
    Observable.create<Any> {
      saveObject(context,name,`object`)
    }.subscribeOn(Schedulers.io()).subscribe()
}


/**
 * 保存在本地的序列化的对象取出
 */
fun getObject(context: Context,name: String): Any? {
    var fis: FileInputStream? = null
    var ois: ObjectInputStream? = null
    try {
        fis = context.openFileInput(name)
        ois = ObjectInputStream(fis)
        return ois.readObject()
    } catch (e: Exception) {
        e.printStackTrace()
        //这里是读取文件产生异常
    } finally {
        if (fis != null) {
            try {
                fis.close()
            } catch (e: IOException) {
                //fis流关闭异常
                e.printStackTrace()
            }

        }
        if (ois != null) {
            try {
                ois.close()
            } catch (e: IOException) {
                //ois流关闭异常
                e.printStackTrace()
            }

        }
    }
    //读取产生异常，返回null
    return null
}


fun openKeybord(mEditText: EditText, mContext: Context) {
    val imm = mContext
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(mEditText, RESULT_SHOWN)
    imm.toggleSoftInput(
        InputMethodManager.SHOW_FORCED,
        HIDE_IMPLICIT_ONLY
    )
    mEditText.apply {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
    }
}

/**
 * 关闭软键盘
 *
 * @param mEditText输入框
 * @param mContext上下文
 */
fun closeKeybord(mEditText: EditText, mContext: Context) {
    val imm = mContext
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
}
