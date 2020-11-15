package com.we.player.controller

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.we.player.R

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/15 上午10:39
 */
class StandardController :BaseViewController{
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_controller_standard
    }

}
