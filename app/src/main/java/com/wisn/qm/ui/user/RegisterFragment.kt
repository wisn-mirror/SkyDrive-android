package com.wisn.qm.ui.user

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.library.base.BaseFragment
import com.wisn.qm.R
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * Created by Wisn on 2020/6/6 下午5:06.
 */
class RegisterFragment : BaseFragment<UserViewModel>() {


    override fun initView(views: View) {
        super.initView(views)
        initTopBar()
        et_register?.setOnClickListener {

            var phone = et_phone?.text.toString();
            var password = et_password?.text.toString()
            var etPassword2 = et_password2?.text.toString()
            if (phone.isEmpty()) {
                ToastUtils.showShort("请输入手机号")
                return@setOnClickListener;
            }
            if (password.isEmpty()) {
                ToastUtils.showShort("请设置密码")
                return@setOnClickListener;
            }
            if (etPassword2.isEmpty()) {
                ToastUtils.showShort("请再次确认密码")
                return@setOnClickListener;
            }
            viewModel.register(phone, password, et_password.text.toString()).observe(this, Observer {
                ToastUtils.showShort(it)
            })
        }
        login?.setOnClickListener {
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
        val addLeftBackImageButton = topbar?.addLeftBackImageButton();
        addLeftBackImageButton?.setColorFilter(Color.BLACK);
        addLeftBackImageButton?.setOnClickListener { popBackStack() }
        val title = topbar?.setTitle("注册");
        title?.setTextColor(Color.BLACK)
        title?.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

    }

    override fun layoutId(): Int {
        return R.layout.fragment_register;
    }
}