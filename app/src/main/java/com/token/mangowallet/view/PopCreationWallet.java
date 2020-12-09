package com.token.mangowallet.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.token.mangowallet.R;
import com.token.mangowallet.view.basepopup.BasePopup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PopCreationWallet extends BasePopup {

    private OnCreationWalletClickListener listener;
    private Activity activity;
    private Unbinder unbinder;

    public PopCreationWallet(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void initAttributes() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_creation_wallet, null);
        setContentView(view);
        unbinder = ButterKnife.bind(this, view);

        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口点击外部消失
    }

    @Override
    protected void initViews(View view) {

    }

    @OnClick({R.id.createBtn, R.id.importBtn, R.id.cancelBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.createBtn:
                if (listener != null) {
                    listener.onCreateWallet();
                }
                break;
            case R.id.importBtn:
                if (listener != null) {
                    listener.onImportWallet();
                }
                break;
            case R.id.cancelBtn:
                if (listener != null) {
                    listener.onCancel();
                }
                break;
        }
    }

    public void setOnCreationWalletClickListener(OnCreationWalletClickListener listener) {
        this.listener = listener;
    }

    public void showPop(View playview) {
        this.showAtLocation(playview, Gravity.BOTTOM, 0, 0);
    }

    public void release() {
        if (unbinder != null && unbinder != Unbinder.EMPTY) {

        }
    }

    public interface OnCreationWalletClickListener {
        void onCancel();

        void onImportWallet();

        void onCreateWallet();
    }
}
