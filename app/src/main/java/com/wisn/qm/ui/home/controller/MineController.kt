package com.wisn.qm.ui.home.controller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.library.base.config.Constant
import com.library.base.config.GlobalUser
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.QMUIRadiusImageView2
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.EditTextDialogBuilder
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.wisn.qm.R
import com.wisn.qm.mode.ConstantKey
import com.wisn.qm.ui.album.details.AlbumDetailsFragment
import com.wisn.qm.ui.home.HomeFragment
import com.wisn.qm.ui.home.HomeViewModel
import com.wisn.qm.ui.upload.UploadListFragment
import com.wisn.qm.ui.user.SettingFragment
import com.wisn.qm.ui.user.UserInfoFragment
import java.io.File

/**
 * Created by Wisn on 2020/6/5 下午11:25.
 */
class MineController(context: Context?, mhomeFragment: HomeFragment?, homeViewModel: HomeViewModel?) : BaseHomeController(context, mhomeFragment, homeViewModel), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    val iv_header = findViewById<QMUIRadiusImageView2>(R.id.iv_header)
    val tv_username = findViewById<TextView>(R.id.tv_username)
    val swiperefresh = findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
    val tv_content = findViewById<TextView>(R.id.tv_content)
    val v_header_click = findViewById<View>(R.id.v_header_click)
    val iv_right = findViewById<ImageView>(R.id.iv_right)
    val groupListView = findViewById<QMUIGroupListView>(R.id.groupListView)
    val setting = groupListView?.createItemView(null, "设置", "", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON)
    val collection = groupListView?.createItemView(null, "收藏夹", " ", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON)
    val uploadlist = groupListView?.createItemView(null, "上传列表", " ", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON)
    val delete = groupListView?.createItemView(null, "回收站", " ", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON)
    val pan = groupListView?.createItemView(null, "网盘模式", " ", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON)
    val localvideo = groupListView?.createItemView(null, "离线电影", " ", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON)

    override val layoutId: Int
        get() = R.layout.home_controller_mine

    init {

        with(v_header_click) {
            setOnClickListener {
                this@MineController.onClick(it)
            }
        }
        iv_header.setOnClickListener(this)
        tv_username.setOnClickListener(this)
        swiperefresh?.setOnRefreshListener(this)

        iv_right.background.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        mHomeViewModel.getUserInfo().observe(mHomeFragment, Observer {
            GlobalUser.userinfo?.photo_file_sha1?.let {
                val imageUrl = Constant.getImageUrl(GlobalUser.userinfo!!.photo_file_sha1)
                imageUrl?.let{
                    Glide.with(context!!).load(imageUrl)
                            .apply(RequestOptions())
                            .into(iv_header)
                }
            }
            tv_username.text = GlobalUser.userinfo!!.user_name
            swiperefresh?.isRefreshing = false
        })

        mHomeViewModel.updateUserName(null).observe(mHomeFragment, Observer {
            tv_username.text = it
        })

        QMUIGroupListView.newSection(context)
                .setTitle("")
                .setDescription("")
                .setLeftIconSize(QMUIDisplayHelper.dp2px(context, 18), ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(setting, this)
                .addItemView(uploadlist, this)
                .addItemView(collection, this)
                .addItemView(delete, this)
                .addItemView(pan, this)
                .addItemView(localvideo, this)
//                .setShowSeparator(false)
                .setOnlyShowMiddleSeparator(true)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(context, 18), 0)
                .addTo(groupListView);


    }

    override fun onClick(v: View?) {
        if (v == v_header_click) {
            mHomeControlListener.run {
                mHomeControlListener.startFragmentByView(UserInfoFragment())
            }
        } else if (v == setting) {
            mHomeControlListener.run {
                mHomeControlListener.startFragmentByView(SettingFragment())
            }
        } else if (v == collection) {
            ToastUtils.showShort("开发中")
        }else if (v == uploadlist) {
            val uploadListFragment = UploadListFragment()
            mHomeControlListener.startFragmentByView(uploadListFragment)
        } else if (v == delete) {
            ToastUtils.showShort("开发中")
        } else if (v == tv_username) {
            val builder = EditTextDialogBuilder(context)
            builder.setTitle("修改昵称")
                    .setSkinManager(QMUISkinManager.defaultInstance(context))
                    .setPlaceholder("在此输入您的昵称")
                    .setInputType(InputType.TYPE_CLASS_TEXT)
                    .addAction("取消") { dialog, index -> dialog.dismiss() }
                    .addAction("确定") { dialog, index ->
                        val text: CharSequence = builder.editText.text
                        if (text != null && text.length > 0) {
                            dialog.dismiss()
                            mHomeViewModel.updateUserName(text.toString())
                        }
                    }
                    .create(R.style.QMUI_Dialog).show()
        } else if (v == iv_header) {
            val builder = QMUIBottomSheet.BottomListSheetBuilder(context)
            builder.setGravityCenter(true)
                    .setSkinManager(QMUISkinManager.defaultInstance(context))
//                    .setTitle("添加到")
                    .setAddCancelBtn(true)
                    .setAllowDrag(true)
                    .setNeedRightMark(false)
                    .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                        dialog.dismiss()
                        if (position == 0) {
                            //或者可以用更简单的方法
                            mHomeFragment.registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
                                mHomeViewModel.updateUserPhoto(it)
                                Glide.with(this).load(it).into(iv_header)
                            }.launch(null)
                        } else if (position == 1) {
                            ToastUtils.showShort("开发中")
                        } else {
                            ToastUtils.showShort("开发中")
                        }
                    }
            builder.addItem("拍照")
            builder.addItem("从相册中选择")
            builder.addItem("查看大图")
            builder.build().show()
        } else {
            ToastUtils.showShort("开发中")
        }
    }

    override fun onRefresh() {
        mHomeViewModel.getUserInfo();
    }

}