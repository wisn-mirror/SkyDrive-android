package com.wisn.qm.ui.home

import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.blankj.utilcode.util.VibrateUtils
import com.library.base.BaseFragment
import androidx.lifecycle.Observer
import com.library.base.utils.DownloadUtils
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.wisn.qm.R
import com.wisn.qm.task.UploadTaskUitls
import com.wisn.qm.ui.album.newalbum.NewAlbumFragment
import com.wisn.qm.ui.home.adapter.HomePagerAdapter
import com.wisn.qm.ui.home.controller.*
import java.util.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_photo_select_bottom.*
import kotlinx.android.synthetic.main.item_photo_select_bottom.view.*

/**
 * Created by Wisn on 2020/4/30 下午8:03.
 */
class HomeFragment : BaseFragment<HomeViewModel>(), HomeControlListener {
    val addablum: String = "addablum"
    var pictureController: PictureController? = null

    override fun initView(views: View) {
        super.initView(views)
        item_photo_select_bottom.tv_upload.onClick {
            viewModel.saveMedianInfo(0)
            pictureController?.onBackPressedExit()
            ToastUtils.showShort("已经添加到上传任务")
        }
        item_photo_select_bottom.tv_addto.onClick {
            val values = viewModel.getUserDirlist().value;
            values?.let {
                val builder = BottomListSheetBuilder(activity)
                var addItem = View.inflate(context, R.layout.item_album_new_album, null)
                builder.setGravityCenter(true)
                        .setSkinManager(QMUISkinManager.defaultInstance(context))
                        .setTitle("添加到")
                        .setAddCancelBtn(true)
                        .setAllowDrag(true)
                        .setNeedRightMark(true)
                        .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                            dialog.dismiss()
                            viewModel.saveMedianInfo(position)
                            ToastUtils.showShort("已经添加到上传任务")
                            pictureController?.onBackPressedExit();
                        }
                for (dirlist in values) {
//                    builder.addItem(ContextCompat.getDrawable(context!!, R.mipmap.icon_tabbar_lab), "Item $i")
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
        showPictureControl(false)
        initTabs()
        initPager()
        UploadTaskUitls.exeRequest(Utils.getApp(), UploadTaskUitls.buildUploadRequest())
        viewModel.checkUpdate().observe(this,Observer {
            QMUIDialog.MessageDialogBuilder(context)
                    .setTitle("更新提醒")
                    .setSkinManager(QMUISkinManager.defaultInstance(context))
                    .setMessage("更新版本号${it.buildVersion} (build${it.buildBuildVersion})")
                    .addAction("取消") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .addAction("下载") { dialog, _ ->
                        dialog.dismiss()
                        DownloadUtils.addDownload(requireContext(),it.downloadURL!!,"更新","更新版本号${it.buildVersion} (build${it.buildBuildVersion})")
                        ToastUtils.showShort("已添加升级下载任务")
                    }
                    .create(R.style.QMUI_Dialog).show()
        })
    }

    override fun onBackPressed() {
        if (item_photo_select_bottom?.visibility == View.VISIBLE) {
            showPictureControl(false)
            pictureController?.onBackPressedExit();
        } else {
            super.onBackPressed()
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_home;
    }

    private fun initPager() {
        val pagers = HashMap<Pager, BaseHomeController>()
        pictureController = PictureController(requireContext(), this, viewModel)
        pagers[Pager.getPagerByPosition(0)] = pictureController!!
        pagers[Pager.getPagerByPosition(1)] = AlbumController(requireContext(), this, viewModel)
//        pagers[Pager.getPagerByPosition(2)] = ShareController(requireContext(), this, viewModel)
        pagers[Pager.getPagerByPosition(3)] = MineController(requireContext(), this, viewModel)
        with(pager) {
            this?.adapter = HomePagerAdapter(pagers)
            tabs!!.setupWithViewPager(this, false)
            this?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    QMUIStatusBarHelper.setStatusBarLightMode(activity)

                    /* if (position == 1 || position == 2 || position == 3) {
                         QMUIStatusBarHelper.setStatusBarLightMode(activity)
                     } else {
                         QMUIStatusBarHelper.setStatusBarDarkMode(activity)
                     }*/
                    /* if(position==2){
                         startFragment(NetCheckFragment())

                     }*/
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })
        }
    }


    private fun initTabs() {
//        LogUtils.d("initTabs")
        val qmuiTabBuilder = tabs!!.tabBuilder()
        qmuiTabBuilder.setSelectedIconScale(1.1f)
                .setTextSize(QMUIDisplayHelper.sp2px(context, 12), QMUIDisplayHelper.sp2px(context, 14))
                .setDynamicChangeIconColor(false)
        val picture = qmuiTabBuilder
                .setNormalDrawable(ContextCompat.getDrawable(requireContext(), R.mipmap.ic_tab_picture_normal))
                .setSelectedDrawable(ContextCompat.getDrawable(requireContext(), R.mipmap.ic_tab_picture_checked))
                .setText("照片")
                .build(context)
        val album = qmuiTabBuilder
                .setNormalDrawable(ContextCompat.getDrawable(requireContext(), R.mipmap.ic_tab_album_normal))
                .setSelectedDrawable(ContextCompat.getDrawable(requireContext(), R.mipmap.ic_tab_album_checked))
                .setText("云相册")
                .build(context)
        val mine = qmuiTabBuilder
                .setNormalDrawable(ContextCompat.getDrawable(requireContext(), R.mipmap.ic_tab_mine_normal))
                .setSelectedDrawable(ContextCompat.getDrawable(requireContext(), R.mipmap.ic_tab_mine_checked))
                .setText("我的")
                .build(context)
        tabs!!
                .addTab(picture)
                .addTab(album)
                .addTab(mine)

    }

    override fun isShareVM(): Boolean {
        return true
    }

    override fun startFragmentByView(fragment: QMUIFragment?) {
        startFragment(fragment)
    }

    override fun showPictureControl(isShow: Boolean?) {
        isShow?.let {
            with(item_photo_select_bottom) {
                this?.visibility = if (isShow) View.VISIBLE else View.GONE
                pager?.setSwipeable(!isShow)
            }
            if (isShow) {
                VibrateUtils.vibrate(100)
            }
            tabs?.visibility = if (isShow) View.INVISIBLE else View.VISIBLE

        }
    }


}