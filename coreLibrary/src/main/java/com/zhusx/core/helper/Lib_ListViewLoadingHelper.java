package com.zhusx.core.helper;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.CallSuper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zhusx.core.interfaces.IChangeAdapter;
import com.zhusx.core.interfaces.IPageData;
import com.zhusx.core.network.HttpRequest;
import com.zhusx.core.network.HttpResult;
import com.zhusx.core.network.Lib_BaseHttpRequestData;
import com.zhusx.core.network.OnHttpLoadingListener;
import com.zhusx.core.utils._Views;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/3/21 9:40
 */

public class Lib_ListViewLoadingHelper<Id, Result extends IPageData, Parameter> implements OnHttpLoadingListener<Id, HttpResult<Result>, Parameter> {
    protected ListView pListView;
    protected Lib_BaseHttpRequestData<Id, Result, Parameter> pLoadData;
    protected SwipeRefreshLayout pSwipeRefreshLayout;

    protected View pLoadingView;
    protected View pErrorView;
    protected View pEmptyView;
    protected Animatable pAnim;
    protected LinearLayout pStatusLayout;

    public Lib_ListViewLoadingHelper(ListView listView, final Lib_BaseHttpRequestData<Id, Result, Parameter> loadData) {
        this.pListView = listView;
        this.pLoadData = loadData;
        createLayout(listView.getContext());
        initListener();
    }

    private void initListener() {
        pListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View vv, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (pListView.getFirstVisiblePosition() == 0 && pListView.getChildCount() > 0 && pListView.getChildAt(0).getTop() >= pListView.getListPaddingTop()) {
                        pSwipeRefreshLayout.setEnabled(true);
                    } else pSwipeRefreshLayout.setEnabled(false);
                }
                return false;
            }
        });
        pSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (pLoadData._isLoading()) {
                    pSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
                pLoadData._reLoadData(true);
            }
        });
        pListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean mLastItemVisible;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && null != pLoadData && mLastItemVisible) {
                    if (pLoadData.hasMoreData()) {
                        pLoadData._reLoadData(false);
                    }
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) {
                if (null != pLoadData) {
                    mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
                }
            }
        });
    }

    private void createLayout(Context context) {
        pSwipeRefreshLayout = new SwipeRefreshLayout(context);
        pSwipeRefreshLayout.setProgressViewOffset(false, 0, 100);
        _Views.insertView(pListView, pSwipeRefreshLayout);

        LinearLayout childRootLayout = new LinearLayout(context);
        childRootLayout.setOrientation(LinearLayout.VERTICAL);
        _Views.insertView(pListView, childRootLayout);

        pStatusLayout = new LinearLayout(context);
        childRootLayout.addView(pStatusLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        pListView.setEmptyView(pStatusLayout);

    }

    public final void _setLoadingView(View child) {
        if (pLoadingView != null) {
            pStatusLayout.removeView(pLoadingView);
        }
        if (child != null) {
            if (child.getParent() != null) {
                ((ViewGroup) child.getParent()).removeView(child);
            }
            pStatusLayout.addView(child, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            child.setVisibility(View.GONE);
        }
        pLoadingView = child;
    }

    public final void _setEmptyView(View child) {
        if (pEmptyView != null) {
            pStatusLayout.removeView(pEmptyView);
        }
        if (child != null) {
            if (child.getParent() != null) {
                ((ViewGroup) child.getParent()).removeView(child);
            }
            pStatusLayout.addView(child, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            child.setVisibility(View.GONE);
        }
        pEmptyView = child;
    }


    public final void _setErrorView(View child) {
        if (pErrorView != null) {
            pStatusLayout.removeView(pErrorView);
        }
        if (child != null) {
            if (child.getParent() != null) {
                ((ViewGroup) child.getParent()).removeView(child);
            }
            pStatusLayout.addView(child, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            child.setVisibility(View.GONE);
        }
        pErrorView = child;
    }

    protected void _setLoadingAnim(Animatable animatable) {
        if (pAnim != null && pAnim.isRunning()) {
            pAnim.stop();
        }
        pAnim = animatable;
    }

    protected void __startLoadingAnim(Id id, HttpRequest<Parameter> request) {
        if (pAnim != null) {
            pAnim.start();
        }
    }

    protected void __stopLoadingAnim() {
        if (pAnim != null) {
            if (pAnim.isRunning()) {
                pAnim.stop();
            }
        }
    }

    @CallSuper
    @Override
    public void onLoadStart(Id id, HttpRequest<Parameter> request) {
        if (request.isRefresh) {
            if (pSwipeRefreshLayout.isRefreshing()) {
                pSwipeRefreshLayout.setRefreshing(true);
            } else {
                if (pLoadingView != null) {
                    pLoadingView.setVisibility(View.VISIBLE);
                    __startLoadingAnim(id, request);
                }
            }
            if (pEmptyView != null && pEmptyView.getVisibility() != View.GONE) {
                pEmptyView.setVisibility(View.GONE);
            }
            if (pErrorView != null && pErrorView.getVisibility() != View.GONE) {
                pErrorView.setVisibility(View.GONE);
            }
        }
    }

    @CallSuper
    @Override
    public void onLoadError(Id id, HttpRequest<Parameter> request, HttpResult<Result> result, boolean b, String s) {
        if (request.isRefresh) {
            if (pSwipeRefreshLayout.isRefreshing()) {
                pSwipeRefreshLayout.setRefreshing(false);
            } else {
                if (pLoadingView != null) {
                    pLoadingView.setVisibility(View.GONE);
                    __stopLoadingAnim();
                }
            }
            if (pErrorView != null && pErrorView.getVisibility() != View.VISIBLE) {
                pErrorView.setVisibility(View.VISIBLE);
            }
            if (pEmptyView != null && pEmptyView.getVisibility() != View.GONE) {
                pEmptyView.setVisibility(View.GONE);
            }
        }
    }

    @CallSuper
    @Override
    public void onLoadComplete(Id id, HttpRequest<Parameter> request, HttpResult<Result> result) {
        IChangeAdapter adapter = null;
        ListAdapter listAdapter = pListView.getAdapter();
        if (listAdapter instanceof HeaderViewListAdapter) {
            listAdapter = ((HeaderViewListAdapter) listAdapter).getWrappedAdapter();
        }
        if (listAdapter instanceof IChangeAdapter) {
            adapter = (IChangeAdapter) listAdapter;
        }
        if (request.isRefresh) {
            if (adapter != null) {
                adapter._setItemToUpdate(result.getData().getListData());
                if (adapter._isEmpty()) {
                    if (pEmptyView != null && pEmptyView.getVisibility() != View.VISIBLE) {
                        pEmptyView.setVisibility(View.VISIBLE);
                    }
                }
            }
            if (pSwipeRefreshLayout.isRefreshing()) {
                pSwipeRefreshLayout.setRefreshing(false);
            } else {
                if (pLoadingView != null) {
                    pLoadingView.setVisibility(View.GONE);
                    __stopLoadingAnim();
                }
            }

            if (pErrorView != null && pErrorView.getVisibility() != View.GONE) {
                pErrorView.setVisibility(View.GONE);
            }
        } else {
            if (adapter != null) {
                adapter._addItemToUpdate(result.getData().getListData());
            }
        }
    }
}
