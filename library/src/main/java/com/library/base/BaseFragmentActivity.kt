package com.library.base

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.library.base.base.BaseViewModel
import com.library.base.base.ViewModelFactory
import com.library.base.event.Message
import com.qmuiteam.qmui.arch.QMUIFragmentActivity
import java.lang.reflect.ParameterizedType

abstract class BaseFragmentActivity<VM : BaseViewModel, DB : ViewDataBinding> : QMUIFragmentActivity() {

    protected lateinit var viewModel: VM

    protected var dataBinding: DB? = null

    override fun onCreateRootView(fragmentContainerId: Int): RootView {
        val cla = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cla && ViewDataBinding::class.java.isAssignableFrom((cla))) {
            dataBinding = layoutView()?.let { DataBindingUtil.getBinding(it) }
        }
        createViewModel()
        lifecycle.addObserver(viewModel)
        registerDefUIChange()
        initView()
        lazyLoadData()
        return super.onCreateRootView(fragmentContainerId)
    }


    open fun layoutView(): RootView? {
        return null
    }

    open fun lazyLoadData() {}
    open fun initView() {}
    open fun handleEvent(msg: Message) {}


    private fun registerDefUIChange() {
        viewModel.defUi.showDialog.observe(this, Observer {
//            ToastUtils.showShort("show")
        })
        viewModel.defUi.disDialog.observe(this, Observer {
//            ToastUtils.showShort("diss")

        })
        viewModel.defUi.toastEvent.observe(this, Observer {
            ToastUtils.showShort(it)
        })
        viewModel.defUi.msgEvent.observe(this, Observer {
            handleEvent(it)
        })
    }

    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, ViewModelFactory()).get(tClass) as VM
        }
    }

}