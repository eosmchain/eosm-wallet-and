package com.token.mangowallet.net.interceptor;

import android.os.Build;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LanguageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.net.common.BaseUrlUtils;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.NRSAUtils;
import com.token.mangowallet.utils.WalletDaoUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Retrofit 基本参数拦截器
 */
public class CommonParamInterceptor implements Interceptor {

    /**
     * 请求方法-GET
     */
    private static final String REQUEST_METHOD_GET = "GET";

    /**
     * 请求方法POST
     */
    private static final String REQUEST_METHOD_POST = "POST";

    private String encryptParams;
    private static final String LANG = "lang";
    private static final String APPTYPE = "appType";
    private static final String APKNAME = "apkName";
    private static final String VERSION = "version";
    private static final String UUID = "uuid";
    private MangoWallet mangoWallet;

    /**
     * 基础参数
     * sign (string): 签名 ,
     * client  (integer, optional): 请求客户端类型：1(ios)，2(android)
     */
//    private final String P_TM = "tm";
    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取原先的请求对象
        Request request = chain.request();
        String headerValue = request.header("urlname");

        boolean bol1 = request.toString().contains(BaseUrlUtils.getInstance().getCompanyBaseUrl());
        boolean bol2 = request.toString().contains(BaseUrlUtils.getInstance().getVoteUrl());
        boolean bol3 = ObjectUtils.equals(Constants.EOS_URL, headerValue);
        boolean bol4 = request.toString().contains("/file/upload");
        boolean bol5 = request.toString().contains(BaseUrlUtils.getInstance().getOTCUrl());
        boolean bol6 = request.toString().contains("/file/uploadFile");
        if ((bol1 || bol2) && !bol3 && !bol4) {
            if (REQUEST_METHOD_GET.equals(request.method())) {
                request = addGetBaseParams(request);
            } else if (REQUEST_METHOD_POST.equals(request.method())) {
                request = addPostBaseParams(request);
            }
        } else if (bol5) {
            request = addPostURLBaseParams(request);
        }
        request.newBuilder().addHeader("application/x-www-form-urlencoded", "charset=utf-8").build();
        LogUtils.dTag(Constants.LOG_TAG, "CommonParamInterceptor: " + request.toString());
        return chain.proceed(request);
    }

    /**
     * 添加GET方法基础参数
     *
     * @param request
     * @return
     */
    private Request addGetBaseParams(Request request) {
//        String tm = TimeUtils.millis2String(System.currentTimeMillis());
        HttpUrl.Builder builder = request.url().newBuilder();
//        if (request.toString().contains(Constants.TEST_URL)) {
//            builder.addQueryParameter(LANG, getLangParams());
//        }
        HttpUrl httpUrl = builder.build();
        //添加签名
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(NODEID).append(SID).append(tm).append(SALT);
//        encryptParams = EncryptUtils.encryptMD5ToString(stringBuilder.toString());

//        httpUrl = httpUrl.newBuilder().addQueryParameter(P_SIGN, encryptParams).build();
        request = request.newBuilder().url(httpUrl).build();
        return request;

    }


    /**
     * 添加POST方法基础参数
     *
     * @param request
     * @return
     */
    private Request addPostURLBaseParams(Request request) {
        String url = "";
        /**
         * request.body() instanceof FormBody 为true的条件为：
         * 在ApiService 中将POST请求中设置
         * 1，请求参数注解为@FieldMap
         * 2，方法注解为@FormUrlEncoded
         */
        FormBody.Builder builder = new FormBody.Builder();
        if (request.body() instanceof FormBody) {
            FormBody oldFormBody = (FormBody) request.body();
            for (int i = 0; i < oldFormBody.size(); i++) {
                //@FieldMap 注解 Map元素中 key 与 value 皆不能为null,否则会出现NullPointerException
                if (oldFormBody.value(i) != null) {
                    builder.add(oldFormBody.name(i), oldFormBody.value(i));
                }
            }
        }
//        Map<String, Object> paramsMap = new HashMap<>();
        FormBody formBody = getPostString(builder, request);
//        Map<String, Object> paramsMap = new HashMap<>();
//        for (int i = 0; i < formBody.size(); i++) {
//            if (formBody.value(i).contains("[")) {
//                paramsMap.put(formBody.name(i), JSONArray.parseArray(formBody.value(i)));
//            } else {
//                paramsMap.put(formBody.name(i), formBody.value(i));
//            }
//        }
//        url = requestPath(request.url(), formBody);
//        JSONObject jsonObject = new JSONObject(paramsMap);
//        LogUtils.e("params:", jsonObject.toString());
        return request.newBuilder().post(formBody)
//                .addHeader("Content-Type", "application/json")
                .removeHeader("content")
                .build();
//        return request.newBuilder().post(RequestBody.create(formBody.toString(), MediaType.parse("application/json; charset=utf-8")))
//                .addHeader("Accept", "application/json")
//                .build();
    }

    private static String requestPath(HttpUrl url, StringBuffer formBody) {
        String path = url.encodedPath();
        String query = url.encodedQuery();
        return url.scheme() + "://" + url.host() + (query != null ? (path + '?' + query + '&' + (ObjectUtils.isEmpty(formBody) ? "" : formBody.toString()))
                : path + (ObjectUtils.isEmpty(formBody) ? "" : '?' + formBody.toString()));
    }

    private static String requestPath(HttpUrl url) {
        String path = url.encodedPath();
        String query = url.encodedQuery();
        return url.scheme() + "://" + url.host() + path;
    }

    /**
     * 添加POST方法基础参数
     *
     * @param request
     * @return
     */
    private Request addPostBaseParams(Request request) {
        /**
         * request.body() instanceof FormBody 为true的条件为：
         * 在ApiService 中将POST请求中设置
         * 1，请求参数注解为@FieldMap
         * 2，方法注解为@FormUrlEncoded
         */
        FormBody.Builder builder = new FormBody.Builder();
        if (request.body() instanceof FormBody) {
            FormBody oldFormBody = (FormBody) request.body();
            for (int i = 0; i < oldFormBody.size(); i++) {
                //@FieldMap 注解 Map元素中 key 与 value 皆不能为null,否则会出现NullPointerException
                if (oldFormBody.value(i) != null) {
                    builder.add(oldFormBody.name(i), oldFormBody.value(i));
                }
            }
        }
        Map<String, Object> paramsMap = new HashMap<>();
        FormBody formBody = getPostFormBody(builder, request);
        for (int i = 0; i < formBody.size(); i++) {
            if (formBody.value(i).contains("[")) {
                paramsMap.put(formBody.name(i), JSONArray.parseArray(formBody.value(i)));
            } else {
                paramsMap.put(formBody.name(i), formBody.value(i));
            }
        }
        String content = request.header("content");
        String jsonData = GsonUtils.toJson(paramsMap);
        try {
            content = NRSAUtils.encrypt(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mangoWallet = WalletDaoUtils.getCurrentWallet();
        if (mangoWallet == null) {
            mangoWallet = WalletDaoUtils.getCurrentWallet(Utils.getApp());
        }
        request.newBuilder().removeHeader("content").build();
        LogUtils.dTag(Constants.LOG_TAG, "CommonParamInterceptor formBody: " + formBody);
        LogUtils.dTag(Constants.LOG_TAG, "CommonParamInterceptor params: " + jsonData + " header content: " + content);
        return request.newBuilder().post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("User-Agent", String.format("%s/%s (Linux; Android %s; %s Build/%s)", AppUtils.getAppName(), AppUtils.getAppVersionCode(), Build.VERSION.RELEASE, Build.MANUFACTURER, Build.ID))
                .removeHeader("content")
                .addHeader(LANG, getLangParams())
                .addHeader(APPTYPE, "android")
                .addHeader(APKNAME, "mangowalletnew")
                .addHeader(VERSION, String.valueOf(AppUtils.getAppVersionCode()))
                .addHeader(UUID, DeviceUtils.getUniqueDeviceId())
                .addHeader("address", mangoWallet.getWalletAddress())
                .addHeader("publicKey", mangoWallet.getPublicKey())
                .addHeader("content", content)
                .build();//application/json
//        return request.newBuilder().post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData))
//                .addHeader("Accept", "application/json")
//                .build();
    }


    /**
     * post添加公共参数
     *
     * @param builder
     * @return
     */
    private FormBody getPostFormBody(FormBody.Builder builder, Request request) {
        if (request.toString().contains(BaseUrlUtils.getInstance().getCompanyBaseUrl())
                || request.toString().contains(BaseUrlUtils.getInstance().getVoteUrl())) {
            builder.add(LANG, getLangParams());
            builder.add(APPTYPE, "android");
            builder.add(APKNAME, "mangowalletnew");
            builder.add(VERSION, String.valueOf(AppUtils.getAppVersionCode()));
            builder.add(UUID, DeviceUtils.getUniqueDeviceId());
        }
        String content = request.header("content");
        if (ObjectUtils.isNotEmpty(content)) {
            String decryptJson = NRSAUtils.decrypt(content);
            try {
                Map map = GsonUtils.fromJson(decryptJson, Map.class);
                Iterator<String> iter = map.keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    Object value = map.get(key);
                    builder.add(key, value + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShort("接收数据解析异常");
            }
        }
        FormBody formBody = builder.build();
        StringBuffer tempParams = new StringBuffer();
        for (int i = 0; i < formBody.size(); i++) {
            tempParams.append(formBody.name(i)).append("=").append(formBody.value(i)).append("&");
        }
        if (!ObjectUtils.isEmpty(tempParams)) {
            tempParams.deleteCharAt(tempParams.lastIndexOf("&"));
        }

        LogUtils.e("params:" + tempParams);
        return formBody;
    }


    /**
     * post添加公共参数
     *
     * @param builder
     * @return
     */
    private FormBody getPostString(FormBody.Builder builder, Request request) {
//        if (request.toString().contains(BaseUrlUtils.getInstance().getOTCUrl())) {
//            builder.add(LANG, getLangParams());
//            builder.add(APPTYPE, "android");
//            builder.add(APKNAME, "mangowalletnew");
//            builder.add(VERSION, String.valueOf(AppUtils.getAppVersionCode()));
//            builder.add(UUID, DeviceUtils.getUniqueDeviceId());
//        }
        String content = request.header("content");
        if (ObjectUtils.isNotEmpty(content)) {
            String decryptJson = NRSAUtils.decrypt(content);
            try {
                Map map = GsonUtils.fromJson(decryptJson, Map.class);
                Iterator<String> iter = map.keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    Object value = map.get(key);
                    builder.add(key, value + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShort("接收数据解析异常");
            }
        }
        FormBody formBody = builder.build();
        StringBuffer tempParams = new StringBuffer();
        for (int i = 0; i < formBody.size(); i++) {
            tempParams.append(formBody.name(i)).append("=").append(formBody.value(i)).append("&");
        }
        if (!ObjectUtils.isEmpty(tempParams)) {
            tempParams.deleteCharAt(tempParams.lastIndexOf("&"));
        }

        LogUtils.e("params:" + tempParams);
        return formBody;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private String getLangParams() {
        String lang = "";
        Locale locale = LanguageUtils.getAppContextLanguage();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (ObjectUtils.equals("en", language)) {
            lang = "en_US";
        } else if (ObjectUtils.equals("ja", language)) {
            lang = "ja_JP";
        } else if (ObjectUtils.equals("ko", language)) {
            lang = "ko_KR";
        } else if (ObjectUtils.equals("zh", language) && ObjectUtils.equals("CN", country)) {
            lang = "zh_CN";
        } else if (ObjectUtils.equals("zh", language) && !ObjectUtils.equals("CN", country)) {
            lang = "zh_TW";
        } else {
            lang = "en_US";
        }
        return lang;
    }


}


