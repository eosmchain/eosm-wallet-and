package com.token.mangowallet.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LanguageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.utils.Constants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LanguageFragment extends BaseFragment {


    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.groupListView)
    QMUIGroupListView groupListView;

    private Unbinder unbinder;
    private AppCompatImageView imageView0;//随系统
    private AppCompatImageView imageView1;//英语
    private AppCompatImageView imageView2;//日本语
    private AppCompatImageView imageView3;//韩语
    private AppCompatImageView imageView4;//简体中文
    private AppCompatImageView imageView5;//繁体中文

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

    }

    @Override
    public void initView() {
        topbar.setTitle(StringUtils.getString(R.string.str_language_settings));
        topbar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        initGroupListView();
    }

    @Override
    protected void initAction() {

    }

    private void initGroupListView() {
        imageView0 = getAccessoryImageView();
        imageView0.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.pitchon_icon));

        imageView1 = getAccessoryImageView();
        imageView1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.pitchon_icon));

        imageView2 = getAccessoryImageView();
        imageView2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.pitchon_icon));

        imageView3 = getAccessoryImageView();
        imageView3.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.pitchon_icon));

        imageView4 = getAccessoryImageView();
        imageView4.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.pitchon_icon));

        imageView5 = getAccessoryImageView();
        imageView5.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.pitchon_icon));

//        QMUICommonListItemView systemLanguageItem = groupListView.createItemView(StringUtils.getString(R.string.str_lang_system_language));
//        systemLanguageItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
//        systemLanguageItem.setOrientation(QMUICommonListItemView.VERTICAL);
//        systemLanguageItem.addAccessoryCustomView(imageView0);

        QMUICommonListItemView englishItem = groupListView.createItemView(StringUtils.getString(R.string.str_lang_english));
        englishItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        englishItem.setOrientation(QMUICommonListItemView.VERTICAL);
        englishItem.addAccessoryCustomView(imageView1);

        QMUICommonListItemView japaneseItem = groupListView.createItemView(StringUtils.getString(R.string.str_lang_japanese));
        japaneseItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        japaneseItem.setOrientation(QMUICommonListItemView.VERTICAL);
        japaneseItem.addAccessoryCustomView(imageView2);

        QMUICommonListItemView koreanItem = groupListView.createItemView(StringUtils.getString(R.string.str_lang_korean));
        koreanItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        koreanItem.setOrientation(QMUICommonListItemView.VERTICAL);
        koreanItem.addAccessoryCustomView(imageView3);

        QMUICommonListItemView simplifiedChineseItem = groupListView.createItemView(StringUtils.getString(R.string.str_lang_simplified_chinese));
        simplifiedChineseItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        simplifiedChineseItem.setOrientation(QMUICommonListItemView.VERTICAL);
        simplifiedChineseItem.addAccessoryCustomView(imageView4);

//        QMUICommonListItemView traditionalChineseItem = groupListView.createItemView(StringUtils.getString(R.string.str_lang_traditional_chinese));
//        traditionalChineseItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
//        traditionalChineseItem.setOrientation(QMUICommonListItemView.VERTICAL);
//        traditionalChineseItem.addAccessoryCustomView(imageView5);

        View.OnClickListener onClickListener =new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = Constants.lang_system_language;
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    if (ObjectUtils.equals(StringUtils.getString(R.string.str_lang_system_language), text)) {
                        type = Constants.lang_system_language;
                        LanguageUtils.applySystemLanguage(true);
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_lang_english), text)) {
                        type = Constants.lang_english;
                        LanguageUtils.applyLanguage(Locale.US,true);
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_lang_japanese), text)) {
                        type = Constants.lang_japanese;
                        LanguageUtils.applyLanguage(Locale.JAPAN,true);
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_lang_korean), text)) {
                        type = Constants.lang_korean;
                        LanguageUtils.applyLanguage(Locale.KOREA,true);
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_lang_simplified_chinese), text)) {
                        type = Constants.lang_simplified_chinese;
                        LanguageUtils.applyLanguage(Locale.SIMPLIFIED_CHINESE,true);
                    } else if (ObjectUtils.equals(StringUtils.getString(R.string.str_lang_traditional_chinese), text)) {
                        type = Constants.lang_traditional_chinese;
                        LanguageUtils.applyLanguage(Locale.TRADITIONAL_CHINESE,true);
                    }
                    SPUtils.getInstance(Constants.SP_WALLET).put(Constants.KEY_LANGUAGE_SETUP, type);
                    updatePitchView(type);
                }
            }
        };

        QMUIGroupListView.newSection(getActivity())
                .setLeftIconSize(QMUIDisplayHelper.dp2px(getActivity(), 12), ViewGroup.LayoutParams.WRAP_CONTENT)
//                .setTitle("")
//                .addItemView(systemLanguageItem, onClickListener)
                .addItemView(englishItem, onClickListener)
                .addItemView(japaneseItem, onClickListener)
                .addItemView(koreanItem, onClickListener)
                .addItemView(simplifiedChineseItem, onClickListener)
//                .addItemView(traditionalChineseItem, onClickListener)
                .addTo(groupListView);

//        groupListView.removeViewAt(0);
        int type = SPUtils.getInstance(Constants.SP_WALLET).getInt(Constants.KEY_LANGUAGE_SETUP, Constants.lang_system_language);
        updatePitchView(type);
    }

    private void updatePitchView(int type) {
        LogUtils.dTag(Constants.LOG_TAG, "updatePitchView type = " + type);
        imageView0.setVisibility(type == Constants.lang_system_language ? View.VISIBLE : View.GONE);
        imageView1.setVisibility(type == Constants.lang_english ? View.VISIBLE : View.GONE);
        imageView2.setVisibility(type == Constants.lang_japanese ? View.VISIBLE : View.GONE);
        imageView3.setVisibility(type == Constants.lang_korean ? View.VISIBLE : View.GONE);
        imageView4.setVisibility(type == Constants.lang_simplified_chinese ? View.VISIBLE : View.GONE);
        imageView5.setVisibility(type == Constants.lang_traditional_chinese ? View.VISIBLE : View.GONE);
    }

    private AppCompatImageView getAccessoryImageView() {
        AppCompatImageView resultImageView = new AppCompatImageView(getActivity());
        resultImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        resultImageView.setScaleType(ImageView.ScaleType.CENTER);
        QMUISkinValueBuilder builder = QMUISkinValueBuilder.acquire();
        builder.tintColor(com.qmuiteam.qmui.R.attr.qmui_skin_support_common_list_chevron_color);
        QMUISkinHelper.setSkinValue(resultImageView, builder);
        QMUISkinValueBuilder.release(builder);
        return resultImageView;
    }
}
