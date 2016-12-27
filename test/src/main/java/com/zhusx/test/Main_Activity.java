package com.zhusx.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

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
    }
}
