package com.zhusx.show;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhusx.core.adapter.Lib_BaseAdapter;
import com.zhusx.core.app.Lib_BaseActivity;
import com.zhusx.core.app._PublicActivity;
import com.zhusx.core.widget.slidingmenu.SlidingMenu;
import com.zhusx.show.ui.P_SourceCodeFragment;

import java.util.Arrays;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/8 13:50
 */

public class Lib_SourceCodeManager {
    private static Lib_SourceCodeManager sourceCodeManager;

    private Lib_SourceCodeManager(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(final Activity activity) {
                if (activity.getClass().getName().startsWith(activity.getPackageName())) {
                    if (!SlidingMenu.class.getSimpleName().equals(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0).getClass().getSimpleName())) {
                        SlidingMenu mSlidingMenu = new SlidingMenu(activity, SlidingMenu.SLIDING_CONTENT);
                        ListView listView = new ListView(activity);
                        listView.setAdapter(new Lib_BaseAdapter<String>(Arrays.asList(activity.getClass().getSimpleName())) {
                            @Override
                            public View getView(LayoutInflater inflater, String bean, int position, View convertView, ViewGroup parent) {
                                View[] vs = _getViewArrays(convertView, parent, R.layout.lib_list_item_1);
                                _toTextView(vs[0]).setText(bean);
                                vs[0].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent in = new Intent(v.getContext(), _PublicActivity.class);
                                        in.putExtra(_PublicActivity._EXTRA_FRAGMENT, P_SourceCodeFragment.class);
                                        in.putExtra(Lib_BaseActivity._EXTRA_String, "java/" + activity.getClass().getName().replace(".", "/") + ".java");
                                        activity.startActivity(in);
                                    }
                                });
                                return vs[0];
                            }
                        });
                        mSlidingMenu.setMenu(listView);
                        mSlidingMenu.setBehindWidth(600);
                        mSlidingMenu.setMode(SlidingMenu.RIGHT);
                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static class Builder {
        Application application;

        public Builder(Application application) {
            this.application = application;
        }

        public Lib_SourceCodeManager build() {
            if (sourceCodeManager == null) {
                sourceCodeManager = new Lib_SourceCodeManager(application);
            }
            return sourceCodeManager;
        }
    }
}
