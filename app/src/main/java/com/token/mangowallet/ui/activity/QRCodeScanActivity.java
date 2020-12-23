package com.token.mangowallet.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.token.mangowallet.R;
import com.token.mangowallet.utils.PhotoUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_QR_SCAN;

public class QRCodeScanActivity extends AppCompatActivity implements QRCodeView.Delegate {

    @BindView(R.id.topBar)
    QMUITopBar topBar;
    @BindView(R.id.zxingview)
    ZXingView zxingview;
    @BindView(R.id.lamplightTv)
    AppCompatTextView lamplightTv;
    @BindView(R.id.photoTv)
    AppCompatTextView photoTv;

    /**
     * 选择照片请求码
     */
    private static final int GET_PIC_CODE = 1001;
    private static final int CAMERA_REQUEST_CODE = 2;//请求码
    private Bitmap scanBitmap;
    private PhotoUtils photoUtils;
    private Unbinder unbinder;
    private boolean isOpenFlashlight = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        unbinder = ButterKnife.bind(this);
        initData();
        initView();
    }

    protected void initData() {
        photoUtils = new PhotoUtils();
        photoUtils.setPhotoPathListener(new PhotoUtils.PhotoPathListener() {
            @Override
            public void getPathSuccess(Object path) {
                LogUtils.dTag("QrcodeScanActivity==", "path = " + path);
                zxingview.decodeQRCode(path.toString());
            }
        });
    }

    public void initView() {
        initTopBar();
        zxingview.setDelegate(this);
    }

    private void initTopBar() {
        topBar.setTitle(StringUtils.getString(R.string.str_qrcode_scan));
        topBar.addLeftImageButton(R.drawable.icon_black_arrows_back, R.id.topbar_left_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onScanQRCodeSuccess(String result) {
        LogUtils.dTag("QrcodeScanActivity==", "result = " + result);
        if (!ObjectUtils.isEmpty(result)) {
            scanQRCodeResult(result.trim());
        }
    }

    private void scanQRCodeResult(String result) {
        LogUtils.dTag("onScanQRCodeSuccess==", "result = " + result);
        vibrate();
        Intent intent = null;
        intent = new Intent();
        intent.putExtra(INTENT_EXTRA_KEY_QR_SCAN, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 手机振动
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = zxingview.getScanBoxView().getTipText();
        String ambientBrightnessTip = getString(R.string.str_qrcode_explain_3);
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                zxingview.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                zxingview.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LogUtils.e("打开相机出错");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == GET_PIC_CODE) {
            try {
                final Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        return QRCodeDecoder.syncDecodeQRCode(bitmap);
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        scanQRCodeResult(result);
                    }
                }.execute();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 打开后置摄像头开始预览，但是并未开始识别
        zxingview.startCamera();
        // 显示扫描框，并且延迟0.1秒后开始识别
        zxingview.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        // 关闭摄像头预览，并且隐藏扫描框
        zxingview.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁二维码扫描控件
        zxingview.onDestroy();

    }

    @OnClick({R.id.lamplightTv, R.id.photoTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lamplightTv:
                if (isOpenFlashlight) {
                    zxingview.closeFlashlight(); // 关闭散光灯
                    isOpenFlashlight = false;
                } else {
                    zxingview.openFlashlight(); // 打开闪光灯
                    isOpenFlashlight = true;
                }
                break;
            case R.id.photoTv:
                photoUtils.albumSelect(QRCodeScanActivity.this, false);
                break;
        }
    }
}

