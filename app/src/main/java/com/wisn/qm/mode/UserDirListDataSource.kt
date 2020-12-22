package com.wisn.qm.mode

import android.util.Log
import androidx.paging.PagingSource
import com.wisn.qm.mode.bean.PageKey
import com.wisn.qm.mode.db.beans.UserDirBean
import com.wisn.qm.mode.net.ApiNetWork
import java.io.IOException
import java.lang.Exception

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/12/22 上午11:48
 */

class UserDirListDataSource : PagingSource<PageKey, UserDirBean>() {
    override suspend fun load(params: LoadParams<PageKey>): LoadResult<PageKey, UserDirBean> {

        return try {
            //页码未定义置为1
            var currentPage = params.key ?: PageKey(-1, 0, pageSize = 20);
            //仓库层请求数据
            Log.d("MainActivity", "请求第${currentPage}页")
            val dirlist = ApiNetWork.newInstance().getUserDirlist(currentPage.pid, currentPage.pageSize, currentPage.lastId)
            var nextPage = if (dirlist.isSuccess() && dirlist.data != null && currentPage.lastId != dirlist.data.nextpageid) {
                PageKey(currentPage.pid, dirlist.data.nextpageid, pageSize = 20);
            } else {
                null
            }
            LoadResult.Page(
                    data = dirlist.data.list,
                    prevKey = null,
                    nextKey = nextPage
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is IOException) {
                Log.d("测试错误数据", "-------连接失败")
            }
            Log.d("测试错误数据", "-------${e.message}")
            LoadResult.Error(throwable = e)
        }

    }

}