package com.wisn.qm.ui.album.newalbum

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.library.base.BaseFragment
import com.qmuiteam.qmui.qqface.QMUIQQFaceView
import com.wisn.qm.R
import com.wisn.qm.mode.ConstantKey
import com.wisn.qm.ui.album.AlbumViewModel
import kotlinx.android.synthetic.main.fragment_newalbum.*


class NewAlbumFragment : BaseFragment<AlbumViewModel>() {
    lateinit var title: QMUIQQFaceView
    lateinit var leftCancel: Button

    override fun layoutId(): Int {
        return R.layout.fragment_newalbum
    }

    override fun initView(views: View) {
        super.initView(views)
        title = topbar?.setTitle("新建相册")!!
        title.setTextColor(Color.BLACK)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        leftCancel = topbar?.addLeftTextButton("取消 ", R.id.topbar_right_add_button)!!
        leftCancel.setTextColor(Color.BLACK)
        leftCancel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        leftCancel.visibility = View.VISIBLE
        leftCancel.setOnClickListener {
            popBackStack()
        }
        var right = topbar?.addRightTextButton("添加 ", R.id.topbar_right_add_button)!!
        right.setTextColor(Color.BLACK)
        right.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        right.setOnClickListener {
            val text = et_albumName?.text?.trim()
            if (text.isNullOrEmpty()) {

                ToastUtils.showShort("请输入相册名称")
            } else {
                text.run {
                    hideSoftInput(et_albumName?.windowToken)
                    viewModel.addUserDir(text.toString()).observe(this@NewAlbumFragment, Observer {
//                    ToastUtils.showShort(it.toString())
                        LiveEventBus
                                .get(ConstantKey.updateAlbum)
                                .post(1);
                        popBackStack()
                    })
                }
            }

        }

    }

}