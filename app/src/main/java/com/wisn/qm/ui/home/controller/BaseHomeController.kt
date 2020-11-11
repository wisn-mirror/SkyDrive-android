package com.wisn.qm.ui.home.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout
import com.wisn.qm.ui.home.HomeFragment
import com.wisn.qm.ui.home.HomeViewModel

/**
 * Created by Wisn on 2020/6/5 下午11:19.
 */
//abstract class BaseHomeController< DB : ViewDataBinding>(context: Context?, homeControlListener: HomeControlListener?) : QMUIWindowInsetLayout(context) {
abstract class BaseHomeController(context: Context?, mhomeFragment: HomeFragment?, homeViewModel: HomeViewModel?) : QMUIWindowInsetLayout(context) {

    @JvmField
    protected var mHomeControlListener: HomeControlListener
    protected var mHomeFragment: HomeFragment
    protected var mHomeViewModel: HomeViewModel
    abstract val layoutId: Int
    var itemView: View

    init {
        itemView = LayoutInflater.from(context).inflate(layoutId, this)
        mHomeControlListener = mhomeFragment as HomeControlListener
        mHomeFragment = mhomeFragment as HomeFragment
        mHomeViewModel = homeViewModel!!
    }

    open fun onBackPressedExit() {

    }
}