package com.wisn.qm.ui.home.controller

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.wisn.qm.R
import com.wisn.qm.ui.home.HomeFragment
import com.wisn.qm.ui.home.HomeViewModel
import com.wisn.qm.ui.home.picture.PictureAdapterV2
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.qmuiteam.qmui.qqface.QMUIQQFaceView
import com.wisn.qm.mode.ConstantKey
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.task.UploadTaskUitls
import com.wisn.qm.ui.home.picture.PictureCallBack


/**
 * Created by Wisn on 2020/6/5 下午11:26.
 */
open class PictureController(context: Context, mhomeFragment: HomeFragment, homeViewModel: HomeViewModel?) : BaseHomeController(context, mhomeFragment, homeViewModel), PictureCallBack, SwipeRefreshLayout.OnRefreshListener {
    private val topbar: QMUITopBarLayout = findViewById(R.id.topbar)
    private val mAdapter by lazy { PictureAdapterV2(this) }
    private var gridLayoutManager: GridLayoutManager
    private val recyclerView: RecyclerView
    private val swiperefresh: SwipeRefreshLayout
    private val leftCancel: Button
    private val title: QMUIQQFaceView

    override val layoutId: Int
        get() = R.layout.home_controller_picture

    init {
        LogUtils.d(" PictureController .init")

        title = topbar.setTitle("文件")
        title.setTextColor(Color.BLACK)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        leftCancel = topbar.addLeftTextButton("取消 ", R.id.topbar_right_add_button)
        leftCancel.setTextColor(Color.BLACK)
        leftCancel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        leftCancel.visibility = View.GONE
        leftCancel.setOnClickListener {
            showPictureControl(false)
        }
        gridLayoutManager = GridLayoutManager(context, 3)
//        with(gridLayoutManager) {
//            spanSizeLookup = SpanSizeLookup(mAdapter)
//        }
        recyclerView = findViewById(R.id.recyclerView)
        swiperefresh = findViewById(R.id.swiperefresh)
        //设置进度View样式的大小，只有两个值DEFAULT和LARGE，表示默认和较大
        swiperefresh.setSize(SwipeRefreshLayout.DEFAULT);
        //设置触发下拉刷新的距离
        swiperefresh.setDistanceToTriggerSync(300);
        var scantip: TextView = findViewById(R.id.scantip)
        with(recyclerView) {
            layoutManager = gridLayoutManager
            adapter = mAdapter
        }
        /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
             recyclerView?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                 LogUtils.d(" PictureController "+scrollY+" "+oldScrollY)
             }
         }*/
        swiperefresh?.setOnRefreshListener(this)

        mAdapter.run {
            LogUtils.d(" PictureController .mAdapter.run ")

            mHomeViewModel.selectData().observe(mHomeFragment, Observer {
                LogUtils.d(" mHomeViewModel.selectData")
                if (leftCancel.visibility == View.VISIBLE) {
                    title.text = "已选中${it?.size}项"
                }
            })
        }
        LiveEventBus
                .get(ConstantKey.updateHomeMedialist, MutableList::class.java)
                .observe(mHomeFragment!!, Observer {
                    LogUtils.d(" mHomeViewModel. updateHomeMedialist")
                    scantip.visibility = View.GONE
                    swiperefresh?.isRefreshing = false
                    if (it.isNullOrEmpty()) {
                        var item_empty: View = View.inflate(context, R.layout.item_empty, null)
                        var empty_tip = item_empty.findViewById<TextView>(R.id.empty_tip)
                        var image = item_empty.findViewById<ImageView>(R.id.image)
                        image.setImageResource(R.mipmap.share_ic_blank_album)
                        empty_tip.setText("本地相册为空,快去拍照吧！")
                        mAdapter.setEmptyView(item_empty)
                    } else {
                        mAdapter.setNewInstance(it as MutableList<MediaInfo>)
                    }
                })
        /* LiveEventBus
                 .get(ConstantKey.updatePhotoList, Int::class.java)
                 .observe(mHomeFragment, Observer {
                     LogUtils.d(" mHomeViewModel.updatePhotoList")
                     UploadTaskUitls.exeRequest(Utils.getApp(), UploadTaskUitls.buildMediaScanWorkerRequest())
                 })*/
        UploadTaskUitls.exeRequest(Utils.getApp(), UploadTaskUitls.buildMediaScanWorkerRequest())
    }

    override fun showPictureControl(isShow: Boolean?) {
        isShow?.let {
            mHomeControlListener.showPictureControl(isShow)
            if (isShow) {
                leftCancel.visibility = View.VISIBLE
            } else {
                leftCancel.visibility = View.GONE
                title.text = "文件"
            }
            mAdapter.updateSelect(isShow)
        }

    }

    override fun changeSelectData(isinit: Boolean, isSelectModel: Boolean, isAdd: Boolean, item: MediaInfo?) {
        if (isinit) {
            mHomeViewModel.selectData().value?.clear();
        }
        if (isSelectModel) {
            if (item != null) {
                if (isAdd) {
                    mHomeViewModel.selectData().value?.add(item)
                } else {
                    mHomeViewModel.selectData().value?.remove(item)
                }
                mHomeViewModel.selectData().value = mHomeViewModel.selectData().value;
            } else {
                title.text = "已选中${mHomeViewModel.selectData.value?.size ?: 0}项"
            }
        } else {
            title.text = "文件"
        }
    }

    override fun getHomeFragment(): HomeFragment {
        return mHomeFragment
    }

    override fun onBackPressedExit() {
        showPictureControl(false)
    }

    override fun onRefresh() {
        LogUtils.d(" PictureController .onRefresh")

        UploadTaskUitls.exeRequest(Utils.getApp(), UploadTaskUitls.buildMediaScanWorkerRequest())

    }
}


open class SpanSizeLookup(var adapterV2: PictureAdapterV2) : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {

        val get = adapterV2.data[position]
        return when (get.itemType) {
            FileType.ImageViewItem -> 1
            FileType.TimeTitle -> 3
            else -> 1
        }

    }

}
