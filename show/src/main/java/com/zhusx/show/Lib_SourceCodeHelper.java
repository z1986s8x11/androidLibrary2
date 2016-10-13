package com.zhusx.show;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zhusx.core.app.Lib_BaseActivity;
import com.zhusx.core.app._PublicActivity;
import com.zhusx.core.utils._Lists;
import com.zhusx.core.widget.slidingmenu.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/3/22 10:31
 */
public class Lib_SourceCodeHelper {
    private List<Class<?>> list = new ArrayList<>();

    public Lib_SourceCodeHelper(Class<?> cls) {
        this.list.add(cls);
    }

    public Lib_SourceCodeHelper(Lib_BaseActivity activity) {
        this.list.add(activity.getClass());
        List<Fragment> lists = activity.getSupportFragmentManager().getFragments();
        if (!_Lists.isEmpty(lists)) {
            for (Fragment f : lists) {
                list.add(f.getClass());
            }
        }
    }

    public void _onOptionsItemSelected(Context context, MenuItem item) {
        switch (item.getGroupId()) {
            case 1986:
                Intent in = new Intent(context, _PublicActivity.class);
                in.putExtra(_PublicActivity._EXTRA_FRAGMENT, P_SourceCodeFragment.class);
                in.putExtra(Lib_BaseActivity._EXTRA_String, "java/" + list.get(item.getItemId()).getName().replace(".", "/") + ".java");
                context.startActivity(in);
                break;
            default:
                break;
        }

    }

    public void _onCreateOptionsMenu(Context context, Menu menu) {
        for (int i = 0; i < list.size(); i++) {
            menu.add(1986, i, 0, list.get(i).getSimpleName());
        }
    }

    SlidingMenu mSlidingMenu;

    public void initContextView(Lib_BaseActivity activity) {
        if (mSlidingMenu == null) {
            mSlidingMenu = new SlidingMenu(activity, SlidingMenu.SLIDING_CONTENT);
            final View right = LayoutInflater.from(activity).inflate(R.layout.lib_layout_linearlayout, null, false);
            mSlidingMenu.setMenu(right);
            mSlidingMenu.setBehindWidth(activity._getFullScreenWidth() - 200);
            final String fileName = "java/" + activity.getClass().getName().replace(".", "/") + ".java";
            P_SourceCodeFragment fragment = new P_SourceCodeFragment();
            Bundle b = new Bundle();
            b.putString(Lib_BaseActivity._EXTRA_String, fileName);
            fragment.setArguments(b);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.lib_content, fragment).commitAllowingStateLoss();
        }
    }
}
