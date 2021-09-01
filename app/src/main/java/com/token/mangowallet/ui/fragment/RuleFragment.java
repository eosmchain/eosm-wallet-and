package com.token.mangowallet.ui.fragment;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.net.common.NetWorkManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class RuleFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.ruleTv)
    AppCompatTextView ruleTv;

    private Unbinder unbinder;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_rule_explain, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }


    @Override
    protected void initData() {
        getNodeRule();
    }

    @Override
    protected void initView() {
        topbar.setTitle(R.string.str_vote_rule);
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    @Override
    protected void initAction() {

    }

    private void getNodeRule() {
        try {
            NetWorkManager.getRequest().nodeRule()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::nodeRuleSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nodeRuleSuccess(JsonObject jsonObject) {
        LogUtils.dTag(LOG_TAG, "jsonObject = " + GsonUtils.toJson(jsonObject));
        dismissTipDialog();
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ruleTv.setText(Html.fromHtml(msgCodeBean.getData(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    ruleTv.setText(Html.fromHtml(msgCodeBean.getData()));
                }
            } else {
                ToastUtils.showLong(msgCodeBean.getMsg());
            }
        } else {
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
//        ToastUtils.showLong(R.string.str_network_error);
    }
}
