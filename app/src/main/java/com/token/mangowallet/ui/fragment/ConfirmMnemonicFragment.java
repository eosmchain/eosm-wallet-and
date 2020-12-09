package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bus.ToWallet;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.utils.WalletDaoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.BUS_CUT_WALLET;
import static com.token.mangowallet.utils.Constants.BUS_TO_WALLET;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class ConfirmMnemonicFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topBar;
    @BindView(R.id.text1)
    AppCompatTextView text1;
    @BindView(R.id.mnemonicTextLayout)
    QMUIFloatLayout mnemonicTextLayout;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.mnemonicTagLayout)
    QMUIFloatLayout mnemonicTagLayout;
    @BindView(R.id.completeBtn)
    AppCompatButton completeBtn;

    private Unbinder unbinder;
    private MangoWallet wallet;
    private List<String> shuffleList = new ArrayList<>();
    private List<String> confirmList = new ArrayList<>();
    private List<String> originList;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_confirm_mnemonic, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @OnClick(R.id.completeBtn)
    public void onViewClicked() {
        if (ObjectUtils.equals(originList, confirmList)) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            WalletDaoUtils.updateCurrent(wallet);
            BusUtils.post(BUS_TO_WALLET, new ToWallet(wallet, BUS_CUT_WALLET));
            getActivity().finish();
        } else {
            ToastUtils.showShort(R.string.str_backup_mnemonics_error);
        }
    }

    @Override
    protected void initView() {
        topBar.setTitle(getString(R.string.str_backup_mnemonic));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });

        for (int i = 0; i < shuffleList.size(); i++) {
            addItemToFloatLayout(mnemonicTagLayout, shuffleList.get(i), true);
        }
        int height = SizeUtils.getMeasuredHeight(mnemonicTagLayout);
        mnemonicTextLayout.setMinimumHeight(height);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        wallet = bundle.getParcelable(EXTRA_WALLET);
        originList = wallet.getMnemonicCode();
        shuffleList.clear();
        shuffleList.addAll(originList);
        Collections.shuffle(shuffleList);
    }

    @Override
    protected void initAction() {

    }

    private void addItemToFloatLayout(QMUIFloatLayout floatLayout, String text, boolean isClick) {
        TextView textView = new TextView(getActivity());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setText(text);
        if (isClick) {
//            boolean isSelect = false;
            int textViewPadding = QMUIDisplayHelper.dp2px(getActivity(), 5);
            textView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
            textView.setGravity(Gravity.CENTER);
            textView.setTag(R.id.mnemon_select_tag, false);
            textView.setBackgroundColor(ColorUtils.getColor(R.color.qmui_config_color_white));

            textView.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
                @Override
                public void onDebouncingClick(View v) {
                    boolean isSelect = (boolean) ((TextView) v).getTag(R.id.mnemon_select_tag);
                    int currentChildCount = 0;
                    String text = ((TextView) v).getText().toString().trim();
                    if (isSelect) {
                        ((TextView) v).setTag(R.id.mnemon_select_tag, false);
                        ((TextView) v).setBackgroundColor(ColorUtils.getColor(R.color.qmui_config_color_white));
                        for (int i = 0; i < confirmList.size(); i++) {
                            String str = confirmList.get(i);
                            if (ObjectUtils.equals(str, text)) {
                                currentChildCount = i;
                                confirmList.remove(currentChildCount);
                                removeItemFromFloatLayout(mnemonicTextLayout, currentChildCount);
                            }
                        }
                    } else {
                        ((TextView) v).setTag(R.id.mnemon_select_tag, true);
                        ((TextView) v).setBackgroundColor(ColorUtils.getColor(R.color.qmui_config_color_gray_9));
                        confirmList.add(text);
                        addItemToFloatLayout(mnemonicTextLayout, text, false);
                    }
                    if (confirmList.size() == originList.size()) {
                        completeBtn.setEnabled(true);
                        completeBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_color_dark_blue));
                    } else {
                        completeBtn.setEnabled(false);
                        completeBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.item_divider_bg_color));
                    }
                }
            });
        }
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(SizeUtils.dp2px(5),
                SizeUtils.dp2px(5),
                SizeUtils.dp2px(5),
                SizeUtils.dp2px(5));
        floatLayout.addView(textView, lp);
    }

    private void removeItemFromFloatLayout(QMUIFloatLayout floatLayout, int index) {
        if (floatLayout.getChildCount() == 0) {
            return;
        }
        floatLayout.removeView(floatLayout.getChildAt(index));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
