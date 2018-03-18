package com.zhusx.core.imp;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2018/03/18 9:21
 */
public class SimpleTextWatcher implements TextWatcher {
    private TextView tv;

    public SimpleTextWatcher(TextView tv) {
        this.tv = tv;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 0) {
            tv.setEnabled(false);
        } else {
            tv.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
