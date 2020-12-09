package com.token.mangowallet.ui.adapter;

import android.view.View;
import android.widget.CheckedTextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.AppHomeBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpinnerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public SpinnerAdapter(@Nullable List<String> data) {
        super(R.layout.item_custom_spinner_dropdown, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String data) {
        /**
         * childTitle : 链上绑定，智能识别
         * id : 5
         * isDel : false
         * lang : zh_CN
         * sort : 1
         * tab : 去中心化网络
         * title : MID身份标识
         * type : 0
         */
        CheckedTextView text1 = baseViewHolder.getView(R.id.text1);
        text1.setText(data);
    }
}
