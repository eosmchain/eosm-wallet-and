package com.token.mangowallet.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;

import com.blankj.utilcode.util.AppUtils;
import com.token.mangowallet.R;

import java.io.File;

public class AppDataUtils {


    /**
     * 下载安装包
     *
     * @param url 下载链接
     */
    public static void downloadApk(Context context, String url, String fileName) {
        File file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String target = file.getPath();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(context.getString(R.string.str_downloading));
        progressDialog.setMessage(context.getString(R.string.str_please_wait));
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setCancelable(false);
        FrontDownloadUtil.get().download(url, target, fileName, new FrontDownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //下载完成进行相关逻辑操作
                AppUtils.installApp(file);
            }

            @Override
            public void onDownloading(int progress) {
                progressDialog.setProgress(progress);
            }

            @Override
            public void onDownloadFailed(Exception e) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
        });
    }
}
