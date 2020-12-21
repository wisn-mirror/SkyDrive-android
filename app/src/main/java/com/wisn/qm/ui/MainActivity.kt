package com.wisn.qm.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentContainerView
import com.library.base.BaseFragmentActivity
import com.qmuiteam.qmui.arch.SwipeBackLayout
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment
import com.qmuiteam.qmui.arch.annotation.FirstFragments
import com.qmuiteam.qmui.arch.annotation.LatestVisitRecord
import com.qmuiteam.qmui.skin.QMUISkinHelper
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIResHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.util.QMUIViewOffsetHelper
import com.qmuiteam.qmui.widget.QMUIRadiusImageView2
import com.wisn.qm.R
import com.wisn.qm.ui.home.HomeFragment

@FirstFragments(value = [HomeFragment::class])
@DefaultFirstFragment(HomeFragment::class)
@LatestVisitRecord
open class MainActivity : BaseFragmentActivity<MainViewModel>() {
    private val storagePermissions: Array<String> = arrayOf("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_WIFI_STATE");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this, storagePermissions, 1)
        QMUIStatusBarHelper.setStatusBarLightMode(this)

    }


    override fun onCreateRootView(fragmentContainerId: Int): RootView {
        return CustomRootView(this, fragmentContainerId)
    }


    internal inner class CustomRootView(context: Context?, fragmentContainerId: Int) : RootView(context, fragmentContainerId) {
        private val fragmentContainer: FragmentContainerView
        private val globalBtn: QMUIRadiusImageView2
        private val globalBtnOffsetHelper: QMUIViewOffsetHelper
        private val btnSize: Int
        private val touchSlop: Int
        private var touchDownX = 0f
        private var touchDownY = 0f
        private var lastTouchX = 0f
        private var lastTouchY = 0f
        private var isDragging = false
        private var isTouchDownInGlobalBtn = false
        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            super.onLayout(changed, left, top, right, bottom)
            globalBtnOffsetHelper.onViewLayout()
        }

        override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
            val x = event.x
            val y = event.y
            val action = event.action
            if (action == MotionEvent.ACTION_DOWN) {
                isTouchDownInGlobalBtn = isDownInGlobalBtn(x, y)
                lastTouchX = x
                touchDownX = lastTouchX
                lastTouchY = y
                touchDownY = lastTouchY
            } else if (action == MotionEvent.ACTION_MOVE) {
                if (!isDragging && isTouchDownInGlobalBtn) {
                    val dx = (x - touchDownX).toInt()
                    val dy = (y - touchDownY).toInt()
                    if (Math.sqrt(dx * dx + dy * dy.toDouble()) > touchSlop) {
                        isDragging = true
                    }
                }
                if (isDragging) {
                    var dx = (x - lastTouchX).toInt()
                    var dy = (y - lastTouchY).toInt()
                    val gx = globalBtn.left
                    val gy = globalBtn.top
                    val gw = globalBtn.width
                    val w = width
                    val gh = globalBtn.height
                    val h = height
                    if (gx + dx < 0) {
                        dx = -gx
                    } else if (gx + dx + gw > w) {
                        dx = w - gw - gx
                    }
                    if (gy + dy < 0) {
                        dy = -gy
                    } else if (gy + dy + gh > h) {
                        dy = h - gh - gy
                    }
                    globalBtnOffsetHelper.leftAndRightOffset = globalBtnOffsetHelper.leftAndRightOffset + dx
                    globalBtnOffsetHelper.topAndBottomOffset = globalBtnOffsetHelper.topAndBottomOffset + dy
                }
                lastTouchX = x
                lastTouchY = y
            } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                isDragging = false
                isTouchDownInGlobalBtn = false
            }
            return isDragging
        }

        private fun isDownInGlobalBtn(x: Float, y: Float): Boolean {
            return globalBtn.left < x && globalBtn.right > x && globalBtn.top < y && globalBtn.bottom > y
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x
            val y = event.y
            val action = event.action
            if (action == MotionEvent.ACTION_DOWN) {
                isTouchDownInGlobalBtn = isDownInGlobalBtn(x, y)
                lastTouchX = x
                touchDownX = lastTouchX
                lastTouchY = y
                touchDownY = lastTouchY
            } else if (action == MotionEvent.ACTION_MOVE) {
                if (!isDragging && isTouchDownInGlobalBtn) {
                    val dx = (x - touchDownX).toInt()
                    val dy = (y - touchDownY).toInt()
                    if (Math.sqrt(dx * dx + dy * dy.toDouble()) > touchSlop) {
                        isDragging = true
                    }
                }
                if (isDragging) {
                    var dx = (x - lastTouchX).toInt()
                    var dy = (y - lastTouchY).toInt()
                    val gx = globalBtn.left
                    val gy = globalBtn.top
                    val gw = globalBtn.width
                    val w = width
                    val gh = globalBtn.height
                    val h = height
                    if (gx + dx < 0) {
                        dx = -gx
                    } else if (gx + dx + gw > w) {
                        dx = w - gw - gx
                    }
                    if (gy + dy < 0) {
                        dy = -gy
                    } else if (gy + dy + gh > h) {
                        dy = h - gh - gy
                    }
                    globalBtnOffsetHelper.leftAndRightOffset = globalBtnOffsetHelper.leftAndRightOffset + dx
                    globalBtnOffsetHelper.topAndBottomOffset = globalBtnOffsetHelper.topAndBottomOffset + dy
                }
                lastTouchX = x
                lastTouchY = y
            } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                isDragging = false
                isTouchDownInGlobalBtn = false
            }
            return isDragging || super.onTouchEvent(event)
        }

        override fun getFragmentContainerView(): FragmentContainerView {
            return fragmentContainer
        }

        init {
            btnSize = QMUIDisplayHelper.dp2px(context, 56)
            fragmentContainer = FragmentContainerView(context!!)
            fragmentContainer.id = fragmentContainerId
//            fragmentContainer.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            fragmentContainer.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                for (i in 0 until childCount) {
                    SwipeBackLayout.updateLayoutInSwipeBack(getChildAt(i))
                }
            }
            addView(fragmentContainer, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            globalBtn = QMUIRadiusImageView2(context)
            globalBtn.setImageResource(R.mipmap.ic_launcher)
            globalBtn.scaleType = ImageView.ScaleType.CENTER_INSIDE
            globalBtn.setRadiusAndShadow(btnSize / 2,
                    QMUIDisplayHelper.dp2px(getContext(), 16), 0.4f)
            globalBtn.borderWidth = 1
            globalBtn.borderColor = QMUIResHelper.getAttrColor(context, R.attr.qmui_skin_support_color_separator)
            globalBtn.setBackgroundColor(QMUIResHelper.getAttrColor(context, R.attr.app_skin_common_background))
            globalBtn.setOnClickListener {
                //                    showGlobalActionPopup(v);
            }
            val globalBtnLp = LayoutParams(btnSize, btnSize)
            globalBtnLp.gravity = Gravity.BOTTOM or Gravity.RIGHT
            globalBtnLp.bottomMargin = QMUIDisplayHelper.dp2px(context, 60)
            globalBtnLp.rightMargin = QMUIDisplayHelper.dp2px(context, 24)
            val builder = QMUISkinValueBuilder.acquire()
            builder.background(R.attr.app_skin_common_background)
            builder.border(R.attr.qmui_skin_support_color_separator)
            builder.tintColor(R.attr.app_skin_common_img_tint_color)
            QMUISkinHelper.setSkinValue(globalBtn, builder)
            builder.release()
            addView(globalBtn, globalBtnLp)
            globalBtnOffsetHelper = QMUIViewOffsetHelper(globalBtn)
            touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        }
    }

    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {
        super.overridePendingTransition(0, 0)
    }

}