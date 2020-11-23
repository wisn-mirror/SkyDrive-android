package com.we.player.helper

import android.content.Context
import android.view.OrientationEventListener

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/23 上午11:38
 */
class OrientationEventListenerHelper : OrientationEventListener {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, rate: Int) : super(context, rate)

    var orientationEventListener: OrientationEventListenerM? = null
    var lastTime: Long = 0

    override fun onOrientationChanged(p0: Int) {
        var time = System.currentTimeMillis()
        if ((time - lastTime) < 300) {
            return
        }
        orientationEventListener?.onOrientationChanged(p0)
        lastTime = time

    }
}