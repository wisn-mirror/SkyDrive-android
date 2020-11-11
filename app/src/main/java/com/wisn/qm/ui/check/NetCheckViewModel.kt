package com.wisn.qm.ui.check

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.library.base.BaseApp
import com.library.base.base.BaseViewModel
import com.library.base.net.broadcast.BroadcastAll
import com.library.base.net.broadcast.BroadcastGroup
import com.library.base.net.broadcast.MessageCall
import com.library.base.net.broadcast.UdpConfig
import com.library.base.utils.NetCheckUtils
import com.wisn.qm.mode.net.ApiNetWork
import java.util.*
import kotlin.concurrent.fixedRateTimer

class NetCheckViewModel : BaseViewModel() {
    private var result: MutableLiveData<String?>? = null
    private var fixedRateTimer: Timer? = null
    private val TAG = "CheckNetViewModel"
    var isSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun initBroadcastListener() {
        try {
            var broadCastGroup = BroadcastGroup.getInstance()
            broadCastGroup?.listenerMessage(UdpConfig.groupIp, UdpConfig.Group_ClientListenport, MessageCall { message, ip ->
                setResult("group $message ", ip)
            })
            var broadCastAll = BroadcastAll.getInstance()
            broadCastAll?.listenerMessage(UdpConfig.All_ClientListenport, MessageCall { message, ip ->
                setResult("broadcast $message ", ip)
            })
            //            WifiManager wifiManager = (WifiManager) App.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            val wifiManager = BaseApp.app.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
            var multicastLock = wifiManager.createMulticastLock("multicast.test")
            fixedRateTimer = fixedRateTimer("", false, 200, 3000) {
                multicastLock!!.acquire()
                broadCastGroup!!.sendBackMessage(UdpConfig.groupIp, UdpConfig.Group_ServerportRecevie, "AABBC")
                LogUtils.d(TAG, "发送 探针 ${Thread.currentThread().name}")
                getResult().postValue("${System.currentTimeMillis()} 发送 探针...")
                broadCastAll!!.sendBackMessage(UdpConfig.groupALL, UdpConfig.All_ServerportRecevie, "AABBC")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setResult(message: String?, ip: String) {
        LogUtils.d(TAG, " MESSAGE:$message ip:$ip")
        fixedRateTimer?.cancel()
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            getResult().setValue(" $message ip:$ip   ${Thread.currentThread().name}")
            setServerIp(ip)
        }
    }

    fun getResult(): MutableLiveData<String?> {
        if (result == null) {
            result = MutableLiveData<String?>()
        }
        return result!!
    }

    fun setServerIp(ip: String) {
//        launchGo()
        launchGoLo({
            LogUtils.d(TAG, Thread.currentThread().name)
            if (NetCheckUtils.isConnectByIp(ip)) {
                ApiNetWork.newInstance().updateBaseUrl(ip)
                LogUtils.d(TAG, Thread.currentThread().name)
                isSuccess.postValue(true)
            }
        })

    }


}