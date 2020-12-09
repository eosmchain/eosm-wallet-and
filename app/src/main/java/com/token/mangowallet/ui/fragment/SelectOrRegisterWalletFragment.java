package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIViewPager;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.listener.ControlListener;
import com.token.mangowallet.utils.AppFilePath;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.WalletDaoUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;

public class SelectOrRegisterWalletFragment extends BaseFragment {


    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.tabs)
    QMUITabSegment tabs;
    @BindView(R.id.pager)
    QMUIViewPager pager;

    private Unbinder unbinder;
    private HashMap<Pager, QMUIWindowInsetLayout> mPages;
    public MangoWallet mangoWallet;
    private QMUIWindowInsetLayout homeComponentsController;
    private QMUIWindowInsetLayout homeUtilController;
    private Bitmap mQRCodeBitmap;
    public Constants.WalletType walletType;

    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        private int mChildCount = 0;

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            QMUIWindowInsetLayout page = mPages.get(Pager.getPagerFromPositon(position));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(page, params);
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount == 0) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }
    };

    @Override
    protected View onCreateView() {
        QMUIStatusBarHelper.translucent(getActivity());
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sel_or_reg, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initView() {
        topBar.setTitle(String.format(getString(R.string.str_import_title), walletType));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        initTabs();
        initPagers();
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
    }

    @Override
    protected void initAction() {
    }

    private void initTabs() {
        QMUITabBuilder builder = tabs.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT_BOLD);
        builder.setSelectedIconScale(1.2f)
                .setTextSize(QMUIDisplayHelper.sp2px(getContext(), 13), QMUIDisplayHelper.sp2px(getContext(), 15))
                .setDynamicChangeIconColor(false);

        QMUITab component = builder
                .setText(String.format(getString(R.string.str_select_account), walletType + ""))
                .setColorAttr(R.attr.qmui_config_color_gray_3, R.attr.qmui_config_color_blue)
                .build(getContext());

        QMUITab util = builder
                .setText(String.format(getString(R.string.str_register_account), walletType + ""))
                .setColorAttr(R.attr.qmui_config_color_gray_3, R.attr.qmui_config_color_blue)
                .build(getContext());

        tabs.addTab(component)
                .addTab(util);
        tabs.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                if (index == 0) {
                    ((SelectWalletController) homeComponentsController).initView();
                }
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }

    private void initPagers() {
        mPages = new HashMap<>();

        ControlListener listener = new ControlListener() {
            @Override
            public void onShare(Bitmap bitmap) {
                mQRCodeBitmap = bitmap;
                if (mQRCodeBitmap != null) {
                    Intent imageIntent = new Intent(Intent.ACTION_SEND);
                    imageIntent.setType("image/*");  //设置分享内容的类型
                    imageIntent.putExtra(Intent.EXTRA_STREAM, AppFilePath.saveBitmap(getActivity(), mQRCodeBitmap));
                    startActivity(Intent.createChooser(imageIntent, getString(R.string.str_share)));
                }
            }

            @Override
            public void onSelectWallet(String address) {
                mangoWallet.setWalletAddress(address);
                WalletDaoUtils.mangoWalletDao.update(mangoWallet);
                popBackStack();
            }
        };
        homeComponentsController = new SelectWalletController(this);
        ((SelectWalletController) homeComponentsController).setControlListener(listener);
        mPages.put(Pager.SELECT, homeComponentsController);
        homeUtilController = new RegisterWalletController(this);
        ((RegisterWalletController) homeUtilController).setControlListener(listener);
        mPages.put(Pager.REGISTER, homeUtilController);
        pager.setAdapter(mPagerAdapter);
        tabs.setupWithViewPager(pager, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((RegisterWalletController) homeUtilController).destroy();
        homeUtilController = null;
        ((SelectWalletController) homeComponentsController).destroy();
        homeComponentsController = null;
        if (mQRCodeBitmap != null) {
            mQRCodeBitmap.recycle();
        }
        mQRCodeBitmap = null;
    }

    enum Pager {
        SELECT, REGISTER;

        public static Pager getPagerFromPositon(int position) {
            switch (position) {
                case 0:
                    return SELECT;
                case 1:
                    return REGISTER;
                default:
                    return SELECT;
            }
        }
    }
}
