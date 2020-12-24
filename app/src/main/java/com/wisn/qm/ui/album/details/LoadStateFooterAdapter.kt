package com.wisn.qm.ui.album.details

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/12/22 上午11:45
 */
class LoadStateFooterAdapter (private val retry: () -> Unit) :
        LoadStateAdapter<LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(parent, retry)
    }

}