package com.zhusx.core.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhusx.core.R;

/**
 * 公共的Activity 依托于传入的Fragment创建UI
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/12 16:20
 */
public final class _PublicActivity extends Lib_BaseActivity {
    public static final String _EXTRA_FRAGMENT = "fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
