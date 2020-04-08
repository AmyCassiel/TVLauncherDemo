package com.jiachang.tv_launcher.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import androidx.core.app.ActivityCompat;

/**
 * @author Mickey.Ma
 * @date 2020-03-25
 * @description
 */
public class LocationUtil {

    private static final String TAG = "LocationUtils";

    /**
     * http://ip-api.com/json/192.168.0.52?fields=520191&lang=en
     * 根据ip获取位置信息
     *
     * @param ip
     * @return {"accuracy":50,"as":"AS4538 China Education and Research Network Center",
     * "city":"Nanjing","country":"China","countryCode":"CN","isp":
     * "China Education and Research Network Center","lat":32.0617,"lon":118.7778,"mobile":false,
     * "org":"China Education and Research Network Center","proxy":false,"query":"58.192.32.1",
     * "region":"JS","regionName":"Jiangsu","status":"success","timezone":"Asia/Shanghai","zip":""}
     */
    public static String Ip2Location(String ip) {
        String city = null;
        String urlStr = "http://ip-api.com/json/" + ip + "?fields=520191&lang=en";
        Log.e(TAG, "Ip2Location: urlStr -- " + urlStr);
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);//读取超时
            urlConnection.setConnectTimeout(5000); // 连接超时
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //找到服务器的情况下,可能还会找到别的网站返回html格式的数据
                InputStream is = urlConnection.getInputStream();

                BufferedReader buff = new BufferedReader(new InputStreamReader(is, "UTF-8"));//注意编码，会出现乱码
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = buff.readLine()) != null) {
                    builder.append(line);
                }
                buff.close();//内部会关闭InputStream
                urlConnection.disconnect();

                String res = builder.toString();
                Log.e(TAG, "Ip2Location: res -- " + res);

                if (res != null) {
                    JSONObject jsonObject = new JSONObject(res);
                    city = jsonObject.optString("city");
                    Log.e(TAG, "Ip2Location: city -- " + city);
                }else {
                    Log.e(TAG, "未获取到数据！");
                }
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city;
    }


    /**
     * 根据ip通过百度api去获取城市
     *
     * @param ip
     * @return
     */
    public static String Ip2LocationByBaiduApi(String ip) {
        try {
            URL url = new URL("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ip);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = null;
            StringBuffer res = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                res.append(line);
            }
            reader.close();
            String ipAddr = res.toString();
            if (StringUtils.isJSONString(ipAddr)) {
                JSONObject jsonObject = new JSONObject(ipAddr);
                if ("1".equals(jsonObject.get("ret").toString())) {
                    String string = jsonObject.get("city").toString();
                    Log.e("geg", "city:" + string);
                    return string;
                } else {
                    return "读取失败";
                }

            } else {
                return "访问后得到的不是json数据, res -- " + ipAddr;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "读取失败 e -- " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "读取失败 e -- " + e.getMessage();
        } catch (JSONException e) {
            e.printStackTrace();
            return "读取失败 e -- " + e.getMessage();
        }
    }

    //百度地图申请的key
    public static String baiduKey = "TSg776uecpSG4xRHZvttYDe57u5au72q";

    public static String getLocation(Context mContext) {
        String locationProvider;
        //获取地理位置管理器
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Looper.prepare();
            Toast.makeText(mContext, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            Log.e("LocationUtil","没有可用的位置提供器");
            Looper.loop();
            return null;
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return null;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //经度和纬度
            return showLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        }
        return null;
    }

    public static String showLocation(String wd, String jd) {
        String path = "http://api.map.baidu.com/geocoder?output=json&location=" + wd + "," + jd + "&key=" + baiduKey;
        String locationStr = HttpUtil.submitGetData(path);
        Log.e("path-------", locationStr);
        if (!locationStr.equals("")) {
            return locationStr;
        }
        return null;
    }

}
