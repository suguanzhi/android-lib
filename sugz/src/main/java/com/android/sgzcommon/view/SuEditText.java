package com.android.sgzcommon.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.sugz.R;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

/**
 * @author sgz
 * @date 2021-01-11
 */
public class SuEditText extends LinearLayout {

    private EditText mEtInput;
    private ImageView mIvClear;

    private OnTextChangeListener mTextChangedListener;
    private OnFocusChangeListener mFocusChangeListener;
    private TextView.OnEditorActionListener mEditorActionListener;

    private static final int RADIUS_STYLE_WRAP = 0;
    private static final int RADIUS_STYLE_CIRCLE = 1;


    public SuEditText(Context context) {
        this(context, null);
    }

    public SuEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SuEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.layout_sgz_edittext, this);
        mEtInput = findViewById(R.id.et_input);
        mIvClear = findViewById(R.id.iv_clear);
        mEtInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkCancleVisiable(mEtInput.getText());
                if (mFocusChangeListener != null) {
                    mFocusChangeListener.onFocusChange(v, hasFocus);
                }
            }
        });
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkCancleVisiable(s);
                if (mTextChangedListener != null) {
                    mTextChangedListener.onTextChanged(s);
                }
            }
        });
        mEtInput.setOnEditorActionListener(mEditorActionListener);
        mEtInput.setText("");
        mIvClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtInput.setText("");
            }
        });
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SuEditText);
        ColorStateList colors = array.getColorStateList(R.styleable.SuEditText_android_textColor);
        int radius = array.getDimensionPixelSize(R.styleable.SuEditText_radius, getResources().getDimensionPixelSize(R.dimen.input_radius));
        int radiusStyle = array.getInt(R.styleable.SuEditText_radius_style, RADIUS_STYLE_WRAP);
        int backgroundColor = array.getColor(R.styleable.SuEditText_background_color, getResources().getColor(R.color.sgz_input_background));
        int borderWidth = array.getDimensionPixelSize(R.styleable.SuEditText_border_width, getResources().getDimensionPixelSize(R.dimen.input_border_width));
        int borderColor = array.getColor(R.styleable.SuEditText_border_color, getResources().getColor(R.color.sgz_input_border_color));
        int clearPadding = array.getDimensionPixelOffset(R.styleable.SuEditText_clear_padding, getResources().getDimensionPixelOffset(R.dimen.input_clear_padding));
        int inputType = array.getInt(R.styleable.SuEditText_android_inputType, EditorInfo.TYPE_NULL);
        int imeOptions = array.getInt(R.styleable.SuEditText_android_imeOptions, EditorInfo.IME_NULL);
        setInputType(inputType);
        setImeOptions(imeOptions);
        if (colors != null) {
            setTextColor(colors);
        }
        Drawable clearSrc = array.getDrawable(R.styleable.SuEditText_android_src);
        if (clearSrc != null) {
            setClearDrawable(clearSrc);
        }
        mIvClear.setPadding(clearPadding, clearPadding, clearPadding, clearPadding);
        if (RADIUS_STYLE_CIRCLE == radiusStyle) {
            radius = Integer.MAX_VALUE;
        }
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(backgroundColor);
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setStroke(borderWidth, borderColor);
        setBackground(gradientDrawable);
        array.recycle();
    }

    /**
     * {@link EditorInfo#inputType}
     *
     * @param inputType
     */
    private void setInputType(int inputType) {
        mEtInput.setInputType(inputType);
    }

    /**
     * {@link EditorInfo#imeOptions}
     *
     * @param options
     */
    public void setImeOptions(int options) {
        mEtInput.setImeOptions(options);
    }

    /**
     * @param s
     */
    private void checkCancleVisiable(Editable s) {
        if (mEtInput.isFocused() && s.length() > 0) {
            mIvClear.setVisibility(VISIBLE);
        } else {
            mIvClear.setVisibility(GONE);
        }
    }

    public void setTextColor(@ColorInt int color) {
        mEtInput.setTextColor(color);
    }

    public void setTextColor(ColorStateList colors) {
        mEtInput.setTextColor(colors);
    }

    public void setText(CharSequence text) {
        mEtInput.setText(text);
    }

    public void setHint(CharSequence hint) {
        mEtInput.setHint(hint);
    }

    /**
     * @return
     */
    public Editable getText() {
        return mEtInput.getText();
    }

    /**
     * @param clearSrc
     */
    private void setClearDrawable(Drawable clearSrc) {
        mIvClear.setImageDrawable(clearSrc);
    }

    /**
     * @param listener
     */
    public void setTextChangeListener(OnTextChangeListener listener) {
        mTextChangedListener = listener;
    }

    /**
     * @param listener
     */
    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        mFocusChangeListener = listener;
    }

    /**
     * @param listener
     */
    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        mEtInput.setOnEditorActionListener(listener);
    }

    /**
     *
     */
    public interface OnTextChangeListener {
        void onTextChanged(Editable e);
    }
}
