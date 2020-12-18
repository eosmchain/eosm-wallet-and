package com.token.mangowallet.net.interceptor;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.token.mangowallet.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.token.mangowallet.utils.Constants.LOG_TAG;
import static com.token.mangowallet.utils.Constants.isTest;

public class LogInterceptor implements Interceptor {
    private static final String TAG = "HTTP_TRACE";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("<---------------------------BEGIN REQUEST---------------------------------->");
        logBuilder.append("\n");
        logBuilder.append("Request encoded url: ").append(request.method()).append(" ")
                .append(requestPath(request.url()));
        logBuilder.append("\n");
        String decodeUrl = requestDecodedPath(request.url());
        if (!TextUtils.isEmpty(decodeUrl)) {
            logBuilder.append("Request decoded url: ").append(request.method()).append(" ")
                    .append(decodeUrl);
        }

        Headers headers = request.headers();
        logBuilder.append("\n=============== Headers ===============\n");
        for (int i = headers.size() - 1; i > -1; i--) {
            logBuilder.append(headers.name(i)).append(" : ").append(headers.get(headers.name(i))).append("\n");

        }
        logBuilder.append("\n=============== END Headers ===============\n");

        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                contentType.charset(UTF8);
            }

            logBuilder.append(buffer.readString(UTF8));
        }
        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        logBuilder.append("\n");
        logBuilder.append("Response timeout: ").append(tookMs).append("ms");
        logBuilder.append("\n");
        logBuilder.append("Response message: ").append(response.message());
        logBuilder.append("\n");
        logBuilder.append("Response code: ").append(response.code());
        LogUtils.d(LOG_TAG, LOG_TAG + " = " + response.code());
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.getBuffer();

            Charset charset = null;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (charset == null) {
                charset = UTF8;
            }
            if (responseBody.contentLength() != 0) {
                logBuilder.append("\n");
                logBuilder.append("Response body: \n").append(buffer.clone().readString(charset));
            }
        }
        headers = response.headers();
        logBuilder.append("\n=============== Headers ===============\n");
        for (int i = headers.size() - 1; i > -1; i--) {
            logBuilder.append(headers.name(i)).append(" : ").append(headers.get(headers.name(i))).append("\n");

        }
        logBuilder.append("\n=============== END Headers ===============\n");

        logBuilder.append("\n");
        logBuilder.append("<-----------------------------END REQUEST--------------------------------->");
        logBuilder.append("\n\n\n");
        if (isTest) {
            FileIOUtils.writeFileFromString(getSDPath(), EncodeUtils.urlDecode(logBuilder.toString(), "UTF-8"));
        }
        LogUtils.d(TAG, EncodeUtils.urlDecode(logBuilder.toString(), "UTF-8"));
        return response;
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    private static File getSDPath() {
        File sdDir = null;
        try {
            boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
            if (sdCardExist) {
                // 这里可以修改为你的路径
                sdDir = new File(Constants.HttpRequestLog, "http_" + TimeUtils.getNowString() + ".txt");
                if (!sdDir.exists()) {
                    File dir = new File(sdDir.getParent());
                    dir.mkdirs();
                    sdDir.createNewFile();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sdDir;
    }

    private String requestDecodedPath(HttpUrl url) {
        try {
            String path = URLDecoder.decode(url.encodedPath(), "UTF-8");
            String query = URLDecoder.decode(url.encodedQuery(), "UTF-8");
            return url.scheme() + "://" + url.host() + (query != null ? (path + '?' + query) : path);
        } catch (Exception ex) {
            /* Quality */
        }
        return null;
    }

    private static String requestPath(HttpUrl url) {
        String path = url.encodedPath();
        String query = url.encodedQuery();
        return url.scheme() + "://" + url.host() + (query != null ? (path + '?' + query) : path);
    }
}