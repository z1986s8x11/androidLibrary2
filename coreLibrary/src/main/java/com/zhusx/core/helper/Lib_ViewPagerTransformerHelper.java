package com.zhusx.core.helper;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/5/16 11:29
 */
public class Lib_ViewPagerTransformerHelper {
    public static class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.9f;
        private static final float MIN_ALPHA = 0.5f;
        private float defaultScale = 0.9f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                ViewHelper.setAlpha(view, 0);
                ViewHelper.setScaleX(view, defaultScale);
                ViewHelper.setScaleY(view, defaultScale);
            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    ViewHelper.setTranslationX(view, horzMargin - vertMargin / 2);
                } else {
                    ViewHelper.setTranslationX(view, -horzMargin + vertMargin / 2);
                }
                // Scale the page down (between MIN_SCALE and 1)
                ViewHelper.setScaleX(view, scaleFactor);
                ViewHelper.setScaleY(view, scaleFactor);
                // Fade the page relative to its size.
                ViewHelper.setAlpha(view, MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                ViewHelper.setAlpha(view, 0);
                ViewHelper.setScaleX(view, defaultScale);
                ViewHelper.setScaleY(view, defaultScale);
            }
        }
    }
}
