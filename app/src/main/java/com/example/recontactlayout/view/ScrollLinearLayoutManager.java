package com.example.recontactlayout.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;

/**
 * Created by ChenR on 2017/11/6.
 */

public class ScrollLinearLayoutManager extends LinearLayoutManager {

    public ScrollLinearLayoutManager(Context context) {
        super(context, VERTICAL, false);
    }

    public void setSelectionForTop (int position, float offset) {

    }

    private static class TopSmoothScroller extends LinearSmoothScroller {

        public TopSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return super.calculateDtToFit(viewStart, viewEnd, boxStart, boxEnd, snapPreference);
        }
    }
}
