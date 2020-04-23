package com.android.sgzcommon.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class FooterRefreshListView extends ListView {

	private float mDownY;
	private boolean mCanRefresh;
	private TextView mLoadMoreView;
	public static final String TAG_LOAD_MORE = "load_more";
	public static final String LOAD_MORE_TEXT_COLOR = "#969696";
	private OnRefreshListener mListener;

	public FooterRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public FooterRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FooterRefreshListView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mLoadMoreView = new TextView(context);
		mLoadMoreView.setTextSize(14);
		mLoadMoreView.setTextColor(Color.parseColor(LOAD_MORE_TEXT_COLOR));
		mLoadMoreView.setBackgroundColor(Color.TRANSPARENT);
		int padding = dp2px(10);
		mLoadMoreView.setPadding(padding, padding, padding, padding);
		mLoadMoreView.setGravity(Gravity.CENTER);
		mLoadMoreView.setTag(TAG_LOAD_MORE);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mLoadMoreView.setLayoutParams(params);
		canRefresh();
	}

	public void completeRefresh() {
		mCanRefresh = false;
		mLoadMoreView.setText("没有更多了");
	}

	public void canRefresh() {
		mCanRefresh = true;
		mLoadMoreView.setText("加载更多");
	}

	@Override
	public void addFooterView(View v) {
		removeLoadMoreView();
		super.addFooterView(v);
		super.addFooterView(mLoadMoreView);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (MotionEvent.ACTION_MASK & ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (isLast() && isPullUp(ev)) {
				addLoadMoreView();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mDownY = 0f;
			if (loadMoreViewMoreHalf()) {
				if (mListener != null && mCanRefresh) {
					mListener.onFooterRefresh();
				}
			}
			removeLoadMoreView();
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * {@link #mLoadMoreView} 显示是否超过一半高度
	 * 
	 * @return
	 */
	private boolean loadMoreViewMoreHalf() {
		boolean moreHalf = false;
		if (loadMoreViewShowing()) {
			int loadMoreViewHeight = mLoadMoreView.getHeight();
			if (mLoadMoreView.getTop() <= getHeight() - loadMoreViewHeight / 2) {
				moreHalf = true;
			}
		}
		return moreHalf;
	}

	private boolean isPullUp(MotionEvent ev) {
		final float offset = ev.getY() - mDownY;
		return offset < 0;
	}

	private void addLoadMoreView() {
		if (!addedLoadMoreView()) {
			super.addFooterView(mLoadMoreView);
		}
	}

	private void removeLoadMoreView() {
		super.removeFooterView(mLoadMoreView);
	}

	private boolean addedLoadMoreView() {
		return findViewWithTag(TAG_LOAD_MORE) != null;
	}

	public int dp2px(float dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
	}

	public boolean isLast() {
		boolean last = false;
		if (getFirstVisiblePosition() + getShowingContentChildCount() == getAllContentChildCount()) {
			View lastContentChildView = getChildAt(getShowingContentChildCount() - 1);
			if (getHeight() == lastContentChildView.getBottom()) {
				last = true;
			}
		}
		return last;
	}

	private int getAllContentChildCount() {
		int count = 0;
		if (getAdapter() != null) {
			if (addedLoadMoreView()) {
				count = getAdapter().getCount() - 1;
			} else {
				count = getAdapter().getCount();
			}
		}
		return count;
	}

	private int getShowingContentChildCount() {
		int childCount = getChildCount();
		if (loadMoreViewShowing()) {
			childCount--;
		}
		return childCount;
	}

	private boolean loadMoreViewShowing() {
		boolean showing = false;
		if (addedLoadMoreView() && mLoadMoreView.getTop() < getHeight()) {
			showing = true;
		}
		return showing;
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	public interface OnRefreshListener {
		void onFooterRefresh();
	}
}
