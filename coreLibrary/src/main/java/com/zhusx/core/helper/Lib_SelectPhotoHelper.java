package com.zhusx.core.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.utils._Files;
import com.zhusx.core.utils._Permissions;
import com.zhusx.core.utils._Uris;

import java.io.File;
import java.io.IOException;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/13 9:22
 */
public class Lib_SelectPhotoHelper {
    public static final int ActivityCameraRequestCode = 0x8A1;
    public static final int ActivityPhotoRequestCode = 0x8B2;
    private final int ActivityClopPhotoRequestCode = 0x8C3;
    @Nullable
    private Activity activity;
    @Nullable
    private Fragment fragment;
    private Context context;
    private boolean isClop = false;
    private int photoWidth, photoHeight;
    private File dirFile;
    private File saveFile;

    public Lib_SelectPhotoHelper(@NonNull Activity activity, int width, int height) {
        this.activity = activity;
        this.context = activity;
        if (width > 0 && height > 0) {
            this.photoWidth = width;
            this.photoHeight = height;
            this.isClop = true;
        }
        if (_Files.isExistSDCard()) {
            dirFile = context.getExternalCacheDir();
        } else {
            dirFile = context.getCacheDir();
        }
    }

    public Lib_SelectPhotoHelper(@NonNull Fragment fragment, int width, int height) {
        this.fragment = fragment;
        this.context = activity;
        if (width > 0 && height > 0) {
            this.photoWidth = width;
            this.photoHeight = height;
            this.isClop = true;
        }
        if (_Files.isExistSDCard()) {
            dirFile = context.getExternalCacheDir();
        } else {
            dirFile = context.getCacheDir();
        }
    }

    public Lib_SelectPhotoHelper(@NonNull Activity activity) {
        this(activity, -1, -1);
    }

    public Lib_SelectPhotoHelper(@NonNull Fragment fragment) {
        this(fragment, -1, -1);
    }

    private void startActivityForResult(Intent intent, int requestId) {
        if (fragment == null) {
            activity.startActivityForResult(intent, requestId);
        } else {
            fragment.startActivityForResult(intent, requestId);
        }
    }

