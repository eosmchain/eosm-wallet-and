package com.token.mangowallet.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.ProBean;
import com.token.mangowallet.bean.ProListBean;
import com.token.mangowallet.utils.BalanceUtils;


import java.math.RoundingMode;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class ClassifyListAdapter extends RecyclerView.Adapter<ClassifyListAdapter.VH> {


    private List<ProBean> mDatas;
    private Context mContext;
    private int viewType;
    private OnGoodsItemClickListener mListener;


    public ClassifyListAdapter(Context context, List<ProBean> data) {
        this.mDatas = data;
        mContext = context;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (viewType == 0) {
            return new VH(LayoutInflater.from(mContext).inflate(R.layout.item_grid_pro, viewGroup, false));
        } else if (viewType == 1) {
            return new VH(LayoutInflater.from(mContext).inflate(R.layout.item_list_pro, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {
        final ProBean item = mDatas.get(i);
        vh.commodityNameTv.setText(ObjectUtils.isEmpty(item.getStoreName()) ? "" : item.getStoreName());
        vh.commodityPriceTv.setText(BalanceUtils.currencyToBase(ObjectUtils.isEmpty(item.getPrice()) ? "0" : item.getPrice(), 2, RoundingMode.FLOOR));
        vh.haveBrowsingTv.setText(String.format(mContext.getString(R.string.str_have_browsing), ObjectUtils.isEmpty(item.getBrowse()) ? "0" : item.getBrowse()));
        String imgUrl = "";
        if (ObjectUtils.isNotEmpty(item.getImage_url())) {
            imgUrl = item.getImage_url().get(0);
        }
        Glide.with(mContext)
                .load(imgUrl)
                .apply(new RequestOptions().placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_placeholder))
                .into(vh.commodityPicIv);

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ObjectUtils.isEmpty(mListener)) {
                    mListener.onItemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        LogUtils.dTag(LOG_TAG, "ClassifyListAdapter size = " + mDatas.size());
        return mDatas.size();
    }

    /**
     * 点击切换布局的时候调用这个方法设置type
     *
     * @param type 商品排列的方式 0：网格；1：垂直列表排列
     */
    public void setType(int type) {
        this.viewType = type;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }


    public class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.commodityPicIv)
        AppCompatImageView commodityPicIv;
        @BindView(R.id.commodityNameTv)
        AppCompatTextView commodityNameTv;
        @BindView(R.id.commodityPriceTv)
        AppCompatTextView commodityPriceTv;
        @BindView(R.id.haveBrowsingTv)
        AppCompatTextView haveBrowsingTv;

        public VH(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }


    public interface OnGoodsItemClickListener {

        void onItemClick(ProBean groupInfo);
    }


    public void setListener(OnGoodsItemClickListener listener) {
        mListener = listener;
    }
}
