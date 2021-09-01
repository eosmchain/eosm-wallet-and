package com.token.mangowallet.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.layout.QMUILayoutHelper;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.token.mangowallet.R;
import com.token.mangowallet.bus.ToWallet;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.ui.adapter.WalletItemAdapter;
import com.token.mangowallet.ui.adapter.WalletTypeAdapter;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.WalletDaoUtils;
import com.token.mangowallet.view.basepopup.BasePopup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.BUS_CUT_WALLET;
import static com.token.mangowallet.utils.Constants.BUS_TO_WALLET;
import static com.token.mangowallet.utils.Constants.WalletType.ALL;
import static com.token.mangowallet.utils.Constants.WalletType.BTC;
import static com.token.mangowallet.utils.Constants.WalletType.EOS;
import static com.token.mangowallet.utils.Constants.WalletType.ETH;
import static com.token.mangowallet.utils.Constants.WalletType.MGP;

public class WalletPopupWindow extends BasePopup {


    @BindView(R.id.pullDownIv)
    AppCompatImageView pullDownIv;
    @BindView(R.id.typeRecycler)
    RecyclerView typeRecycler;
    @BindView(R.id.titleTv)
    AppCompatTextView titleTv;
    @BindView(R.id.addWalletIv)
    AppCompatImageView addWalletIv;
    @BindView(R.id.titleLayout)
    FrameLayout titleLayout;
    @BindView(R.id.walletItemRecycler)
    RecyclerView walletItemRecycler;
    @BindView(R.id.contentLayout)
    QMUIFrameLayout contentLayout;

    private Activity activity;
    private Unbinder unbinder;
    private int mLastCheckedPosition = -1;
    private List<Constants.WalletType> walletTypeList;
    private WalletTypeAdapter walletTypeAdapter;
    private WalletItemAdapter walletItemAdapter;
    private int mRadius;
    private OnAddWalletClickListener listener;
    private List<MangoWallet> allWalletList;
    private List<MangoWallet> filterWalletList = CollectionUtils.newArrayList();
    private Constants.WalletType mCurWalletType = ALL;

    public WalletPopupWindow(Activity activity) {
        super(activity);
        this.activity = activity;
        initData();
    }

    private void initData() {
        allWalletList = WalletDaoUtils.loadAll();
        filterWalletList.clear();
        for (int i = 0, size = allWalletList.size(); i < size; i++) {
            MangoWallet mangoWallet = allWalletList.get(i);
            Constants.WalletType walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
            if (walletType == MGP) {
                filterWalletList.add(mangoWallet);
            }
        }
//        Iterator<MangoWallet> it_b = allWalletList.iterator();
//        while (it_b.hasNext()) {
//            MangoWallet a = it_b.next();
//            Constants.WalletType walletType = Constants.WalletType.getPagerFromPositon(a.getWalletType());
//            if (walletType == ETH || walletType == BTC) {
//                it_b.remove();
//            }
//        }
        mRadius = QMUIDisplayHelper.dp2px(activity, 10);
        walletTypeList = CollectionUtils.newArrayList();
        walletTypeList.add(ALL);
        walletTypeList.add(MGP);
//        walletTypeList.add(ETH);
//        walletTypeList.add(BTC);
        walletTypeList.add(EOS);
    }

    @Override
    protected void initAttributes() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_wallet_menu, null);
        setContentView(view);
        unbinder = ButterKnife.bind(this, view);

        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口点击外部消失
        setBackgroundDimEnable(false);
    }

    @Override
    protected void initViews(View view) {
        contentLayout.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);


        walletItemAdapter = new WalletItemAdapter(filterWalletList);
        walletItemRecycler.setLayoutManager(new LinearLayoutManager(activity));
        walletItemRecycler.setAdapter(walletItemAdapter);
        walletItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

                MangoWallet mangoWallet = filterWalletList.get(position);
                WalletDaoUtils.updateCurrent(mangoWallet);
                BusUtils.post(BUS_TO_WALLET, new ToWallet(mangoWallet, BUS_CUT_WALLET));
                dismiss();
            }
        });
        walletItemAdapter.setOnWalletItemClickListener(new WalletItemAdapter.OnWalletItemClickListener() {
            @Override
            public void onToWalletMore(MangoWallet wallet) {

            }
        });
        walletTypeAdapter = new WalletTypeAdapter(walletTypeList);
        typeRecycler.setLayoutManager(new LinearLayoutManager(activity));
        typeRecycler.setAdapter(walletTypeAdapter);
        walletTypeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                mCurWalletType = walletTypeList.get(position);
                if (listener != null) {
                    listener.onSelectWalletType(mCurWalletType);
                }
                setItemChecked(position);
                filterWallet();
                int title;
                if (mCurWalletType == MGP) {
                    title = R.string.str_mgptoken;
                } else if (mCurWalletType == ETH) {
                    title = R.string.str_ethereum;
                } else if (mCurWalletType == BTC) {
                    title = R.string.str_bitcoin;
                } else if (mCurWalletType == EOS) {
                    title = R.string.str_eosio;
                } else {
                    title = R.string.str_identity_wallet;
                }
                titleTv.setText(title);
//                walletItemAdapter.setNewData(filterWalletList);
                walletItemAdapter.notifyDataSetChanged();
            }
        });
        setItemChecked(0);
        pullDownIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void filterWallet() {
        filterWalletList.clear();
        if (mCurWalletType == ALL) {
            addWalletIv.setVisibility(View.GONE);
            filterWalletList.addAll(allWalletList);
        } else {
            addWalletIv.setVisibility(View.VISIBLE);
            for (int i = 0, size = allWalletList.size(); i < size; i++) {
                MangoWallet wallet = allWalletList.get(i);
                if (Constants.WalletType.getPagerFromPositon(wallet.getWalletType()) == mCurWalletType) {
                    filterWalletList.add(wallet);
                }
            }
        }
    }

    /**
     * @param position
     */
    public void setItemChecked(int position) {
        if (mLastCheckedPosition == position) {
            return;
        }
        if (walletTypeAdapter.getBooleanArray().get(position)) {
            walletTypeAdapter.getBooleanArray().put(position, false);
        } else {
            walletTypeAdapter.getBooleanArray().put(position, true);
        }
        if (mLastCheckedPosition > -1) {
            if (mLastCheckedPosition != position) {
                walletTypeAdapter.getBooleanArray().put(mLastCheckedPosition, false);
                walletTypeAdapter.notifyItemChanged(mLastCheckedPosition);
            }
        }
        walletTypeAdapter.notifyDataSetChanged();
        mLastCheckedPosition = position;
    }

    public void showPop(View playview) {
        allWalletList = WalletDaoUtils.loadAll();
        filterWallet();
        this.showAtLocation(playview, Gravity.BOTTOM, 0, 0);
        walletItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
    }

    public void release() {
        if (unbinder != null && unbinder != Unbinder.EMPTY) {

        }
    }

    public void setOnAddWalletClickListener(OnAddWalletClickListener listener) {
        this.listener = listener;
    }


    public interface OnAddWalletClickListener {
        void onAddWallet(View v);

        void onSelectWalletType(Constants.WalletType walletType);

        void onSelectWallet(MangoWallet mangoWallet);
    }

    @OnClick({R.id.addWalletIv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addWalletIv:
                if (listener != null) {
                    listener.onAddWallet(view);
                }
                break;
        }
    }
}
