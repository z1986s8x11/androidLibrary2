package com.zhusx.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhusx.core.app._PublicActivity;
import com.zhusx.show.ui.P_ProjectFragment;

public class Main_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goSource(View v) {
        Intent intent = new Intent(this, _PublicActivity.class);
        intent.putExtra(_PublicActivity._EXTRA_FRAGMENT, P_ProjectFragment.class);
        startActivity(intent);
    }
}
