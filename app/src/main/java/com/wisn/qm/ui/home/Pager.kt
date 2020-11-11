package com.wisn.qm.ui.home

/**
 * Created by Wisn on 2020/6/5 下午11:37.
 */
enum class Pager {
    Picture,Album, Share, Mine;

    companion object {
        @JvmStatic
        fun getPagerByPosition(position: Int): Pager {
            return when (position) {
                0 -> Picture
                1 -> Album
//                2 -> Share
                else -> Mine
            }
        }
    }
}