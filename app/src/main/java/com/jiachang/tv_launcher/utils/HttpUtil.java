package com.jiachang.tv_launcher.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;

import static android.provider.Telephony.Mms.Part.CHARSET;

/**
 * @author Mickey.Ma
 * @date 2020-03-24
 * @description
 */
public class HttpUtil {
    /**
     * 以GET方式的请求，并返回结果字符串。
     *
     * @param url 请求地址
     * @return 如果失败，返回为null
     */
    public static String get(String url) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            URI uri = new URI(url);
            HttpGet get = new HttpGet(uri);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                StringBuffer buffer = new StringBuffer();
                InputStream in = null;
                try {
                    in = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, CHARSET));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    reader.close();
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
                return buffer.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
}
