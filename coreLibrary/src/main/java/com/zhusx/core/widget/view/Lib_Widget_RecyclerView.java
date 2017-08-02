package com.zhusx.core.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import com.zhusx.core.adapter.Lib_BaseRecyclerAdapter;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/8/2 9:19
 */

public class Lib_Widget_RecyclerView extends RecyclerView {
    private ItemTouchHelper pTouchHelper;

    public Lib_Widget_RecyclerView(Context context) {
        super(context);
    }

    public Lib_Widget_RecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Lib_Widget_RecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 调用方式
     * view.setOnTouchListener((v, event) -> {
     * if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN)
     * mItemTouchHelper.startDrag(holder);
     * return false;
     * });
     */
    public void _startDrag(ViewHolder viewHolder) {
        if (pTouchHelper == null) {
            setHasFixedSize(true);
            pTouchHelper = new ItemTouchHelper(new _DragItemTouchHelper());
            pTouchHelper.attachToRecyclerView(this);
        }
        pTouchHelper.startDrag(viewHolder);
    }

    /**
     * 调用方式
     * view.setOnTouchListener((v, event) -> {
     * if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN)
     * mItemTouchHelper.startDrag(holder);
     * return false;
     * });
     */
    public class _DragItemTouchHelper extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
            if (viewHolder.getItemViewType() != target.getItemViewType()) return false;
            if (recyclerView.getAdapter() instanceof Lib_BaseRecyclerAdapter) {
                ((Lib_BaseRecyclerAdapter) recyclerView.getAdapter())._moveItemToUpdate(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }
            return false;
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
            if (getAdapter() instanceof Lib_BaseRecyclerAdapter) {
                ((Lib_BaseRecyclerAdapter) getAdapter())._removeItemToUpdate(viewHolder.getAdapterPosition());
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                final float alpha = 1.0f - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
//                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //拖动后会回调
            viewHolder.itemView.setAlpha(1.0f);
//            viewHolder.itemView.setBackgroundColor(Color.BLACK);
        }
    }
}
