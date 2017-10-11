package com.zhusx.core.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.zhusx.core.debug.LogUtil;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/13 9:33
 */
public class _Systems {

    /**
     * 拿到apk版本
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        if (context == null) {
            return versionName;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (NameNotFoundException e) {
            if (LogUtil.DEBUG) {
                LogUtil.e(e);
            }
        }
        return versionName;
    }

    /**
     * 拿到app 的名称
     */
    public static CharSequence getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            return packageManager.getApplicationLabel(applicationInfo);
        } catch (NameNotFoundException e) {
            if (LogUtil.DEBUG) {
                LogUtil.e(e);
            }
        }
        return "";
    }

    /**
     * 拿到apk version Code
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = -1;
        if (context == null) {
            return versionCode;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (NameNotFoundException e) {
            if (LogUtil.DEBUG) {
                LogUtil.e(e);
            }
        }
        return versionCode;
    }

    /**
     * 复制字符串
     */
    public static void copy(Context context, String str) {
        if (Build.VERSION.SDK_INT >= 11) {
            android.content.ClipboardManager cmb1 = (android.content.ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
            cmb1.setPrimaryClip(ClipData.newPlainText(null, str));
        } else {
            android.text.ClipboardManager cmb2 = (android.text.ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
            cmb2.setText(str);
        }
    }

    /**
     * @return 拿到上一次复制的字符串
     */
    public static String getLastCopyText(Context context) {
        if (Build.VERSION.SDK_INT >= 11) {
            android.content.ClipboardManager cmb1 = (android.content.ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
            return cmb1.getText().toString();
        } else {
            android.text.ClipboardManager cmb2 = (android.text.ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
            return cmb2.getText().toString();
        }
    }

    /**
     * 返回手机服务商名字
     */
    public static String getProvidersName(Context context) {
        String ProvidersName = null;
        if (context == null) {
            return ProvidersName;
        }
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String IMSI = getIMSI(context);
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        } else {
            ProvidersName = "其他服务商";
        }
        return ProvidersName;
    }

    /**
     * @return 手机IMSI号码(国际移动用户识别码)
     */
    public static String getIMSI(Context context) {
        if (context == null) {
            return null;
        }
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // 返回唯一的用户ID;就是这张卡的IMSI编号
        return telephonyManager.getSubscriberId();
    }

    /**
     * 需要 android.permission.READ_PHONE_STATE
     *
     * @return 返回手机ICCID号码(国际移动装备辨识码)
     */
    public static String getICCID(Context context) {
        if (context == null) {
            return null;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // 返回唯一的用户ID;就是这张卡的IMSI编号
            return telephonyManager.getSimSerialNumber();
        } catch (SecurityException e) {
            return null;
        }
    }

    /**
     * @return 手机串号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            return null;
        }
    }

    /**
     * @return 获取设备唯一字符串
     */
    public static String getUUID(Context context) {
        String uuid = getIMEI(context);
        if (TextUtils.isEmpty(uuid) || "0".equals(uuid) || "000000000000000".equals(uuid)) {
            uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (TextUtils.isEmpty(uuid) || "unknown".equals(uuid) || "9774d56d682e549c".equals(uuid)) {
                /*=============兼容老版本==============*/
                //不做兼容 可删除
                SharedPreferences prefs = context.getSharedPreferences("lib_device_id", 0);
                uuid = prefs.getString("device_id", null);
                if (uuid != null) {
                    return uuid;
                }
               /*=====================================*/
                uuid = android.os.Build.SERIAL;
                if (TextUtils.isEmpty(uuid) || "unknown".equals(uuid)) {
                    //SharedPreferences prefs = context.getSharedPreferences("lib_device_id", 0);
                    uuid = prefs.getString("device_id", null);
                    if (uuid == null) {
                        uuid = UUID.randomUUID().toString();
                        prefs.edit().putString("device_id", uuid).apply();
                    }
                }
            }
        }
        return uuid;
    }

    /**
     * 返回本地手机号码，这个号码不一定能获取到
     */
    public static String getNativePhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

    /**
     * 使用Wifi时获取IP
     */
    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public static String getWifiIp(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            if (LogUtil.DEBUG) {
                LogUtil.e("wifi 未开启!");
            }
            //boolean b = wifiManager.setWifiEnabled(true);
            return null;
        }
        if (_Systems.isPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = (ipAddress & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF) + "." + (ipAddress >> 24 & 0xFF);
            return ip;
        } else {
            if (LogUtil.DEBUG) {
                LogUtil.e(" 没有添加权限 permission.ACCESS_WIFI_STATE");
            }
            return null;
        }
    }

    /**
     * 打开Wifi 按钮
     */
    public static boolean openWifeConnect(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            return wifiManager.setWifiEnabled(true);
        }
        return false;
    }

    public static String getGPRSIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtil.w(ex);
        }
        return "";
    }

    /**
     * 用户储存当前屏幕休眠时间
     */
    private static int currentScreenTimeout = -2;
    /**
     * 是否自动亮度
     */
    private static boolean isAutoScreen;

    /**
     * 设置 当前 屏幕超时休眠时间
     */
    @RequiresPermission(Manifest.permission.WRITE_SETTINGS)
    public static void setScreenTimeOut(Context context, int currentScreen) {
        try {
            if (currentScreenTimeout == -2) {
                isAutoScreen = isAutoBrightness(context.getContentResolver());
                if (isAutoScreen) {
                    stopAutoBrightness(context);
                }
                currentScreenTimeout = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, -1);
            }
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, currentScreen);
        } catch (Exception e) {
            LogUtil.w(e);
            if (LogUtil.DEBUG) {
                LogUtil.e("<uses-permission android:name=\"android.permission.WRITE_SETTINGS\"/> ");
            }
        }
    }

    /**
     * 恢复 当前 屏幕超时休眠时间
     */
    @RequiresPermission(Manifest.permission.WRITE_SETTINGS)
    public void restoreScreenTimeOut(Context context) {
        try {
            if (currentScreenTimeout != -2) {
                if (isAutoScreen) {
                    startAutoBrightness(context);
                }
                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, currentScreenTimeout);
                currentScreenTimeout = -2;
            }
        } catch (Exception e) {
            LogUtil.w(e);
            if (LogUtil.DEBUG) {
                LogUtil.e(" 需要权限:<uses-permission android:name=\"android.permission.WRITE_SETTINGS\"/> ");
            }
        }
    }

    /**
     * 判断是否开启了自动亮度调节
     */
    public static boolean isAutoBrightness(ContentResolver aContentResolver) {
        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(aContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            LogUtil.w(e);
        }
        return automicBrightness;
    }

    /**
     * 停止自动亮度调节 需要android.permission.WRITE_SETTINGS
     */
    @RequiresPermission(Manifest.permission.WRITE_SETTINGS)
    public static void stopAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /**
     * 设置自动亮度调节 需要 android.permission.WRITE_SETTINGS
     */
    @RequiresPermission(Manifest.permission.WRITE_SETTINGS)
    public static void startAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    /**
     * 获取屏幕的亮度
     */
    public static int getScreenBrightness(Context context) {
        int nowBrightnessValue = 0;
        try {
            nowBrightnessValue = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            LogUtil.w(e);
        }
        return nowBrightnessValue;
    }

    /**
     * 设置当前屏幕亮度值 0--255
     */
    public static void setScreenBrightness(Context context, int paramInt) {
        if (paramInt < 10) {
            paramInt = 10;
        }
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
            // WindowManager.LayoutParams localLayoutParams = localWindow
            // .getAttributes();
            // float f = paramInt / 255.0F;
            // localLayoutParams.screenBrightness = f;
            // localWindow.setAttributes(localLayoutParams);
        } catch (Exception e) {
            LogUtil.w(e);
        }
    }

    private static Camera camera;

    /**
     * 打开闪光灯 需要
     * android.permission.CAMERA
     * android.permission.FLASHLIGHT"
     * android.hardware.camera"
     * android.hardware.camera.autofocus
     * android.hardware.camera.flash
     */
    @RequiresPermission(Manifest.permission.CAMERA)
    public static void openFlashlight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                String[] ids = manager.getCameraIdList();
                if (ids.length > 0) {
                    manager.setTorchMode(ids[0], true);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            if (camera == null) {
                camera = Camera.open();
                camera.startPreview();
                Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
            }
        }
    }

    /**
     * 关闭闪光灯
     */
    public static void closeFlashlight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                String[] ids = manager.getCameraIdList();
                if (ids.length > 0) {
                    manager.setTorchMode(ids[0], false);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            if (camera != null) {
                Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.release();
                camera = null;
            }
        }
    }

    /**
     * 执行cmd
     */
    public static String exec(String cmd) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        // Process proc = runtime.exec("cmd.exe /c dir");
        Process proc = runtime.exec(cmd);
        String message = _Strings.toString(proc.getInputStream());
        proc.destroy();
        return message;
    }

    /**
     * 检查是否有某权限
     *
     * @param permission android.Manifest.permission.常量
     * @return 是否有此权限
     */
    public static boolean isPermission(Context context, String... permission) {
        try {
            int pCount = permission.length;
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // 得到自己的包名
            String pkgName = pi.packageName;
            PackageInfo pkgInfo = pm.getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS);// 通过包名，返回包信息
            String sharedPkgList[] = pkgInfo.requestedPermissions;// 得到权限列表
            for (int i = 0; i < sharedPkgList.length; i++) {
                String permName = sharedPkgList[i];
                /**
                 * //通过permName得到该权限的详细信息 PermissionInfo tmpPermInfo
                 * =pm.getPermissionInfo(permName, 0);
                 * //权限分为不同的群组，通过权限名，我们得到该权限属于什么类型的权限。 PermissionGroupInfo pgi =
                 * pm.getPermissionGroupInfo(tmpPermInfo.group, 0); tv.append(i
                 * + "-" + permName + "\n"); tv.append(i + "-" +
                 * pgi.loadLabel(pm).toString() + "\n"); tv.append(i + "-" +
                 * tmpPermInfo.loadLabel(pm).toString()+ "\n"); tv.append(i +
                 * "-" + tmpPermInfo.loadDescription(pm).toString()+ "\n");
                 */

                for (int j = 0; j < permission.length; j++) {
                    if (String.valueOf(permName).equalsIgnoreCase(permission[j])) {
                        pCount--;
                        if (pCount == 0) {
                            return true;
                        }
                        continue;
                    }
                }
            }
        } catch (NameNotFoundException e) {
            if (LogUtil.DEBUG) {
                LogUtil.e("包名不存在");
                LogUtil.w(e);
            }

        }
        return false;
    }

    /**
     * 判断当前应用程序是否后台运行
     */
    public static boolean isBackgroundRunable(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // 后台运行
                    return true;
                } else {
                    // 前台运行
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断手机是否处理睡眠
     */
    public static boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        return isSleeping;
    }

    /**
     * 启动对应包名的应用
     */
    public static void startAppByPackageName(Context context, String packageName) {
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String packageName1 = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            ComponentName cn = new ComponentName(packageName1, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }

    }

    /**
     * @return 是否运行在 主线程
     */
    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static String getMetaDataFromApplication(Context activity, String name) {
        try {
            ApplicationInfo appInfo = activity.getPackageManager()
                    .getApplicationInfo(activity.getPackageName(),
                            PackageManager.GET_META_DATA);
            String mTag = appInfo.metaData.getString(name);
            return mTag;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取32位应用签名
     */
    public static String getSignature(Context context) {
        return getSignature(context, context.getPackageName());
    }

    /**
     * 获取32位应用签名
     */
    public static String getSignature(Context context, String packName) {
        char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            /** 通过包管理器获得指定包名包含签名的包信息 **/
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packName, PackageManager.GET_SIGNATURES);
            /******* 通过返回的包信息获得签名数组 *******/
            Signature[] signatures = packageInfo.signatures;
            /******* 循环遍历签名数组拼接应用签名 *******/
            if (signatures.length == 0) {
                return null;
            }
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(signatures[0].toByteArray());
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            int i = 0;
            int j = 0;
            while (true) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                int m = j + 1;
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                j = m + 1;
                arrayOfChar[m] = hexDigits[(k & 0xF)];
                i++;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getCPUCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    // 设置字体
    // Typeface mFace =
    // Typeface.createFromAsset(getContext().getAssets(),"fonts/samplefont.ttf");
    // mPaint.setTypeface(mFace);

    /**
     * 模拟点击返回键
     */
    public static void onBackPressed() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            }
        }).start();
    }

    /**
     * 需要权限 android.permission.write_sms
     */
    public static void writeSms(Context context, String phone, String body) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("address", phone);
        values.put("type", 1);
        values.put("date", System.currentTimeMillis());
        values.put("body", body);
        cr.insert(Uri.parse("content://sms"), values);
    }
}
