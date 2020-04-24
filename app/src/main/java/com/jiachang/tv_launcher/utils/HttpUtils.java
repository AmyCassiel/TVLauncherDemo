package com.jiachang.tv_launcher.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static android.provider.Telephony.Mms.Part.CHARSET;

/**
 * @author Mickey.Ma
 * @date 2020-04-21
 * @description
 */
public class HttpUtils {

    private static String HTTP_TAG = "HttpUtil";

    // 网络连接判断
    public static boolean netWorkCheck(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            return info.isConnected();
        } else {
            return false;
        }
    }

    public static String post(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpUtils.post(requestUrl, accessToken, contentType, params);
    }


    public static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return HttpUtils.post(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return HttpUtils.postGeneralUrl(url, contentType, params, encoding);
    }

    public static String urlpost(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpUtils.urlpost(requestUrl, accessToken, contentType, params);
    }

    public static String urlpost(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        /*if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }*/
        return HttpUtils.urlpost(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String urlpost(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?hictoken=" + accessToken;
        return HttpUtils.postGeneralUrl(url, contentType, params, encoding);
    }

    /*获取postURL*/
    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        System.err.println("result:" + result);
        return result;
    }

    public static String mpost(String url, Map<String, Object> map) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            Object value = map.get(key);
            if (value instanceof String[]) {
                String[] strings = (String[]) value;
                for (String temp : strings) {
                    params.add(new BasicNameValuePair(key, temp));
                }
            } else {
                params.add(new BasicNameValuePair(key, value.toString()));
            }
        }
        UrlEncodedFormEntity uefEntity;
        String resData = null;
        try {
            uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(uefEntity);
            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            resData = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resData;
    }


    public HttpUtils() {
        // TODO Auto-generated constructor stub
    }

    public static String getJsonContent(String url_path) {
        try {
            URL url = new URL(url_path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            int code = connection.getResponseCode();
            if (code == 200) {
                return changeInputStream(connection.getInputStream());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }


    private static String changeInputStream(InputStream inputStream) {
        // TODO Auto-generated method stub
        String jsonString = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] data = new byte[1024];
        try {
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);

            }
            jsonString = new String(outputStream.toByteArray());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return jsonString;

    }

    /**
     * 获得值
     *
     * @param key 键
     * @return 值
     */
    public static String getConfig(String key) {
        Properties p;
        p = new Properties();
        FileInputStream fis = null;
        URL url;
        url = HttpUtils.class.getClassLoader().getResource("config.properties");
        try {
            fis = new FileInputStream(url.getPath());
            p.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return p.getProperty(key);
    }

    /**
     * 提交数据
     *
     * @param url
     * @param map
     * @return
     */
    public static String mPost(String url, Map<String, Object> map) {
        Map<String, String> headerMap = new HashMap();
//        headerMap.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjM5MzQ1NzgzMzgsImFwcElkIjoiN1YzZHh5aUtzOFdteG1iNmF4aW9idTFlZHRhaHVSWlMiLCJ1dWlkIjoxNjQ4ODc4NzgxNTgwNTMzMjM1fQ.wEMJUPwsJE1HhJsKOR-5_87TueqoKeyyEDL4hB5iVpU");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            Object value = map.get(key);
            if (value instanceof String[]) {
                String[] strings = (String[]) value;
                for (String temp : strings) {
                    params.add(new BasicNameValuePair(key, temp));
                }
            } else {
                params.add(new BasicNameValuePair(key, value.toString()));
            }
        }
        System.out.println(params);
        UrlEncodedFormEntity uefEntity;
        String resData = null;
        try {
            uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(uefEntity);
            if (!headerMap.isEmpty()) {
                for (Map.Entry<String, String> vo : headerMap.entrySet()) {
                    httpPost.setHeader(vo.getKey(), vo.getValue());
                }
            }

            System.out.println("httpPost = " + httpPost);
            HttpResponse httpResponse;
            try {
                httpResponse = httpClient.execute(httpPost);
                Log.i(HTTP_TAG, "httpResponse = " + httpResponse);
                HttpEntity entity = httpResponse.getEntity();
                resData = EntityUtils.toString(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resData;
    }

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

    public static String httpPostWithjson(String url, String json) {
        String result = "";
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            BasicResponseHandler handler = new BasicResponseHandler();
            StringEntity entity = new StringEntity(json, CHARSET);//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            result = httpClient.execute(httpPost, handler);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
