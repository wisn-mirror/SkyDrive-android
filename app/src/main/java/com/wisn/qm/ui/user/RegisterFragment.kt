package com.wisn.qm.ui.user

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.library.base.BaseFragment
import com.qmuiteam.qmui.kotlin.onClick
import com.wisn.qm.R
import com.wisn.qm.databinding.FragmentRegisterBinding

/**
 * Created by Wisn on 2020/6/6 下午5:06.
 */
class RegisterFragment : BaseFragment<UserViewModel, FragmentRegisterBinding>() {


    override fun initView(views: View) {
        super.initView(views)
        initTopBar()
        dataBinding?.etRegister?.onClick {

            var phone = dataBinding?.etPhone?.text.toString();
            var password = dataBinding?.etPassword?.text.toString()
            var etPassword2 = dataBinding?.etPassword2?.text.toString()
            if (phone.isEmpty()) {
                ToastUtils.showShort("请输入手机号")
                return@onClick;
            }
            if (password.isEmpty()) {
                ToastUtils.showShort("请设置密码")
                return@onClick;
            }
            if (etPassword2.isEmpty()) {
                ToastUtils.showShort("请再次确认密码")
                return@onClick;
            }
            viewModel.register(phone, password, dataBinding?.etPassword2?.text.toString()).observe(this, Observer {
                ToastUtils.showShort(it)
            })
        }
        dataBinding?.login?.onClick {
//            startFragment(LoginFragment())
            popBackStack()
        }
        viewModel.defUi.msgEvent.observe(this, Observer {
            if (it.code == 100) {
                popBackStack()
            }
        })
    }

    private fun initTopBar() {
        val addLeftBackImageButton = dataBinding?.topbar?.addLeftBackImageButton();
        addLeftBackImageButton?.setColorFilter(Color.BLACK);
        addLeftBackImageButton?.setOnClickListener { popBackStack() }
        val title = dataBinding?.topbar?.setTitle("注册");
        title?.setTextColor(Color.BLACK)
        title?.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

    }

    override fun layoutId(): Int {
        return R.layout.fragment_register;
    }
}