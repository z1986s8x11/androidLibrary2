package com.zhusx.core.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

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

    public interface PermissionRequest {
        void allowPermission(String... permission);

        void notAllowedPermission(String... permission);
    }

    public interface OnPermissionResultListener {
        void registerPermissionResult(ActivityCompat.OnRequestPermissionsResultCallback listener);

        void unregisterPermissionResult(ActivityCompat.OnRequestPermissionsResultCallback listener);
    }
}
