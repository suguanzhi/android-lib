package com.android.sgzcommon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.sgzcommon.utils.UnitUtils;
import com.android.sugz.R;

import androidx.annotation.Nullable;

/**
 * @author sgz
 * @date 2020/9/4
 */
public class InputEditView extends LinearLayout {

    private ImageView mIvAdd;
    private ImageView mIvSub;
    private EditText mEtInput;

    private String defaultValue;
    private OnAddOrSubClickListener clickistener;
    private OnValueChangeListener changeListener;

    public InputEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_sgz_input_edit, this);
        LinearLayout container = view.findViewById(R.id.ll_container);
        mIvAdd = container.findViewById(R.id.iv_add);
        mIvSub = container.findViewById(R.id.iv_subtract);
        mEtInput = container.findViewById(R.id.et_input);
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = 0;
                String v = s.toString();
                try {
                    value = Integer.parseInt(v);
                } catch (Exception e) {
                    e.printStackTrace();
                    mEtInput.setText(defaultValue);
                }
                if (changeListener != null) {
                    changeListener.onValueChange(value);
                }
            }
        });
        mIvAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mEtInput.getText().toString();
                int value = 0;
                try {
                    value = Integer.parseInt(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ++value;
                mEtInput.setText(value + "");
                if (clickistener != null) {
                    clickistener.onAddClick();
                }
            }
        });
        mIvSub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mEtInput.getText().toString();
                int value = 0;
                try {
                    value = Integer.parseInt(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (value > 0) {
                    --value;
                }
                mEtInput.setText(value + "");
                if (clickistener != null) {
                    clickistener.onSubClick();
                }
            }
        });
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.InputEditView);

        int width = array.getDimensionPixelSize(R.styleable.InputEditView_width, 0);
        int height = array.getDimensionPixelSize(R.styleable.InputEditView_height, 0);
        if (height != 0) {
            ViewGroup.LayoutParams params = container.getLayoutParams();
            params.width = width;
            params.height = height;
            container.setLayoutParams(params);
        }
        defaultValue = array.getString(R.styleable.InputEditView_value);
        if (TextUtils.isEmpty(defaultValue)) {
            defaultValue = "0";
        }
        setValue(defaultValue);
        Drawable backgroundDrawable = array.getDrawable(R.styleable.InputEditView_background);
        if (backgroundDrawable != null) {
            container.setBackground(backgroundDrawable);
        }
        Drawable inputDrawable = array.getDrawable(R.styleable.InputEditView_input_background);
        if (inputDrawable != null) {
            setInputBackground(inputDrawable);
        }
        int inputTextColor = array.getColor(R.styleable.InputEditView_text_color, Color.BLACK);
        mEtInput.setTextColor(inputTextColor);
        float inputTextSize = array.getDimension(R.styleable.InputEditView_text_size, UnitUtils.sp2px(14));
        mEtInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputTextSize);
        Drawable addDrawable = array.getDrawable(R.styleable.InputEditView_add);
        if (addDrawable != null) {
            mIvAdd.setImageDrawable(addDrawable);
        }
        Drawable subDrawable = array.getDrawable(R.styleable.InputEditView_sub);
        if (subDrawable != null) {
            mIvSub.setImageDrawable(subDrawable);
        }
        int addSubPadding = array.getDimensionPixelSize(R.styleable.InputEditView_add_sub_padding, 0);
        mIvAdd.setPadding(addSubPadding, addSubPadding, addSubPadding, addSubPadding);
        mIvSub.setPadding(addSubPadding, addSubPadding, addSubPadding, addSubPadding);
        array.recycle();
    }

    public void setInputBackground(Drawable background) {
        mEtInput.setBackground(background);
    }

    public void setValue(String value) {
        mEtInput.setText(value);
    }

    public int getValue() {
        int value = 0;
        try {
            String s = mEtInput.getText().toString();
            value = Integer.parseInt(s);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return value;
    }

    public void setOnAddOrSubClickListener(OnAddOrSubClickListener listener) {
        clickistener = listener;
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        changeListener = listener;
    }

    public interface OnAddOrSubClickListener {
        void onAddClick();

        void onSubClick();
    }

    public interface OnValueChangeListener {
        void onValueChange(int value);
    }
}
