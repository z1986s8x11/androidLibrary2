package com.zhusx.core.adapter;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.interfaces.IChangeAdapter;
import com.zhusx.core.utils._Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/3/2 10:43
 */
public abstract class Lib_BaseRecyclerAdapter<T> extends RecyclerView.Adapter<Lib_BaseRecyclerAdapter._ViewHolder> implements IChangeAdapter<T> {
    private List<T> mList;
    protected LayoutInflater mLayoutInflater;

    public Lib_BaseRecyclerAdapter() {
        this(null, new ArrayList<T>());
    }

    public Lib_BaseRecyclerAdapter(List<T> list) {
        this(null, list);
    }

    public Lib_BaseRecyclerAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public Lib_BaseRecyclerAdapter(Context context, List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mList = list;
        if (context != null) {
            mLayoutInflater = LayoutInflater.from(context);
        }
    }

    public List<T> getListData() {
        return mList;
    }

    @Override
    public _ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        return new _ViewHolder(mLayoutInflater.inflate(__getLayoutResource(viewType), parent, false));
    }

    @Override
    public void onBindViewHolder(_ViewHolder holder, int position) {
        __bindViewHolder(holder, position, mList.get(position));
    }

    protected abstract void __bindViewHolder(_ViewHolder holder, int position, T t);

    protected abstract int __getLayoutResource(int viewType);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class _ViewHolder extends RecyclerView.ViewHolder {
        SparseArray viewHolder = new SparseArray();
        public View rootView;

        _ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
        }

        public void setText(int id, String text) {
            ((TextView) getView(id)).setText(text);
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
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }
    }

    @Override
    public void _addItemToUpdate(T t) {
        mList.add(t);
        notifyItemInserted(mList.size() - 1);
    }

    @Override
    public void _addItemToUpdate(List<T> list) {
        if (!_Lists.isEmpty(list)) {
            mList.addAll(list);
            notifyItemRangeInserted(mList.size() - list.size(), list.size());
        }
    }

    @Override
    public void _addItemToUpdate(int position, T t) {
        if (position < 0 || position > mList.size()) {
            if (LogUtil.DEBUG) {
                LogUtil.e("_addItemToUpdate 失败! 当前List.size():+" + mList.size() + ";position:" + position);
            }
            return;
        }
        mList.add(position, t);
        notifyItemRangeInserted(mList.size() - position, mList.size());
    }

    @Override
    public void _setItemToUpdate(T t) {
        mList.clear();
        mList.add(t);
        notifyDataSetChanged();
    }

    @Override
    public void _setItemToUpdate(List<T> list) {
        mList.clear();
        if (!_Lists.isEmpty(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public void _clearItemToUpdate() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public boolean _removeItemToUpdate(T t) {
        int position = mList.indexOf(t);
        if (position != -1) {
            mList.remove(position);
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }

    @Override
    public boolean _removeItemToUpdate(int position) {
        T t = mList.remove(position);
        notifyItemRemoved(position);
        if (t != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean _isEmpty() {
        return _Lists.isEmpty(mList);
    }

    @Override
    public void _moveItemToUpdate(int from, int to) {
        Collections.swap(mList, from, to);
        notifyItemMoved(from, to);
    }
}
