package com.jiachang.tv_launcher.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jiachang.tv_launcher.bean.FoodItemUploadBean;
import com.jiachang.tv_launcher.bean.FormFileUploadBean;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.provider.Telephony.Mms.Part.CHARSET;

/**
 * @author Mickey.Ma
 * @date 2020-04-21
 * @description
 */
public class HttpUtils {

    private static String HTTP_TAG = "HttpUtil";

    public static void sendFood(Context context,String mac, List<FoodItemUploadBean> foodItems){
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(foodItems));
        ApiRetrofit.initRetrofit(Constant.hostUrl)
                .sendGoods(mac, body)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        Toast.makeText(context,"订购成功！",Toast.LENGTH_LONG).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(context,"订购失败，请联系前台！",Toast.LENGTH_LONG).show();
                        Log.e("FoodListActivity", Objects.requireNonNull(throwable.getMessage()));
                    }
                });
    }

    /**网络连接判断*/
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

    public static String httpPut(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        int code = conn.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        Log.e("code=" ,""+ conn.getResponseCode());
        StringBuffer buf = new StringBuffer();
        String inputLine = in.readLine();
        while (inputLine != null) {
            buf.append(inputLine).append("\r\n");
            inputLine = in.readLine();
        }
        in.close();
        //
        return (buf.toString().trim()) != null ? (buf.toString().trim()) : (code + "");
    }

    public static JSONObject httpPost(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuffer buf = new StringBuffer();
        String inputLine = in.readLine();
        while (inputLine != null) {
            buf.append(inputLine).append("\r\n");
            inputLine = in.readLine();
        }
        in.close();
        //
        return new JSONObject(buf.toString().trim());
    }

    public static String httpPostApplication(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuffer buf = new StringBuffer();
        String inputLine = in.readLine();
        while (inputLine != null) {
            buf.append(inputLine).append("\r\n");
            inputLine = in.readLine();
        }
        in.close();
        //
        return buf.toString().trim();
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
            HttpResponse httpResponse;
            try {
                httpResponse = httpClient.execute(httpPost);
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

    /**
     * 提交文件及参数到服务器
     * @param actionUrl 为要使用的URL
     * @param params 为表单内容
     * @param  files 要上传的文件，可以上传多个文件，这根据需要页定*/
    public static boolean post(String actionUrl,Map<String, String> params,Map<String, File> files) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000);
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false);
        conn.setRequestMethod("POST"); // Post方式
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);
        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\""
                    + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }
        DataOutputStream outStream = new DataOutputStream(
                conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        // 发送文件数据
        if (files != null){
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: multipart/form-data; charset="
                        + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());
            }
        }
        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
        // 得到响应码
        boolean success = conn.getResponseCode()==200;
        InputStream in = conn.getInputStream();
        InputStreamReader isReader = new InputStreamReader(in);
        BufferedReader bufReader = new BufferedReader(isReader);
        String line = null;
        String data = "getResult=";
        while ((line = bufReader.readLine()) != null){
            data += line;
        }
        outStream.close();
        conn.disconnect();
        return success;
    }


    /**上传日志文件*/
    /**
     * 提交数据到服务器
     * @param path 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file 上传文件
     */
    public static boolean post(String path, Map<String, String> params, FormFileUploadBean file) throws Exception{
        return post(path, params, new FormFileUploadBean[]{file});
    }

    /**
     * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:
     *  <FORM METHOD=POST ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet" enctype="multipart/form-data">
     <INPUT TYPE="text" NAME="name">
     <INPUT TYPE="text" NAME="id">
     <input type="file" name="imagefile"/>
     <input type="file" name="zip"/>
     </FORM>
     * @param path 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param files 上传文件
     */
    public static boolean post(String path, Map<String, String> params, FormFileUploadBean[] files) throws Exception{
        final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志

        int fileDataLength = 0;
        for(FormFileUploadBean uploadFile : files){//得到文件类型数据的总长度
            StringBuilder fileExplain = new StringBuilder();
            fileExplain.append("--");
            fileExplain.append(BOUNDARY);
            fileExplain.append("\r\n");
            fileExplain.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
            fileExplain.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
            fileExplain.append("\r\n");
            fileDataLength += fileExplain.length();
            if(uploadFile.getInStream()!=null){
                fileDataLength += uploadFile.getFile().length();
            }else{
                fileDataLength += uploadFile.getData().length;
            }
        }
        StringBuilder textEntity = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
            textEntity.append("--");
            textEntity.append(BOUNDARY);
            textEntity.append("\r\n");
            textEntity.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
            textEntity.append(entry.getValue());
            textEntity.append("\r\n");
        }
        //计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;

        URL url = new URL(path);
        int port = url.getPort()==-1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        OutputStream outStream = socket.getOutputStream();
        //下面完成HTTP请求头的发送
        String requestmethod = "POST "+ url.getPath()+" HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        String contenttype = "Content-Type: multipart/form-data; boundary="+ BOUNDARY+ "\r\n";
        outStream.write(contenttype.getBytes());
        String contentlength = "Content-Length: "+ dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        String host = "Host: "+ url.getHost() +":"+ port +"\r\n";
        outStream.write(host.getBytes());
        //写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        //把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());
        //把所有文件类型的实体数据发送出来
        for(FormFileUploadBean uploadFile : files){
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append(BOUNDARY);
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
            fileEntity.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
            outStream.write(fileEntity.toString().getBytes());
            if(uploadFile.getInStream()!=null){
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = uploadFile.getInStream().read(buffer, 0, 1024))!=-1){
                    outStream.write(buffer, 0, len);
                }
                uploadFile.getInStream().close();
            }else{
                outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
            }
            outStream.write("\r\n".getBytes());
        }
        //下面发送数据结束标志，表示数据已经结束
        outStream.write(endline.getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if(reader.readLine().indexOf("200")==-1){//读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
            return false;
        }
        outStream.flush();
        outStream.close();
        reader.close();
        socket.close();
        return true;
    }
}
