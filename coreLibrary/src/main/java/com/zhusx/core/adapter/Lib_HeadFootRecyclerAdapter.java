package com.zhusx.core.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/9/26 16:45
 */

public class Lib_HeadFootRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NORMAL = 0;
    private static final int FOOTER_INIT_INDEX = 2000;
    private static final int HEADER_INIT_INDEX = 1000;
    /**
     * RecyclerView使用的，真正的Adapter
     */
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart + getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart + getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart + getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            int headerViewsCountCount = getHeaderViewsCount();
            notifyItemRangeChanged(fromPosition + headerViewsCountCount, toPosition + headerViewsCountCount + itemCount);
        }
    };

    public Lib_HeadFootRecyclerAdapter(RecyclerView.Adapter innerAdapter) {
        setAdapter(innerAdapter);
    }

    /**
     * 设置adapter
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        if (mInnerAdapter != null) {
            mInnerAdapter.unregisterAdapterDataObserver(mDataObserver);
        }
        this.mInnerAdapter = adapter;
        mInnerAdapter.registerAdapterDataObserver(mDataObserver);
    }

    public RecyclerView.Adapter getInnerAdapter() {
        return mInnerAdapter;
    }

    public void addHeaderView(View header) {
        if (!mHeaderViews.contains(header)) {
            mHeaderViews.add(header);
            // animate
            notifyItemInserted(mHeaderViews.size() - 1);
        }
    }

    public void removeHeaderView(View header) {
        if (mHeaderViews.contains(header)) {
            // animate
            notifyItemRemoved(mHeaderViews.indexOf(header));
            mHeaderViews.remove(header);
        }
    }

    public void addFooterView(View footer) {
        if (!mFooterViews.contains(footer)) {
            mFooterViews.add(footer);
            notifyItemInserted(mHeaderViews.size() + getInnerItemCount() + mFooterViews.size() - 1);
        }
    }

    public void removeFooterView(View footer) {
        if (mFooterViews.contains(footer)) {
            notifyItemRemoved(mHeaderViews.size() + getInnerItemCount() + mFooterViews.indexOf(footer));
            mFooterViews.remove(footer);
        }
    }

    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    public boolean isHeader(int position) {
        return position < mHeaderViews.size();
    }

    public boolean isFooter(int position) {
        return position >= getHeaderViewsCount() + getInnerItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType < HEADER_INIT_INDEX) {
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        } else if (viewType < FOOTER_INIT_INDEX) {
            return new Lib_HeadFootRecyclerAdapter.ViewHolder(mHeaderViews.get(viewType - HEADER_INIT_INDEX));
        } else {
            return new Lib_HeadFootRecyclerAdapter.ViewHolder(mFooterViews.get(viewType - FOOTER_INIT_INDEX));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position)) {
            return;
        }
        final int adjPosition = position - getHeaderViewsCount();
        int adapterCount;
        if (mInnerAdapter != null) {
            adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mInnerAdapter.onBindViewHolder(holder, adjPosition);
//                if (mInnerAdapter instanceof SwipeMenuAdapter) {
//                    View itemView = holder.itemView;
//                    if (itemView instanceof SwipeMenuLayout) {
//                        SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) itemView;
//                        int childCount = swipeMenuLayout.getChildCount();
//                        for (int i = 0; i < childCount; i++) {
//                            View childView = swipeMenuLayout.getChildAt(i);
//                            if (childView instanceof SwipeMenuView) {
//                                ((SwipeMenuView) childView).bindAdapterPosition(adjPosition);
//                            }
//                        }
//                    }
//
//                }
//                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mInnerAdapter != null) {
            return getHeaderViewsCount() + getFooterViewsCount() + mInnerAdapter.getItemCount();
        } else {
            return getHeaderViewsCount() + getFooterViewsCount();
        }
    }

    private int getInnerItemCount() {
        if (mInnerAdapter == null) {
            return 0;
        } else {
            return mInnerAdapter.getItemCount();
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (isHeader(position)) {
            return HEADER_INIT_INDEX + position;
        }
        if (isFooter(position)) {
            return FOOTER_INIT_INDEX + position - getHeaderViewsCount() - getInnerItemCount();
        }
        int adapterCount;
        if (mInnerAdapter != null) {
            adapterCount = mInnerAdapter.getItemCount();
            int adjPosition = position - getHeaderViewsCount();
            if (adjPosition < adapterCount) {
                return mInnerAdapter.getItemViewType(adjPosition);
            }
        }
        return TYPE_NORMAL;
    }

    @Override
    public long getItemId(int position) {
        if (mInnerAdapter != null && position >= getHeaderViewsCount()) {
            int adjPosition = position - getHeaderViewsCount();
            int adapterCount = mInnerAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mInnerAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeader(position) || isFooter(position))
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            if (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition())) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
        mInnerAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewRecycled(holder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
