package com.zhusx.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;

/**
 * 用户Android 6.0权限验证
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/5/5 10:25
 */
public class _Permissions {
    public static <T extends Activity & OnPermissionResultListener> void _requestPermission(final T activity, final String requestPermission, final PermissionRequest listener) {
        if (ActivityCompat.checkSelfPermission(activity, requestPermission) == PackageManager.PERMISSION_GRANTED) {
            listener.allowPermission(requestPermission);
            return;
        }
        final int requestId = activity.hashCode() & 0xFFFF;
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
        if (ActivityCompat.checkSelfPermission(fragment.getActivity(), requestPermission) == PackageManager.PERMISSION_GRANTED) {
            listener.allowPermission(requestPermission);
            return;
        }
        final int requestId = fragment.hashCode() & 0xFFFF;
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

    public interface PermissionRequest {
        void allowPermission(String... permission);

        void notAllowedPermission(String... permission);
    }

    public interface OnPermissionResultListener {
        void registerPermissionResult(ActivityCompat.OnRequestPermissionsResultCallback listener);

        void unregisterPermissionResult(ActivityCompat.OnRequestPermissionsResultCallback listener);
    }
}
