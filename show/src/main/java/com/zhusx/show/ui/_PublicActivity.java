package com.zhusx.show.ui;

import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;

import com.zhusx.core.R;
import com.zhusx.core.app.Lib_BaseActivity;
import com.zhusx.core.debug.LogUtil;

/**
 * 公共的Activity 依托于传入的Fragment创建UI
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/12 16:20
 */
public final class _PublicActivity extends Lib_BaseActivity {
    public static final String _EXTRA_FRAGMENT = "fragment";
    public static final String _EXTRA_THEME_STYLE_ID = "lib_extra_theme_style_id";
    @StyleRes
    public static int THEME_STYLE_ID = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int themeStyleId = THEME_STYLE_ID;
        if (getIntent().hasExtra(_EXTRA_THEME_STYLE_ID)) {
            try {
                themeStyleId = getIntent().getIntExtra(_EXTRA_THEME_STYLE_ID, THEME_STYLE_ID);
            } catch (Exception e) {
                if (LogUtil.DEBUG) {
                    LogUtil.w(e);
                }
            }
        }
        if (themeStyleId != -1) {
            setTheme(themeStyleId);
        }

        setContentView(R.layout.lib_layout_linearlayout);
        try {
            Class<Fragment> fragmentClass = (Class<Fragment>) getIntent().getSerializableExtra(_EXTRA_FRAGMENT);
            Fragment fragment = fragmentClass.newInstance();
            fragment.setArguments(getIntent().getExtras());
            _replaceFragment(R.id.lib_content, fragment);
        } catch (Exception e) {
            e.printStackTrace();
            _showToast("Fragment 初始化失败");
            finish();
        }
    }
}
