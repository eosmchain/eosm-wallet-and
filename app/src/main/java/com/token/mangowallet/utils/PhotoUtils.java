package com.token.mangowallet.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ScreenUtils;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.durban.Controller;
import com.yanzhenjie.durban.Durban;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.token.mangowallet.utils.Constants.CropimageFilePath;


/**
 * @time 2018/7/26 14:26
 * @class 描述:拍照或相册选择图片(可裁剪)
 */
public class PhotoUtils {

    public static final int PICTURE_CROP_CODE = 1002;

    public interface PhotoPathListener {
        /**
         * 将拍照或选择相册的路径进行传值
         *
         * @param result
         */
        void getPathSuccess(Object result);

    }

    /**
     * 拍照或者选择图片成功的监听
     */
    private PhotoPathListener mPhotoPathListener;

    public void setPhotoPathListener(PhotoPathListener photoPathListener) {
        this.mPhotoPathListener = photoPathListener;
    }

    /**
     * 拍照
     *
     * @param activity
     * @return
     */
    public void takePhoto(final Activity activity) {

        Album.camera(activity).image().onResult(new Action<String>() {
            @Override
            public void onAction(@NonNull String result) {
                mPhotoPathListener.getPathSuccess(result);
            }
        }).onCancel(new Action<String>() {
            @Override
            public void onAction(@NonNull String result) {

            }
        }).start();

    }

    /**
     * 从相册选取一张图片
     *
     * @param activity
     * @return
     */
    public void albumSelect(final Activity activity, boolean icCamera) {
        Album.image(activity).singleChoice().widget(Widget.newLightBuilder(activity)
                .statusBarColor(ContextCompat.getColor(activity, R.color.qmui_config_color_white))
                .toolBarColor(ContextCompat.getColor(activity, R.color.qmui_config_color_white)).build())
                .camera(icCamera).columnCount(3).onResult(new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                mPhotoPathListener.getPathSuccess(result.get(0).getPath());
            }
        }).onCancel(new Action<String>() {
            @Override
            public void onAction(@NonNull String result) {

            }
        }).start();
    }

    /**
     * 从相册选取多张图片
     *
     * @param activity
     * @return
     */
    public void albumMultipleSelect(final Activity activity, int count, boolean icCamera) {
        Album.image(activity).multipleChoice().selectCount(count)
                .widget(Widget.newLightBuilder(activity)
                        .statusBarColor(ContextCompat.getColor(activity, R.color.qmui_config_color_white))
                        .toolBarColor(ContextCompat.getColor(activity, R.color.qmui_config_color_white)).build())
                .camera(icCamera).columnCount(3).onResult(new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                mPhotoPathListener.getPathSuccess(result);
            }
        }).onCancel(new Action<String>() {
            @Override
            public void onAction(@NonNull String result) {

            }
        }).start();
    }

    /**
     * 裁剪图片
     *
     * @param activity
     * @param photoPath   图片路径
     * @param widthRatio  宽度裁剪比例
     * @param heightRatio 高度裁剪比例
     */
    public void cropImage(final Activity activity, final String photoPath, final float widthRatio, final float heightRatio) {
        //裁剪图片
        Durban.with(activity).title(activity.getString(R.string.str_crop_image)).inputImagePaths(photoPath).outputDirectory
                (CropimageFilePath).maxWidthHeight(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight())
                .compressFormat(Durban.COMPRESS_PNG).compressQuality(90).aspectRatio(1.6f, 1f).gesture(Durban.GESTURE_ALL).controller(
                Controller.newBuilder()
                        .enable(false)
                        .rotation(true)
                        .rotationTitle(true)
                        .scale(true)
                        .scaleTitle(true)
                        .build()).requestCode(200).start();
    }

    /**
     * 裁剪图片
     *
     * @param fragment
     * @param photoPath   图片路径
     * @param widthRatio  宽度裁剪比例
     * @param heightRatio 高度裁剪比例
     */
    public void cropImage(Fragment fragment, final String photoPath, final float widthRatio, final float heightRatio) {
        //裁剪图片
        Durban.with(fragment).title(fragment.getString(R.string.str_crop_image)).inputImagePaths(photoPath).outputDirectory
                (CropimageFilePath).maxWidthHeight(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight())
                .compressFormat(Durban.COMPRESS_PNG).compressQuality(90).aspectRatio(1f, 1f).gesture(Durban.GESTURE_ALL).controller(
                Controller.newBuilder()
                        .enable(false)
                        .rotation(true)
                        .rotationTitle(true)
                        .scale(true)
                        .scaleTitle(true)
                        .build()).requestCode(200).start();
    }

    /**
     * 裁剪图片
     */
    public void cropImage(BaseFragment fragment, Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        fragment.startActivityForResult(intent, PICTURE_CROP_CODE);
    }

    /**
     * 将图片地址转换成Base64编码的字符串
     *
     * @param path
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     * 将图片地址转换成Base64编码的字符串
     *
     * @param file
     * @return base64编码的字符串
     */
    public byte[] imageToBytes(File file) {
        byte[] buffer = new byte[0];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(fis.available());
            byte[] bytes = new byte[fis.available()];
            int temp;
            while ((temp = fis.read(bytes)) != -1) {
                baos.write(bytes, 0, temp);
            }
            fis.close();
            baos.close();
            buffer = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer;
    }

    /**
     * 返回true 表示可以使用  返回false表示不可以使用
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            //针对魅族手机
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    /**
     * 截屏工具类
     */
    public static boolean shotScreen(Activity activity) {
        Bitmap bitmap = ScreenUtils.screenShot(activity);
        String storePath = CropimageFilePath;
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
            fos.flush();
            fos.close();

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            activity.getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
