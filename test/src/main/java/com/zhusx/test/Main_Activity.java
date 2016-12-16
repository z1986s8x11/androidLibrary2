package com.zhusx.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhusx.core.helper.Lib_AnimatorHelper;
import com.zhusx.core.utils._Densitys;

public class Main_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSource(view);
            }
        });
    }

    Lib_AnimatorHelper helper;

    public void goSource(View v) {
        if (helper == null) {
            helper = new Lib_AnimatorHelper(this);
        }
        helper.startTop(v, _Densitys.dip2px(this, 30), new Lib_AnimatorHelper.IMakeAnimView() {
            @Override
            public View makeView(Context context) {
                TextView t = new TextView(context);
                t.setText("+1");
                return t;
            }
        });
//        Intent intent = new Intent(this, _PublicActivity.class);
//        intent.putExtra(_PublicActivity._EXTRA_FRAGMENT, P_ProjectFragment.class);
//        startActivity(intent);
//        new A_Activity();
    }
}
