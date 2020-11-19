package com.we.player.controller.controller

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import com.we.player.R
import com.we.player.controller.component.ErrorControlView
import com.we.player.controller.component.PlayControlView
import com.we.player.controller.component.PreviewControlView

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/15 上午10:39
 */
class StandardController : GestureController, GestureDetector.OnGestureListener {
    var gestureDetector: GestureDetector? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        addIViewItemControllerOne(PreviewControlView(context))
        addIViewItemControllerOne(ErrorControlView(context))
        addIViewItemControllerOne(PlayControlView(context))
        gestureDetector = GestureDetector(context, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_controller_standard
    }











    override fun onDown(p0: MotionEvent?): Boolean {
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
    }


    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
//        TODO("Not yet implemented")
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
//        TODO("Not yet implemented")
        return false
    }


    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }
}
