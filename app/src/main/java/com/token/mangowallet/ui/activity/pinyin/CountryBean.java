package com.token.mangowallet.ui.activity.pinyin;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.token.mangowallet.utils.PinyinUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CountryBean implements PyEntity {
    private static final String TAG = CountryBean.class.getSimpleName();
    public int code;
    public String name, locale, pinyin;
    public int flag;
    private static ArrayList<CountryBean> countries = null;

    public CountryBean(int code, String name, String locale, int flag) {
        this.code = code;
        this.name = name;
        this.flag = flag;
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                "flag='" + flag + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public static ArrayList<CountryBean> getAll(@NonNull Context ctx, @Nullable ExceptionCallback callback) {
        if(countries != null) return countries;
        countries = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(ctx.getResources().getAssets().open("country_code.json")));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null)
                sb.append(line);
            br.close();
            JSONArray ja = new JSONArray(sb.toString());
            String key = getKey(ctx);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                int flag = 0;
                String locale = jo.getString("locale");
                if(!TextUtils.isEmpty(locale)) {
                    flag = ctx.getResources().getIdentifier("flag_" + locale.toLowerCase(), "mipmap", ctx.getPackageName());
                }
                countries.add(new CountryBean(jo.getInt("code"), jo.getString(key), locale, flag));
            }

            Log.i(TAG, countries.toString());
        } catch (IOException e) {
            if(callback != null) callback.onIOException(e);
            e.printStackTrace();
        } catch (JSONException e) {
            if(callback != null) callback.onJSONException(e);
            e.printStackTrace();
        }
        return countries;
    }

    public static CountryBean fromJson(String json){
        if(TextUtils.isEmpty(json)) return null;
        try {
            JSONObject jo = new JSONObject(json);
            return new CountryBean(jo.optInt("code") ,jo.optString("name"), jo.optString("locale"), jo.optInt("flag"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toJson(){
        return "{\"name\":\"" + name + "\", \"code\":" + code + ", \"flag\":" + flag + ",\"locale\":\"" + locale + "\"}";
    }

    public static void destroy(){ countries = null; }

    private static String getKey(Context ctx) {
        String country = ctx.getResources().getConfiguration().locale.getCountry();
        return "CN".equalsIgnoreCase(country)? "zh"
                : "TW".equalsIgnoreCase(country)? "tw"
                : "HK".equalsIgnoreCase(country)? "tw"
                : "en";
    }

    private static boolean inChina(Context ctx) {
        return "CN".equalsIgnoreCase(ctx.getResources().getConfiguration().locale.getCountry());
    }

    @Override
    public int hashCode() {
        return code;
    }

    @NonNull
    @Override
    public String getPinyin() {
        if(pinyin == null) {
            pinyin = PinyinUtil.getPingYin(name);
        }
        return pinyin;
    }
}

