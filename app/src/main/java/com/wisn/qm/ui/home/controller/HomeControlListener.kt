package com.wisn.qm.ui.home.controller

import com.qmuiteam.qmui.arch.QMUIFragment

interface HomeControlListener{
    fun startFragmentByView(fragment: QMUIFragment?)
    fun showPictureControl(isShow: Boolean?)
}