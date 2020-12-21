package com.wisn.qm.ui.upload

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.LogUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.library.base.BaseFragment
import com.qmuiteam.qmui.qqface.QMUIQQFaceView
import com.wisn.qm.R
import com.wisn.qm.mode.ConstantKey
import kotlinx.android.synthetic.main.fragment_uploadlist.*
import kotlinx.android.synthetic.main.item_empty.view.*

open class UploadListFragment : BaseFragment<UploadListViewModel>(), SwipeRefreshLayout.OnRefreshListener {
    lateinit var title: QMUIQQFaceView
    private val mAdapter by lazy { UploadListAdapter() }

    override fun layoutId(): Int {
        return R.layout.fragment_uploadlist
    }

    override fun initView(views: View) {
        title =   topbar?.setTitle("上传列表")!!
        title.setTextColor(Color.BLACK)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        val addLeftBackImageButton =   topbar?.addLeftBackImageButton()
        addLeftBackImageButton?.setColorFilter(Color.BLACK)
        addLeftBackImageButton?.setOnClickListener {
            popBackStack()
        }
        with(  recyclerView) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.adapter = mAdapter
        }
        viewModel.getUploadList().observe(this, Observer {
              swiperefresh?.isRefreshing=false
            if (it.isNullOrEmpty()) {
                var item_empty: View = View.inflate(context, R.layout.item_empty, null)
                item_empty.image.setImageResource(R.mipmap.share_ic_blank_album)
                item_empty.empty_tip.setText("上传列表为空,快去添吧！")
                mAdapter.setEmptyView(item_empty)
            } else {
                mAdapter.setNewInstance(it)
            }
        })
          swiperefresh?.setOnRefreshListener(this)
        LiveEventBus
                .get(ConstantKey.updatePhotoList, Int::class.java)
                .observe(this, Observer {
                    LogUtils.d("updatePhotoList")
                    viewModel.getUploadList()
                })

    }

    override fun onRefresh() {
        viewModel.getUploadList()
    }

}
