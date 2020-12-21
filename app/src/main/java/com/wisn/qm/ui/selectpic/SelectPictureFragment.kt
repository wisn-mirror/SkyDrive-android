package com.wisn.qm.ui.selectpic

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.library.base.BaseFragment
import com.qmuiteam.qmui.qqface.QMUIQQFaceView
import com.wisn.qm.R
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.beans.MediaInfo

import kotlinx.android.synthetic.main.fragment_selectfile.*

open class SelectPictureFragment : BaseFragment<SelectPictureViewModel>(), SelectPictureCallBack {
    lateinit var title: QMUIQQFaceView
    lateinit var leftCancel: Button
    lateinit var rightButton: Button
    private val mAdapter by lazy { SelectPictureAdapter(this) }
    lateinit var gridLayoutManager: GridLayoutManager

    override fun layoutId(): Int {
        return R.layout.fragment_selectfile
    }

    override fun initView(views: View) {
        title = topbar?.setTitle("照片库")!!
        title.setTextColor(Color.BLACK)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        leftCancel =topbar?.addLeftTextButton("取消 ", R.id.topbar_right_add_button)!!
        leftCancel.setTextColor(Color.BLACK)
        leftCancel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        leftCancel.visibility = View.VISIBLE
        leftCancel.setOnClickListener {
            popBackStack()
        }
//        val get = arguments?.get(ConstantKey.albuminfo) as UserDirBean

        rightButton = topbar?.addRightTextButton("确定 ", R.id.topbar_right_add_button)!!
        rightButton.setTextColor(Color.BLACK)
        rightButton.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        rightButton.setOnClickListener {
            var intent=Intent()
            val value = viewModel.selectData().value
            value?.let {
                intent.putExtra("data",value)
                setFragmentResult(101,intent )
            }
            popBackStack()
           /* MessageDialogBuilder(activity)
//                    .setTitle("")
                    .setMessage("确定要添加吗？")
                    .setSkinManager(QMUISkinManager.defaultInstance(context))
                    .addAction("取消") { dialog, index -> dialog.dismiss() }
                    .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_POSITIVE) { dialog, index ->
                        dialog.dismiss()
                        viewModel.saveMedianInfo(get)
                        Toast.makeText(activity, "已经添加到上传任务", Toast.LENGTH_SHORT).show()
                        popBackStack()
                    }
                    .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show()*/
        }
        gridLayoutManager = GridLayoutManager(context, 3)
        with(gridLayoutManager) {
            spanSizeLookup = SelectSpanSizeLookup(mAdapter)
        }
        with(recyclerView) {
            this?.layoutManager = gridLayoutManager
            this?.adapter = mAdapter
        }

        viewModel.getMediaImageList().observe(this, Observer {
            mAdapter.setNewInstance(it)
        })

        viewModel.selectData().observe(this, Observer {
            if (it != null && it.size > 0) {
                rightButton.visibility = View.VISIBLE
                title.text = "已选中${it.size}项"
            } else {
                rightButton.visibility = View.GONE
                title.text = "请选择"
            }
        })

    }

    override fun changeSelectData(isAdd: Boolean, item: MediaInfo?) {
        if (item != null) {
            if (isAdd) {
                viewModel.selectData().value?.add(item)
            } else {
                viewModel.selectData().value?.remove(item)
            }
            viewModel.selectData().value = viewModel.selectData().value;
        } else {
            title.text = "已选中${viewModel.selectData.value?.size}项"
        }
    }
}

open class SelectSpanSizeLookup(var adapterV2: SelectPictureAdapter) : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {

        val get = adapterV2.data[position]
        return when (get.itemType) {
            FileType.ImageViewItem -> 1
            FileType.TimeTitle -> 3
            else -> 1
        }

    }

}
