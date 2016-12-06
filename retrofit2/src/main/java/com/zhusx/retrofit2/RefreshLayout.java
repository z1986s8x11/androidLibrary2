package com.zhusx.retrofit2;

/**
 * 还未完成
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/9/29 15:59
 */

//public class RefreshLayout<T extends IPageData> extends FrameLayout implements OnHttpLoadingListener<LoadData.Api, HttpResult<T>, Object> {
//    private View mEmptyView;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
//    private ProgressDialog dialog;
//    private LoadData<T> mLoadData;
//    private RecyclerView mRecyclerView;
//
//    public RefreshLayout(final RecyclerView recyclerView, final LoadData<T> loadData) {
//        super(recyclerView.getContext());
//        this.mLoadData = loadData;
//        this.mRecyclerView = recyclerView;
//        ViewGroup rootView = (ViewGroup) recyclerView.getParent();
//        int index = rootView.indexOfChild(recyclerView);
//        rootView.addView(this, index, recyclerView.getLayoutParams());
//        rootView.removeView(recyclerView);
//
//        mSwipeRefreshLayout = new SwipeRefreshLayout(recyclerView.getContext());
//        mSwipeRefreshLayout.setProgressViewOffset(false, 0, 100);
//        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
//        mSwipeRefreshLayout.addView(recyclerView);
//        addView(mSwipeRefreshLayout);
//
//        dialog = new ProgressDialog(recyclerView.getContext());
//        dialog.setCanceledOnTouchOutside(false);
//
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (loadData._isLoading()) {
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    return;
//                }
//                loadData._reLoadData(true);
//            }
//        });
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                    int visibleItemCount = layoutManager.getChildCount();
//                    int totalItemCount = layoutManager.getItemCount();
//                    int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//                    if (visibleItemCount > 0 && lastVisibleItemPosition >= totalItemCount - 1 && totalItemCount >= visibleItemCount) {
//                        if (!loadData._isLoading()) {
//                            if (loadData.hasMoreData()) {
//                                loadData._reLoadData(false);
//                            }
//                        }
//                    }
//                }
//            }
//        });
//        setEmptyView();
//    }
//
//    public void setEmptyView() {
//        if (mEmptyView != null) {
//            removeView(mEmptyView);
//        }
//        mEmptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_empty, null, false);
//        if (mEmptyView.getParent() != null) {
//            if (!(mEmptyView.getParent() instanceof ViewGroup)) {
//                return;
//            }
//            ((ViewGroup) mEmptyView.getParent()).removeView(mEmptyView);
//        }
//        addView(mEmptyView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mEmptyView.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onLoadStart(LoadData.Api api, HttpRequest<Object> request) {
//        if (request.isRefresh) {
//            if (mEmptyView != null && mEmptyView.getVisibility() == View.VISIBLE) {
//                mEmptyView.setVisibility(View.GONE);
//            }
//            if (!mSwipeRefreshLayout.isRefreshing()) {
//                mSwipeRefreshLayout.setRefreshing(true);
//            }
//        } else {
//            if (mSwipeRefreshLayout.isEnabled()) {
//                mSwipeRefreshLayout.setEnabled(false);
//            }
//            dialog.setMessage("loading...第" + mLoadData.getNextPage() + "页");
//            dialog.show();
//        }
//    }
//
//    @Override
//    public void onLoadError(LoadData.Api api, HttpRequest<Object> request, HttpResult<T> tHttpResult, boolean b, String s) {
//        if (request.isRefresh) {
//            if (mSwipeRefreshLayout.isRefreshing()) {
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//            if (mEmptyView != null && mEmptyView.getVisibility() != View.GONE) {
//                mEmptyView.setVisibility(View.GONE);
//            }
//        } else {
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }
//        }
//        if (!mSwipeRefreshLayout.isEnabled()) {
//            mSwipeRefreshLayout.setEnabled(true);
//        }
//        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onLoadComplete(LoadData.Api api, HttpRequest<Object> request, HttpResult<T> data) {
//        if (request.isRefresh) {
//            if (mSwipeRefreshLayout.isRefreshing()) {
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//            if (_Lists.isEmpty(data.data.getListData())) {
//                if (mEmptyView != null && mEmptyView.getVisibility() != View.VISIBLE) {
//                    mEmptyView.setVisibility(View.VISIBLE);
//                }
//            } else {
//                if (mEmptyView != null && mEmptyView.getVisibility() != View.GONE) {
//                    mEmptyView.setVisibility(View.GONE);
//                }
//            }
//            if (mRecyclerView.getAdapter() instanceof IChangeAdapter) {
//                ((IChangeAdapter) mRecyclerView.getAdapter())._setItemToUpdate(data.data.getListData());
//            }
//        } else {
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }
//            if (mRecyclerView.getAdapter() instanceof IChangeAdapter) {
//                ((IChangeAdapter) mRecyclerView.getAdapter())._addItemToUpdate(data.data.getListData());
//            }
//        }
//        if (!mSwipeRefreshLayout.isEnabled()) {
//            mSwipeRefreshLayout.setEnabled(true);
//        }
//    }
//}
