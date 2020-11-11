package com.wisn.qm.ui.user

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.library.base.BaseFragment
import com.qmuiteam.qmui.kotlin.onClick
import com.wisn.qm.R
import com.wisn.qm.databinding.FragmentLoginBinding
import com.wisn.qm.ui.home.HomeFragment

/**
 * Created by Wisn on 2020/6/6 下午5:06.
 */
class LoginFragment : BaseFragment<UserViewModel, FragmentLoginBinding>() {
    private val TAG: String = "LoginFragment"
    override fun initView(views: View) {
        super.initView(views)
        initTopBar()
//        viewModel.getResult()?.observe(this, Observer { s -> Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show() })
        dataBinding?.login?.setOnClickListener {
            Log.d(TAG, "测试11" + dataBinding?.etPhone?.text.toString())
            var phone = dataBinding?.etPhone?.text.toString();
            var password = dataBinding?.etPassword?.text.toString()
            if (phone.isNullOrEmpty()) {
                ToastUtils.showShort("请输入手机号")
                return@setOnClickListener;
            }
            if (password.isNullOrEmpty()) {
                ToastUtils.showShort("请输入密码")
                return@setOnClickListener;
            }
            viewModel.login(phone, password).observe(this, Observer { ToastUtils.showShort(it) })
        }
        dataBinding?.tvRegister?.onClick {
            startFragment(RegisterFragment())
        }
        viewModel.defUi.msgEvent.observe(this, Observer {
            if (it.code == 100) {
                startFragmentAndDestroyCurrent(HomeFragment(),false)
            }
        })
    }

    private fun initTopBar() {
//        val  addLeftBackImageButton = dataBinding?.topbar?.addLeftBackImageButton();
//        addLeftBackImageButton?.setColorFilter(Color.BLACK);
//        addLeftBackImageButton?.setOnClickListener { popBackStack() }
        val title = dataBinding?.topbar?.setTitle("登录");
        title?.setTextColor(Color.BLACK)
        title?.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

    }

    override fun layoutId(): Int {
        return R.layout.fragment_login;
    }
}