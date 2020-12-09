package com.token.mangowallet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.token.mangowallet.R;

public class RequiredTextView extends AppCompatTextView {

    private String prefix = "";
    private int prefixColor = Color.RED;

    public RequiredTextView(@NonNull Context context) {
        super(context);
    }

    public RequiredTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RequiredTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        String text;
        TypedArray ta = null;
        if (attrs != null) {
            ta = context.obtainStyledAttributes(attrs, R.styleable.RequiredTextView);
            prefix = ta.getString(R.styleable.RequiredTextView_prefix);
            prefixColor = ta.getInteger(R.styleable.RequiredTextView_prefix_color, Color.RED);
            text = ta.getString(R.styleable.RequiredTextView_android_text);

        } else {
            text = ObjectUtils.isEmpty(getText()) ? "" : getText().toString().trim();
        }
//        if (ObjectUtils.isEmpty(prefix)) {
//            prefix = "*";
//        }
        if (ObjectUtils.isEmpty(text)) {
            text = "";
        }
        setText(text);
        if (ta != null) {
            ta.recycle();
        }
    }

    public void setText(String text) {
        Spannable span = new SpannableString(prefix + text);
        span.setSpan(new ForegroundColorSpan(prefixColor), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(span);
    }

}
