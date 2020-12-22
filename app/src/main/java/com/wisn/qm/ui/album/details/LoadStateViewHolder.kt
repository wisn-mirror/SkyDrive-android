package com.wisn.qm.ui.album.details

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.wisn.qm.R
import kotlinx.android.synthetic.main.item_loadstate.view.*

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/12/22 上午11:40
 */
class LoadStateViewHolder (parent: ViewGroup, var retry: () -> Unit) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loadstate, parent, false)
) {

    fun bindState(loadState: LoadState) {
        when (loadState) {
            is LoadState.Error -> {
                itemView.btn_retry.visibility = View.VISIBLE
                itemView.btn_retry.setOnClickListener {
                    retry()
                }
                Log.d("MainActivity", "error了吧")
            }
            is LoadState.Loading -> {
                itemView.ll_loading.visibility = View.VISIBLE
            }
            else -> {
                Log.d("MainActivity", "--其他的错误")
            }
        }

    }
}