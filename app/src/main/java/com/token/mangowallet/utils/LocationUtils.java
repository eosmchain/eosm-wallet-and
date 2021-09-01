package com.token.mangowallet.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;

import static com.token.mangowallet.utils.Constants.LOG_TAG;

public class LocationUtils {

    private static LocationUtils instance;
    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private Context mContext;
    private OnLocationListener listener;

    private LocationUtils(Context context) {
        this.mContext = context;
        getLocation();
    }

    public static LocationUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (LocationUtils.class) {
                if (instance == null) {
                    instance = new LocationUtils(context);
                }
            }
        }
        return instance;
    }

    private void getLocation() {
        //1.获取位置管理器
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            LogUtils.dTag(LOG_TAG, "如果是GPS定位");
            locationProvider = LocationManager.GPS_PROVIDER;
            if (listener != null) {
                listener.OnIsGPSOpen(true);
            }
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            LogUtils.dTag(LOG_TAG, "如果是网络定位");
            locationProvider = LocationManager.NETWORK_PROVIDER;
            if (listener != null) {
                listener.OnIsGPSOpen(true);
            }
        } else {
            LogUtils.dTag(LOG_TAG, "没有可用的位置提供器");
            locationProvider = LocationManager.PASSIVE_PROVIDER;
            if (listener != null) {
                listener.OnIsGPSOpen(true);
            }

        }
        //需要检查权限。
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location == null) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
            location = locationManager.getLastKnownLocation(locationProvider);
            if (location == null) {
                locationProvider = LocationManager.PASSIVE_PROVIDER;
                location = locationManager.getLastKnownLocation(locationProvider);
            }
        }
        if (location != null) {
            setLocation(location);
        }

        if (listener != null) {
            listener.OnLocation(location);
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
        locationManager.requestLocationUpdates(locationProvider, 1000, 1, locationListener);
    }

    /**
     * 返回查询条件
     *
     * @return
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(true);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    private void setLocation(Location location) {
        this.location = location;
        String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
        LogUtils.dTag(LOG_TAG, "address" + address);
    }

    //获取经纬度
    public Location showLocation() {
        return location;
    }

    // 移除定位监听
    public void removeLocationUpdatesListener() {
        // 需要检查权限,否则编译不过
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager != null) {
            instance = null;
            locationManager.removeUpdates(locationListener);
        }
        if (listener != null) {
            listener = null;
        }
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */
    LocationListener locationListener = new LocationListener() {

        /**
         * 当某个位置提供者的状态发生改变时
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
        }

        /**
         * 某个设备打开时
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /**
         * 某个设备关闭时
         */
        @Override
        public void onProviderDisabled(String provider) {
        }

        /**
         * 手机位置发生变动
         */
        @Override
        public void onLocationChanged(Location location) {
            location.getAccuracy();//精确度
            if (listener != null) {
                listener.OnLocation(location);
            }
            setLocation(location);
        }
    };

    public void setOnLocationListener(OnLocationListener listener) {
        this.listener = listener;
    }

    public interface OnLocationListener {
        void OnIsGPSOpen(boolean isGPSOpen);

        void OnLocation(Location location);
    }
}

//2.使用方法
//        Location location = GpsUtil.getInstance(getActivity()).showLocation();
//        if (location != null) {
//        Log.d("location", "高度" + location.getAltitude() + "纬度" + location.getLatitude() + "经度" + location.getLongitude() + "城市" + getAddress(location));
//        }
//
//        3.获取详细地址的方法，原生
//// 获取地址信息
//private List<Address> getAddress(Location location) {
//        List<Address> result = null;
//        try {
//        if (location != null) {
//        Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());
//        result = gc.getFromLocation(location.getLatitude(),
//        location.getLongitude(), 1);
//        }
//        } catch (Exception e) {
//        e.printStackTrace();
//        }
//        return result;
//        }
