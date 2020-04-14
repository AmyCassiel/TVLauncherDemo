package com.jiachang.tv_launcher.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;

import static android.provider.Telephony.Mms.Part.CHARSET;

/**
 * @author Mickey.Ma
 * @date 2020-03-24
 * @description
 */
public class HttpUtil {
    /**
     * 发送Get请求到服务器
     * @param strUrlPath:接口地址（带参数）
     * @return
     */
    public static String submitGetData(String strUrlPath) {
        String strResult = "";
        try {
            URL url = new URL(strUrlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setUseCaches(true);
            //添加header头信息以便服务器辨别来自于哪个前端请求
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("CLIENT-TYPE", "a");
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            strResult = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResult;
    }

    /**
     * 获取有线网卡的MAC地址
     * 第一种方法
     */
    public static String getWireMac(){
        String strMacAddress = null;
        try {
            byte[] b = NetworkInterface.getByName("eth0")
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
//                    buffer.append(':');
                }
                System.out.println("b:"+(b[i]&0xFF));
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddress = buffer.toString().toUpperCase();
            Log.d("TAG",strMacAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strMacAddress;
    }

    /**第二种方法*/
    public static String getLocalEthernetMacAddress() {
        String mac=null;
        try {
            Enumeration localEnumeration=NetworkInterface.getNetworkInterfaces();
            Log.e("localEnumeration = ",""+localEnumeration);
            while (localEnumeration.hasMoreElements()) {
                NetworkInterface localNetworkInterface=(NetworkInterface) localEnumeration.nextElement();
                String interfaceName=localNetworkInterface.getDisplayName();
                Log.e("interfaceName = ",interfaceName);
                if (interfaceName==null) {
                    continue;
                }

                if ("eth0".equals(interfaceName)) {
                    // MACAddr = convertMac(localNetworkInterface
                    // .getHardwareAddress());
//                    mac = localNetworkInterface.getHardwareAddress().toString();
                    mac=convertToMac(localNetworkInterface.getHardwareAddress());
                    if (mac!=null) {//&&mac.startsWith("0:")

                        mac="00000AISPVUI00000000"+mac.toUpperCase(Locale.ENGLISH);
                        Log.e("mac = ",mac);
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
        StringBuilder sb=new StringBuilder();
        for (int i=0; i<mac.length; i++) {
            byte b=mac[i];
            int value=0;
            if (b>=0&&b<=16) {
                value=b;
                sb.append("0"+Integer.toHexString(value));
            } else if (b>16) {
                value=b;
                sb.append(Integer.toHexString(value));
            } else {
                value=256+b;
                sb.append(Integer.toHexString(value));
            }
            /*if (i!=mac.length-1) {
                sb.append(":");
            }*/
        }
        return sb.toString();
    }


    /**获取无线的mac地址*/
    /**
     * Android 6.0 之前（不包括6.0）获取mac地址
     * 必须的权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     * @param context * @return
     */
    public static String getMacDefault(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        Log.e("mac = ",mac);
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
            Log.e("mac = ",mac);
        }
        return mac;
    }

    /**
     * Android 6.0-Android 7.0 获取mac地址
     */
    public static String getMacAddress() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            while (null != str) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();//去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }

        return macSerial;
    }

    /**
     * Android 7.0之后获取Mac地址
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     * @return
     */
    public static String getMacFromHardware() {
        try {
            Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (interfaceEnumeration.hasMoreElements()){
                NetworkInterface networkInterface = interfaceEnumeration.nextElement();
                byte[] addr = networkInterface.getHardwareAddress();
                if (addr==null || addr.length==0){
                    continue;
                }
                StringBuilder buf = new StringBuilder();
                for (byte b: addr){
                    buf.append(String.format("%02X:",b));
                }
                if (buf.length()>0){
                    buf.deleteCharAt(buf.length()-1);

                }
                return buf.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
