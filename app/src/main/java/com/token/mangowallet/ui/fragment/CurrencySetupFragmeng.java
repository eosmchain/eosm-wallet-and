package com.token.mangowallet.ui.fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CurrencyData;
import com.token.mangowallet.bean.CurrencySetupBean;
import com.token.mangowallet.net.common.BaseUrlUtils;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.common.ServerInfo;
import com.token.mangowallet.utils.Constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.token.mangowallet.utils.Constants.CORPORATION_URL;
import static com.token.mangowallet.utils.Constants.KEY_CURRENCY_DATA;
import static com.token.mangowallet.utils.Constants.KEY_CURRENCY_SYMBOL;
import static com.token.mangowallet.utils.Constants.KEY_SERVER;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.SP_MangoWallet_info;

public class CurrencySetupFragmeng extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.groupListView)
    QMUIGroupListView groupListView;
    @BindView(R.id.appVersionTv)
    AppCompatTextView appVersionTv;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;

    private Unbinder unbinder;
    private List<CurrencyData> currencyList;
    private QMUIBottomSheet bottomSheet;
    private float pressY = 0;
    private float moveY = 0;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_language, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        QMUIQQFaceView topbarTitleView = topbar.setTitle(StringUtils.getString(R.string.str_currency_settings));
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        topbarTitleView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下
                        pressY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE://移动
                        moveY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP://松开
                        if (moveY - pressY > 0 && (Math.abs(moveY - pressY) > 200)) {//wang向下
                            if (bottomSheet == null) {
                                showSimpleBottomSheetList();
                            } else {
                                bottomSheet.show();
                            }
                        } else if (moveY - pressY < 0 && (Math.abs(moveY - pressY) > 25)) {//wang向上

                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        getCoinSymbol();
    }

    @Override
    protected void initView() {
        appVersionTv.setVisibility(View.GONE);
    }

    @Override
    protected void initAction() {

    }

    private void updataGroupListView() {
        if (currencyList != null) {
            String mCurveCurrencySymbol = SPUtils.getInstance(SP_MangoWallet_info).getString(KEY_CURRENCY_SYMBOL, "$");
            QMUIGroupListView.Section section = QMUIGroupListView.newSection(getActivity());
            for (int i = 0; i < currencyList.size(); i++) {
                CurrencyData dataBean = currencyList.get(i);
                QMUICommonListItemView listItemView = groupListView.createItemView(dataBean.getName() + "[" + dataBean.getSymbolName() + "]");
                listItemView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
                listItemView.setOrientation(QMUICommonListItemView.VERTICAL);
                listItemView.addAccessoryCustomView(getAccessoryImageView());
                listItemView.setTag(R.id.currencyID, i);
                if (ObjectUtils.equals(mCurveCurrencySymbol, dataBean.getSymbol())) {
                    listItemView.getAccessoryContainerView().setVisibility(View.VISIBLE);
                } else {
                    listItemView.getAccessoryContainerView().setVisibility(View.GONE);
                }
                section.addItemView(listItemView, onClickListener);
            }
            section.addTo(groupListView);

        }
    }

    ClickUtils.OnDebouncingClickListener onClickListener = new ClickUtils.OnDebouncingClickListener() {
        @Override
        public void onDebouncingClick(View v) {
            if (v instanceof QMUICommonListItemView) {
//                CharSequence text = ((QMUICommonListItemView) v).getText();
                int currencyID = (int) ((QMUICommonListItemView) v).getTag(R.id.currencyID);
                CurrencyData dataBean = currencyList.get(currencyID);
                SPUtils.getInstance(SP_MangoWallet_info).put(KEY_CURRENCY_DATA, GsonUtils.toJson(dataBean));
                SPUtils.getInstance(SP_MangoWallet_info).put(KEY_CURRENCY_SYMBOL, dataBean.getSymbol());
                popBackStack();
            }
        }
    };

    private void getCoinSymbol() {
        showTipDialog(getString(R.string.str_loading));
        try {
            NetWorkManager.getRequest().getCoinSymbol()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::coinSymbolSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void coinSymbolSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(jsonObject));
            CurrencySetupBean currencySetupBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), CurrencySetupBean.class);
            if (currencySetupBean.getCode() == 0) {
                currencyList = currencySetupBean.getData();
                updataGroupListView();
            }
        }
    }


    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.dTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    private AppCompatImageView getAccessoryImageView() {
        AppCompatImageView resultImageView = new AppCompatImageView(getActivity());
        resultImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        resultImageView.setScaleType(ImageView.ScaleType.CENTER);
        resultImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.pitchon_icon));
        return resultImageView;
    }

    private void showSimpleBottomSheetList() {
        if (bottomSheet == null) {
            LinkedHashMap<String, ServerInfo> serverInfoMap = BaseUrlUtils.getInstance().serverInfoMap;
            String serverName = SPUtils.getInstance(Constants.SP_MangoWallet_info).getString(KEY_SERVER, CORPORATION_URL);
            ServerInfo serverInfo = serverInfoMap.get(serverName);
            QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
            builder.setGravityCenter(true)
                    .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                    .setTitle("当前：" + serverInfo.getKserverName())
                    .setAddCancelBtn(true)
                    .setAllowDrag(false)
                    .setNeedRightMark(false)
                    .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            dialog.dismiss();
                            SPUtils.getInstance(Constants.SP_MangoWallet_info).put(KEY_SERVER, tag);
                        }
                    });

            Iterator iter = serverInfoMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                ServerInfo val = (ServerInfo) entry.getValue();
                LogUtils.dTag(LOG_TAG, key + " ：" + val.getKserverName());
                if (val != null) {
                    builder.addItem(val.getKserverName(), key);
                }
            }
            bottomSheet = builder.build();
        }
        if (!bottomSheet.isShowing()) {
            bottomSheet.show();
        }
    }

}
