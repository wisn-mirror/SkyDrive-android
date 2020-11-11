package com.wisn.qm.ui.preview

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.library.base.BaseFragment
import com.library.base.base.NoViewModel
import com.library.base.base.ViewModelFactory
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.wisn.qm.R
import com.wisn.qm.databinding.FragmentPreviewBinding
import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.ui.album.newalbum.NewAlbumFragment
import com.wisn.qm.ui.home.HomeViewModel

class PreviewFragment(var data: MutableList<MediaInfo>, var position: Int) : BaseFragment<NoViewModel, FragmentPreviewBinding>(), PreviewCallback {

    override fun layoutId(): Int {
        return R.layout.fragment_preview
    }

    override fun initView(views: View) {
        super.initView(views)
        var mHomeViewModel = ViewModelProvider(requireActivity(), ViewModelFactory()).get(HomeViewModel::class.java)

        dataBinding?.groupContent?.visibility = View.GONE

        dataBinding?.vpContent?.adapter = PreviewAdapter(data, this@PreviewFragment)

        dataBinding?.vpContent?.setCurrentItem(position, false)

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
                        .setOnSheetItemClickListener { dialog, itemView, position, tag  ->
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

    }

    override fun onFetchTransitionConfig(): TransitionConfig {
        return SCALE_TRANSITION_CONFIG
    }
    override fun onContentClick(view: View) {
        if (dataBinding?.groupContent?.visibility == View.GONE) {
            dataBinding?.groupContent?.visibility = View.VISIBLE
        } else {
            dataBinding?.groupContent?.visibility = View.GONE
        }
    }


}