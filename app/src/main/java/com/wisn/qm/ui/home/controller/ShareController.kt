package com.wisn.qm.ui.home.controller

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.wisn.qm.R
import com.wisn.qm.ui.home.HomeFragment
import com.wisn.qm.ui.home.HomeViewModel
import com.wisn.qm.ui.home.bean.Share
import java.util.*

/**
 * Created by Wisn on 2020/6/5 下午11:26.
 */
//class ShareController(context: Context?, homeControlListener: HomeControlListener?) : BaseHomeController<HomeControllerShareBinding>(context, homeControlListener!!) {
class ShareController(context: Context?, mhomeFragment: HomeFragment?, homeViewModel: HomeViewModel?) : BaseHomeController(context, mhomeFragment,homeViewModel) {
    private var recyclerView: RecyclerView
    override val layoutId: Int
        get() = R.layout.home_controller_share

    init {
//        mBinding.recyclerView
        LogUtils.d("ShareController ${layoutId}")
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val data: MutableList<Share> = ArrayList()
        for (i in 0..99) {
            data.add(Share(i % 2))
        }
//        recyclerView.adapter = ShareAdapter(data)
    }
}