package com.library.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.library.base.base.BaseViewModel
import com.library.base.base.ViewModelFactory
import com.library.base.event.Message
import com.squareup.leakcanary.LeakCanary
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    protected lateinit var viewModel: VM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       BaseApp.refwatcher?.watch(this)
        createViewModel()
        lifecycle.addObserver(viewModel)
        registerDefUIChange()
        initView(savedInstanceState)
        lazyLoadData()
    }

    override fun onStart() {
        super.onStart()
    }


    open fun layoutId(): Int {
        return -1
    }

    open fun layoutView(): View? {
        return null
    }

    open fun lazyLoadData() {}
    open fun initView(savedInstanceState: Bundle?) {}
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