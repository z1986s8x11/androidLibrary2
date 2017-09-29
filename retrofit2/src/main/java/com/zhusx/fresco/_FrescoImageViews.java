package com.zhusx.fresco;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.DrawableRes;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zhusx.core.utils._Uris;
import com.zhusx.retrofit2.R;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/7/25 14:09
 */

public class _FrescoImageViews {

    public static void init(Application application) {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(application)
                .setDownsampleEnabled(true)
                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                .build();
        Fresco.initialize(application, config);
    }

    public void _setAutoPlayImage(SimpleDraweeView imageView, String uri) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(imageView.getController())
                .setAutoPlayAnimations(true)
                .build();
        imageView.setController(draweeController);
    }

    public void _setAutoPlayImage(SimpleDraweeView imageView, @DrawableRes int id) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(_Uris.fromResource(imageView.getContext(), id))
                .setOldController(imageView.getController())
                .setAutoPlayAnimations(true)
                .build();
        imageView.setController(draweeController);
    }

    public void _help(SimpleDraweeView imageView, String url, String lowUrl) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(url)
                .setLowResImageRequest(ImageRequest.fromUri(lowUrl))
                .setImageRequest(ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
//                        .setRotationOptions(RotationOptions.disableRotation())// 取消旋转
                        .setRotationOptions(RotationOptions.autoRotate())// 自动旋转
                        .setPostprocessor(new BasePostprocessor() {
                            @Override
                            public String getName() {
                                return "redMeshPostprocessor";
                            }

                            @Override
                            public void process(Bitmap bitmap) {
                                /* 在这里进行图片处理 */

                            }
                        }) // 设置后处理器
                        .setProgressiveRenderingEnabled(true) // 渐进式JPEG图
                        .setLocalThumbnailPreviewsEnabled(true) // 缩略图预览, 仅支持本地图片URI
                        .build())
                .setOldController(imageView.getController())// 设置旧 Controller
                .setAutoPlayAnimations(true)
                .setTapToRetryEnabled(true) // 点击重新加载图
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                         /* 成功 */
                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                       /* 如果允许呈现渐进式JPEG，同时图片也是渐进式图片，onIntermediateImageSet会在每个扫描被解码后回调 */
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                         /* 失败 */
                    }
                })// 监听下载事件
                .build();
        /*设置 Hierarchy*/
        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
        hierarchy.setPlaceholderImage(R.drawable.ic_launcher); // 修改占位图
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP); // 修改缩放类型
        hierarchy.setActualImageFocusPoint(new PointF(0.5f, 0.5f)); // 居中显示
        hierarchy.setRoundingParams(RoundingParams.fromCornersRadius(10)// 设置圆角
                .setBorder(Color.GREEN, 1) // 设置边框颜色及宽度
                .setOverlayColor(Color.WHITE) // 固定背景颜色
                .setCornersRadii(10, 10, 10, 10) // 指定四个角的圆角度数
                .setRoundAsCircle(false) // 设置为圆圈
        );
        hierarchy.setFailureImage(R.drawable.ic_launcher); // 设置加载失败的占位图
        hierarchy.setRetryImage(R.drawable.ic_launcher); // 设置重试加载的占位图
        hierarchy.setProgressBarImage(new ProgressBarDrawable()); // 图片加载进度条, 如果想精确显示加载进度，需要重写 Drawable.onLevelChange
        hierarchy.setFadeDuration(250); // 淡出效果
        imageView.setHierarchy(hierarchy);

        imageView.setController(draweeController);
    }
}
