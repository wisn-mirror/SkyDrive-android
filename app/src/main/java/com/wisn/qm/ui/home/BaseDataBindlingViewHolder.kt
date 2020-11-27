package com.wisn.qm.ui.home

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by Wisn on 2020/6/6 下午6:18.
 */
open class BaseDataBindlingViewHolder(view: View) : BaseViewHolder(view) {
    var dataBinding: ViewDataBinding? = null

    fun <DB> getDataBinding(): DB? {
        return dataBinding as? DB
    }

    fun <DB : ViewDataBinding> setDataBinding(view: View) {
        dataBinding = DataBindingUtil.bind<DB>(view)
    }


}