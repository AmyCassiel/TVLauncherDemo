package com.jiachang.tv_launcher.utils;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Locale;

/**
 * @author Mickey.Ma
 * @date 2020-04-01
 * @description
 */
public class IPUtils {
    /**
     * 获取公网IP地址
     */
    public static String getPubIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String line = "";
        String cname = "";
        try {
            infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    strber.append(line + "\n");
                }
                inStream.close();
                // 从反馈的结果中提取出IP地址
                int start = strber.indexOf("{");
                int end = strber.indexOf("}");
                String json = strber.substring(start, end + 1);
                if (json != null) {
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(json);
                        cname = jsonObject.optString("cname");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    LogUtils.e("提示", "IP接口异常，无法获取IP地址！");
                }
            }else {
                LogUtils.e("提示", "IP接口异常，无法获取IP地址！");

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cname;
    }
    /**
     * 获取有线网卡的MAC地址
     */
    public static String getLocalEthernetMacAddress() {
        String mac = null;
        try {
            Enumeration localEnumeration = NetworkInterface.getNetworkInterfaces();
            while (localEnumeration.hasMoreElements()) {
                NetworkInterface localNetworkInterface = (NetworkInterface) localEnumeration.nextElement();
                String interfaceName = localNetworkInterface.getDisplayName();
                if (interfaceName == null) {
                    continue;
                }

                if ("eth0".equals(interfaceName)) {
                    mac = convertToMac(localNetworkInterface.getHardwareAddress());
                    if (mac != null) {
                        mac = "00000AISPVUI00000000" + mac.toUpperCase(Locale.ENGLISH);
                        LogUtils.d("IPUtils.91","mac = "+ mac);
                    }
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return mac;
    }

    private static String convertToMac(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            byte b = mac[i];
            int value = 0;
            if (b >= 0 && b <= 16) {
                value = b;
                sb.append("0" + Integer.toHexString(value));
            } else if (b > 16) {
                value = b;
                sb.append(Integer.toHexString(value));
            } else {
                value = 256 + b;
                sb.append(Integer.toHexString(value));
            }
        }
        return sb.toString();
    }
}
