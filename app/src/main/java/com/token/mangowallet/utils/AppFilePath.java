package com.token.mangowallet.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.AppUtils;
import com.yanzhenjie.album.provider.CameraFileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static com.token.mangowallet.utils.Constants.CropimageFilePath;

public class AppFilePath {


    // KeyStore文件外置存储目录
    public static String Wallet_DIR;


    public static void init(Context context) {

        Wallet_DIR = context.getFilesDir().getAbsolutePath();
    }

    /**
     * 这种目录下的文件在应用被卸载时也会跟着被删除
     *
     * @param context
     * @return
     */
    public static File getExternalFilesDir(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            File path = context.getExternalFilesDir(null);

            if (path != null) {
                return path;
            }
        }
        final String filesDir = "/Android/data/" + context.getPackageName() + "/files/";
        return new File(Environment.getExternalStorageDirectory().getPath() + filesDir);
    }

    /**
     * 这种目录下的文件在应用被卸载时也会跟着被删除
     *
     * @param context
     * @return
     */
    public static File getExternalCacheDir(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            File path = context.getExternalCacheDir();

            if (path != null) {
                return path;
            }
        }
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }


    /**
     * 这种目录下的文件在应用被卸载时不会被删除
     * 钱包等数据可以存放到这里
     *
     * @return
     */
    public static String getExternalPrivatePath(String name) {
        String namedir = "/" + name + "/";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {

            return Environment.getExternalStorageDirectory().getPath() + namedir;
        } else {
            return null;
//            return new File(DATA_ROOT_DIR_OUTER, name).getPath();
        }
    }

    /**
     * 将图片存到本地
     */
    public static Uri saveBitmap(Context context, Bitmap bm) {
        try {
            String fileName = creatFileName();
            String dir = CropimageFilePath + File.separator + fileName + ".png";
            File f = new File(dir);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Uri photoUri = getUri(context, f);
            return photoUri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author:hemin
     */
    public static String creatFileName() {
        Random random = new Random();
        //文件夹名字的长度
        int length = 10;//default
//		for(int i = 0; i <30; i++){
//			int a  = random.nextInt(10);
//			System.out.println(a);
        String numstr = "123456789";
        String chastr_b = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String chastr_s = "abcdefghijklmnopqrstuvwxyz";
        String specil = "_";
        String base = numstr + chastr_b + chastr_s + specil;
        //文件夹名的规范文件夹不能包含以下字符：
        //井号 (#)；百分号 (%)；“&”；星号 (*)；竖线 (|)；反斜杠 (\)；冒号(:)；
        //双引号 (")；小于号 (<)；大于号 (>)；问号 (?)；斜杠 (/)；前导或尾随空格 (' ')；这样的空格将被去除；
        //需求是将文件名的大写开头，数字结尾
        StringBuffer sb = new StringBuffer();

        sb.append(chastr_b.charAt(random.nextInt(chastr_b.length())));
        for (int i = 0; i < length - 2; i++) {
            int num = random.nextInt(base.length());
            sb.append(base.charAt(num));
        }
        //追加最后一个数字
        sb.append(numstr.charAt(random.nextInt(numstr.length())));
        //System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * Generates an externally accessed URI based on path.
     *
     * @param context context.
     * @param outPath file path.
     * @return the uri address of the file.
     */
    @NonNull
    public static Uri getUri(@NonNull Context context, @NonNull File outPath) {
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(outPath);
        } else {
            uri = CameraFileProvider.getUriForFile(context, CameraFileProvider.getProviderName(context), outPath);
        }
        return uri;
    }

    public static byte[] compressImage(String srcPath) {

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
}
