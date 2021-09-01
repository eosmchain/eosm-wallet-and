package com.token.mangowallet.view;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ClickUtils;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.view.basepopup.BasePopup;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PictureChoicePop extends BasePopup {

    public static final int REQUEST_CODE_TAKE_PHOTO = 1001;
    @BindView(R.id.photographTv)
    AppCompatTextView photographTv;
    @BindView(R.id.albumSelectTv)
    AppCompatTextView albumSelectTv;
    @BindView(R.id.functionLayout)
    QMUILinearLayout functionLayout;
    @BindView(R.id.cancelBtn)
    QMUIRoundButton cancelBtn;

    private BaseFragment fragment;
    private Unbinder unbinder;
    private OnPictureChoiceListener listener;
    private int mRadius;
    private Uri imageUri;

    private String photoPath = "";

    public PictureChoicePop(@NonNull BaseFragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
        mRadius = QMUIDisplayHelper.dp2px(fragment.getActivity(), 8);
        initImgUri();
    }

    @Override
    protected void initAttributes() {
        LayoutInflater inflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_picure_choose, null);
        setContentView(view);
        unbinder = ButterKnife.bind(this, view);

        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口点击外部消失
        setBackgroundDimEnable(false);
    }

    @Override
    protected void initViews(View view) {
        functionLayout.setRadius(mRadius);
        photographTv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (listener != null) {
                    listener.onCamera(v);
                }
            }
        });
        albumSelectTv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (listener != null) {
                    listener.onAlbumSelect(v);
                }
//                photoUtils.albumSelect(fragment.getActivity(), true);
            }
        });
        cancelBtn.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (listener != null) {
                    listener.onCancel(v);
                }
            }
        });

    }

    private void initImgUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
    }

    public void showPop(View playview) {
        this.showAtLocation(playview, Gravity.BOTTOM, 0, 0);
    }

    public void setOnPictureChoiceListener(OnPictureChoiceListener listener) {
        this.listener = listener;
    }

    public interface OnPictureChoiceListener {
        void onCamera(View v);

        void onAlbumSelect(View v);

        void onCancel(View v);
    }
}
