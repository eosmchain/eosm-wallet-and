package com.token.mangowallet.net.interceptor;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.token.mangowallet.net.common.BaseUrlUtils;
import com.token.mangowallet.utils.Constants;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseUrlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取request
        Request request = chain.request();
        //从request中获取原有的HttpUrl实例oldHttpUrl
        HttpUrl oldHttpUrl = request.url();
        String oldUrl = oldHttpUrl.toString();
        //获取request的创建者builder
        Request.Builder builder = request.newBuilder();
        //从request中获取headers，通过给定的键url_name
        List<String> headerValues = request.headers("urlname");
        if (headerValues != null && headerValues.size() > 0) {
            //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
            builder.removeHeader("urlname");
            //匹配获得新的BaseUrl
            String headerValue = headerValues.get(0);
            HttpUrl newBaseUrl = null;
            if (Constants.CORPORATION_URL.equals(headerValue)) {
                newBaseUrl = HttpUrl.parse(BaseUrlUtils.getInstance().getCompanyBaseUrl());
            } else if (Constants.DIGICCY_PRICE_URL.equals(headerValue)) {
                newBaseUrl = HttpUrl.parse(Constants.TICKER_API_URL);
            } else if (Constants.EOS_URL.equals(headerValue)) {
                String baseUrl = BaseUrlUtils.getInstance().getDIGICCYBaseUrl();
                if (ObjectUtils.isEmpty(baseUrl)) {
                    newBaseUrl = oldHttpUrl;
                } else {
                    newBaseUrl = HttpUrl.parse(baseUrl);
                }
            } else if (Constants.VOTEURL.equals(headerValue)) {
                String baseUrl = BaseUrlUtils.getInstance().getVoteUrl();
                if (ObjectUtils.isEmpty(baseUrl)) {
                    newBaseUrl = oldHttpUrl;
                } else {
                    newBaseUrl = HttpUrl.parse(baseUrl);
                }
            } else if (Constants.OTCURL.equals(headerValue)) {
                String baseUrl = BaseUrlUtils.getInstance().getOTCUrl();
                if (ObjectUtils.isEmpty(baseUrl)) {
                    newBaseUrl = oldHttpUrl;
                } else {
                    newBaseUrl = HttpUrl.parse(baseUrl);
                }
            } else if (Constants.MMGPSME.equals(headerValue)) {
                newBaseUrl = HttpUrl.parse(Constants.MMGPS_API_URL);
            } else {
                newBaseUrl = oldHttpUrl;
            }

            //重建这个request，通过builder.url(newFullUrl).build()；
            //重建新的HttpUrl，修改需要修改的url部分
            HttpUrl newFullUrl = oldHttpUrl
                    .newBuilder()
                    .scheme(newBaseUrl.scheme())//更换网络协议
                    .host(newBaseUrl.host())//更换主机名
                    .port(newBaseUrl.port())
                    .build();
            //重建这个request，通过builder.url(newFullUrl).build()；
            // 然后返回一个response至此结束修改

            // 然后返回一个response至此结束修改
            LogUtils.dTag(Constants.LOG_TAG, "BaseUrlInterceptor: " + newBaseUrl.toString());
            return chain.proceed(builder.url(newFullUrl).build());
        }
        return chain.proceed(request);
    }
}
