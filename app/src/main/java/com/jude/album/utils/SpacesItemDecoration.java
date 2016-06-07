package com.jude.album.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int halfSpace;

    public SpacesItemDecoration(int space) {
        this.halfSpace = space / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position>0){
            int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
            if (spanIndex == 0) {
                outRect.left = halfSpace*2;
                outRect.right = halfSpace;
            } else {//if you just have 2 span . Or you can use (staggeredGridLayoutManager.getSpanCount()-1) as last span
                outRect.left = halfSpace;
                outRect.right = halfSpace*2;
            }
            outRect.bottom = halfSpace*2;
        }
    }
}