package com.wisn.qm.ui.check

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.library.base.BaseFragment
import com.library.base.config.GlobalUser
import com.qmuiteam.qmui.arch.QMUIFragmentActivity
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.qqface.QMUIQQFaceView
import com.wisn.qm.R
import com.wisn.qm.databinding.FragmentNetcheckBinding
import com.wisn.qm.ui.MainActivity
import com.wisn.qm.ui.home.HomeFragment
import com.wisn.qm.ui.user.LoginFragment


/**
 * Created by Wisn on 2020/6/6 下午5:08.
 */
class NetCheckFragment : BaseFragment<NetCheckViewModel, FragmentNetcheckBinding>() {
    lateinit var title: QMUIQQFaceView
    var ipAddressByWifi: String? = null
    override fun layoutId(): Int {
        return R.layout.fragment_netcheck
    }

    private fun initTopBar() {
        title = dataBinding?.topbar?.setTitle("查找服务器")!!
        title.setTextColor(Color.BLACK)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        val addLeftBackImageButton = dataBinding?.topbar?.addLeftBackImageButton()
        addLeftBackImageButton?.setColorFilter(Color.BLACK)
        addLeftBackImageButton?.setOnClickListener {
            popBackStack()
        }
    }

    override fun initView(views: View) {
        super.initView(views)
        initTopBar()
        dataBinding?.selectOk?.onClick {
//            viewModel.checkNet()
            LogUtils.d("getNetinfo 222", Thread.currentThread().name)
            val sp1 = dataBinding?.spinner1?.selectedItem.toString()
            val sp2 = dataBinding?.spinner2?.selectedItem.toString()
            val sp3 = dataBinding?.spinner3?.selectedItem.toString()
            val sp4 = dataBinding?.spinner4?.selectedItem.toString()
            if (sp1.isNullOrEmpty() || sp2.isNullOrEmpty() || sp3.isNullOrEmpty() || sp4.isNullOrEmpty()) {
                return@onClick
            }
            viewModel.setServerIp("$sp1.$sp2.$sp3.$sp4")
        }
        viewModel.getResult().observe(this, Observer {
            val text = dataBinding?.result?.text;
            dataBinding?.result?.text = "$text \n$it"
        })
        dataBinding?.result?.setMovementMethod(ScrollingMovementMethod.getInstance());
        viewModel.initBroadcastListener()
        val wifiConnected = NetworkUtils.isWifiConnected()
        if (wifiConnected) {
            ipAddressByWifi = NetworkUtils.getIpAddressByWifi();
            dataBinding?.tvInfo?.text = "当前Wifi IP:${ipAddressByWifi}"
        } else {
            dataBinding?.tvInfo?.text = "请连接Wifi"
        }
        getContext()?.let {
            val split = ipAddressByWifi?.split(".")
            setAdapter(it, dataBinding?.spinner1!!, 1, null, split?.get(0))
            setAdapter(it, dataBinding?.spinner2!!, 2,  split?.get(0), split?.get(1))
            setAdapter(it, dataBinding?.spinner3!!, 3, null, split?.get(2))
            setAdapter(it, dataBinding?.spinner4!!, 4, null, split?.get(3))
            dataBinding?.spinner1!!.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                    val info = adapterView.getItemAtPosition(i).toString() //获取i所在的文本
                    setAdapter(it, dataBinding?.spinner2!!, 2, info, split?.get(1))
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            })

        }
        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                if (GlobalUser.token.isNullOrEmpty()) {
                    startFragmentAndDestroyCurrent(LoginFragment(), false)
                } else {
                    startFragmentAndDestroyCurrent(HomeFragment(), false)
                }
            }
        })
        dataBinding?.rgModel?.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if (checkedId == R.id.radio_Button_input) {
                    dataBinding?.group?.visibility = View.VISIBLE
                } else {
                    dataBinding?.group?.visibility = View.GONE
                }
            }
        })
        dataBinding?.group?.visibility = View.GONE
    }

    fun setAdapter(context: Context, spanner: Spinner, position: Int, lastSelect: String?, selectStr: String?) {
        var spinnerItems: MutableList<String> = ArrayList<String>();
        if (position == 1) {
            spinnerItems = mutableListOf("10", "172", "192")
        } else if (position == 2) {
            if ("10".equals(lastSelect)|| "192".equals(lastSelect)) {
                for (index in 0 until 256) {
                    spinnerItems.add(index.toString())
                }
            } else if ("172".equals(lastSelect)) {
                for (index in 16 until 32) {
                    spinnerItems.add(index.toString())
                }
            }
        } else if (position == 3 || position == 4) {
            for (index in 0 until 256) {
                spinnerItems.add(index.toString())
            }
        }
        var spinnerAdapter = ArrayAdapter<String>(context, R.layout.item_spinner_textview, spinnerItems)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_textview)
        spanner.setAdapter(spinnerAdapter)
        selectStr?.let {
            val indexOf = spinnerItems.indexOf(selectStr)
            if (indexOf >= 0) {
                spanner.setSelection(indexOf)
            }
        }

    }
}