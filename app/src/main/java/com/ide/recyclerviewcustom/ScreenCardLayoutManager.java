package com.ide.recyclerviewcustom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class ScreenCardLayoutManager extends LinearLayoutManager {
    private static final int RATIO_CARD_OVERLAID = 3; // 何分の1を重ねて表示するか

    ScreenCardLayoutManager(Context context) {
        super(context);
        this.setOrientation(LinearLayoutManager.VERTICAL);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        detachAndScrapAttachedViews(recycler);

        int parentTop = getPaddingTop();
        int parentLeft = getPaddingLeft();
        int parentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int parentBottom = parentTop + parentHeight;
        for (int i = 0, top = parentTop; (i < getItemCount() && top < parentBottom); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view, i);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);
            layoutDecorated(view, parentLeft, top, parentLeft + width, top + height);
            top += height / RATIO_CARD_OVERLAID;
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() <= 0) {
            return 0;
        }

        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        final int parentHeight = getHeight();
        final int parentBottom = parentTop + parentHeight;
        int scrolled = 0;
        if (dy < 0) {
            // 下方向スワイプ，上にスクロール
            while (scrolled > dy) {
                final View topView = getChildAt(0);
                final int hangingTop = Math.max(parentTop - getDecoratedTop(topView), 0);
                final int scrollBy = Math.min(-dy - (-scrolled), hangingTop);
                scrolled -= scrollBy;
                offsetChildrenVertical(scrollBy);
                int mFirstPosition = getPosition(topView);
                if (mFirstPosition > 0 && scrolled > dy) {
                    mFirstPosition--;
                    View view = recycler.getViewForPosition(mFirstPosition);
                    addView(view, 0);
                    measureChildWithMargins(view, 0, 0);
                    int width = getDecoratedMeasuredWidth(view);
                    int height = getDecoratedMeasuredHeight(view);
                    int top = getDecoratedTop(topView) - getDecoratedMeasuredHeight(topView) / RATIO_CARD_OVERLAID;
                    layoutDecorated(view, parentLeft, top, parentLeft + width, top + height);
                } else {
                    break;
                }
            }
        } else if (dy > 0) {
            // 上方向スワイプ，下にスクロール
            while (scrolled < dy) {
                final View bottomView = getChildAt(getChildCount() - 1);
                int mLastPosition = getPosition(bottomView);
                final int bottomViewHeight = getDecoratedMeasuredHeight(bottomView);
                final int hangingBottom = (mLastPosition < getItemCount() - 1) ?
                        Math.max(getDecoratedTop(bottomView) + bottomViewHeight / RATIO_CARD_OVERLAID - parentBottom, 0) :
                        Math.max(getDecoratedBottom(bottomView) - parentBottom, 0);
                final int scrollBy = -Math.min(dy - scrolled, hangingBottom);
                scrolled -= scrollBy;
                offsetChildrenVertical(scrollBy);
                if (mLastPosition < getItemCount() - 1 && scrolled < dy) {
                    mLastPosition++;
                    View view = recycler.getViewForPosition(mLastPosition);
                    addView(view, getChildCount());
                    measureChildWithMargins(view, 0, 0);
                    int width = getDecoratedMeasuredWidth(view);
                    int height = getDecoratedMeasuredHeight(view);
                    int top = getDecoratedTop(bottomView) + getDecoratedMeasuredHeight(bottomView) / 3;
                    layoutDecorated(view, parentLeft, top, parentLeft + width, top + height);
                } else {
                    break;
                }
            }
        }
        // スクロールによってはみ出たビューをリサイクル
        final int childCount = getChildCount();
        boolean firstFound = false;
        int firstValidId = 0;
        int lastValidId = 0;
        for (int i = 0; i < childCount; i++) {
            final View view = getChildAt(i);
            if (view.hasFocus() || (getDecoratedBottom(view) >= 0 && getDecoratedTop(view) <= parentHeight)) {
                if (!firstFound) {
                    firstValidId = i;
                    firstFound = true;
                }
                lastValidId = i;
            }
        }
        for (int i = childCount - 1; i > lastValidId; i--) {
            removeAndRecycleViewAt(i, recycler);
        }
        for (int i = firstValidId - 1; i >= 0; i--) {
            removeAndRecycleViewAt(i, recycler);
        }
        return scrolled;
    }
}
