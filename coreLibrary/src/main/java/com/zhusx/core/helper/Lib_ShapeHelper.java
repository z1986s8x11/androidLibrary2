package com.zhusx.core.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhusx.core.R;
import com.zhusx.core.debug.LogUtil;


/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/12 16:56
 */
public class Lib_ShapeHelper {
    private static final String XML_NS = "http://schemas.android.com/apk/res/android";

    public static void initShapeDrawable(View view, Context context, AttributeSet attrs) {
        if (view == null || context == null || attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Lib_Widget_TextView);
        GradientDrawable gradientDrawable = new GradientDrawable();
        GradientDrawable gradientDrawable2 = null;
        int status = typedArray.getInt(R.styleable.Lib_Widget_TextView_lib_status, -1);
        if (status != -1) {
            switch (status) {
                case 0:
                    status = android.R.attr.state_pressed;
                    break;
                case 1:
                    status = android.R.attr.state_enabled;
                    break;
                case 2:
                    status = android.R.attr.state_checked;
                    break;
                case 3:
                    status = android.R.attr.state_selected;
                    break;
                default:
                    status = -1;
                    break;
            }
            view.setClickable(true);
            if (status != -1) {
                Drawable background2 = typedArray.getDrawable(R.styleable.Lib_Widget_TextView_lib_background2);
                Drawable background = view.getBackground();
                if (background2 != null && background != null) {
                    StateListDrawable stateListDrawable = new StateListDrawable();
                    switch (status) {
                        case android.R.attr.state_pressed:
                        case android.R.attr.state_selected:
                        case android.R.attr.state_checked:
                            stateListDrawable.addState(new int[]{status}, background2);
                            stateListDrawable.addState(new int[]{-status}, background);
                            stateListDrawable.addState(new int[]{}, background2);
                            break;
                        case android.R.attr.state_enabled:
                            stateListDrawable.addState(new int[]{status}, background);
                            stateListDrawable.addState(new int[]{-status}, background2);
                            stateListDrawable.addState(new int[]{}, background);
                            break;
                    }
                    _setBackgroundDrawable(view, stateListDrawable);
                    typedArray.recycle();
                    return;
                }
            }
            gradientDrawable2 = new GradientDrawable();
        }
        if (status == -1) {
            int solidColor = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_solidColor, -1);
            int strokeWidth = typedArray.getDimensionPixelSize(R.styleable.Lib_Widget_TextView_lib_strokeWidth, -1);
            int gradientStartColor = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_gradientStartColor, -1);
            if (solidColor == -1 && strokeWidth == -1 && gradientStartColor == -1) {
                /*没有颜色改变,默认不进行任何操作*/
                typedArray.recycle();
                return;
            }
        }
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        if (gradientDrawable2 != null) {
            gradientDrawable2.setShape(GradientDrawable.RECTANGLE);
        }
        int radius = typedArray.getDimensionPixelSize(R.styleable.Lib_Widget_TextView_lib_radius, 0);
        if (radius != 0) {
            gradientDrawable.setCornerRadius(radius);
            if (gradientDrawable2 != null) {
                gradientDrawable2.setCornerRadius(radius);
            }
        } else {
            int bottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.Lib_Widget_TextView_lib_bottomLeftRadius, 0);
            int bottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.Lib_Widget_TextView_lib_bottomRightRadius, 0);
            int topLeftRadius = typedArray.getDimensionPixelSize(R.styleable.Lib_Widget_TextView_lib_topLeftRadius, 0);
            int topRightRadius = typedArray.getDimensionPixelSize(R.styleable.Lib_Widget_TextView_lib_topRightRadius, 0);
            if (bottomLeftRadius > 0 || bottomRightRadius > 0 || topLeftRadius > 0 || topRightRadius != 0) {
                //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
                gradientDrawable.setCornerRadii(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius});
                if (gradientDrawable2 != null) {
                    gradientDrawable2.setCornerRadii(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius});
                }
            }
        }
        int strokeColor = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_strokeColor, Color.GRAY);
        int strokeDashGap = typedArray.getDimensionPixelSize(R.styleable.Lib_Widget_TextView_lib_strokeDashGap, 0);
        int strokeDashWidth = typedArray.getDimensionPixelSize(R.styleable.Lib_Widget_TextView_lib_strokeDashWidth, 0);
        int strokeWidth = typedArray.getDimensionPixelSize(R.styleable.Lib_Widget_TextView_lib_strokeWidth, -1);
        if (strokeWidth > 0) {
            gradientDrawable.setStroke(strokeWidth, strokeColor, strokeDashWidth, strokeDashGap);
            if (gradientDrawable2 != null) {
                int strokeColor2 = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_strokeColor2, strokeColor);
                gradientDrawable2.setStroke(strokeWidth, strokeColor2, strokeDashWidth, strokeDashGap);
            }
        }
        int gradientStartColor = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_gradientStartColor, -1);
        int gradientEndColor = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_gradientEndColor, -1);
        if (gradientStartColor != -1 && gradientEndColor != -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                int gradientCenterColor = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_gradientCenterColor, -1);
                int orientation = typedArray.getInt(R.styleable.Lib_Widget_TextView_lib_orientation, -1);
                if (orientation != -1) {
                    switch (orientation) {
                        case 0:
                            gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                            break;
                        case 1:
                            gradientDrawable.setOrientation(GradientDrawable.Orientation.TR_BL);
                            break;
                        case 2:
                            gradientDrawable.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                            break;
                        case 3:
                            gradientDrawable.setOrientation(GradientDrawable.Orientation.BR_TL);
                            break;
                        case 4:
                            gradientDrawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                            break;
                        case 5:
                            gradientDrawable.setOrientation(GradientDrawable.Orientation.BL_TR);
                            break;
                        case 6:
                            gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                            break;
                        case 7:
                            gradientDrawable.setOrientation(GradientDrawable.Orientation.TR_BL);
                            break;
                    }
                    if (gradientDrawable2 != null) {
                        gradientDrawable2.setOrientation(gradientDrawable.getOrientation());
                    }
                }
                if (gradientCenterColor == -1) {
                    gradientDrawable.setColors(new int[]{gradientStartColor, gradientEndColor});
                    if (gradientDrawable2 != null) {
                        int gradientStartColor2 = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_gradientStartColor, gradientStartColor);
                        int gradientEndColor2 = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_gradientEndColor, gradientEndColor);
                        gradientDrawable2.setColors(new int[]{gradientStartColor2, gradientEndColor2});
                    }
                } else {
                    gradientDrawable.setColors(new int[]{gradientStartColor, gradientCenterColor, gradientEndColor});
                    if (gradientDrawable2 != null) {
                        int gradientStartColor2 = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_gradientStartColor, gradientStartColor);
                        int gradientEndColor2 = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_gradientEndColor, gradientEndColor);
                        int gradientCenterColor2 = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_gradientCenterColor, gradientCenterColor);
                        gradientDrawable2.setColors(new int[]{gradientStartColor2, gradientCenterColor2, gradientEndColor2});
                    }
                }
            } else {
                gradientDrawable.setColor(gradientStartColor);
                if (gradientDrawable2 != null) {
                    int gradientStartColor2 = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_gradientStartColor, gradientStartColor);
                    gradientDrawable2.setColor(gradientStartColor2);
                }
            }
        } else {
            int backgroundColor = Color.TRANSPARENT;
            if (view.getBackground() instanceof ColorDrawable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    backgroundColor = ((ColorDrawable) view.getBackground()).getColor();
                }
            }
            int solidColor = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_solidColor, backgroundColor);
            gradientDrawable.setColor(solidColor);
            if (gradientDrawable2 != null) {
                int solidColor2 = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_solidColor2, solidColor);
                gradientDrawable2.setColor(solidColor2);
            }
        }
        if (gradientDrawable2 != null) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            switch (status) {
                case android.R.attr.state_pressed:
                case android.R.attr.state_selected:
                case android.R.attr.state_checked:
                    stateListDrawable.addState(new int[]{status}, gradientDrawable2);
                    stateListDrawable.addState(new int[]{-status}, gradientDrawable);
                    stateListDrawable.addState(new int[]{}, gradientDrawable2);
                    break;
                case android.R.attr.state_enabled:
                    stateListDrawable.addState(new int[]{status}, gradientDrawable);
                    stateListDrawable.addState(new int[]{-status}, gradientDrawable2);
                    stateListDrawable.addState(new int[]{}, gradientDrawable);
                    break;
            }
            _setBackgroundDrawable(view, stateListDrawable);
        } else {
            _setBackgroundDrawable(view, gradientDrawable);
        }
        if (view instanceof TextView) {
            int topDrawableRes = typedArray.getResourceId(R.styleable.Lib_Widget_TextView_lib_drawableTop, -1);
            int rightDrawableRes = typedArray.getResourceId(R.styleable.Lib_Widget_TextView_lib_drawableRight, -1);
            int bottomDrawableRes = typedArray.getResourceId(R.styleable.Lib_Widget_TextView_lib_drawableBottom, -1);
            int leftDrawableRes = typedArray.getResourceId(R.styleable.Lib_Widget_TextView_lib_drawableLeft, -1);
            if (topDrawableRes + rightDrawableRes + bottomDrawableRes + leftDrawableRes != -4) {
                Drawable[] drawables = ((TextView) view).getCompoundDrawables();
                if (leftDrawableRes != -1) {
                    if (drawables[0] == null) {
                        drawables[0] = view.getResources().getDrawable(attrs.getAttributeIntValue(XML_NS, "drawableLeft", -1));
                    }
                    drawables[0] = toDrawable(status, drawables[0], view.getResources().getDrawable(leftDrawableRes));
                }
                if (topDrawableRes != -1) {
                    if (drawables[1] == null) {
                        drawables[1] = view.getResources().getDrawable(attrs.getAttributeIntValue(XML_NS, "drawableTop", -1));
                    }
                    drawables[1] = toDrawable(status, drawables[1], view.getResources().getDrawable(topDrawableRes));
                }
                if (rightDrawableRes != -1) {
                    if (drawables[2] == null) {
                        drawables[2] = view.getResources().getDrawable(attrs.getAttributeIntValue(XML_NS, "drawableRight", -1));
                    }
                    drawables[2] = toDrawable(status, drawables[2], view.getResources().getDrawable(rightDrawableRes));
                }
                if (bottomDrawableRes != -1) {
                    if (drawables[3] == null) {
                        drawables[3] = view.getResources().getDrawable(attrs.getAttributeIntValue(XML_NS, "drawableBottom", -1));
                    }
                    drawables[3] = toDrawable(status, drawables[2], view.getResources().getDrawable(bottomDrawableRes));
                }
                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
            int textColor = ((TextView) view).getCurrentTextColor();
            int textColor2 = typedArray.getColor(R.styleable.Lib_Widget_TextView_lib_textColor2, -1);
            if (textColor2 != -1) {
                ColorStateList colorStateList = null;
                switch (status) {
                    case android.R.attr.state_pressed:
                    case android.R.attr.state_selected:
                    case android.R.attr.state_checked:
                        colorStateList = new ColorStateList(new int[][]{new int[]{status}, new int[]{-status}, new int[]{}}, new int[]{textColor2, textColor, textColor});
                        break;
                    case android.R.attr.state_enabled:
                        colorStateList = new ColorStateList(new int[][]{new int[]{status}, new int[]{-status}, new int[]{}}, new int[]{textColor, textColor2, textColor2});
                        break;
                }
                ((TextView) view).setTextColor(colorStateList);
            }
        }
        typedArray.recycle();
    }

    public static void initImageDrawable(ImageView view, Context context, AttributeSet attrs) {
        if (view == null || context == null || attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Lib_Widget_ImageView);
        int status = typedArray.getInt(R.styleable.Lib_Widget_ImageView_lib_status, -1);
        if (status != -1) {
            switch (status) {
                case 0:
                    status = android.R.attr.state_pressed;
                    break;
                case 1:
                    status = android.R.attr.state_enabled;
                    break;
                case 2:
                    status = android.R.attr.state_checked;
                    break;
                case 3:
                    status = android.R.attr.state_selected;
                    break;
                default:
                    status = -1;
                    break;
            }
            if (status != -1) {
                boolean isBackground = false;
                Drawable src2 = typedArray.getDrawable(R.styleable.Lib_Widget_ImageView_lib_src2);
                if (src2 != null) {
                    Drawable source = view.getDrawable();
                    if (source == null) {
                        source = view.getBackground();
                        isBackground = true;
                    }
                    if (source != null) {
                        StateListDrawable stateListDrawable = new StateListDrawable();
                        switch (status) {
                            case android.R.attr.state_pressed:
                            case android.R.attr.state_selected:
                            case android.R.attr.state_checked:
                                stateListDrawable.addState(new int[]{status}, src2);
                                stateListDrawable.addState(new int[]{-status}, source);
                                stateListDrawable.addState(new int[]{}, source);
                                break;
                            case android.R.attr.state_enabled:
                                stateListDrawable.addState(new int[]{status}, source);
                                stateListDrawable.addState(new int[]{-status}, src2);
                                stateListDrawable.addState(new int[]{}, source);
                                break;
                        }
                        if (isBackground) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                view.setBackground(stateListDrawable);
                            } else {
                                view.setBackgroundDrawable(stateListDrawable);
                            }
                        } else {
                            view.setImageDrawable(stateListDrawable);
                        }
                    }
                }
            }
        }
        typedArray.recycle();
    }

    private static void _setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    private static Drawable toDrawable(int status, Drawable res, Drawable libRes) {
        if (res == null) {
            return libRes;
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        switch (status) {
            case android.R.attr.state_pressed:
            case android.R.attr.state_selected:
            case android.R.attr.state_checked:
                stateListDrawable.addState(new int[]{status}, libRes);
                stateListDrawable.addState(new int[]{-status}, res);
                stateListDrawable.addState(new int[]{}, res);
                break;
            case android.R.attr.state_enabled:
                stateListDrawable.addState(new int[]{status}, res);
                stateListDrawable.addState(new int[]{-status}, libRes);
                stateListDrawable.addState(new int[]{}, res);
                break;
        }
        return stateListDrawable;
    }
}
