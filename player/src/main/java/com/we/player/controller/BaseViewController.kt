package com.we.player.controller

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:55
 */
abstract class  BaseViewController : FrameLayout, IViewController {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true)
    }

    override fun hideController() {
    }

    override fun showController() {
    }

    override fun setLocked(isLock: Boolean) {
    }

    override fun isLocked(): Boolean {
        return false
    }
    fun addIViewItemController(vararg iviewItemController: IViewItemController){
        iviewItemController.forEach {
            addView(it.getView())
        }

    }
}