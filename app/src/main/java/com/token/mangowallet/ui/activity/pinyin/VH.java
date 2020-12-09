package com.token.mangowallet.ui.activity.pinyin;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.token.mangowallet.R;

class VH extends RecyclerView.ViewHolder {

    AppCompatTextView tvName;
    AppCompatTextView tvCode;
    AppCompatImageView ivFlag;

    VH(View itemView) {
        super(itemView);
        ivFlag = (AppCompatImageView) itemView.findViewById(R.id.iv_flag);
        tvName = (AppCompatTextView) itemView.findViewById(R.id.tv_name);
        tvCode = (AppCompatTextView) itemView.findViewById(R.id.tv_code);
    }
}
