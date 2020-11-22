package com.we.player.controller.controller

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import androidx.core.view.isGone
import com.blankj.utilcode.util.LogUtils
import com.we.player.R
import com.we.player.controller.component.*

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/15 上午10:39
 */
class StandardController : GestureController, View.OnClickListener {
    var lock_left: ImageView? = null
    var lock_right: ImageView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        addIViewItemControllerOne(PreviewControlView(context))
        addIViewItemControllerOne(ErrorControlView(context))
        addIViewItemControllerOne(PlayControlView(context))
        addIViewItemControllerOne(GestureControlView(context))
        addIViewItemControllerOne(TitleControlView(context))
        lock_left = findViewById(R.id.lock_left)
        lock_right = findViewById(R.id.lock_right)
        lock_left?.setOnClickListener(this)
        lock_right?.setOnClickListener(this)
        lock_left?.visibility= GONE
        lock_right?.visibility= GONE
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation?) {
        //必须是全屏幕的时候才有锁定
        if (isVisible && mediaPlayerController?.isFullScreen()!!) {
            if (islock) {
                lock_left?.visibility = VISIBLE
                lock_right?.visibility = VISIBLE
            } else {
                lock_left?.visibility = GONE
                lock_right?.visibility = VISIBLE
            }

        } else {
            lock_left?.visibility = GONE
            lock_right?.visibility = GONE
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.item_controller_standard
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.lock_right, R.id.lock_left -> {
                setLocked(!islock)


            }

        }
    }

    override fun onBackPressed(): Boolean {
        mediaPlayerController?.let {
            if (it.isFullScreen()) {
                it.stopFullScreen()
                return true
            }
        }
        return super.onBackPressed()
    }


}
