package com.zhusx.core.adapter;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
    protected final int VIEW_TYPE_EMPTY = 500;

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
        if (viewType == VIEW_TYPE_EMPTY) {
            return new _ViewHolder(mLayoutInflater.inflate(this.__getEmptyLayoutResource(), parent, false));
        }
        return new _ViewHolder(mLayoutInflater.inflate(__getLayoutResource(viewType), parent, false));
    }

    @Override
    public void onBindViewHolder(_ViewHolder holder, int position) {
        if (position == 0 && getItemViewType(position) == VIEW_TYPE_EMPTY) {
            __onBindEmptyViewHolder(holder);
            return;
        }
        __bindViewHolder(holder, position, mList.get(position));
    }

    protected int __getEmptyLayoutResource() {
        return -1;
    }

    protected void __onBindEmptyViewHolder(_ViewHolder holder) {
    }

    protected abstract void __bindViewHolder(_ViewHolder holder, int position, T t);

    protected abstract int __getLayoutResource(int viewType);

    @Override
    public int getItemCount() {
        int count = mList.size();
        if (count == 0) {
            if (__getEmptyLayoutResource() > 0) {
                return 1;
            }
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && _isEmpty() && __getEmptyLayoutResource() > 0) {
            return VIEW_TYPE_EMPTY;
        }
        return 0;
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
        if (mList.size() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(mList.size() - 1);
        }
    }

    @Override
    public void _addItemToUpdate(List<T> list) {
        if (!_Lists.isEmpty(list)) {
            mList.addAll(list);
            if (mList.size() == list.size()) {
                notifyDataSetChanged();
            } else {
                notifyItemRangeInserted(mList.size() - list.size(), list.size());
            }
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
        if (mList.size() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(position);
        }
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
            if (_isEmpty()) {
                notifyDataSetChanged();
            } else {
                notifyItemRemoved(position);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean _removeItemToUpdate(int position) {
        T t = mList.remove(position);
        if (_isEmpty()) {
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(position);
        }
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (__getEmptyLayoutResource() > 0) {
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (position == 0) {
                            return _isEmpty() ? gridManager.getSpanCount() : 1;
                        }
                        return 1;
                    }
                });
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(_ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (__getEmptyLayoutResource() > 0) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                if (holder.getLayoutPosition() == 0 && _isEmpty()) {
                    StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                    p.setFullSpan(true);
                }
            }
        }
    }
}
