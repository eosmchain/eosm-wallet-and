package com.token.mangowallet.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseActivity;
import com.token.mangowallet.view.X5WebView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.token.mangowallet.utils.Constants.EXTRA_TO_URL;
import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class WebActivity extends BaseActivity {

    @BindView(R.id.activity_web_title)
    TextView mTvTitle;
    @BindView(R.id.activity_web_pb)
    ProgressBar mPb;
    @BindView(R.id.activity_web_webView)
    X5WebView webView;

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;

    private String currentUrl;
    private String flag;
    private String[] moreStr = new String[2];
    /**
     * 记录title和标题
     */
    private Map<String, String> titles = new HashMap<>();
    private QMUIPopup mListPopup;

    @Override
    public int getContentView() {
        return R.layout.activity_web;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initData() {
        currentUrl = getIntent().getStringExtra(EXTRA_TO_URL);
        flag = getIntent().getStringExtra("flag");
    }


    @Override
    public void initView() {
        //状态栏沉浸式
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white), true);
        BarUtils.setStatusBarLightMode(this, true);

        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ArrayList<View> outView = new ArrayList<View>();
                getWindow().getDecorView().findViewsWithText(outView, "QQ浏览器", View.FIND_VIEWS_WITH_TEXT);
                int size = outView.size();
                if (outView != null && outView.size() > 0) {
                    outView.get(0).setVisibility(View.GONE);
                }
            }
        });
        mPb.setMax(100);
        initWebView();
    }

    /**
     * 初始化webview
     */
    private void initWebView() {
        // 添加js对象(必要)
        webView.addJavascriptInterface(new EOSWebInteration(this), "EOSWebInteration");
        webView.clearCache(true);
        webView.setInitialScale(25);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new ChromeClient());
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 表示按返回键时的操作
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        // 后退
                        webView.goBack();
                        // 已处理
                        return true;
                    }
                }
                return false;
            }
        });

        WebSettings setting = webView.getSettings();
        WebSettings webSetting = webView.getSettings();
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);//设置支持缩放
        setting.setDisplayZoomControls(false);//是否使用内置的缩放机制
        webSetting.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSetting.setSupportMultipleWindows(false);//是否支持多窗口
        webSetting.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSetting.setAppCacheEnabled(true);
        // 解决图片不显示
        setting.setBlockNetworkImage(false);//是否禁止从网络（通过http和https URI schemes访问的资源）下载图片资源，默认值为false
        // webSetting.setDatabaseEnabled(true);
        setting.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        setting.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        setting.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        setting.setAllowUniversalAccessFromFileURLs(false);
        setting.setDefaultTextEncodingName("UTF-8");

        webSetting.setDomStorageEnabled(true);//DOM存储API是否可用，默认false。调用相机必加，去掉或为false会闪退
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        webView.loadUrl(currentUrl);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 初始化更多弹窗
     */
    private void initPopup(View view) {
        if (mListPopup == null) {
            moreStr[0] = getResources().getString(R.string.str_web_refresh);
            moreStr[1] = getResources().getString(R.string.str_external_browser_open);
            List<String> data = new ArrayList<>();
            Collections.addAll(data, moreStr);

            ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, data);
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0:
                            //刷新当前页面
                            webView.reload();
                            break;
                        case 1:
                            try {
                                Uri uri = Uri.parse(currentUrl);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            } catch (Exception e) {
                                LogUtils.e("webError", e.getMessage());
                            }
                            break;
                    }
                    if (mListPopup != null) {
                        mListPopup.dismiss();
                    }
                }
            };
            mListPopup = QMUIPopups.listPopup(this,
                    QMUIDisplayHelper.dp2px(this, 150),
                    QMUIDisplayHelper.dp2px(this, 120),
                    adapter,
                    onItemClickListener)
                    .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                    .preferredDirection(QMUIPopup.DIRECTION_TOP)
                    .shadow(true)
                    .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 5))
                    .onDismiss(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                        }
                    });

        }
        mListPopup.show(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    @OnClick({R.id.activity_web_back, R.id.iv_activity_web_close, R.id.iv_activity_web_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_web_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.iv_activity_web_close:
                finish();
                break;
            case R.id.iv_activity_web_more:
                initPopup(view);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理页面返回或取消选择结果
         */
        switch (requestCode) {
            case FILE_CHOOSER_RESULT_CODE:
                if (null == uploadMessage && null == uploadMessageAboveL) {
                    return;
                }
                Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                if (uploadMessageAboveL != null) {
                    onActivityResultAboveL(requestCode, resultCode, data);
                } else if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(result);
                    uploadMessage = null;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null)
            webView.destroy();
        super.onDestroy();
    }

    @Override
    public boolean isShowState() {
        return true;
    }

    class ChromeClient extends WebChromeClient {
        public ChromeClient() {
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mPb.setVisibility(View.GONE);
            } else {
                mPb.setVisibility(View.VISIBLE);
                mPb.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (TextUtils.isEmpty(title)) {
                title = "";
            }
            mTvTitle.setText(title);
            titles.put(currentUrl, title);
        }

        /**
         * For Android < 3.0
         * 过时的方法：openFileChooser
         *
         * @param filePathCallback
         */
        public void openFileChooser(ValueCallback<Uri> filePathCallback) {
            uploadMessage = filePathCallback;
            Log.d(LOG_TAG, "openFileChooser1: 被调用几次？");
        }

        /**
         * For Android  >= 3.0
         * 过时的方法：openFileChooser
         *
         * @param filePathCallback
         * @param acceptType
         */
        public void openFileChooser(ValueCallback filePathCallback, String acceptType) {
            uploadMessage = filePathCallback;
            String title = acceptType;
        }

        /**
         * For Android  >= 4.1
         * 过时的方法：openFileChooser
         *
         * @param filePathCallback
         * @param acceptType
         * @param capture
         */
        public void openFileChooser(ValueCallback<Uri> filePathCallback, String acceptType, String capture) {
            uploadMessage = filePathCallback;
            String title = acceptType;
        }

        /**
         * For Android >= 5.0
         *
         * @param webView
         * @param filePathCallback
         * @param fileChooserParams
         * @return
         */
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            uploadMessageAboveL = filePathCallback;
            Log.d(LOG_TAG, "onShowFileChooser2: 被调用几次？");
            /**
             * 返回true，如果filePathCallback被调用；返回false，如果忽略处理
             */
            return true;
        }
    }

    class EOSWebInteration {
        Activity activity;

        public EOSWebInteration(Activity activity) {
            this.activity = activity;
        }
    }
}
