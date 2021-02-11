package com.tcc.app.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcc.app.interfaces.LoadMoreListener;


public class LoadMoreRecyclerView extends RecyclerView {
    private boolean isLoading;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;

    public LoadMoreRecyclerView(@NonNull Context context) {
        super(context);
    }

    public LoadMoreRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLoadMoreListener(final LoadMoreListener mLoadMoreListener) {

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (getLayoutManager() instanceof LinearLayoutManager) {
                    final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
                    assert linearLayoutManager != null;
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                } else if (getLayoutManager() instanceof GridLayoutManager) {
                    final GridLayoutManager linearLayoutManager = (GridLayoutManager) getLayoutManager();
                    assert linearLayoutManager != null;
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                }

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    Log.d("LoadMoreRecyclerView", "Load more triggered");
                    mLoadMoreListener.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    public void setLoadedCompleted() {
        isLoading = false;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
