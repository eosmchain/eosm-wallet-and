package com.token.mangowallet.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.CountryBean;
import com.token.mangowallet.bean.GoodsImgeModel;
import com.token.mangowallet.bean.GoodsManaBean;
import com.token.mangowallet.bean.GoodsTypeBean;
import com.token.mangowallet.bean.MarketBean;
import com.token.mangowallet.bean.MsgCodeBean;
import com.token.mangowallet.bean.PayConfigBean;
import com.token.mangowallet.bean.entity.ImgSection;
import com.token.mangowallet.bean.entity.UploadImgBean;
import com.token.mangowallet.bus.AddGoodsSucEffect;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.SelImgAdapter;
import com.token.mangowallet.ui.adapter.decoration.GridSectionAverageGapItemDecoration;
import com.token.mangowallet.utils.AppFilePath;
import com.token.mangowallet.utils.PhotoUtils;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.view.BottomSheetPopupWindow;
import com.token.mangowallet.view.CashierInputFilter;
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
import static com.token.mangowallet.utils.Constants.EXTRA_GOODS;
import static com.token.mangowallet.utils.Constants.EXTRA_IS_EDIT;
import static com.token.mangowallet.utils.Constants.EXTRA_WALLET;
import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.percent;

public class AddGoodsFragment extends BaseFragment {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.goodsNameEt)
    AppCompatEditText goodsNameEt;
    @BindView(R.id.typeEt)
    AppCompatTextView typeEt;
    @BindView(R.id.specificationEt)
    AppCompatEditText specificationEt;
    @BindView(R.id.unitEt)
    AppCompatEditText unitEt;
    @BindView(R.id.inventoryEt)
    AppCompatEditText inventoryEt;
    @BindView(R.id.priceEt)
    AppCompatEditText priceEt;
    @BindView(R.id.postageEt)
    AppCompatEditText postageEt;
    @BindView(R.id.actualChargeSellerEt)
    AppCompatEditText actualChargeSellerEt;
    @BindView(R.id.bonusIncentivesEt)
    AppCompatEditText bonusIncentivesEt;
    @BindView(R.id.mortgageAwayEt)
    AppCompatEditText mortgageAwayEt;
    @BindView(R.id.introductionEt)
    AppCompatEditText introductionEt;
    @BindView(R.id.importNumTv)
    AppCompatTextView importNumTv;
    @BindView(R.id.picPromptTv)
    AppCompatTextView picPromptTv;
    @BindView(R.id.addPicRecyclerView)
    RecyclerView addPicRecyclerView;
    @BindView(R.id.arrowsIv)
    AppCompatImageView arrowsIv;
    @BindView(R.id.areaTv)
    AppCompatTextView areaTv;
    @BindView(R.id.submitBtn)
    QMUIRoundButton submitBtn;
    @BindView(R.id.payConfigValTv)
    AppCompatTextView payConfigValTv;


    private Unbinder unbinder;
    private MangoWallet mangoWallet;
    private List<GoodsTypeBean.DataBean> goodsTypeData;
    private String walletAddress;
    private QMUIBottomSheet bottomSheet;
    private GoodsTypeBean.DataBean dataBean;
    private PhotoUtils photoUtils;
    private SelImgAdapter selImgAdapter;
    private List<ImgSection> imgSectionList = new ArrayList<>();
    private ImgSection addImgSection1, addImgSection2;
    private List<GoodsImgeModel> goodsImgeModelList = new ArrayList<>();
    private int type;
    private boolean isBuyerGain = false;
    private List<String> buyerGainList;
    private int cateID;
    private boolean isEdit = false;
    private GoodsManaBean.DataBean goodsData;
    private int bottomSheetType = -1;
    private List<CountryBean.DataBean> countryList;
    private CountryBean.DataBean countryData;
    private List<PayConfigBean.DataBean> payConfigList;
    private BottomSheetPopupWindow mBottomSheetPopupWindow;
    private List<PayConfigBean.DataBean> mSelPositionList;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_goods, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mangoWallet = bundle.getParcelable(EXTRA_WALLET);
        isEdit = bundle.getBoolean(EXTRA_IS_EDIT);
        if (isEdit) {
            goodsData = bundle.getParcelable(EXTRA_GOODS);
        }
        walletAddress = mangoWallet.getWalletAddress();
        photoUtils = new PhotoUtils();
        defaultModel();
        getCountry();
        getPayConfig();
    }

    @Override
    protected void initView() {
        topBar.setTitle(isEdit ? R.string.str_update_merchandise : R.string.str_goods_upload);
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        addImgSection1 = new ImgSection(false, true, GOODS_COVER, null);
        addImgSection2 = new ImgSection(false, true, GOODS_SLIDESHOW, null);
        imgSectionList.add(new ImgSection(true, false, GOODS_COVER, getString(R.string.str_goods_cover)));
        imgSectionList.add(addImgSection1);
        imgSectionList.add(new ImgSection(true, false, GOODS_SLIDESHOW, getString(R.string.str_goods_slideshow)));
        imgSectionList.add(addImgSection2);
        selImgAdapter = new SelImgAdapter(imgSectionList);
        addPicRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        addPicRecyclerView.addItemDecoration(new GridSectionAverageGapItemDecoration(15, 15, 15, 15));
        addPicRecyclerView.setAdapter(selImgAdapter);
        submitBtn.setText(isEdit ? R.string.str_update : R.string.str_submit);
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
        actualChargeSellerEt.setFilters(new InputFilter[]{new CashierInputFilter(percent, 2)});
        bonusIncentivesEt.setFilters(new InputFilter[]{new CashierInputFilter(percent, 2)});
        mortgageAwayEt.setFilters(new InputFilter[]{new CashierInputFilter(percent, 2)});
        MultiEditTextListening(actualChargeSellerEt);
        MultiEditTextListening(bonusIncentivesEt);
        MultiEditTextListening(mortgageAwayEt);
        MultiEditTextListening(introductionEt);
    }

    protected void MultiEditTextListening(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (editText.getId()) {
                    case R.id.actualChargeSellerEt:
                        BigDecimal b1 = new BigDecimal(ObjectUtils.isEmpty(actualChargeSellerEt.getText()) ?
                                "0" : actualChargeSellerEt.getText().toString());
                        BigDecimal b2 = new BigDecimal(ObjectUtils.isEmpty(mortgageAwayEt.getText()) ?
                                "0" : mortgageAwayEt.getText().toString());
                        String text = percent.subtract((b1.add(b2))).toPlainString();
                        bonusIncentivesEt.setText(text);
                        break;
                    case R.id.bonusIncentivesEt:

                        break;
                    case R.id.mortgageAwayEt:

                        break;
                    case R.id.introductionEt:
                        importNumTv.setText(s.length() + "/150");
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (editText.getId()) {
                    case R.id.actualChargeSellerEt:

                        break;
                    case R.id.bonusIncentivesEt:

                        break;
                    case R.id.mortgageAwayEt:

                        break;
                    case R.id.introductionEt:

                        break;
                }
            }
        });
    }

    private void updataView() {
        if (isEdit) {
            if (goodsData != null) {
                goodsNameEt.setText(goodsData.getStoreName());
                specificationEt.setText(goodsData.getStoreType());
                unitEt.setText(goodsData.getStoreUnit());
                inventoryEt.setText(String.valueOf(goodsData.getStock()));//stock
                priceEt.setText(String.valueOf(goodsData.getPrice()));
                postageEt.setText(String.valueOf(goodsData.getPostage()));
                introductionEt.setText(goodsData.getStoreInfo());
                List<String> imageUrlList = goodsData.getImage_url();
                if (ObjectUtils.isNotEmpty(imageUrlList)) {
                    if (imageUrlList.size() > 0) {
                        ImgSection imgSection = new ImgSection(false, false, GOODS_COVER, imageUrlList.get(0));
                        imgSectionList.set(1, imgSection);
                    }
                }
                List<String> sliderImagesList = goodsData.getSliderImages();
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
            }
        }
    }

    @OnClick({R.id.submitBtn, R.id.typeEt, R.id.mortgageAwayEt, R.id.areaTv, R.id.arrowsIv2, R.id.payConfigValTv, R.id.arrowsIv3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submitBtn:
//                ImgSection imgSection = imgSectionList.get(1);
//                uploadImge((String) imgSection.object);
                if (isVerify()) {
                    addGoods();
                }
                break;
            case R.id.typeEt:
                allCategory();
                break;
            case R.id.mortgageAwayEt:
                if (isBuyerGain) {
                    bottomSheetType = 1;
                    showSimpleBottomSheetList();
                }
                break;
            case R.id.areaTv:
            case R.id.arrowsIv2:
                if (countryList == null) {
                    getCountry();
                } else {
                    bottomSheetType = 2;
                    showSimpleBottomSheetList();
                }
                break;
            case R.id.payConfigValTv:
            case R.id.arrowsIv3:
                if (ObjectUtils.isNotEmpty(payConfigList)) {
                    showPayBottomSheetList(view, true);
                }
                break;
        }
    }

    private void defaultModel() {
        showTipDialog(getString(R.string.str_loading));
        Map params = MapUtils.newHashMap();
        params.put("id", "2");//1 首页显示用 2 店铺上传用
        String json = GsonUtils.toJson(params);
        try {
            String content = NRSAUtils.encrypt(json);
            NetWorkManager.getRequest().defaultModel(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::defaultModelSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPayConfig() {
        showTipDialog(getString(R.string.str_loading));
        try {

            NetWorkManager.getRequest().getPayConfig()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::payConfigSuccess, this::onError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addGoods() {
        showTipDialog(getString(R.string.str_loading));
        for (ImgSection imgSection : imgSectionList) {
            if (!imgSection.isHeader && !imgSection.isAdd) {
                if (imgSection.type == GOODS_COVER) {
                    if (ObjectUtils.isNotEmpty(imgSection.object)) {
                        type = imgSection.type;//goodsImgeModelList
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
        try {//upPro
            boolean isPostage = true;
            String postage = postageEt.getText().toString();
            if (ObjectUtils.isNotEmpty(postage)) {
                if (BigDecimal.ZERO.compareTo(new BigDecimal(postage)) > 0) {//-1表示小于，0是等于，1是大于。
                    isPostage = false;
                }
            }
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
            String giveProStr = actualChargeSellerEt.getText().toString();
            String bonusProStr = bonusIncentivesEt.getText().toString();
            String buyerProStr = mortgageAwayEt.getText().toString();

            BigDecimal bdGivePro = new BigDecimal(ObjectUtils.isEmpty(giveProStr) ? "0" : giveProStr);
            BigDecimal bdBonusPro = new BigDecimal(ObjectUtils.isEmpty(bonusProStr) ? "0" : bonusProStr);
            BigDecimal bdBuyerPro = new BigDecimal(ObjectUtils.isEmpty(buyerProStr) ? "0" : buyerProStr);

            String givePro = bdGivePro.divide(percent).toPlainString();
            String bonusPro = bdBonusPro.divide(percent).toPlainString();
            String buyerPro = bdBuyerPro.divide(percent).toPlainString();

            String payIds = "";
            for (int i = 0; i < mSelPositionList.size(); i++) {
                PayConfigBean.DataBean dataBean = mSelPositionList.get(i);
                payIds += String.valueOf(dataBean.getId()) + ",";
            }
            Map params = MapUtils.newHashMap();
            params.put("address", walletAddress);
            params.put("image", image);/////
            params.put("sliderImage", sliderImage);
            params.put("storeName", goodsNameEt.getText().toString());
            params.put("storeUnit", unitEt.getText().toString());
            params.put("storeInfo", introductionEt.getText().toString());
            params.put("storeType", specificationEt.getText().toString());
            params.put("cateId", String.valueOf(cateID));
            params.put("isNew", !isEdit);
            params.put("payIds", payIds);
            params.put("isPostage", isPostage);
            params.put("postage", postage);
            params.put("price", priceEt.getText().toString());
            params.put("stock", inventoryEt.getText().toString());
            params.put("givePro", givePro);
            params.put("bonusPro", bonusPro);
            params.put("buyerPro", buyerPro);
            params.put("countryNum", String.valueOf(countryData.getId()));
            params.put("countryName", countryData.getCountyName());
            String json;
            String content;
            Observable<JsonObject> request;
            if (isEdit) {
                params.put("isShow", goodsData.isIsShow());
                params.put("id", String.valueOf(goodsData.getProID()));
                json = GsonUtils.toJson(params);
                content = NRSAUtils.encrypt(json);
                request = NetWorkManager.getRequest().upPro(content);
            } else {
                json = GsonUtils.toJson(params);
                content = NRSAUtils.encrypt(json);
                request = NetWorkManager.getRequest().addPro(content);
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
                                            , RequestBody.create(AppFilePath.compressImage(filePath), MediaType.parse("image/*")));
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
                        , RequestBody.create(AppFilePath.compressImage(filePath), MediaType.parse("image/*")));
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

    private void allCategory() {
        if (goodsTypeData == null) {
            showTipDialog(getString(R.string.str_loading));
            try {
                NetWorkManager.getRequest().allCategory()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::allCategorySuccess, this::onError);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            bottomSheetType = 0;
            showSimpleBottomSheetList();
        }
    }

    private void defaultModelSuccess(JsonObject jsonObject) {
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
                    ViewUtils.setEditableEditText(bonusIncentivesEt, true);
                    ViewUtils.setEditableEditText(mortgageAwayEt, true);
                    mortgageAwayEt.setText(ObjectUtils.isEmpty(dataBean.getBuyerGainPro()) ? "0" : dataBean.getBuyerGainPro());
                    arrowsIv.setVisibility(View.GONE);
                    bonusIncentivesEt.setHint(getString(R.string.str_please_import));
                } else if (dataBean.getBuyerGainEditFlag() == 1) {//1 可调整固定比例
                    ViewUtils.setEditableEditText(actualChargeSellerEt, true);
                    ViewUtils.setEditableEditText(bonusIncentivesEt, true);
                    ViewUtils.setEditableEditText(mortgageAwayEt, false);
                    mortgageAwayEt.setText(ObjectUtils.isEmpty(dataBean.getBuyerGainPro()) ? "50" : dataBean.getBuyerGainPro());
                    arrowsIv.setVisibility(View.GONE);
                    bonusIncentivesEt.setHint(getString(R.string.str_please_import));
                } else if (dataBean.getBuyerGainEditFlag() == 2) {//2 使用比例选项
                    ViewUtils.setEditableEditText(actualChargeSellerEt, true);
                    ViewUtils.setEditableEditText(bonusIncentivesEt, true);
                    ViewUtils.setEditableEditText(mortgageAwayEt, false);
                    isBuyerGain = true;
                    buyerGainList = dataBean.getBuyerGainPros();
                    arrowsIv.setVisibility(View.VISIBLE);
                    mortgageAwayEt.setEnabled(true);
                    bonusIncentivesEt.setHint(getString(R.string.str_please_choose));
                }
                actualChargeSellerEt.setHint(String.format(getString(R.string.str_please_import_interval), "0", "100"));
                bonusIncentivesEt.setHint(String.format(getString(R.string.str_please_import_interval), "0", "100"));
                actualChargeSellerEt.setText(ObjectUtils.isEmpty(dataBean.getSellerGainPro()) ? "0" : dataBean.getSellerGainPro());
                bonusIncentivesEt.setText(ObjectUtils.isEmpty(dataBean.getOrderGainPro()) ? "0" : dataBean.getOrderGainPro());
            } else {
                ToastUtils.showLong(marketBean.getMsg());
            }
        }
    }

    private void payConfigSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            PayConfigBean payConfigBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), PayConfigBean.class);
            if (payConfigBean.getCode() == 0) {
                payConfigList = payConfigBean.getData();

            }
        }
    }

    private void uploadGoodsSuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            MsgCodeBean msgCodeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), MsgCodeBean.class);
            if (msgCodeBean.getCode() == 0) {
                notifyEffect(new AddGoodsSucEffect(3));
                popBackStack();
            }
            ToastUtils.showLong(msgCodeBean.getMsg());
        }
    }

    private void uploadImgeSuccess(JsonObject jsonObject) {
        LogUtils.dTag(LOG_TAG, "type = " + type + " jsonObject = " + GsonUtils.toJson(jsonObject));
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

    private void allCategorySuccess(JsonObject jsonObject) {
        dismissTipDialog();
        if (jsonObject != null) {
            GoodsTypeBean goodsTypeBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), GoodsTypeBean.class);
            if (goodsTypeBean.getCode() == 0) {
                goodsTypeData = goodsTypeBean.getData();
                bottomSheetType = 0;
                showSimpleBottomSheetList();
            } else {
                ToastUtils.showLong(goodsTypeBean.getMsg());
            }
        }
        LogUtils.dTag(LOG_TAG, "data = " + GsonUtils.toJson(jsonObject));
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.dTag(LOG_TAG, "e = " + e.getMessage() + " ======= " + e.toString());
    }

    // ================================ 生成不同类型的BottomSheet
    private void showSimpleBottomSheetList() {
        String title = "";
        if (bottomSheetType == 0) {
            title = getString(R.string.str_please_select_type);
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
                            dataBean = goodsTypeData.get(position);
                            cateID = dataBean.getCateID();
                            typeEt.setText(dataBean.getCateName());
                        } else if (bottomSheetType == 1) {
                            String buyerGain = buyerGainList.get(position);
                            BigDecimal b1 = new BigDecimal(ObjectUtils.isEmpty(actualChargeSellerEt.getText().toString()) ?
                                    "0" : actualChargeSellerEt.getText().toString());
                            BigDecimal b2 = new BigDecimal(ObjectUtils.isEmpty(buyerGain) ?
                                    "0" : buyerGain);
                            String text = percent.subtract((b1.add(b2))).toPlainString();
                            bonusIncentivesEt.setText(text);
                            mortgageAwayEt.setText(buyerGain);
                        } else if (bottomSheetType == 2) {
                            countryData = countryList.get(position);
                            String mCountyName = countryData.getCountyName();
                            areaTv.setText(mCountyName);
                        }
                    }
                });
        if (bottomSheetType == 0) {
            if (goodsTypeData != null) {
                for (int i = 0; i < goodsTypeData.size(); i++) {
                    GoodsTypeBean.DataBean dataBean = goodsTypeData.get(i);
                    if (dataBean != null) {
                        builder.addItem(dataBean.getCateName());
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

    private void showPayBottomSheetList(View playview, boolean isRefresh) {
        if (mBottomSheetPopupWindow == null) {
            mBottomSheetPopupWindow = new BottomSheetPopupWindow(getActivity());
            mBottomSheetPopupWindow.setAnimationStyle(R.style.dialogSlideAnim);
//            mBottomSheetPopupWindow.setHeight(ScreenUtils.getScreenHeight() / 2);
            mBottomSheetPopupWindow.createPopup();
            mBottomSheetPopupWindow.setData(payConfigList);
            mBottomSheetPopupWindow.setOnBottomSheetListener(new BottomSheetPopupWindow.OnBottomSheetListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onComplete(List<PayConfigBean.DataBean> selPositionList) {
                    mSelPositionList = selPositionList;
                    mBottomSheetPopupWindow.dismiss();
                    StringBuffer stringBuffer = new StringBuffer();
                    if (ObjectUtils.isNotEmpty(selPositionList)) {
                        for (int i = 0; i < selPositionList.size(); i++) {
                            PayConfigBean.DataBean dataBean = selPositionList.get(i);
                            stringBuffer.append(dataBean.getName()).append(",");
                        }
                    }

                    if (ObjectUtils.isNotEmpty(stringBuffer)) {
                        payConfigValTv.setText(stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());
                    } else {
                        payConfigValTv.setText("");
                    }
                }
            });
        }

        mBottomSheetPopupWindow.showAtLocation(playview, Gravity.BOTTOM, 0, 0);
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
        if (ObjectUtils.isEmpty(goodsNameEt.getText().toString())) {
            isVer = false;
            msg = str1 + getString(R.string.str_goods_name).replace(":", "");
        } else if (ObjectUtils.isEmpty(specificationEt.getText().toString())) {
            isVer = false;
            msg = str1 + getString(R.string.str_specification).replace(":", "");
        } else if (ObjectUtils.isEmpty(unitEt.getText().toString())) {
            isVer = false;
            msg = str1 + getString(R.string.str_unit).replace(":", "");
        } else if (ObjectUtils.isEmpty(inventoryEt.getText().toString())) {
            isVer = false;
            msg = str1 + getString(R.string.str_inventory).replace(":", "");
        } else if (ObjectUtils.isEmpty(priceEt.getText().toString())) {
            isVer = false;
            msg = str1 + getString(R.string.str_price).replace(":", "");
        } else if (ObjectUtils.isEmpty(postageEt.getText().toString())) {
            isVer = false;
            msg = str1 + getString(R.string.str_postage).replace(":", "");
        } else if (ObjectUtils.isEmpty(actualChargeSellerEt.getText().toString())) {
            isVer = false;
            msg = str1 + getString(R.string.str_actual_charge_seller).replace(":", "");
        } else if (ObjectUtils.isEmpty(bonusIncentivesEt.getText().toString())) {
            isVer = false;
            msg = str1 + getString(R.string.str_bonus_incentives).replace(":", "");
        } else if (ObjectUtils.isEmpty(mortgageAwayEt.getText().toString())) {
            isVer = false;
            msg = str1 + getString(R.string.str_mortgage_away).replace(":", "");
        } else if (ObjectUtils.isEmpty(introductionEt.getText().toString())) {
            isVer = false;
            msg = str1 + getString(R.string.str_introduction).replace(":", "");
        } else if (ObjectUtils.isEmpty(dataBean)) {
            isVer = false;
            msg = str1 + getString(R.string.str_type).replace(":", "");
        } else if (ObjectUtils.isEmpty(specificationEt.getText().toString())) {
            isVer = false;
            msg = str1 + getString(R.string.str_type).replace(":", "");
        } else if (imgSectionList.get(1).isAdd) {
            isVer = false;
            msg = str1 + getString(R.string.str_goods_cover).replace(":", "");
        } else if (imgSectionList.get(3).isAdd) {
            isVer = false;
            msg = str1 + getString(R.string.str_goods_slideshow).replace(":", "");
        } else if (countryData == null) {
            isVer = false;
            msg = getString(R.string.str_please_choose) + getString(R.string.str_sales_area).replace(":", "");
        } else if (ObjectUtils.isEmpty(mSelPositionList)) {
            msg = getString(R.string.str_please_pay_mode_multi_select);
        } else {
            String s1 = actualChargeSellerEt.getText().toString();
            String s2 = bonusIncentivesEt.getText().toString();
            String s3 = mortgageAwayEt.getText().toString();
            BigDecimal bigd1 = new BigDecimal(s1);
            BigDecimal bigd2 = new BigDecimal(s2);
            BigDecimal bigd3 = new BigDecimal(s3);
            BigDecimal bigd = bigd1.add(bigd2).add(bigd3);
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
