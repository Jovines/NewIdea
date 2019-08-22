package com.tree.newidea.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.mukesh.MarkdownView

class MyMarkdownView : MarkdownView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun setMarkDownText(markdownText: String?) {
        val stainBuilder = StringBuilder()
        markdownText?.split("\n")?.forEach { stainBuilder.append(it) }
        stainBuilder.forEach { a -> Log.d("测试","$a")}
        super.setMarkDownText(markdownText)
    }



}
