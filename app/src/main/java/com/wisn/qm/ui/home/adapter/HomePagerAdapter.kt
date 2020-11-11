package com.wisn.qm.ui.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.wisn.qm.ui.home.Pager
import com.wisn.qm.ui.home.Pager.Companion.getPagerByPosition
import com.wisn.qm.ui.home.controller.BaseHomeController
import java.util.*

/**
 * Created by Wisn on 2020/6/6 下午12:50.
 */
class HomePagerAdapter(private val mPages: HashMap<Pager, BaseHomeController>) : PagerAdapter() {
    private var mChildCount = 0

    override fun isViewFromObject(view: View, ob: Any): Boolean {
        return view === ob
    }

    override fun getCount(): Int {
        return mPages.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val page = mPages[getPagerByPosition(position)]
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        container.addView(page, params)
        return page!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getItemPosition(obj: Any): Int {
        return if (mChildCount == 0) {
            POSITION_NONE
        } else super.getItemPosition(obj)
    }

    override fun notifyDataSetChanged() {
        mChildCount = count
        super.notifyDataSetChanged()
    }

}