package com.wisn.qm.ui.netpreview

import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.blankj.utilcode.util.Utils
import com.library.base.BaseApp
import com.library.base.BaseFragment
import com.library.base.base.NoViewModel
import com.library.base.config.Constant
import com.we.player.player.exo.ExoPlayerFactory
import com.we.player.render.impl.TextureRenderViewFactory
import com.we.player.view.VideoView
import com.wisn.qm.R
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.beans.UserDirBean
import com.wisn.qm.task.UploadTaskUitls
import com.wisn.qm.ui.netpreview.view.NetListVideoController
import kotlinx.android.synthetic.main.fragment_netpreview.*


class NetPreviewFragment(var data: MutableList<UserDirBean>, var position: Int) : BaseFragment<NoViewModel>(), PreviewCallback {


    var recyclerView: RecyclerView? = null
    var playPosition: Int? = null
    var SelectPosition: Int? = null

    val videoView by lazy {
        var videoview = VideoView(BaseApp.app)
        videoview.renderViewFactory = TextureRenderViewFactory()
        videoview?.mediaPlayer = ExoPlayerFactory()
        videoview?.iViewController = NetListVideoController(BaseApp.app)
        videoview.setLooping(true)
        videoview
    }

    override fun layoutId(): Int {
        return R.layout.fragment_netpreview
    }

    override fun initView(views: View) {
        super.initView(views)
//        var mHomeViewModel = ViewModelProvider(requireActivity(), ViewModelFactory()).get(HomeViewModel::class.java)

        group_content?.visibility = View.GONE

        vp_content?.adapter = NetPreviewAdapter(data, this@NetPreviewFragment)

        vp_content?.setCurrentItem(position, false)
        vp_content?.overScrollMode = View.OVER_SCROLL_NEVER
        vp_content?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

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
                vp_content?.post {
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

        tv_delete?.setOnClickListener {
            //删除
            /* val values = mHomeViewModel.getUserDirlist().value;
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
                             mHomeViewModel.saveMedianInfo(position,false)
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
             }*/
        }
        tv_collection?.setOnClickListener {
            //收藏
            /* val get = data.get( vp_content?.currentItem!!)
             mHomeViewModel.saveMedianInfo(0, get,false)
             ToastUtils.showShort("已经添加到上传任务")*/
        }
        iv_back?.setOnClickListener {
            popBackStack()
        }

        recyclerView = vp_content?.getChildAt(0) as RecyclerView?
    }


    override fun onFetchTransitionConfig(): TransitionConfig {
        return SCALE_TRANSITION_CONFIG
    }


    fun startPlay(position: Int) {
        recyclerView?.children?.forEach {
            if (it.tag is NetPreviewVideoViewHolder) {
                var viewholder = it.tag as NetPreviewVideoViewHolder
                if (position == viewholder.pos) {
                    videoView.release()
                    recycleVideoView()
                    val get = data.get(position)
                    videoView.setUrl(Constant.getImageUrl(get.sha1!!)!!)
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
        if (group_content?.visibility == View.GONE) {
            group_content?.visibility = View.VISIBLE
        } else {
            group_content?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView?.release()
        UploadTaskUitls.exeRequest(Utils.getApp(), UploadTaskUitls.buildUploadRequest())

    }


}