package com.zhusx.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhusx.show.ui.P_ProjectClassFragment;
import com.zhusx.show.ui._PublicActivity;
import com.zhusx.test.test.A_Activity;

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

    public void goSource(View v) {
        Intent intent = new Intent(this, _PublicActivity.class);
        intent.putExtra(_PublicActivity._EXTRA_FRAGMENT, P_ProjectClassFragment.class);
        startActivity(intent);
        new A_Activity();
    }
}
