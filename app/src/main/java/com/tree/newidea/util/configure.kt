package com.tree.newidea.util

import com.tree.newidea.R
import com.tree.newidea.bean.NotepadBean

/**
 * Created by Tree on 2019/8/16 2:49
 */

/**
 * 闪屏页到Mian的时间
 */
const val activityStartTime = 1500.toLong()

/**
 * 大时间格式,和详细时间格式
 */
const val timeFormat1 = "yyyy-MM-dd"//大时间格式请勿更改
const val timeFormat2 = "yyyy-MM-dd HH-mm"


var momeList:MutableList<Int>? = null

/**
 * 本地记事本数据
 */

var note:NotepadBean? = null


/**
 *时间轴
 */
var timelineList: MutableList<NotepadBean.DatesBean.TextsBean>? = null

/**
 * 代办事项
 */
var todoList: MutableList<NotepadBean.DatesBean.TextsBean>? = null


/**
 * 手机屏幕的高度和宽度，单位是px
 */
var phoneWidth: Int = 0
var phoneHeight: Int = 0

/**
 * 状态栏的高度
 */
var statusBarHeight: Int = 0

/**
 * 主页顶部是否展开
 */
var isTopOpen = false


/**
 * main头部透明度变化时间,不要低于900，会抽风
 */
const val headTransparencyChangeTime = 1000.toLong()

/**
 * 头部放缩时间
 */
const val headExpansionAnimationTime = 800.toLong()

/**
 * 单位为dp
 */
const val sidebarWidth = 100
