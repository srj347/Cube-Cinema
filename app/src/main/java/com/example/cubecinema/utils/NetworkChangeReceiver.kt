package com.example.cubecinema.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.cubecinema.listeners.NetworkChangeListener

class NetworkChangeReceiver : BroadcastReceiver() {

    private var networkChangeListener: NetworkChangeListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        val status: Int = NetworkUtil.getConnectivityStatusString(context)

        if (intent.action == "android.net.conn.CONNECTIVITY_CHANGE") {
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                networkChangeListener?.onNetworkChanged(false)

            } else {
                networkChangeListener?.onNetworkChanged(true)
            }
        }
    }

    fun setNetworkChangeListener(networkChangeListener: NetworkChangeListener) {
        this.networkChangeListener = networkChangeListener
    }

}