    public <T extends Activity & _Permissions.OnPermissionResultListener> void _startSelectPhoto(final T activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            _Permissions._requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, new _Permissions.PermissionRequest() {
                @Override
                public void allowPermission(String... strings) {
                    startSelectPhoto();
                }

                @Override
                public void notAllowedPermission(String... strings) {
                    new AlertDialog.Builder(activity)
                            .setMessage("程序需要获取相册访问权限,用于获取图片")
                            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    _Permissions.goSettingPermission(activity);
                                }
                            })
                            .setNegativeButton("关闭", null)
                            .create()
                            .show();
                }
            });
        } else {
            startSelectPhoto();
        }
    }

    public <T extends Fragment & _Permissions.OnPermissionResultListener> void _startSelectPhoto(final T fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            _Permissions._requestPermission(fragment, Manifest.permission.READ_EXTERNAL_STORAGE, new _Permissions.PermissionRequest() {
                @Override
                public void allowPermission(String... strings) {
                    startSelectPhoto();
                }

                @Override
                public void notAllowedPermission(String... strings) {
                    new AlertDialog.Builder(fragment.getActivity())
                            .setMessage("程序需要获取相册访问权限,用于获取图片")
                            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    _Permissions.goSettingPermission(fragment.getActivity());
                                }
                            })
                            .setNegativeButton("关闭", null)
                            .create()
                            .show();
                }
            });
        } else {
            startSelectPhoto();
        }
    }

    /**
     * 从相册选取
     */
    @Deprecated
    public void startSelectPhoto() {
        saveFile = new File(dirFile, System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, ActivityPhotoRequestCode);
    }

    /**
     * 照像获取图片
     */
    public <T extends Activity & _Permissions.OnPermissionResultListener> void _startTakePhoto(final T activity) {
        _Permissions._requestPermission(activity, Manifest.permission.CAMERA, new _Permissions.PermissionRequest() {
            @Override
            public void allowPermission(String... strings) {
                startTakePhoto();
            }

            @Override
            public void notAllowedPermission(String... strings) {
                new AlertDialog.Builder(activity).setMessage("程序需要获取相机权限,用于摄像头拍摄")
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                _Permissions.goSettingPermission(activity);
                            }
                        })
                        .setNegativeButton("关闭", null)
                        .create()
                        .show();
            }
        });
    }

    public <T extends Fragment & _Permissions.OnPermissionResultListener> void _startTakePhoto(final T fragment) {
        _Permissions._requestPermission(fragment, Manifest.permission.CAMERA, new _Permissions.PermissionRequest() {
            @Override
            public void allowPermission(String... strings) {
                startTakePhoto();
            }

            @Override
            public void notAllowedPermission(String... strings) {
                new AlertDialog.Builder(fragment.getActivity()).setMessage("程序需要获取相机权限,用于摄像头拍摄")
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                _Permissions.goSettingPermission(fragment.getActivity());
                            }
                        })
                        .setNegativeButton("关闭", null)
                        .create()
                        .show();
            }
        });
    }

    /**
     * 照像获取图片
     */
    @Deprecated
    public void startTakePhoto() {
        saveFile = new File(dirFile, System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (saveFile.exists()) {
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, _Uris.fromFile(context, saveFile));
        startActivityForResult(intent, ActivityCameraRequestCode);
    }

    public interface onDataListener {
        void onPhoto(File saveFile);
    }

    public void _onActivityResult(int requestCode, int resultCode, Intent data,
                                  onDataListener listener) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ActivityCameraRequestCode:
                    if (isClop) {
                        gotoClop(saveFile, photoWidth, photoHeight);
                    } else {
                        listener.onPhoto(saveFile);
                    }
                    break;
                case ActivityPhotoRequestCode:
                    Uri uri = data.getData();
                    String filePath;
                    try {
                        filePath = _Files.getUriPath(context, uri);
                        if (!TextUtils.isEmpty(filePath)) {
                            if (isClop) {
                                gotoClop(new File(filePath), photoWidth, photoHeight);
                            } else {
                                File file = new File(filePath);
                                listener.onPhoto(file);
                            }
                        } else {
                            if (LogUtil.DEBUG) {
                                LogUtil.e("_Files.getUriPath(context, uri) at" + String.valueOf(uri) + " is null");
                            }
                        }
                    } catch (SecurityException e) {
                        new AlertDialog.Builder(context)
                                .setMessage("程序需要获取扩展空间访问权限")
                                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        _Permissions.goSettingPermission(context);
                                    }
                                })
                                .setNegativeButton("关闭", null)
                                .create()
                                .show();
                    }
                    break;
                case ActivityClopPhotoRequestCode:
                    Bitmap bm = data.getParcelableExtra("data");
                    if (bm != null) {
                        if (saveFile.exists()) {
                            saveFile.delete();
                        }
                        boolean isSuccess = _Files.saveToFile(bm, saveFile.getPath());
                        if (!bm.isRecycled()) {
                            bm.recycle();
                        }
                        if (isSuccess) {
                            listener.onPhoto(saveFile);
                        }
                    }
                    break;
            }
        }
    }

    private void gotoClop(File file, int width, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(_Uris.fromFile(context, file), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, ActivityClopPhotoRequestCode);
    }

//
//    获取 虚拟文件 流
//    url : https://developer.android.google.cn/about/versions/nougat/android-7.0.html#gles_32
//          https://developer.android.com/about/versions/nougat/android-7.0.html
//
//    final static private int REQUEST_CODE = 64;
//    // We listen to the OnActivityResult event to respond to the user's selection.
//    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
//        try {
//            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//                Uri uri = null;
//                if (resultData != null) {
//                    uri = resultData.getData();
//                    ContentResolver resolver = getContentResolver();
//                    // Before attempting to coerce a file into a MIME type,
//                    // check to see what alternative MIME types are available to
//                    // coerce this file into.
//                    String[] streamTypes = resolver.getStreamTypes(uri, "*/*");
//                    AssetFileDescriptor descriptor =
//                            resolver.openTypedAssetFileDescriptor(
//                                    uri,
//                                    streamTypes[0],
//                                    null);
//                    // Retrieve a stream to the virtual file.
//                    InputStream inputStream = descriptor.createInputStream();
//                }
//            }
//        } catch (Exception ex) {
//            Log.e("EXCEPTION", "ERROR: ", ex);
//        }
//    }
}
