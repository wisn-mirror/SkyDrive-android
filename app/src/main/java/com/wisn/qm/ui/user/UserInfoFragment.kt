package com.wisn.qm.ui.user

import android.graphics.Color
import android.graphics.Typeface
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.library.base.BaseApp
import com.library.base.BaseFragment
import com.library.base.base.ViewModelFactory
import com.library.base.config.GlobalUser
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.qqface.QMUIQQFaceView
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.CheckBoxMessageDialogBuilder
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.wisn.qm.R
import com.wisn.qm.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_userinfo.*

open class UserInfoFragment : BaseFragment<UserViewModel>(), View.OnClickListener {

    lateinit var title: QMUIQQFaceView
    lateinit var username: QMUICommonListItemView
    lateinit var phone: QMUICommonListItemView
    lateinit var registertime: QMUICommonListItemView

    override fun layoutId() = R.layout.fragment_userinfo

    private val homeViewModel: HomeViewModel
        get() {
            var mHomeViewModel = ViewModelProvider(requireActivity(), ViewModelFactory()).get(HomeViewModel::class.java)
            return mHomeViewModel
        }

    override fun initView(views: View) {
        super.initView(views)
        title =   topbar?.setTitle("个人信息")!!
        title.setTextColor(Color.BLACK)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        val addLeftBackImageButton =   topbar?.addLeftBackImageButton()
        addLeftBackImageButton?.setColorFilter(Color.BLACK)
        addLeftBackImageButton?.setOnClickListener {
            popBackStack()
        }

        username =   groupListView?.createItemView(null, "用户名", GlobalUser.userinfo?.user_name
                ?: "", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON)!!
        phone =   groupListView?.createItemView(null, "手机号", GlobalUser.userinfo?.phone
                ?: "", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_NONE)!!
        registertime =   groupListView?.createItemView(null, "注册时间", GlobalUser.userinfo?.signup_at
                ?: "", QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_NONE)!!

        QMUIGroupListView.newSection(context)
                .setTitle("")
                .setDescription("")
                .setLeftIconSize(QMUIDisplayHelper.dp2px(context, 18), ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(username, this)
                .addItemView(phone, this)
                .addItemView(registertime, this)
//                .setOnlyShowMiddleSeparator(true)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(context, 18), 0)
                .addTo(  groupListView);

          rb_loginout?.onClick {
            CheckBoxMessageDialogBuilder(activity)
                    .setTitle("退出后是否删除账号信息?")
                    .setMessage("删除账号信息")
                    .setChecked(true)
                    .setSkinManager(QMUISkinManager.defaultInstance(context))
                    .addAction("取消") { dialog, _ ->

                        dialog.dismiss()
                    }
                    .addAction("退出") { dialog, _ ->
                        viewModel.singout()
                        dialog.dismiss()
                        GlobalUser.clearToken()
                        BaseApp.app.loginEvent()
                    }
                    .create(R.style.QMUI_Dialog).show()
        }
        homeViewModel.updateUserName(null).observe(this, Observer {
            username.setDetailText(it)
        })
    }

    override fun onClick(v: View?) {
        if (v == username) {
            val builder = QMUIDialog.EditTextDialogBuilder(context)
            builder.setTitle("修改昵称")
                    .setSkinManager(QMUISkinManager.defaultInstance(context))
                    .setPlaceholder("在此输入您的昵称")
                    .setInputType(InputType.TYPE_CLASS_TEXT)
                    .addAction("取消") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .addAction("确定") { dialog, _ ->
                        val text: CharSequence = builder.editText.text
                        if (text.isNotEmpty()) {
                            dialog.dismiss()
                            homeViewModel.updateUserName(text.toString())
                        }
                    }
                    .create(R.style.QMUI_Dialog).show()
        }

    }

}