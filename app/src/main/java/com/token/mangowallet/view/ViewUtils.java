package com.token.mangowallet.view;

import android.text.InputFilter;
import android.widget.EditText;

public class ViewUtils {
    public static final CashierInputFilter filter = new CashierInputFilter();

    public static void setEditableEditText(EditText editText, boolean isEnabled) {
        editText.setEnabled(isEnabled);//去掉点击时编辑框下面横线:
        editText.setFocusable(isEnabled);//不可编辑
        editText.setFocusableInTouchMode(isEnabled);//不可编辑
    }


    public static void cashierFiltersEditText(EditText editText) {
        InputFilter[] filters = {filter};
        editText.setFilters(filters);
    }
}
