package com.zhusx.show.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhusx.core.adapter.Lib_BaseAdapter;
import com.zhusx.core.app.Lib_BaseActivity;
import com.zhusx.core.app.Lib_BaseFragment;
import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.helper.Lib_Subscribes;
import com.zhusx.show.R;
import com.zhusx.show.process.P_ProjectClassScanHelper;

import java.util.Arrays;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/4/12 15:49
 */
public class P_ProjectClassFragment extends Lib_BaseFragment {
    private ListView mListView;
    private Lib_BaseAdapter<P_ProjectClassScanHelper> adapter;
    private Class cls = getClass();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lib_fragment_project, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(adapter = new Lib_BaseAdapter<P_ProjectClassScanHelper>() {
            @Override
            public View getView(LayoutInflater inflater, final P_ProjectClassScanHelper bean, int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (bean.isDir()) {
                    holder = _getViewHolder(convertView, parent, R.layout.lib_item_list_showcode_folder);
                    holder.setText(R.id.tv_name, bean.name);
                    holder.rootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), _PublicActivity.class);
                            intent.putExtra(_PublicActivity._EXTRA_FRAGMENT, cls);
                            intent.putExtra(_EXTRA_String, bean.path);
                            startActivity(intent);

                        }
                    });
                } else {
                    holder = _getViewHolder(convertView, parent, R.layout.lib_item_list_showcode_file);
                    holder.setText(R.id.tv_name, bean.name.substring(0, bean.name.lastIndexOf("_")));
                    holder.rootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(bean.path)) {
                                return;
                            }
                            try {
                                Class<?> cls = Class.forName(bean.path);
                                Intent intent;
                                Bundle b = new Bundle();
                                if (Activity.class.isAssignableFrom(cls)) {
                                    intent = new Intent(v.getContext(), cls);
                                } else if (Fragment.class.isAssignableFrom(cls)) {
                                    intent = new Intent(v.getContext(), _PublicActivity.class);
                                    b.putSerializable(_PublicActivity._EXTRA_FRAGMENT, cls);
                                    b.putString(_EXTRA_String, bean.path);
                                } else {
                                    intent = new Intent(getActivity(), _PublicActivity.class);
                                    intent.putExtra(_PublicActivity._EXTRA_FRAGMENT, P_SourceCodeFragment.class);
                                    intent.putExtra(Lib_BaseActivity._EXTRA_String, __getFilterPackage());
                                    intent.putExtra(_EXTRA_String_ID, "java/" + bean.path.replaceAll("\\.", "/") + ".java");
                                }
                                intent.putExtras(b);
                                startActivity(intent);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                _showToast(bean.path + " 没有找到");
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                                _showToast("Activity 在mainfest.xml 没有注册");
                            } catch (Exception e) {
                                e.printStackTrace();
                                _showToast("发生未知异常");
                            }
                        }
                    });
                }
                return holder.rootView;
            }

            @Override
            public int getItemViewType(int position) {
                if (getItem(position).isDir()) {
                    return 0;
                }
                return 1;
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }
        });
        if (getArguments() != null && getArguments().getString(_EXTRA_String) != null) {
            adapter._setItemToUpdate(Arrays.asList(P_ProjectClassScanHelper.getInstance().get(getArguments().getString(_EXTRA_String)).list()));
        } else {
            Lib_Subscribes.subscribe(new Lib_Subscribes.Subscriber<P_ProjectClassScanHelper>() {
                @Override
                public P_ProjectClassScanHelper doInBackground() {
                    return P_ProjectClassScanHelper.getInstance()._init(getActivity()).get(__getFilterPackage());
                }

                @Override
                public void onComplete(P_ProjectClassScanHelper helper) {
                    if (helper == null || helper.list() == null) {
                        _showToast("未找到_Activity 或者 _Fragment 文件");
                        return;
                    }
                    adapter._setItemToUpdate(Arrays.asList(helper.list()));
                }

                @Override
                public void onError(Throwable t) {
                    _showToast("未知错误");
                    if (LogUtil.DEBUG) {
                        LogUtil.e(t);
                    }
                }
            }, this);
        }
    }


    protected String __getFilterPackage() {
        return getActivity().getPackageName();
    }
}
