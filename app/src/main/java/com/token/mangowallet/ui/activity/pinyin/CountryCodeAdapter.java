package com.token.mangowallet.ui.activity.pinyin;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.token.mangowallet.R;

import java.util.List;

public class CountryCodeAdapter extends PyAdapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private OnItemClickListener listener;


    public CountryCodeAdapter(Activity activity, List<? extends PyEntity> entities) {
        super(entities);
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateLetterHolder(ViewGroup parent, int viewType) {
        return new LetterHolder(activity.getLayoutInflater().inflate(R.layout.item_letter, parent, false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new VH(activity.getLayoutInflater().inflate(R.layout.item_country_large_padding, parent, false));
    }

    @Override
    public void onBindHolder(RecyclerView.ViewHolder holder, PyEntity entity, int position) {
        VH vh = (VH) holder;
        final CountryBean country = (CountryBean) entity;
        vh.ivFlag.setImageResource(country.flag);
        vh.tvName.setText(country.name);
        vh.tvCode.setText("+" + country.code);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(holder.itemView, entity, position);
            }
//            Intent data = new Intent();
//            data.putExtra("country", country.toJson());
//            setResult(Activity.RESULT_OK, data);
//            finish();
        });
    }

    @Override
    public void onBindLetterHolder(RecyclerView.ViewHolder holder, LetterEntity entity, int position) {
        ((LetterHolder) holder).textView.setText(entity.letter.toUpperCase());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(@NonNull View view, PyEntity entity, int position);
    }
}
