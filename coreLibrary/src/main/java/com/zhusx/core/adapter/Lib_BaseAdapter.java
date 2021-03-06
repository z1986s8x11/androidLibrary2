package com.zhusx.core.adapter;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.interfaces.IChangeAdapter;
import com.zhusx.core.utils._Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/12 16:14
 */
public abstract class Lib_BaseAdapter<T> extends BaseAdapter implements IChangeAdapter<T> {
    protected List<T> p_list = new ArrayList<>();
    protected LayoutInflater inflater;

    public Lib_BaseAdapter() {
        this(null, null);
    }

    public Lib_BaseAdapter(List<T> list) {
        this(null, list);
    }

    public Lib_BaseAdapter(Context context) {
        this(context, null);
    }

    public Lib_BaseAdapter(Context context, List<T> list) {
        if (context != null) {
            this.inflater = LayoutInflater.from(context);
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        this.p_list = list;
    }

    @Override
    public int getCount() {
        return p_list.size();
    }

    @Override
    public T getItem(int position) {
        return p_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return getView(inflater, p_list.get(position), position, convertView, parent);
    }

    /**
     * 拿到上下文
     */
    public Context _getContext() {
        return inflater.getContext();
    }

    /**
     * 返回当前的List
     */
    public List<T> _getListsData() {
        return p_list;
    }

    /**
     * 更新列表
     */
    @Override
    public void _setItemToUpdate(List<T> replaceList) {
        if (replaceList == null) {
            p_list.clear();
            notifyDataSetChanged();
            return;
        }
        this.p_list = replaceList;
        notifyDataSetChanged();
    }

    /**
     * 更新列表
     */
    @Override
    public void _setItemToUpdate(T item) {
        p_list.clear();
        p_list.add(item);
        notifyDataSetChanged();
    }

    /**
     * 清空
     */
    @Override
    public void _clearItemToUpdate() {
        p_list.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加项
     */
    @Override
    public void _addItemToUpdate(T bean) {
        if (p_list.add(bean)) {
            notifyDataSetChanged();
        } else {
            if (LogUtil.DEBUG) {
                LogUtil.e("_addItemToUpdate 失败!");
            }
        }
    }

    @Override
    public void _addItemToUpdate(int position, T bean) {
        if (position < 0 || position > p_list.size()) {
            if (LogUtil.DEBUG) {
                LogUtil.e("_addItemToUpdate 失败! 当前List.size():+" + p_list.size() + ";position:" + position);
            }
            return;
        }
        p_list.add(position, bean);
        notifyDataSetChanged();
    }

    /**
     * 添加项
     */
    @Override
    public void _addItemToUpdate(List<T> bean) {
        if (!_Lists.isEmpty(bean)) {
            p_list.addAll(bean);
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean _removeItemToUpdate(T m) {
        if (p_list.remove(m)) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean _removeItemToUpdate(int position) {
        T bean = p_list.remove(position);
        if (bean != null) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public void _moveItemToUpdate(int from, int to) {
        Collections.swap(p_list, from, to);
        notifyDataSetChanged();
    }

    @Override
    public boolean _isEmpty() {
        return _Lists.isEmpty(p_list);
    }

    public T _removeItem(int position) {
        return p_list.remove(position);
    }

    public abstract View getView(LayoutInflater inflater, T bean, int position, View convertView, ViewGroup parent);

    public View[] _getViewArrays(View convertView, ViewGroup parent, int layoutId, int... viewIds) {
        View[] views;
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, parent, false);
            views = new View[viewIds.length + 1];
            views[0] = convertView;
            for (int i = 0; i < viewIds.length; i++) {
                views[i + 1] = convertView.findViewById(viewIds[i]);
            }
            convertView.setTag(views);
        } else {
            views = (View[]) convertView.getTag();
        }
        return views;
    }

    protected static class ViewHolder {
        private SparseArray viewHolder = new SparseArray();
        public View rootView;

        public void setText(@IdRes int id, String text) {
            ((TextView) getView(id)).setText(text);
        }

        public void setText(@IdRes int id, @StringRes int stringId) {
            ((TextView) getView(id)).setText(stringId);
        }

        public void setCheck(@IdRes int id, boolean checked) {
            ((CompoundButton) getView(id)).setChecked(checked);
        }

        public void setTextColor(@IdRes int id, @ColorInt int resId) {
            ((TextView) getView(id)).setTextColor(resId);
        }

        public void setTextColorRes(@IdRes int id, @ColorRes int resId) {
            ((TextView) getView(id)).setTextColor(rootView.getResources().getColor(resId));
        }

        public void setImageRes(@IdRes int id, @DrawableRes int resId) {
            ((ImageView) getView(id)).setImageResource(resId);
        }

        public void setBackgroundRes(@IdRes int id, @DrawableRes @ColorRes int resId) {
            getView(id).setBackgroundResource(resId);
        }

        public void setBackgroundColor(@IdRes int id, @ColorInt int resId) {
            getView(id).setBackgroundColor(resId);
        }


        public <T extends View> T getView(int id) {
            View childView = (View) viewHolder.get(id);
            if (childView == null) {
                childView = rootView.findViewById(id);
                if (childView != null) {
                    viewHolder.put(id, childView);
                }
            }
            return (T) childView;
        }
    }

    public ViewHolder _getViewHolder(View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, parent, false);
        }
        ViewHolder viewTag = (ViewHolder) convertView.getTag();
        if (viewTag == null) {
            viewTag = new ViewHolder();
            viewTag.rootView = convertView;
            convertView.setTag(viewTag);
        }
        return viewTag;
    }


    protected TextView _toTextView(View v) {
        return (TextView) v;
    }

    protected ImageView _toImageView(View v) {
        return (ImageView) v;
    }

    protected CheckBox _toCheckBox(View v) {
        return (CheckBox) v;
    }

    /**
     * 用源生ListView 多选时,需要重写 返回true
     */
    @Override
    public boolean hasStableIds() {
        return super.hasStableIds();
    }
}