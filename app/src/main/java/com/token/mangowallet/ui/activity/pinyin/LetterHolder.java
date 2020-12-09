package com.token.mangowallet.ui.activity.pinyin;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class LetterHolder extends RecyclerView.ViewHolder {
    public final AppCompatTextView textView;
    public LetterHolder(View itemView) {
        super(itemView);
        textView = (AppCompatTextView) itemView;
    }
}
