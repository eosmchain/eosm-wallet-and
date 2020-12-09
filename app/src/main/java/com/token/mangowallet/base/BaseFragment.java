/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.token.mangowallet.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.SwipeBackLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.R;
import com.token.mangowallet.ui.home.HomeFragment;
import com.token.mangowallet.utils.MangoFragmentManager;

/**
 * Created by cgspine on 2018/1/7.
 */

public abstract class BaseFragment extends QMUIFragment {

    /**
     * View有没有加载过
     */
    protected boolean isViewInitiated;

    private QMUITipDialog tipDialog;

    public void showTipDialog(String msg) {
        if (tipDialog == null) {
            tipDialog = new QMUITipDialog.Builder(getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(msg)
                    .create();
        }
        if (!tipDialog.isShowing()) {
            tipDialog.show();
        }
    }

    public void dismissTipDialog() {
        if (tipDialog != null) {
            if (tipDialog.isShowing()) { //check if dialog is showing.
                tipDialog.dismiss();
            }
        }
        tipDialog = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isViewInitiated) {
            initData();
            initView();
            initAction();
        }
        isViewInitiated = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        isViewInitiated = false;
        dismissTipDialog();
        super.onDestroy();
    }

    @Override
    protected int backViewInitOffset(Context context, int dragDirection, int moveEdge) {
        if (moveEdge == SwipeBackLayout.EDGE_TOP || moveEdge == SwipeBackLayout.EDGE_BOTTOM) {
            return 0;
        }
        return QMUIDisplayHelper.dp2px(context, 100);
    }

    @Override
    public Object onLastFragmentFinish() {
        return new HomeFragment();
    }

    public View getEmptyView(int mipmap, String tip) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_empty, null, false);
        AppCompatImageView mIv = view.findViewById(R.id.iv_empty);
        mIv.setImageResource(mipmap);
        AppCompatTextView mTvTip = view.findViewById(R.id.tv_tip);
        mTvTip.setText(tip);
        return view;
    }

    public View getEmptyView(RecyclerView recyclerView, int mipmap, String tip) {
        View view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.view_empty, (ViewGroup) recyclerView.getParent(), false);
        AppCompatImageView mIv = view.findViewById(R.id.iv_empty);
        mIv.setImageResource(mipmap);
        AppCompatTextView mTvTip = view.findViewById(R.id.tv_tip);
        mTvTip.setText(tip);
        return view;
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(QMUIFragment fragment, Class<T> cls) {
        return new ViewModelProvider(fragment).get(cls);
    }

    public synchronized void startFragment(String fragmentName) {
        try {
            BaseFragment fragment = MangoFragmentManager.getInstance().getFragment(fragmentName).newInstance();
            startFragment(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void startFragment(String fragmentName, Bundle bundle) {
        try {
            BaseFragment fragment = MangoFragmentManager.getInstance().getFragment(fragmentName).newInstance();
            fragment.setArguments(bundle);
            startFragment(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void startFragmentAndDestroyCurrent(String fragmentName, Bundle bundle, boolean useNewTransitionConfigWhenPop) {
        try {
            BaseFragment fragment = MangoFragmentManager.getInstance().getFragment(fragmentName).newInstance();
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            startFragmentAndDestroyCurrent(fragment, useNewTransitionConfigWhenPop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initAction();

    int type;
    boolean isOnHiddenChanged;
    boolean isSetUserVisibleHint;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            addTrace("1", getClass().getSimpleName(), getClass().getSimpleName());
        }
        isOnHiddenChanged = true;
        type = 1;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            addTrace("2", getClass().getSimpleName(), getClass().getSimpleName());
        }
        isSetUserVisibleHint = true;
        type = 2;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (type == 1) {
            if (!isOnHiddenChanged && !isHidden()) {
                addTrace(String.valueOf(type), getClass().getSimpleName(), getClass().getSimpleName());
            }
        } else if (type == 2) {
            if (!isSetUserVisibleHint && getUserVisibleHint()) {
                addTrace(String.valueOf(type), getClass().getSimpleName(), getClass().getSimpleName());
            }
        } else {
            if (!isOnHiddenChanged && !isHidden()) {
                addTrace(String.valueOf(type), getClass().getSimpleName(), getClass().getSimpleName());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isSetUserVisibleHint = false;
        isOnHiddenChanged = false;
    }

    private void addTrace(String ID, String NAME, String TYPE) {
        //上传事件
        if (MyApplication.mFirebaseAnalytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, ID);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, NAME);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, TYPE);
            MyApplication.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
        }
    }
}
