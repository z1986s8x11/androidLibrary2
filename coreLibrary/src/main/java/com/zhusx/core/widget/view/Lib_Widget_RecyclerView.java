package com.zhusx.core.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import com.zhusx.core.interfaces.IChangeAdapter;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/8/2 9:19
 */

public class Lib_Widget_RecyclerView extends RecyclerView {
    private ItemTouchHelper pTouchHelper;
    private SnapHelper pSnapHelper;

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
     * 设置页码模式
     */
    public void _setPagerSnap() {
        //new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
        if (pSnapHelper == null) {
            pSnapHelper = new PagerSnapHelper();
        }
        pSnapHelper.attachToRecyclerView(this);
    }

    /************************************滑动 删除*************************************/
    /**
     * ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new _SwipeItemTouchHelper());
     * itemTouchHelper.attachToRecyclerView(RecyclerView);
     */
    public static class _SwipeItemTouchHelper extends ItemTouchHelper.Callback {
        RecyclerView recyclerView;

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if (viewHolder.getItemViewType() != target.getItemViewType()) {
                return false;
            }
            int from, to;
            from = viewHolder.getAdapterPosition();
            to = target.getAdapterPosition();
            if (recyclerView.getAdapter() instanceof IChangeAdapter) {
                ((IChangeAdapter) recyclerView.getAdapter())._moveItemToUpdate(from, to);
            }
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (recyclerView != null) {
                if (recyclerView.getAdapter() != null) {
                    if (recyclerView.getAdapter() instanceof IChangeAdapter) {
                        ((IChangeAdapter) recyclerView.getAdapter())._removeItemToUpdate(viewHolder.getAdapterPosition());
                    }
                }
            }
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public void onChildDraw(final Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            if (this.recyclerView == null) {
                this.recyclerView = recyclerView;
            }
            // Fade out the view as it is swiped out of the parent's bounds
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//                View itemView = viewHolder.itemView;
//                Bitmap icon;
//                if (dX > 0) {
//                    icon = BitmapFactory.decodeResource(recyclerView.getContext().getResources(), R.drawable.home_topic);
//                    // Set color for right swipe
//                    p.setColor(ContextCompat.getColor(recyclerView.getContext(), R.color.lib_red));
//                    // Draw Rect with varying right side, equal to displacement dX
//                    c.drawRect((float) itemView.getLeft() + _DensityUtil.dip2px(recyclerView.getContext(), 0), (float) itemView.getTop(), dX + _DensityUtil.dip2px(recyclerView.getContext(), 0),
//                            (float) itemView.getBottom(), p);
//                    // Set the image icon for right swipe
//                    c.drawBitmap(icon, (float) itemView.getLeft() + _DensityUtil.dip2px(recyclerView.getContext(), 16), (float) itemView.getTop() +
//                            ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2, p);
//                    icon.recycle();
//                }
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                __onItemSelected(viewHolder, actionState);
            }

            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            __onItemClear(recyclerView, viewHolder);
        }

        protected void __onItemSelected(RecyclerView.ViewHolder viewHolder, int actionState) {
        }

        protected void __onItemClear(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        }
    }


    /************************************拖动 交换*************************************/
    /**
     * 调用方式
     * view.setOnTouchListener((v, event) - {
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
     * view.setOnTouchListener((v, event) - {
     * if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN)
     * mItemTouchHelper.startDrag(holder);
     * return false;
     * });
     */
    public static class _DragItemTouchHelper extends ItemTouchHelper.Callback {
        RecyclerView recyclerView;

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
            if (viewHolder.getItemViewType() != target.getItemViewType()) return false;
            if (recyclerView.getAdapter() instanceof IChangeAdapter) {
                ((IChangeAdapter) recyclerView.getAdapter())._moveItemToUpdate(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }
            return false;
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
            if (recyclerView != null) {
                if (recyclerView.getAdapter() != null && recyclerView.getAdapter() instanceof IChangeAdapter) {
                    ((IChangeAdapter) recyclerView.getAdapter())._removeItemToUpdate(viewHolder.getAdapterPosition());
                }
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (this.recyclerView == null) {
                this.recyclerView = recyclerView;
            }
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
