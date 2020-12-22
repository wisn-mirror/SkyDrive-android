package com.wisn.qm.ui.home.controller

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemLongClickListener
import com.jeremyliao.liveeventbus.LiveEventBus
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.wisn.qm.R
import com.wisn.qm.mode.ConstantKey
import com.wisn.qm.ui.album.details.AlbumDetailsFragment
import com.wisn.qm.ui.album.details.AlbumDetailsPageingFragment
import com.wisn.qm.ui.album.newalbum.NewAlbumFragment
import com.wisn.qm.ui.home.HomeFragment
import com.wisn.qm.ui.home.HomeViewModel
import com.wisn.qm.ui.home.adapter.AlbumAdapter


/**
 * Created by Wisn on 2020/6/5 下午11:25.
 */
class AlbumController(context: Context?, mhomeFragment: HomeFragment?, homeViewModel: HomeViewModel?) : BaseHomeController(context, mhomeFragment, homeViewModel), SwipeRefreshLayout.OnRefreshListener {
    private val topbar: QMUITopBarLayout = findViewById(R.id.topbar)
    private val mAdapter by lazy { AlbumAdapter() }
    private val recyclerView: RecyclerView
    private val swiperefresh: SwipeRefreshLayout

    override val layoutId: Int
        get() = R.layout.home_controller_album

    init {
        val Leftbutton = topbar.setTitle("云相册")
        Leftbutton.setTextColor(Color.BLACK)
        Leftbutton.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        val addfile = topbar.addRightTextButton("新建", R.id.topbar_right_add_button)
        addfile.setTextColor(Color.BLACK)
        addfile.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        addfile.setOnClickListener {
            mHomeControlListener.let {
                mHomeControlListener.startFragmentByView(NewAlbumFragment())
            }
            mHomeViewModel.getUserDirlist()
        }
        recyclerView = findViewById(R.id.recyclerView)
        swiperefresh = findViewById(R.id.swiperefresh)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        swiperefresh?.setOnRefreshListener(this)

        mAdapter.run {
            mHomeViewModel.getUserDirlist().observe(mhomeFragment!!, Observer {
                swiperefresh?.isRefreshing = false
                LogUtils.d("updateAlbum!!!!!!!!!!!!!!!!!!!!!!!!!!1")
                if (it.isNullOrEmpty()) {
                    mAdapter.setEmptyView(R.layout.item_empty)
                } else {
                    mAdapter.setNewInstance(it)
                }
            })
            mAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<*, *>, view: View, i: Int ->
                mHomeControlListener.let {
                    val albumDetailsFragment = AlbumDetailsPageingFragment()
                    albumDetailsFragment.arguments = Bundle()
                    albumDetailsFragment.requireArguments().putSerializable(ConstantKey.albuminfo, mAdapter.getItem(i))
                    mHomeControlListener.startFragmentByView(albumDetailsFragment)
                }
            }
            mAdapter.setOnItemLongClickListener(object : OnItemLongClickListener {
                override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int): Boolean {
                    val item = mAdapter.getItem(position)
                    QMUIDialog.MessageDialogBuilder(context)
                            .setTitle("删除文件夹")
                            .setSkinManager(QMUISkinManager.defaultInstance(context))
                            .setMessage("确定要删除${item.filename}相册吗?")
                            .addAction("取消") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .addAction("确定") { dialog, _ ->
                                dialog.dismiss()
                                mHomeViewModel.deleteDirs(item.id.toString()).observe(mhomeFragment, Observer {
                                    if (it) {
                                        mHomeViewModel.getUserDirlist()
                                    }
                                })
                            }
                            .create(R.style.QMUI_Dialog).show()
//
                    return true;
                }

            })
        }
        LiveEventBus
                .get(ConstantKey.updateAlbum, Int::class.java)
                .observe(mhomeFragment!!, Observer {
                    LogUtils.d("updateAlbum")
                    mHomeViewModel.getUserDirlist()
                })
    }

    override fun onRefresh() {
        mHomeViewModel.getUserDirlist()
    }
}