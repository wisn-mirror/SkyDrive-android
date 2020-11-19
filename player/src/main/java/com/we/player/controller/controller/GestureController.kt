package com.we.player.controller.controller

import android.content.Context
import android.util.AttributeSet
import com.we.player.controller.BaseViewController

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/19 下午5:02
 */
abstract class GestureController : BaseViewController {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {}


}