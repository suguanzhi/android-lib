package com.android.sgzcommon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.sgzcommon.adapter.MPagerAdapter;
import com.android.sgzcommon.utils.UnitUtils;
import com.android.sugz.R;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by sgz on 2019/10/24 0024.
 */
public class NavigatorViewPager extends LinearLayout implements ViewPager.OnPageChangeListener {

    private int mPadding;
    private int mPosition;
    private int mTextColor;
    private int mTextColorSelected;
    private float mRatio;
    private View[] mLines;
    private TextView[] mTexts;
    private ViewPager mViewPager;
    private Drawable mLineBackground;
    private LinearLayout mNavItemsLayout;
    private OnPageChangeListener listener;

    public NavigatorViewPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRatio = 1f;
        mPosition = -1;
        mLines = new View[]{};
        mTexts = new TextView[]{};
        setOrientation(VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;

        LinearLayout navParentLayout = new LinearLayout(getContext());
        navParentLayout.setId(generateViewId());
        navParentLayout.setOrientation(VERTICAL);
        addView(navParentLayout, params);

        mNavItemsLayout = new LinearLayout(getContext());
        mNavItemsLayout.setId(generateViewId());

        LinearLayout navLinesLayout = new LinearLayout(getContext());
        navLinesLayout.setId(generateViewId());

        mViewPager = new ViewPager(getContext());
        mViewPager.setId(generateViewId());
        mViewPager.addOnPageChangeListener(this);
        addView(mViewPager, params);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.pager_nav);
        String items = a.getString(R.styleable.pager_nav_items);
        int textSize = a.getDimensionPixelSize(R.styleable.pager_nav_text_size, UnitUtils.sp2px(14f));
        int lineHeight = a.getDimensionPixelSize(R.styleable.pager_nav_line_height, UnitUtils.dp2px(4f));
        int lineMargin = a.getDimensionPixelSize(R.styleable.pager_nav_line_margin, 0);
        mTextColor = a.getColor(R.styleable.pager_nav_text_color, Color.parseColor("#212121"));
        mTextColorSelected = a.getColor(R.styleable.pager_nav_text_color_selected, Color.BLACK);
        Drawable navBackground = a.getDrawable(R.styleable.pager_nav_background);
        if (navBackground != null) {
            navParentLayout.setBackground(navBackground);
        }
        int navLayoutWidth = a.getDimensionPixelSize(R.styleable.pager_nav_nav_width, LayoutParams.MATCH_PARENT);
        mPadding = a.getDimensionPixelSize(R.styleable.pager_nav_padding, UnitUtils.dp2px(10f));
        mLineBackground = a.getDrawable(R.styleable.pager_nav_line);
        a.recycle();

        LayoutParams navLayoutParams = new LayoutParams(navLayoutWidth, LayoutParams.WRAP_CONTENT);
        navLayoutParams.gravity = Gravity.CENTER;
        navParentLayout.addView(mNavItemsLayout, navLayoutParams);
        navParentLayout.addView(navLinesLayout, navLayoutParams);

        if (!TextUtils.isEmpty(items)) {
            String[] texts = items.split(",");
            LayoutParams itemParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
            itemParams.weight = 1;
            itemParams.gravity = Gravity.CENTER;
            mTexts = new TextView[texts.length];
            mViewPager.setOffscreenPageLimit(texts.length);
            for (int i = 0; i < texts.length; i++) {
                TextView text = new TextView(getContext());
                text.setId(generateViewId());
                text.setText(texts[i]);
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                text.setTextColor(mTextColor);
                text.setGravity(Gravity.CENTER);
                text.setPadding(0, mPadding, 0, mPadding);
                text.setTag(i);
                text.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        mViewPager.setCurrentItem(position);
                    }
                });
                mNavItemsLayout.addView(text, itemParams);
                mTexts[i] = text;
            }
            LayoutParams lineParams = new LayoutParams(0, lineHeight);
            lineParams.weight = 1;
            lineParams.leftMargin = lineMargin;
            lineParams.rightMargin = lineMargin;
            mLines = new View[texts.length];
            for (int i = 0; i < texts.length; i++) {
                View line = new View(getContext());
                if (i == mPosition) {
                    if (mLineBackground != null) {
                        line.setBackground(mLineBackground);
                    } else {
                        line.setBackground(new ColorDrawable(Color.RED));
                    }
                } else {
                    line.setBackground(new ColorDrawable(Color.TRANSPARENT));
                }
                navLinesLayout.addView(line, lineParams);
                mLines[i] = line;
            }
        }
    }

    public void setFragments(FragmentManager fm, List<Fragment> fragments) {
        MPagerAdapter adapter = new MPagerAdapter(fm, fragments);
        mViewPager.setAdapter(adapter);
    }

    public void setCurrentItem(int item) {
        mViewPager.setCurrentItem(item);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("NavigatorViewPager", "onMeasure: " + MeasureSpec.getSize(widthMeasureSpec));
        measureChild(mNavItemsLayout, widthMeasureSpec, heightMeasureSpec);
        int navItemsWidth = mNavItemsLayout.getMeasuredWidth();
        int navParentWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (navParentWidth > 0) {
            mRatio = navItemsWidth * 1.0f / navParentWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (listener != null) {
            listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        if (mLines.length > 0 && position < mLines.length) {
            float x = (positionOffsetPixels / mLines.length) * mRatio;
            mLines[position].setTranslationX(x);
            if (position != mPosition) {
                mPosition = position;
                for (int i = 0; i < mLines.length; i++) {
                    if (i == mPosition) {
                        if (mLineBackground != null) {
                            mLines[i].setBackground(mLineBackground);
                        } else {
                            mLines[i].setBackground(new ColorDrawable(Color.RED));
                        }
                    } else {
                        mLines[i].setBackground(new ColorDrawable(Color.TRANSPARENT));
                    }
                }
                for (int i = 0; i < mTexts.length; i++) {
                    if (i == position) {
                        mTexts[i].setTextColor(mTextColorSelected);
                        mTexts[i].getPaint().setFakeBoldText(true);
                    } else {
                        mTexts[i].setTextColor(mTextColor);
                        mTexts[i].getPaint().setFakeBoldText(false);
                    }
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (listener != null) {
            listener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (listener != null) {
            listener.onPageScrollStateChanged(state);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.listener = listener;
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }
}
