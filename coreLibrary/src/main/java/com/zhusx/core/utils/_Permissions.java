package com.zhusx.core.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

/**
 * 用户Android 6.0权限验证
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/5/5 10:25
 */
public class _Permissions {
    public static <T extends Activity & OnPermissionResultListener> void _requestPermission(final T activity, final String requestPermission, final PermissionRequest listener) {
        final int requestId = listener.hashCode() & 0xFFFF;
        if (ActivityCompat.checkSelfPermission(activity, requestPermission) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isMIUI()) {
                    AppOpsManager appOpsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
                    int checkOp = appOpsManager.checkOpNoThrow(opsPermissionToManifestPermission(requestPermission), Binder.getCallingUid(), activity.getPackageName());
                    switch (checkOp) {
                        //有权限
                        case AppOpsManager.MODE_ALLOWED:
                            //出错了
                        case AppOpsManager.MODE_ERRORED:
                            //没出现过...估计和4一样
                        case AppOpsManager.MODE_DEFAULT:
                            //权限需要询问
                        case 4:
                            listener.allowPermission(requestPermission);
                            break;
                        //被禁止了
                        case AppOpsManager.MODE_IGNORED:
                            activity.registerPermissionResult(new ActivityCompat.OnRequestPermissionsResultCallback() {
                                @Override
                                public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                                    if (requestId != requestCode) {
                                        return;
                                    }
                                    if (permissions.length == 0 || grantResults.length == 0) {
                                        activity.unregisterPermissionResult(this);
                                        listener.notAllowedPermission(permissions);
                                    } else {
                                        for (int i = 0; i < grantResults.length; i++) {
                                            if (permissions[i].equals(requestPermission)) {
                                                activity.unregisterPermissionResult(this);
                                                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                                    listener.allowPermission(permissions);
                                                } else {
                                                    listener.notAllowedPermission(permissions);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestId);
                            break;
                    }
                }
            } else {
                listener.allowPermission(requestPermission);
            }
            return;
        }
        activity.registerPermissionResult(new ActivityCompat.OnRequestPermissionsResultCallback() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestId != requestCode) {
                    return;
                }
                for (int i = 0; i < grantResults.length; i++) {
                    if (permissions[i].equals(requestPermission)) {
                        activity.unregisterPermissionResult(this);
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            listener.allowPermission(permissions);
                        } else {
                            listener.notAllowedPermission(permissions);
                        }
                    }
                }
            }
        });
        ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestId);
    }

    public static <T extends Fragment & OnPermissionResultListener> void _requestPermission(final T fragment, final String requestPermission, final PermissionRequest listener) {
        final int requestId = fragment.hashCode() & 0xFFFF;
        if (ActivityCompat.checkSelfPermission(fragment.getActivity(), requestPermission) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isMIUI()) {
                    AppOpsManager appOpsManager = (AppOpsManager) fragment.getActivity().getSystemService(Context.APP_OPS_SERVICE);
                    int checkOp = appOpsManager.checkOpNoThrow(opsPermissionToManifestPermission(requestPermission), Binder.getCallingUid(), fragment.getActivity().getPackageName());
                    switch (checkOp) {
                        //有权限
                        case AppOpsManager.MODE_ALLOWED:
                            //出错了
                        case AppOpsManager.MODE_ERRORED:
                            //没出现过...估计和4一样
                        case AppOpsManager.MODE_DEFAULT:
                            //权限需要询问
                        case 4:
                            listener.allowPermission(requestPermission);
                            break;
                        //被禁止了
                        case AppOpsManager.MODE_IGNORED:
                            fragment.requestPermissions(new String[]{requestPermission}, requestId);
                            break;
                    }
                }
            } else {
                listener.allowPermission(requestPermission);
            }
            return;
        }
        fragment.registerPermissionResult(new ActivityCompat.OnRequestPermissionsResultCallback() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestId != requestCode) {
                    return;
                }
                for (int i = 0; i < grantResults.length; i++) {
                    if (permissions[i].equals(requestPermission)) {
                        fragment.unregisterPermissionResult(this);
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            listener.allowPermission(permissions);
                        } else {
                            listener.notAllowedPermission(permissions);
                        }
                    }
                }
            }
        });
        fragment.requestPermissions(new String[]{requestPermission}, requestId);
    }

    public static void goSettingPermission(Activity activity) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    private static boolean isMIUI() {
        if (TextUtils.isEmpty(Build.HOST)) {
            return false;
        }
        return Build.HOST.contains("-miui-");
    }

    /**
     * 是否开启 允许通知的权限  -- 主要在华为机型上部分应用默认是关闭的
     */
    public static boolean isNotificationEnabled(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            final String CHECK_OP_NO_THROW = "checkOpNoThrow";
//            final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
//            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Activity.APP_OPS_SERVICE);
//            ApplicationInfo appInfo = context.getApplicationInfo();
//            String pkg = context.getPackageName();
//            int uid = appInfo.uid;
//            try {
//                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
//                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
//                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
//                int value = (int) opPostNotificationValue.get(Integer.class);
//                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static String opsPermissionToManifestPermission(String permission) {
        String checkStr;
        switch (permission) {
            case Manifest.permission.CAMERA:
                checkStr = AppOpsManager.OPSTR_CAMERA;
                break;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                checkStr = AppOpsManager.OPSTR_READ_EXTERNAL_STORAGE;
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                checkStr = AppOpsManager.OPSTR_WRITE_EXTERNAL_STORAGE;
                break;
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                checkStr = AppOpsManager.OPSTR_COARSE_LOCATION;
                break;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                checkStr = AppOpsManager.OPSTR_FINE_LOCATION;
                break;
            case Manifest.permission.READ_PHONE_STATE:
                checkStr = AppOpsManager.OPSTR_READ_PHONE_STATE;
                break;
            case Manifest.permission.RECORD_AUDIO:
                checkStr = AppOpsManager.OPSTR_RECORD_AUDIO;
                break;
            case Manifest.permission.MODIFY_AUDIO_SETTINGS:
                checkStr = AppOpsManager.OPSTR_RECORD_AUDIO;
                break;
            case Manifest.permission.CALL_PHONE:
                checkStr = AppOpsManager.OPSTR_CALL_PHONE;
                break;
            case Manifest.permission.READ_CONTACTS:
                checkStr = AppOpsManager.OPSTR_READ_CONTACTS;
                break;
            case Manifest.permission.WRITE_CONTACTS:
                checkStr = AppOpsManager.OPSTR_WRITE_CONTACTS;
                break;
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                checkStr = AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW;
                break;
            case Manifest.permission.WRITE_SETTINGS:
                checkStr = AppOpsManager.OPSTR_WRITE_SETTINGS;
                break;
            case Manifest.permission.READ_SMS:
                checkStr = AppOpsManager.OPSTR_READ_SMS;
                break;
            case Manifest.permission.SEND_SMS:
                checkStr = AppOpsManager.OPSTR_SEND_SMS;
                break;
            default:
                throw new NullPointerException("未收集到对应[" + permission + "]的AppOpsManager.OPSTR 常量");
        }
        return checkStr;
    }

    public interface PermissionRequest {
        void allowPermission(String... permission);

        void notAllowedPermission(String... permission);
    }

    public interface OnPermissionResultListener {
        void registerPermissionResult(ActivityCompat.OnRequestPermissionsResultCallback listener);

        void unregisterPermissionResult(ActivityCompat.OnRequestPermissionsResultCallback listener);
    }
}
