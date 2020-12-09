package com.token.mangowallet.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.bean.entity.UploadImgBean;
import com.token.mangowallet.net.common.NetWorkManager;
import com.token.mangowallet.ui.adapter.decoration.GridSectionAverageGapItemDecoration;
import com.token.mangowallet.utils.PhotoUtils;
import com.yanzhenjie.album.AlbumFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class TestFragment extends BaseFragment {


    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.submitBtn)
    QMUIRoundButton submitBtn;

    private Unbinder unbinder;
    public final static String addImge = "addImge";
    public final static int count = 100;
    private TestImgeAdapter testImgeAdapter;
    private PhotoUtils photoUtils;
    private List<String> imgList = new ArrayList();
    private byte[] mfileData;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getActivity(), R.color.durban_White));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_test, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initData() {
        topBar.setTitle("TEST");
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                popBackStack();
            }
        });
        photoUtils = new PhotoUtils();
        imgList.add(addImge);
    }

    @Override
    protected void initView() {
        testImgeAdapter = new TestImgeAdapter(imgList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.addItemDecoration(new GridSectionAverageGapItemDecoration(25, 25, 25, 25));
        recyclerView.setAdapter(testImgeAdapter);
    }

    @Override
    protected void initAction() {
        photoUtils.setPhotoPathListener(new PhotoUtils.PhotoPathListener() {
            @Override
            public void getPathSuccess(Object result) {
                if (result instanceof ArrayList) {
                    ArrayList<AlbumFile> resultPath = (ArrayList<AlbumFile>) result;
                    if (ObjectUtils.isNotEmpty(resultPath)) {
                        for (int i = 0; i < resultPath.size(); i++) {
                            String imgPath = resultPath.get(i).getPath();
                            imgList.add(imgPath);
                        }
                        imgList.remove(addImge);
                        imgList.add(imgList.size(), addImge);
                        testImgeAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        testImgeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String imgData = imgList.get(position);
                if (ObjectUtils.equals(addImge, imgData)) {
                    photoUtils.albumMultipleSelect(getActivity(), count, true);
                } else {
                    testImgeAdapter.removeAt(position);
                }
            }
        });
    }

    @OnClick(R.id.submitBtn)
    public void onViewClicked() {
        uploadImge();
    }

    private void uploadImge() {
        try {
            boolean isNull = true;
            MultipartBody.Builder mMultipartBodyBuilder = new MultipartBody.Builder();
            for (int i = 0; i < imgList.size(); i++) {
                String imgData = imgList.get(i);
                if (!ObjectUtils.equals(addImge, imgData)) {
                    File file = new File(imgData);
                    if (!file.exists()) {
                        LogUtils.d("找不到该文件");
                        continue;
                    }
                    photoUtils.imageToBytes(file);
                    mMultipartBodyBuilder.addFormDataPart("file", URLEncoder.encode(file.getName(), "UTF-8")
                            , RequestBody.create(compressImage(imgData), MediaType.parse("multipart/form-data")));
                    isNull = false;
                }
            }
            if (isNull) {
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

    private void uploadImgeSuccess(JsonObject jsonObject) {
        LogUtils.dTag(LOG_TAG, " jsonObject = " + GsonUtils.toJson(jsonObject));
        if (jsonObject != null) {
            UploadImgBean uploadImgBean = GsonUtils.fromJson(GsonUtils.toJson(jsonObject), UploadImgBean.class);
            if (uploadImgBean.getCode() == 0) {
                List<String> goodsImgeList = uploadImgBean.getData();
                for (int i = 0; i < goodsImgeList.size(); i++) {
                    String goodsImge = goodsImgeList.get(i);
                }
            }
        }
    }

    private void onError(Throwable e) {
        dismissTipDialog();
//        ToastUtils.showLong(R.string.str_network_error);
        LogUtils.dTag(LOG_TAG, "e = " + e.getMessage() + " ======= " + e.toString());
    }

    /**
     * 压缩本地图片
     *
     * @param srcPath
     * @return
     */
    private Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        //其实是无效的,大家尽管尝试
        return bitmap;
    }

    private byte[] compressImage(String srcPath) {

        Bitmap image = BitmapFactory.decodeFile(srcPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        int length = baos.toByteArray().length / 1024;

        if (length > 5000) {
            //重置baos即清空baos
            baos.reset();
            //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        } else if (length > 4000) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        } else if (length > 3000) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        } else if (length > 2000) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        }
        //循环判断如果压缩后图片是否大于1M,大于继续压缩
        while (baos.toByteArray().length / 1024 > 1024) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //每次都减少10
            options -= 10;
        }
        try {
            baos.close();
            image.recycle();
            image = null;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return baos.toByteArray();
    }

//    public static String compressImage(String filePath) {
//
//        //原文件
//        File oldFile = new File(filePath);
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//
//        //压缩文件路径 照片路径/
//        String targetPath = oldFile.getPath();
//        int quality = 50;//压缩比例0-100
//
//        File outputFile = new File(targetPath);
//        try {
//            if (!outputFile.exists()) {
//                outputFile.getParentFile().mkdirs();
//                //outputFile.createNewFile();
//            } else {
//                outputFile.delete();
//            }
//            FileOutputStream out = new FileOutputStream(outputFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return filePath;
//        }
//        return outputFile.getPath();
//    }
}
