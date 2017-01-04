package com.zhusx.core.network;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.utils._Systems;
import com.zhusx.core.utils._Networks;

/**
 * 需要开启权限 android.permission.CHANGE_NETWORK_STATE
 *              android.permission.CHANGE_WIFI_STATE
 *              android.permission.ACCESS_NETWORK_STATE
 *              android.permission.ACCESS_WIFI_STATE
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/12 16:42
 */
public class Lib_NetworkStateReceiver extends BroadcastReceiver {
    public static _Networks.NetType _Current_NetWork_Status = _Networks.NetType.Default;
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
            if (LogUtil.DEBUG) {
                LogUtil.d(Lib_NetworkStateReceiver.this, "网络状态改变.");
            }
            if (!_Networks.isNetworkConnected(context)) {
                if (LogUtil.DEBUG) {
                    LogUtil.d(Lib_NetworkStateReceiver.this, "没有网络连接.");
                }
                _Current_NetWork_Status = _Networks.NetType.NoneNet;
            } else {
                _Current_NetWork_Status = _Networks.getAPNType(context);
                if (LogUtil.DEBUG) {
                    LogUtil.d(Lib_NetworkStateReceiver.this, "网络连接成功." + _Current_NetWork_Status.name());
                }
            }
        }
    }

    /**
     * 注册网络状态广播
     */
    public void registerNetworkStateReceiver(Context mContext) {
        if (_Systems.isPermission(mContext, Manifest.permission.ACCESS_NETWORK_STATE)) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ANDROID_NET_CHANGE_ACTION);
            mContext.getApplicationContext().registerReceiver(this, filter);
        } else {
            if (LogUtil.DEBUG) {
                LogUtil.e(this, "需要权限:" + Manifest.permission.ACCESS_NETWORK_STATE);
            }
        }
    }

    /**
     * 注销网络状态广播
     */
    public void unRegisterNetworkStateReceiver(Context mContext) {
        mContext.getApplicationContext().unregisterReceiver(this);
    }
}