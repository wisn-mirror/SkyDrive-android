package com.we.player.controller.controller

import android.content.Context
import android.util.AttributeSet
import com.we.player.R
import com.we.player.controller.component.ErrorControlView
import com.we.player.controller.component.GestureControlView
import com.we.player.controller.component.PlayControlView
import com.we.player.controller.component.PreviewControlView

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/15 上午10:39
 */
class StandardController : GestureController{

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        addIViewItemControllerOne(PreviewControlView(context))
        addIViewItemControllerOne(ErrorControlView(context))
        addIViewItemControllerOne(PlayControlView(context))
        addIViewItemControllerOne(GestureControlView(context))
    }

    override fun getLayoutId(): Int {
        return R.layout.item_controller_standard
    }
}
