package com.wisn.qm.ui.user

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.library.base.BaseFragment
import com.qmuiteam.qmui.qqface.QMUIQQFaceView
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.MessageDialogBuilder
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.wisn.qm.R
import kotlinx.android.synthetic.main.fragment_setting.*


open class SettingFragment : BaseFragment<UserViewModel>(), View.OnClickListener {
    lateinit var title: QMUIQQFaceView
    val autoUpload by lazy {  groupListView?.createItemView(null, "是否自动同步", null, QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_SWITCH) }
    val lowBatteryUpload by lazy {  groupListView?.createItemView(null, "低电量是否同步", null, QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_SWITCH) }
    val versionItem by lazy {  groupListView?.createItemView(null, "版本号", AppUtils.getAppVersionName(), QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_NONE) }
    val about by lazy {  groupListView?.createItemView(null, "关于APP", " ", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON) }
    override fun layoutId() = R.layout.fragment_setting
    override fun initView(views: View) {
        super.initView(views)
        title =  topbar?.setTitle("设置")!!
        title.setTextColor(Color.BLACK)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        val addLeftBackImageButton =  topbar?.addLeftBackImageButton()
        addLeftBackImageButton?.setColorFilter(Color.BLACK)
        addLeftBackImageButton?.setOnClickListener {
            popBackStack()
        }


        autoUpload?.switch?.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            ToastUtils.showShort(b.toString())
            if (b) {
                autoUpload?.setDetailText("自动")
//                versionItem?.showNewTip(false)
//                versionItem?.showRedDot(false)
//                versionItem?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_NONE


            } else {
                autoUpload?.setDetailText("手动")
//                versionItem?.showNewTip(true)
//                versionItem?.showRedDot(true)
//                versionItem?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
            }
        }
        lowBatteryUpload?.switch?.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            ToastUtils.showShort(b.toString())
            if (b) {
                lowBatteryUpload?.setDetailText("是")
//                versionItem?.showNewTip(false)
//                versionItem?.showRedDot(false)
//                versionItem?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_NONE


            } else {
                lowBatteryUpload?.setDetailText("否")
//                versionItem?.showNewTip(true)
//                versionItem?.showRedDot(true)
//                versionItem?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
            }
        }

        QMUIGroupListView.newSection(context)
                .setTitle("")
                .setDescription("")
                .setLeftIconSize(QMUIDisplayHelper.dp2px(context, 18), ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(autoUpload, this)
                .addItemView(lowBatteryUpload, this)
                .addItemView(versionItem, this)
                .addItemView(about, this)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(context, 18), 0)
                .addTo( groupListView);

    }

    override fun onClick(v: View?) {
        if (v == versionItem) {
            ToastUtils.showShort("当前为最新版本")
        } else if (v == about) {
            MessageDialogBuilder(activity)
                    .setTitle("关于APP")
                    .setSkinManager(QMUISkinManager.defaultInstance(context))
                    .setMessage("专注家庭内部媒体文件同步的app")
                    .addAction("取消") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show()
        }

    }

}