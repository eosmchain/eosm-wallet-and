package com.token.mangowallet.ui.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UtilsTransActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CategoryBean;
import com.token.mangowallet.bean.CountryBean;
import com.token.mangowallet.bean.GoodsImgeModel;
import com.token.mangowallet.bean.MarketBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.StoreIncomeBean;
import com.token.mangowallet.bean.entity.ImgSection;
import com.token.mangowallet.bean.entity.UploadImgBean;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.SelImgAdapter;
import com.token.mangowallet.ui.adapter.decoration.GridSectionAverageGapItemDecoration;
import com.token.mangowallet.utils.AppFilePath;
import com.token.mangowallet.utils.LocationUtils;
import com.token.mangowallet.utils.PhotoUtils;
import com.token.mangowallet.utils.RSAUtils;
import com.token.mangowallet.view.RequiredTextView;
import com.token.mangowallet.view.ViewUtils;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.token.mangowallet.bean.entity.ImgSection.GOODS_COVER;
import static com.token.mangowallet.bean.entity.ImgSection.GOODS_SLIDESHOW;
import static com.token.mangowallet.utils.Constants.EXTRA_STORE;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.percent;

public class EditStoreFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.storeNameTv)
    RequiredTextView storeNameTv;
    @BindView(R.id.storeNameEt)
    AppCompatEditText storeNameEt;
    @BindView(R.id.classifyTv)
    RequiredTextView classifyTv;
    @BindView(R.id.classifyChooseTv)
    AppCompatTextView classifyChooseTv;
    @BindView(R.id.actualChargeSellerTv)
    RequiredTextView actualChargeSellerTv;
    @BindView(R.id.actualChargeSellerEt)
    AppCompatEditText actualChargeSellerEt;
    @BindView(R.id.mortgageAwayTv)
    RequiredTextView mortgageAwayTv;
    @BindView(R.id.mortgageAwayEt)
    AppCompatEditText mortgageAwayEt;
    @BindView(R.id.businessTimeTv)
    RequiredTextView businessTimeTv;
    @BindView(R.id.businessTimeEt)
    AppCompatEditText businessTimeEt;
    @BindView(R.id.salesAreaTv)
    RequiredTextView salesAreaTv;
    @BindView(R.id.areaTv)
    AppCompatTextView areaTv;
    @BindView(R.id.addressDetailTv)
    RequiredTextView addressDetailTv;
    @BindView(R.id.addressDetailEt)
    AppCompatEditText addressDetailEt;
    @BindView(R.id.importNumTv)
    AppCompatTextView importNumTv;
    @BindView(R.id.addressDetailLayout)
    FrameLayout addressDetailLayout;
    @BindView(R.id.addPicRecyclerView)
    RecyclerView addPicRecyclerView;
    @BindView(R.id.picPromptTv)
    AppCompatTextView picPromptTv;
    @BindView(R.id.arrowsIv)
    AppCompatImageView arrowsIv;
    @BindView(R.id.submitBtn)
    QMUIRoundButton submitBtn;


    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private String walletAddress;
    private QMUIBottomSheet bottomSheet;
    private PhotoUtils photoUtils;
    private SelImgAdapter selImgAdapter;
    private List<ImgSection> imgSectionList = new ArrayList<>();
    private ImgSection addImgSection1, addImgSection2;
    private List<GoodsImgeModel> goodsImgeModelList = new ArrayList<>();
    private int type;
    private boolean isBuyerGain = false;
    private List<String> buyerGainList;
    private StoreIncomeBean.DataBean.ShopBean storeData;
    private List<CountryBean.DataBean> countryList;
    private CountryBean.DataBean countryData;
    private List<CategoryBean.DataBean> categoryBeanList;
    private int categoryId = -1;
    private int countryId = -1;
    private boolean isToSetting = false;
    private String mLatitude;
    private String mLongitude;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_store, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        storeData = bundle.getParcelable(EXTRA_STORE);
        walletAddress = mangoWallet.getWalletAddress();
        photoUtils = new PhotoUtils();
        getCategory();
        defaultModel();
        getCountry();
        startLocation();
    }

    @Override
    protected void initView() {
        topBar.setTitle(ObjectUtils.isEmpty(storeData) ? R.string.str_add_store : R.string.str_edit_store);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        addImgSection1 = new ImgSection(false, true, GOODS_COVER, null);
        addImgSection2 = new ImgSection(false, true, GOODS_SLIDESHOW, null);
        imgSectionList.add(new ImgSection(true, false, GOODS_COVER, getString(R.string.str_store_cover)));
        imgSectionList.add(addImgSection1);
        imgSectionList.add(new ImgSection(true, false, GOODS_SLIDESHOW, getString(R.string.str_store_slideshow)));
        imgSectionList.add(addImgSection2);
        selImgAdapter = new SelImgAdapter(imgSectionList);
        addPicRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        addPicRecyclerView.addItemDecoration(new GridSectionAverageGapItemDecoration(15, 15, 15, 15));
        addPicRecyclerView.setAdapter(selImgAdapter);
        updataView();
    }

    @Override
    protected void initAction() {
        selImgAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                ImgSection imgSection = imgSectionList.get(position);
                if (!imgSection.isHeader()) {
                    if (imgSection.isAdd) {
                        if (imgSection.type == GOODS_COVER) {
                            photoUtils.albumSelect(getActivity(), true);
                        } else {
                            int count = 9;
                            if (imgSectionList.get(3).isAdd) {
                                count = 9;
                            } else {
                                if (imgSectionList.get(imgSectionList.size() - 1).isAdd) {
                                    count = 12 - imgSectionList.size() + 1;
                                } else {
                                    count = 0;
                                }
                            }
                            photoUtils.albumMultipleSelect(getActivity(), count, true);
                        }
                    } else {
                        if (imgSection.type == GOODS_COVER) {
                            imgSection.isAdd = true;
                            imgSection.object = null;
                            selImgAdapter.setData(1, imgSection);
                        } else {
                            if (imgSectionList.size() < 12) {
                                if (!imgSectionList.get(imgSectionList.size() - 1).isAdd) {
                                    imgSectionList.add(imgSectionList.size(), addImgSection2);
                                }
                            }
                            imgSectionList.remove(imgSection);
                            selImgAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        photoUtils.setPhotoPathListener(new PhotoUtils.PhotoPathListener() {
            @Override
            public void getPathSuccess(Object path) {
                if (path instanceof ArrayList) {
                    ArrayList<AlbumFile> result = (ArrayList<AlbumFile>) path;
                    if (ObjectUtils.isNotEmpty(result)) {
                        for (int i = 0; i < result.size(); i++) {
                            String imgPath = result.get(i).getPath();
                            ImgSection imgSection = new ImgSection(false, false, GOODS_SLIDESHOW, imgPath);
                            imgSectionList.add(imgSection);
                        }
                        imgSectionList.remove(addImgSection2);
                        if (imgSectionList.size() < 12) {
                            imgSectionList.add(imgSectionList.size(), addImgSection2);
                        }
                        selImgAdapter.notifyDataSetChanged();
                    }
                } else {
                    ImgSection imgSection = new ImgSection(false, false, GOODS_COVER, path);
                    selImgAdapter.setData(1, imgSection);
                }
            }
        });
        MultiEditTextListening(actualChargeSellerEt);
        MultiEditTextListening(mortgageAwayEt);
        MultiEditTextListening(addressDetailEt);
    }

    /**
     * 定位权限
     */
    public void startLocation() {
        PermissionUtils.permission(PermissionConstants.LOCATION)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(UtilsTransActivity activity, ShouldRequest shouldRequest) {
//                        DialogHelper.showRationaleDialog(getActivity(), shouldRequest);
                        // 转到手机设置界面，用户设置GPS
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                            }
                        }).launch(intent); // 设置完成后返回到原来的界面
                    }
                }).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                LocationUtils.getInstance(getActivity()).setOnLocationListener(locationListener);
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
            }
        }).request();
        LocationUtils.getInstance(getActivity()).setOnLocationListener(locationListener);
    }

    LocationUtils.OnLocationListener locationListener = new LocationUtils.OnLocationListener() {
        @Override
        public void OnIsGPSOpen(boolean isGPSOpen) {
            if (!isToSetting) {
                AppUtils.launchAppDetailsSettings();
                ToastUtils.showLong(R.string.str_unable_location_permission);
                isToSetting = true;
            }
        }

        @Override
        public void OnLocation(Location location) {
            if (location != null) {
                mLatitude = String.valueOf(location.getLatitude());
                mLongitude = String.valueOf(location.getLongitude());
                LocationUtils.getInstance(getActivity()).removeLocationUpdatesListener();
            }
        }
    };

    protected void MultiEditTextListening(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (editText.getId()) {
                    case R.id.mortgageAwayEt:
                    case R.id.actualChargeSellerEt:
                        BigDecimal b1 = new BigDecimal(ObjectUtils.isEmpty(actualChargeSellerEt.getText()) ?
                                "0" : actualChargeSellerEt.getText().toString());
                        BigDecimal b2 = new BigDecimal(ObjectUtils.isEmpty(mortgageAwayEt.getText()) ?
                                "0" : mortgageAwayEt.getText().toString());
                        String text = percent.subtract((b1.add(b2))).toPlainString();
                        break;
                    case R.id.addressDetailEt:
                        importNumTv.setText(s.length() + "/150");
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (editText.getId()) {
                    case R.id.actualChargeSellerEt:

                        break;
                    case R.id.mortgageAwayEt:

                        break;
                    case R.id.addressDetailEt:

                        break;
                }
            }
        });
    }

    private void updataView() {
        if (storeData != null) {
            storeNameEt.setText(ObjectUtils.isEmpty(storeData.getName()) ? "" : storeData.getName());
            actualChargeSellerEt.setText(ObjectUtils.isEmpty(storeData.getStorePro()) ? "0" : storeData.getStorePro().multiply(percent).toPlainString());
            businessTimeEt.setText(ObjectUtils.isEmpty(storeData.getBankTime()) ? "" : storeData.getBankTime());
            addressDetailEt.setText(ObjectUtils.isEmpty(storeData.getStoreAddress()) ? "" : storeData.getStoreAddress());//stock

            List<String> imageUrlList = storeData.getImgs();
            if (ObjectUtils.isNotEmpty(imageUrlList)) {
                if (imageUrlList.size() > 0) {
                    ImgSection imgSection = new ImgSection(false, false, GOODS_COVER, imageUrlList.get(0));
                    imgSectionList.set(1, imgSection);
                }
            }
            List<String> sliderImagesList = storeData.getDetailImgs();
            if (ObjectUtils.isNotEmpty(sliderImagesList)) {
                if (sliderImagesList.size() > 0) {
                    imgSectionList.remove(addImgSection2);
                    for (int i = 0; i < sliderImagesList.size(); i++) {
                        ImgSection imgSection = new ImgSection(false, false, GOODS_SLIDESHOW, sliderImagesList.get(i));
                        imgSectionList.add(3 + i, imgSection);
                    }
                }
            }
            if (imgSectionList.size() < 12) {
                imgSectionList.add(imgSectionList.size(), addImgSection2);
            }
            if (storeData.getStatus() == 3) {
                Snackbar.make(submitBtn, storeData.getFailMsg(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
        storeNameTv.setText(getString(R.string.str_store_name) + ":");
        classifyTv.setText(getString(R.string.str_classify) + ":");
        actualChargeSellerTv.setText(getString(R.string.str_charge_proportion) + ":");
        businessTimeTv.setText(getString(R.string.str_do_business) + ":");
        salesAreaTv.setText(getString(R.string.str_marketing_area) + ":");
    }

    @OnClick({R.id.submitBtn, R.id.classifyChooseTv, R.id.mortgageAwayEt, R.id.areaTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submitBtn:
                if (isVerify()) {
                    addStore();
                }
                break;
            case R.id.classifyChooseTv:
                if (ObjectUtils.isEmpty(categoryBeanList)) {
                    getCategory();
                } else {
                    showSimpleBottomSheetList(0);
                }
                break;
            case R.id.mortgageAwayEt:
                if (isBuyerGain) {
                    if (ObjectUtils.isEmpty(buyerGainList)) {
                        getCategory();
                    } else {
                        showSimpleBottomSheetList(1);
                    }
                }
                break;
            case R.id.areaTv:
                if (ObjectUtils.isEmpty(countryList)) {
                    getCountry();
                } else {
                    showSimpleBottomSheetList(2);
                }
                break;
        }
    }

    private void defaultModel() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("id", "1");//1 商城 2 本地化店铺
        String json = GsonUtils.toJson(params);
        try {
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().defaultModel(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::defaultModel, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分类
     */
    private void getCategory() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("type", "2");//1 首页显示用 2 店铺上传用
        String json = GsonUtils.toJson(params);
        try {
            String content = RSAUtils.encrypt(json);
            NetWorkManager.getRequest().getCategory(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::categorySuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void categorySuccess(JsonObject jsonData) {
        dismissTipDialog();
        if (jsonData != null) {
            LogUtils.dTag(LOG_TAG, "jsonData = " + GsonUtils.toJson(jsonData));
            CategoryBean categoryBean = GsonUtils.fromJson(GsonUtils.toJson(jsonData), CategoryBean.class);
            if (categoryBean.getCode() == 0) {
                categoryBeanList = categoryBean.getData();
                if (ObjectUtils.isNotEmpty(categoryBeanList)) {
                    if (storeData != null) {
                        if (ObjectUtils.isNotEmpty(storeData.getCategoryId())) {
                            categoryId = storeData.getCategoryId();
                            for (CategoryBean.DataBean dataBean : categoryBeanList) {
                                if (categoryId == dataBean.getId()) {
                                    classifyChooseTv.setText(dataBean.getName());
                                }
                            }
                        }
                    }
                }
            } else {
                ToastUtils.showLong(categoryBean.getMsg());
            }
        }
    }

    private void addStore() {
        showTipDialog(getString(R.string.str_loading));
        for (ImgSection imgSection : imgSectionList) {
            if (!imgSection.isHeader && !imgSection.isAdd) {
                if (imgSection.type == GOODS_COVER) {
                    if (ObjectUtils.isNotEmpty(imgSection.object)) {
                        type = imgSection.type;
                        String filePath = (String) imgSection.object;
                        if (filePath.contains("http")) {
                            goodsImgeModelList.add(new GoodsImgeModel(type, filePath));
                            type = GOODS_SLIDESHOW;
                            uploadImge(imgSectionList);
                        } else {
                            uploadImge(filePath);
                        }

                    }
                }
            }
        }
    }

    private void addPro() {
        try {
            String image = "";
            String sliderImage = "";
            List list = new ArrayList();
            for (GoodsImgeModel goodsImgeModel : goodsImgeModelList) {
                if (goodsImgeModel.type == GOODS_COVER) {
                    image = goodsImgeModel.filePath;
                } else {
                    list.add(goodsImgeModel.filePath);
                }
            }
            sliderImage = GsonUtils.toJson(list);//percent
            list.clear();
            list.add(image);
            image = GsonUtils.toJson(list);
            String storeProStr = actualChargeSellerEt.getText().toString();
            String buyerProStr = mortgageAwayEt.getText().toString();

            BigDecimal bdStorePro = new BigDecimal(ObjectUtils.isEmpty(storeProStr) ? "0" : storeProStr);
            BigDecimal bdBuyerPro = new BigDecimal(ObjectUtils.isEmpty(buyerProStr) ? "0" : buyerProStr);

            String storePro = bdStorePro.divide(percent).toPlainString();
            String buyerPro = bdBuyerPro.divide(percent).toPlainString();
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);
            params.put("name", storeNameEt.getText().toString());
            params.put("categoryId", String.valueOf(categoryId));
            params.put("storePro", storePro);
            params.put("rewardPro", "");
            params.put("buyerPro", buyerPro);
            params.put("bankTime", businessTimeEt.getText().toString());
            params.put("country", String.valueOf(countryId));
            params.put("storeAddress", addressDetailEt.getText().toString());
            params.put("homeImg", image);
            params.put("detailImg", sliderImage);
            params.put("longitude", mLongitude);
            params.put("latitude", mLatitude);
            Observable<JsonObject> request;
            String json;
            String content;
            if (storeData != null) {
                if (ObjectUtils.isNotEmpty(storeData.getUserId())) {
                    params.put("id", String.valueOf(storeData.getLifeID()));
                    json = GsonUtils.toJson(params);
                    content = RSAUtils.encrypt(json);
                    request = NetWorkManager.getRequest().editStore(content);
                } else {
                    json = GsonUtils.toJson(params);
                    content = RSAUtils.encrypt(json);
                    request = NetWorkManager.getRequest().addStore(content);
                }
            } else {
                json = GsonUtils.toJson(params);
                content = RSAUtils.encrypt(json);
                request = NetWorkManager.getRequest().addStore(content);
            }
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::uploadGoodsSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
        goodsImgeModelList.clear();
    }

    private void getCountry() {
        showTipDialog(getString(R.string.str_loading));
        try {
            NetWorkManager.getRequest().getCountry()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::countrySuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void countrySuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            CountryBean countryBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), CountryBean.class);
            if (countryBean.getCode() == 0) {
                countryList = countryBean.getData();
                if (storeData != null) {
                    if (ObjectUtils.isNotEmpty(storeData.getCountry())) {
                        countryId = storeData.getCountry();
                        for (CountryBean.DataBean dataBean : countryList) {
                            if (countryId == dataBean.getId()) {
                                areaTv.setText(dataBean.getCountyName());
                            }
                        }
                    }
                }
            }
        }
    }

    private void uploadImge(Object imagObj) {
        try {
            boolean isNull = true;
            MultipartBody.Builder mMultipartBodyBuilder = new MultipartBody.Builder();
            if (imagObj instanceof List) {
                List<ImgSection> imgList = (List<ImgSection>) imagObj;
                for (int i = 0; i < imgList.size(); i++) {
                    ImgSection imgSection = imgList.get(i);
                    if (!imgSection.isHeader && !imgSection.isAdd) {
                        if (imgSection.type == GOODS_SLIDESHOW) {
                            if (ObjectUtils.isNotEmpty(imgSection.object)) {
                                type = imgSection.type;
                                String filePath = (String) imgSection.object;
                                if (filePath.contains("http")) {
                                    goodsImgeModelList.add(new GoodsImgeModel(type, filePath));
                                } else {
                                    File file = new File(filePath);
                                    if (!file.exists()) {
                                        LogUtils.d("找不到该文件");
                                        continue;
                                    }
                                    mMultipartBodyBuilder.addFormDataPart("file", URLEncoder.encode(file.getName(), "UTF-8")
                                            , RequestBody.create(AppFilePath.compressImage(filePath), MediaType.parse("multipart/form-data")));
                                    isNull = false;
                                }
                            }
                        }
                    }
                }
            } else {
                String filePath = (String) imagObj;
                File file = new File(filePath);
                if (!file.exists()) {
                    LogUtils.d("找不到该文件");
                    return;
                }
                mMultipartBodyBuilder.addFormDataPart("file", URLEncoder.encode(file.getName(), "UTF-8")
                        , RequestBody.create(AppFilePath.compressImage(filePath), MediaType.parse("multipart/form-data")));
                isNull = false;
            }
            if (isNull) {
                if (ObjectUtils.isNotEmpty(goodsImgeModelList)) {
                    if (goodsImgeModelList.size() > 0) {
                        addPro();
                    }
                }
                return;
            }
            MultipartBody multipartBody = mMultipartBodyBuilder.build();
            if (multipartBody.size() <= 0) {
                return;
            }
            NetWorkManager.getRequest().upload(multipartBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::uploadImgeSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void defaultModel(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MarketBean marketBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MarketBean.class);
            if (marketBean.getCode() == 0) {
                MarketBean.DataBean dataBean = marketBean.getData();
                BigDecimal bdBuyerGainPro;
                BigDecimal bdSellerGainPro;
                BigDecimal bdOrderGainPro;
                if (dataBean.getBuyerGainEditFlag() == 0) {//0 固定比例不可调整
                    ViewUtils.setEditableEditText(actualChargeSellerEt, true);
                    ViewUtils.setEditableEditText(mortgageAwayEt, true);
                    mortgageAwayEt.setText(ObjectUtils.isEmpty(dataBean.getBuyerGainPro()) ? "0" : dataBean.getBuyerGainPro());
                    isBuyerGain = false;
                    arrowsIv.setVisibility(View.GONE);
                    mortgageAwayEt.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.album_dp_15), 0);
                } else if (dataBean.getBuyerGainEditFlag() == 1) {//1 可调整固定比例
                    ViewUtils.setEditableEditText(actualChargeSellerEt, true);
                    ViewUtils.setEditableEditText(mortgageAwayEt, false);
                    mortgageAwayEt.setText(ObjectUtils.isEmpty(dataBean.getBuyerGainPro()) ? "50" : dataBean.getBuyerGainPro());
                    arrowsIv.setVisibility(View.GONE);
                    mortgageAwayEt.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.album_dp_15), 0);
                    isBuyerGain = false;
                } else if (dataBean.getBuyerGainEditFlag() == 2) {//2 使用比例选项
                    ViewUtils.setEditableEditText(actualChargeSellerEt, true);
                    ViewUtils.setEditableEditText(mortgageAwayEt, false);
                    isBuyerGain = true;
                    buyerGainList = dataBean.getBuyerGainPros();
                    arrowsIv.setVisibility(View.VISIBLE);
                    mortgageAwayEt.setPadding(0, 0, 0, 0);
                    mortgageAwayEt.setEnabled(true);
                }
                actualChargeSellerEt.setHint(String.format(getString(R.string.str_please_import_interval), "0", "100"));
                actualChargeSellerEt.setText(ObjectUtils.isEmpty(dataBean.getSellerGainPro()) ? "0" : dataBean.getSellerGainPro());
            } else {
                ToastUtils.showLong(marketBean.getMsg());
            }
        }
    }

    private void uploadGoodsSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                popBackStack();
            }
            ToastUtils.showLong(msgCodeBean.getMsg());
        }
    }

    private void uploadImgeSuccess(JsonObject jsonObject) {
        LogUtils.dTag(LOG_TAG, "jsonObject = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            UploadImgBean uploadImgBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), UploadImgBean.class);
            if (uploadImgBean.getCode() == 0) {
                List<String> goodsImgeList = uploadImgBean.getData();
                for (int i = 0; i < goodsImgeList.size(); i++) {
                    String goodsImge = goodsImgeList.get(i);
                    goodsImgeModelList.add(new GoodsImgeModel(type, goodsImge));
                }
            }
        }
        if (type == GOODS_SLIDESHOW) {
            addPro();
        } else {
            uploadImge(imgSectionList);
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.eTag(LOG_TAG, "e = " + e.toString() + " ===== " + e.getMessage());
    }

    // ================================ 生成不同类型的BottomSheet
    private void showSimpleBottomSheetList(int bottomSheetType) {
        String title = "";
        if (bottomSheetType == 0) {
            title = getString(R.string.str_please_choose) + getString(R.string.str_classify);
        } else if (bottomSheetType == 1) {
            title = getString(R.string.str_please_choose) + getString(R.string.str_amount_proportion);
        } else if (bottomSheetType == 2) {
            title = getString(R.string.str_please_choose) + getString(R.string.str_united_states);
        }
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
        builder.setGravityCenter(true)
                .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                .setTitle(title)
                .setAddCancelBtn(true)
                .setAllowDrag(false)
                .setNeedRightMark(false)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        if (bottomSheetType == 0) {
                            CategoryBean.DataBean dataBean = categoryBeanList.get(position);
                            categoryId = dataBean.getId();
                            classifyChooseTv.setText(dataBean.getName());
                        } else if (bottomSheetType == 1) {
                            String buyerGain = buyerGainList.get(position);
                            mortgageAwayEt.setText(buyerGain);
                        } else if (bottomSheetType == 2) {
                            countryData = countryList.get(position);
                            String mCountyName = countryData.getCountyName();
                            countryId = countryData.getId();
                            areaTv.setText(mCountyName);
                        }
                    }
                });
        if (bottomSheetType == 0) {
            if (ObjectUtils.isNotEmpty(categoryBeanList)) {
                for (int i = 0; i < categoryBeanList.size(); i++) {
                    CategoryBean.DataBean dataBean = categoryBeanList.get(i);
                    if (dataBean != null) {
                        builder.addItem(dataBean.getName());
                    }
                }
            }
        } else if (bottomSheetType == 1) {
            if (buyerGainList != null) {
                for (int i = 0; i < buyerGainList.size(); i++) {
                    String buyerGain = buyerGainList.get(i);
                    if (ObjectUtils.isNotEmpty(buyerGain)) {
                        builder.addItem(buyerGain);
                    }
                }
            }
        } else if (bottomSheetType == 2) {
            if (countryList != null) {
                for (int i = 0; i < countryList.size(); i++) {
                    CountryBean.DataBean dataBean = countryList.get(i);
                    if (dataBean != null) {
                        builder.addItem(dataBean.getCountyName());
                    }
                }
            }
        }

        bottomSheet = builder.build();
        if (!bottomSheet.isShowing()) {
            bottomSheet.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissTipDialog();

    }

    private boolean isVerify() {
        String str1 = getString(R.string.str_please_import);
        String msg = "";
        boolean isVer = true;
        if (ObjectUtils.isEmpty(storeNameEt.getText())) {
            isVer = false;
            msg = str1 + getString(R.string.str_store_name);
        } else if (ObjectUtils.isEmpty(categoryId) || categoryId == -1) {
            isVer = false;
            msg = getString(R.string.str_please_choose) + getString(R.string.str_classify);
        } else if (ObjectUtils.isEmpty(actualChargeSellerEt.getText())) {
            isVer = false;
            msg = str1 + getString(R.string.str_charge_proportion);
        } else if (ObjectUtils.isEmpty(mortgageAwayEt.getText())) {
            isVer = false;
            msg = getString(R.string.str_please_choose) + getString(R.string.str_mortgage_away).replace(":", "");
        } else if (ObjectUtils.isEmpty(businessTimeEt.getText())) {
            isVer = false;
            msg = str1 + getString(R.string.str_do_business);
        } else if (ObjectUtils.isEmpty(countryId) || countryId == -1) {
            isVer = false;
            msg = getString(R.string.str_please_choose) + getString(R.string.str_marketing_area);
        } else if (ObjectUtils.isEmpty(addressDetailEt.getText())) {
            isVer = false;
            msg = getString(R.string.str_please_enter_address_detail);
        } else if (imgSectionList.get(1).isAdd) {
            isVer = false;
            msg = getString(R.string.str_please_choose) + getString(R.string.str_goods_cover);
        } else if (imgSectionList.get(3).isAdd) {
            isVer = false;
            msg = getString(R.string.str_please_choose) + getString(R.string.str_store_slideshow);
        } else {
            String s1 = actualChargeSellerEt.getText().toString();
            String s3 = mortgageAwayEt.getText().toString();
            BigDecimal bigd1 = new BigDecimal(s1);
            BigDecimal bigd3 = new BigDecimal(s3);
            BigDecimal bigd = bigd1.add(bigd3);
            if (percent.compareTo(bigd) != 0) {//-1表示小于，0是等于，1是大于。
                isVer = false;
                msg = getString(R.string.str_market_prompt);
            }
        }
        if (ObjectUtils.isNotEmpty(msg))
            ToastUtils.showLong(msg);
        return isVer;
    }
}
