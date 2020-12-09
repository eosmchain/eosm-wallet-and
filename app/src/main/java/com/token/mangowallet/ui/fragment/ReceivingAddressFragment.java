package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.ShippingAddressBean;
import com.token.mangowallet.bus.ShippingAddressEffect;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.net.exception.CompanyException;
import com.token.mangowallet.ui.adapter.ReceivingAddressAdapter;
import com.token.mangowallet.utils.RSAUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.token.mangowallet.utils.Constants.EXTRA_ISADD_ADDRESS;
import static com.token.mangowallet.utils.Constants.EXTRA_RECEIVER_ADDRESS;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class ReceivingAddressFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    private boolean isAdd = true;
    private ShippingAddressBean shippingAddressBean;
    private ShippingAddressBean.DataBean addressDataBean;
    private List<ShippingAddressBean.DataBean> dataBeanList = new ArrayList<>();
    private MangoWallet mangoWallet;
    private ReceivingAddressAdapter addressAdapter;
    private String walletAddress;
    private boolean isDefault;
    private int mPosition;
    private boolean isDel;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_receiving_address, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletAddress = mangoWallet.getWalletAddress();
//        shippingAddressBean = bundle.getParcelable(EXTRA_RECEIVER_ADDRESS);
//        if (shippingAddressBean != null) {
//            dataBeanList.clear();
//            dataBeanList.addAll(shippingAddressBean.getData());
//        }
    }

    @Override
    protected void initView() {
        topBar.setTitle(R.string.address_title_manage);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        topBar.addRightImageButton(R.mipmap.ic_add_black, R.id.topbar_right_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                toAddAddress();
            }
        });

        addressAdapter = new ReceivingAddressAdapter(dataBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(addressAdapter);
        addressAdapter.setEmptyView(R.layout.view_empty);
        addressAdapter.setOnEditAddressClickListener(new ReceivingAddressAdapter.OnEditAddressClickListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, int position) {
                isDefault = isChecked;
                mPosition = position;
                addressDataBean = dataBeanList.get(position);
                updateAddr(addressDataBean, isChecked);
            }

            @Override
            public void onEditAddress(int position) {
                isAdd = false;
                mPosition = position;
                addressDataBean = dataBeanList.get(position);
                toAddAddress();
            }

            @Override
            public void onDeleteAddress(int position) {
                mPosition = position;
                addressDataBean = dataBeanList.get(position);
                delAddr(String.valueOf(addressDataBean.getAddrID()));
            }
        });
    }

    @Override
    protected void initAction() {
        addressAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                ShippingAddressBean.DataBean addressDataBean = dataBeanList.get(position);
                notifyEffect(new ShippingAddressEffect(addressDataBean));
                popBackStack();
            }
        });
    }

    private void toAddAddress() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_ISADD_ADDRESS, isAdd);
        bundle.putParcelable(EXTRA_WALLET, mangoWallet);
        if (!isAdd) {
            bundle.putParcelable(EXTRA_RECEIVER_ADDRESS, addressDataBean);
        }
        startFragment("EditAddressFragment", bundle);
    }

    /**
     * 查询所有收货地址
     */
    private void findAddr() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().findAddr(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::allAddressSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除收货地址
     */
    private void delAddr(String addrID) {
        isDel = true;
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("addrID", addrID);
        params.put("address", walletAddress);
        String json = GsonUtils.toJson(params);
        try {
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().delAddr(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::delOrUpdateAddrSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新地址
     */
    private void updateAddr(ShippingAddressBean.DataBean dataBean, boolean isDefault) {
        isDel = false;
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("id", String.valueOf(dataBean.getAddrID()));
        params.put("userId", String.valueOf(dataBean.getUserId()));
        params.put("userName", String.valueOf(dataBean.getUserName()));
        params.put("phone", String.valueOf(dataBean.getPhone()));
        params.put("city", String.valueOf(dataBean.getCity()));
        params.put("detailedAddress", String.valueOf(dataBean.getDetailedAddress()));
        params.put("isDefault", isDefault);
        params.put("country", String.valueOf(dataBean.getCountry()));
        String json = GsonUtils.toJson(params);
        try {
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().updateAddr(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::delOrUpdateAddrSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void allAddressSuccess(JsonObject jsonData) {
        dismissTipDialog();
        LogUtils.dTag(LOG_TAG, GsonUtils.toJson(jsonData));
        shippingAddressBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), ShippingAddressBean.class);
        if (ObjectUtils.isNotEmpty(shippingAddressBean.getData())) {
            dataBeanList.clear();
            dataBeanList.addAll(shippingAddressBean.getData());
        }
        addressAdapter.notifyDataSetChanged();
    }

    private void delOrUpdateAddrSuccess(JsonObject jsonData) {
        CompanyException companyException = GsonUtils.fromJson(GsonUtils.toJson(jsonData), CompanyException.class);
        if (companyException.getCode() == 0) {
            if (isDel) {
                dismissTipDialog();
                addressAdapter.removeAt(mPosition);
            } else {
                findAddr();
            }
        } else {
            ToastUtils.showLong(companyException.getMsg());
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    @Override
    public void onResume() {
        super.onResume();
        findAddr();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissTipDialog();
    }
}
