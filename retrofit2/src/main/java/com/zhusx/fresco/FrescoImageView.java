package com.zhusx.fresco;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.zhusx.core.utils._Intents;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/7/25 14:09
 */

public class FrescoImageView extends SimpleDraweeView {
    public FrescoImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public FrescoImageView(Context context) {
        super(context);
    }

    public FrescoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrescoImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FrescoImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void _setAutoPlayImage(String uri) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(getController())
                .setAutoPlayAnimations(true)
                .build();
        setController(draweeController);
    }

    public void _setAutoPlayImage(@DrawableRes int id) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(_Intents.parseUri(getContext(), id))
                .setOldController(getController())
                .setAutoPlayAnimations(true)
                .build();
        setController(draweeController);
    }

    public void _setImageToListener(String url, String lowUrl) {
        ControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onSubmit(String id, Object callerContext) {
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable anim) {
            }

            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
            }

            @Override
            public void onRelease(String id) {
            }
        };
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(url)
                .setLowResImageRequest(ImageRequest.fromUri(lowUrl))
                .setOldController(getController())
                .setAutoPlayAnimations(true)
                .setControllerListener(controllerListener)
                .build();
        setController(draweeController);
    }
}
