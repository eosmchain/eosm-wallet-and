package com.token.mangowallet.view;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.ui.adapter.SpinnerAdapter;
import com.token.mangowallet.view.basepopup.BasePopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SpinnerPop extends BasePopup {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private BaseFragment fragment;
    private Unbinder unbinder;
    private OnItemClickListener listener;
    private List<String> mSpinnerList = new ArrayList<>();
    private SpinnerAdapter adapter;
    private boolean isOneSpinner = true;
    private View view;
    int[] location;

    public SpinnerPop(@NonNull BaseFragment fragment, View view) {
        super(fragment.getActivity());
        this.fragment = fragment;
        this.view = view;
        location = new int[2];
        //得到view到屏幕顶部的宽度和高度
        view.getLocationOnScreen(location);
        mSpinnerList.clear();
        mSpinnerList.addAll(Arrays.asList(StringUtils.getStringArray(R.array.spinner_values1)));
    }

    @Override
    protected void initAttributes() {
        LayoutInflater inflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_spinner, null);
        setContentView(view);
        unbinder = ButterKnife.bind(this, view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setFocusable(false);// 设置弹出窗口点击外部消失
        setBackgroundDimEnable(false);
        setAnimationStyle(R.style.dropdownAnim);
    }

    @Override
    protected void initViews(View view) {
        adapter = new SpinnerAdapter(mSpinnerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragment.getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(fragment.getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (listener != null) {
                    listener.onItemClick(adapter, view, position, isOneSpinner);
                }
            }
        });
    }

    public void setIsOneSpinner(boolean isOneSpinner) {
        this.isOneSpinner = isOneSpinner;
        mSpinnerList.clear();
        if (isOneSpinner) {
            mSpinnerList.addAll(Arrays.asList(StringUtils.getStringArray(R.array.spinner_values1)));
        } else {
            mSpinnerList.addAll(Arrays.asList(StringUtils.getStringArray(R.array.spinner_values2)));
        }
        adapter.setList(mSpinnerList);
        adapter.notifyDataSetChanged();
    }

    public void show(View anchor) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.N) {
            //只有24这个版本有问题，源码的问题
            this.showAsDropDown(anchor);
        } else {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            int y = location[1];
            this.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, y + anchor.getHeight());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position, boolean isOneSpinner);
    }
}
