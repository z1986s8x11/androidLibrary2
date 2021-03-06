package com.zhusx.core.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/2/1.13:33
 */
public class _EditTexts {
    /**
     * 显示密码
     */
    public static void showPassword(EditText editText) {
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //第二种实现
        //editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        setSelection(editText);
    }

    /**
     * 隐藏密码
     */
    public static void hidePassword(EditText editText) {
        editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        //第二种实现
        //editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setSelection(editText);
    }

    /**
     * 光标移动到最后
     */
    public static void setSelection(EditText ed) {
        Editable text = ed.getText();
        Selection.setSelection(text, text.length());
    }

    public static void setText(EditText ed, String message) {
        ed.setText(message);
        setSelection(ed);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     */
    public static void hideInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 强制显示
        // imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
        if (isOpen) {
            // 强制隐藏键盘
            // imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                View v = activity.getCurrentFocus();
                if (v != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 键盘自动弹出
     */
    public static void showInputMethod(final EditText mEditText) {
        showInputMethod(mEditText, 500);
    }

    /**
     * 键盘自动弹出
     */
    public static void showInputMethod(final EditText mEditText, long time) {
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) mEditText
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mEditText, 0);
                setSelection(mEditText);
            }
        }, time);
    }

    /**
     * 插入图片
     */
    public static void append(EditText mEditText, String tag, @DrawableRes int res) {
        Spannable spannable = Spannable.Factory.getInstance().newSpannable(tag);
        Drawable drawable = mEditText.getResources().getDrawable(res);
        Paint.FontMetrics FontMetrics = mEditText.getPaint().getFontMetrics();
        int fontHeight = (int) Math.abs(FontMetrics.ascent - FontMetrics.descent);
        drawable.setBounds(0, 0, fontHeight, fontHeight);//这里设置图片的大小
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(imageSpan, 0, tag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannable.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "被点击", Toast.LENGTH_SHORT).show();
//            }
//        }, 0, tag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mEditText.setMovementMethod(LinkMovementMethod.getInstance());
        mEditText.getText().insert(mEditText.getSelectionStart(), spannable);
    }
}
