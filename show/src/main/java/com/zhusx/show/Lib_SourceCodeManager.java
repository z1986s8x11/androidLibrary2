package com.zhusx.show;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhusx.core.adapter.Lib_BaseAdapter;
import com.zhusx.core.utils._Activitys;
import com.zhusx.core.widget.slidingmenu.SlidingMenu;
import com.zhusx.show.html.P_UncaughtException;
import com.zhusx.show.ui.P_ProjectClassFragment;
import com.zhusx.show.ui.P_SourceCodeFragment;
import com.zhusx.show.ui._PublicActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/8 13:50
 */

public class Lib_SourceCodeManager {
    private static Lib_SourceCodeManager sourceCodeManager;

    private Lib_SourceCodeManager(Application application, final Builder builder) {
        final String highlightPackageName = builder.highlightPackageName;
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(final Activity activity, Bundle bundle) {
                List<Class> list;
                if (activity instanceof _PublicActivity) {
                    if (activity.getIntent() == null) {
                        return;
                    }
                    Class serializable = (Class) activity.getIntent().getSerializableExtra(_PublicActivity._EXTRA_FRAGMENT);
                    if (serializable == null || serializable == P_ProjectClassFragment.class || serializable == P_SourceCodeFragment.class) {
                        return;
                    }
                    list = new ArrayList<>();
                    list.add(serializable);
                } else {
                    if (!activity.getClass().getName().startsWith(highlightPackageName)) {
                        return;
                    }
                    list = new ArrayList<>();
                    list.add(activity.getClass());
                }
//                if (activity instanceof FragmentActivity) {
//                    List<Fragment> fragments = ((FragmentActivity) activity).getSupportFragmentManager().getFragments();
//                    if (!_Lists.isEmpty(fragments)) {
//                        for (int i = 0; i < fragments.size(); i++) {
//                            list.add(fragments.get(i).getClass());
//                        }
//                    }
//                }
                SlidingMenu mSlidingMenu = new SlidingMenu(activity, SlidingMenu.SLIDING_WINDOW);
                ListView listView = new ListView(activity);
                listView.setAdapter(new Lib_BaseAdapter<Class>(list) {
                    @Override
                    public View getView(LayoutInflater inflater, final Class bean, int position, View convertView, ViewGroup parent) {
                        View[] vs = _getViewArrays(convertView, parent, R.layout.lib_list_item_1);
                        _toTextView(vs[0]).setText(bean.getSimpleName());
                        vs[0].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in = new Intent(v.getContext(), _PublicActivity.class);
                                in.putExtra(_PublicActivity._EXTRA_FRAGMENT, P_SourceCodeFragment.class);
                                in.putExtra(_Activitys._EXTRA_String_ID, "java/" + bean.getName().replace(".", "/") + ".java");
                                in.putExtra(_Activitys._EXTRA_String, highlightPackageName);
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

            @Override
            public void onActivityStarted(final Activity activity) {
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
        String highlightPackageName;
        private boolean uncaughtException;

        public Builder(Application application) {
            this.application = application;
        }

        public Builder setHighlightPackageName(String highlightPackageName) {
            this.highlightPackageName = highlightPackageName;
            return this;
        }

        /**
         * 设置是否启用捕获全局异常
         */
        public Builder setUncaughtException(boolean uncaught) {
            this.uncaughtException = uncaught;
            return this;
        }

        public Lib_SourceCodeManager build() {
            if (sourceCodeManager == null) {
                sourceCodeManager = new Lib_SourceCodeManager(application, this);
            }
            if (highlightPackageName == null) {
                highlightPackageName = application.getPackageName();
            }
            if (uncaughtException) {
                /* 监听全局异常 */
                P_UncaughtException._getInstance()._init(application);
                StrictMode.enableDefaults();
            }
            return sourceCodeManager;
        }
    }
}
