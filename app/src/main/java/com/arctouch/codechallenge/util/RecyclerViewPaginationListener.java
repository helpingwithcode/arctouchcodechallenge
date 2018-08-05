package com.arctouch.codechallenge.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

//Adapted the implementation used in this repository: https://github.com/riyanhax/RecyclerView-Pagination---Retrofit-2.git

public abstract class RecyclerViewPaginationListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;

    public RecyclerViewPaginationListener(LinearLayoutManager linearLayoutManager){
        this.layoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading())
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) loadMoreItems();
    }

    protected abstract void loadMoreItems();

    public abstract boolean isLoading();
}
