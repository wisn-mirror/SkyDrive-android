package com.wisn.qm.ui.preview

import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.library.base.BaseApp
import com.library.base.BaseFragment
import com.library.base.base.NoViewModel
import com.library.base.base.ViewModelFactory
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.we.player.controller.controller.ListVideoController
import com.we.player.player.exo.ExoPlayerFactory
import com.we.player.render.impl.TextureRenderViewFactory
import com.we.player.view.VideoView
import com.wisn.qm.R
import com.wisn.qm.databinding.FragmentPreviewBinding
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.ui.album.newalbum.NewAlbumFragment
import com.wisn.qm.ui.home.HomeViewModel

class PreviewFragment(var data: MutableList<MediaInfo>, var position: Int) : BaseFragment<NoViewModel, FragmentPreviewBinding>(), PreviewCallback {


    var recyclerView: RecyclerView? = null
    var playPosition: Int? = null
    var SelectPosition: Int? = null

    val videoView by lazy {
        var videoview = VideoView(BaseApp.app)
        videoview.renderViewFactory = TextureRenderViewFactory()
        videoview?.mediaPlayer = ExoPlayerFactory()
        videoview?.iViewController = ListVideoController(BaseApp.app)
        videoview.setLooping(true)
        videoview
    }

    override fun layoutId(): Int {
        return R.layout.fragment_preview
    }

    override fun initView(views: View) {
        super.initView(views)
        var mHomeViewModel = ViewModelProvider(requireActivity(), ViewModelFactory()).get(HomeViewModel::class.java)

        dataBinding?.groupContent?.visibility = View.GONE

        dataBinding?.vpContent?.adapter = PreviewAdapter(data, this@PreviewFragment)

        dataBinding?.vpContent?.setCurrentItem(position, false)
        dataBinding?.vpContent?.overScrollMode = View.OVER_SCROLL_NEVER
        dataBinding?.vpContent?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        @Px positionOffsetPixels: Int) {
            }


            override fun onPageSelected(position: Int) {
                SelectPosition = position
                if (playPosition == position) {
                    //如果是当前的 返回
                    return
                }
                val get = data.get(position)
                if (get.itemType != FileType.VideoViewItem) {
                    return
                }
                dataBinding?.vpContent?.post {
                    startPlay(position)
                }
            }


            override fun onPageScrollStateChanged(@ScrollState state: Int) {
//                ViewPager2.SCROLL_STATE_IDLE, ViewPager2.SCROLL_STATE_DRAGGING, ViewPager2.SCROLL_STATE_SETTLING
                when (state) {
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        //空闲状态
                        if (SelectPosition == playPosition) {
                            videoView.start()
                        }
                    }
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        //滑动状态
                        videoView.pause()

                    }
                    ViewPager2.SCROLL_STATE_SETTLING -> {
                        //滑动后自然沉降的状态
                        videoView.pause()

                    }
                }
            }

        })

        dataBinding?.tvAddto?.onClick {
            val values = mHomeViewModel.getUserDirlist().value;
            var addItem = View.inflate(context, R.layout.item_album_new_album, null)
            values?.let {
                val builder = QMUIBottomSheet.BottomListSheetBuilder(activity)
                builder.setGravityCenter(true)
                        .setSkinManager(QMUISkinManager.defaultInstance(context))
                        .setTitle("添加到")
                        .setAddCancelBtn(true)
                        .setAllowDrag(true)
                        .setNeedRightMark(true)
                        .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                            dialog.dismiss()
                            mHomeViewModel.saveMedianInfo(position)
                            ToastUtils.showShort("已经添加到上传任务")
                        }
                for (dirlist in values) {
                    builder.addItem(dirlist.filename)
                }

                builder.addContentFooterView(addItem)
                val build = builder.build();
                build.show()
                addItem.onClick {
                    build.dismiss()
                    startFragment(NewAlbumFragment())
                }
            }
        }
        dataBinding?.tvUpload?.onClick {
            val get = data.get(dataBinding?.vpContent?.currentItem!!)
            mHomeViewModel.saveMedianInfo(position, get)
            ToastUtils.showShort("已经添加到上传任务")
        }
        dataBinding?.ivBack?.onClick {
            popBackStack()
        }

        recyclerView = dataBinding?.vpContent?.getChildAt(0) as RecyclerView?
    }


    override fun onFetchTransitionConfig(): TransitionConfig {
        return SCALE_TRANSITION_CONFIG
    }


    fun startPlay(position: Int) {
        recyclerView?.children?.forEach {
            if (it.tag is PreviewVideoViewHolder) {
                var viewholder = it.tag as PreviewVideoViewHolder
                if (position == viewholder.pos) {
                    videoView.release()
                    recycleVideoView()
                    val get = data.get(position)
                    videoView.setUrl(get.filePath!!)
                    videoView.iViewController?.addIViewItemControllerOne(viewholder.preview, true)
                    viewholder?.content.addView(videoView, 0)
                    videoView.start()
                    playPosition = position
                    return@forEach
                }
            }
        }
    }


    fun recycleVideoView() {
        if (videoView != null) {
            videoView.parent?.let {
                it as ViewGroup
                it.removeView(videoView)
            }
        }
    }

    override fun onContentClick(view: View) {
        if (dataBinding?.groupContent?.visibility == View.GONE) {
            dataBinding?.groupContent?.visibility = View.VISIBLE
        } else {
            dataBinding?.groupContent?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView?.release()
    }


}