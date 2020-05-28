package com.jiachang.tv_launcher.fragment.mainfragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.utils.IPUtils;
import com.jiachang.tv_launcher.utils.ImageUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jiachang.tv_launcher.utils.Constants.API_KEY;
import static com.jiachang.tv_launcher.utils.Constants.hotelName;
import static com.jiachang.tv_launcher.utils.Constants.img;
import static com.jiachang.tv_launcher.utils.HttpUtils.netWorkCheck;

/**
 * @author Mickey.Ma
 * @date 2020-03-25
 * @description
 */
public class TopbarFragment extends Fragment {
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.day)
    TextView monthDay;
    @BindView(R.id.weather_txt)
    TextView weatherTxt;
    @BindView(R.id.weather_icon)
    ImageView mImageView;
    @BindView(R.id.welcome)
    TextView welcome;
    @BindView(R.id.hotel_icon)
    ImageView hotelIcon;

    private Unbinder mUb;
    protected Activity mActivity;

    /**
     * 天气的温度
     */
    private String temperature, text;
    private int code;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    weatherTxt.setText(temperature + "℃" + "\n" + "\n" + text);
                    if (code >= 0){
                        weatherIcon(code);
                    }else {
                        mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_unknown));
                    }
                    break;
                case 2:
                    getTime();
                    break;
                default:
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View topLayout = inflater.inflate(R.layout.main_top_bar, container, false);
        mUb = ButterKnife.bind(this, topLayout);
        initView();
        hotelName = mActivity.getApplicationContext()
                .getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS).getString("hotelName", "");
        img = mActivity.getApplicationContext()
                .getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS).getString("image","");
        return topLayout;
    }

    private void initView() {
        getTime();

        new Thread() {
            @Override
            public void run() {
                getWeather(IPUtils.getPubIp());
            }
        }.start();

        Log.d("TopbarFragment", "hotelName = " + hotelName);
        if (netWorkCheck(mActivity)) { //機器がネットワークに接続されているかどうかを判断する
            //网络已连接
            if (hotelName != null && !hotelName.isEmpty()) {
                welcome.setText("欢迎入住" + hotelName);
                if (img != null && !hotelName.isEmpty()) {
                    hotelIcon.setImageBitmap(ImageUtil.returnBitmap(img));
                }
            }
        } else {
            // ネットワーク未接続
            if (hotelName != null && !hotelName.isEmpty()) {
                welcome.setText("欢迎入住" + hotelName);
                if (img !=
                        null && !hotelName.isEmpty()) {
                    hotelIcon.setImageBitmap(ImageUtil.returnBitmap(img));
                }
            }
        }
        new WeatherThread().start(); //启动天气的线程
        new TimeThread().start(); //启动时间的线程
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity= null;
    }
    @Override
    public void onResume() {
        super.onResume();
        getTime();
    }

    private void getTime() {
        Calendar calendar = Calendar.getInstance();//取得当前时间的星期
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        Log.i("weekday", "" + week);
        long sysTime = System.currentTimeMillis();//获取系统时间
        if (sysTime != 0) {
            CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);//时间显示格式
            Log.i("sysTimeStr", "" + sysTimeStr);
            CharSequence sysDayStr = DateFormat.format("yyyy/MM/dd", sysTime);
            Log.i("sysDayStr", "" + sysDayStr);
            if (!sysTimeStr.equals(0) & !sysDayStr.equals(0)) {
                time.setText(sysTimeStr);
                monthDay.setText(sysDayStr);
            }
        } else {
            Toast.makeText(mActivity, "时间为空", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取天气
     */
    private void getWeather(String city) {
        String url = "https://api.seniverse.com/v3/weather/now.json?key=" + API_KEY + "&location=" + city + "&language=zh-Hans&unit=c";
        //异步加载
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    URLConnection connection = url.openConnection();//获取互联网连接
                    InputStream is = connection.getInputStream();//获取输入流
                    InputStreamReader isr = new InputStreamReader(is, "utf-8");//字节转字符，字符集是utf-8
                    BufferedReader bufferedReader = new BufferedReader(isr);//通过BufferedReader可以读取一行字符串
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        JSONObject json = JSONObject.parseObject(line);
                        String result = json.getString("results");
                        Log.i("result", result);
                        JSONArray array = json.getJSONArray("results");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            String now = jo.getString("now");
                            Log.i("now", now);
                            JSONObject jos = jo.getJSONObject("now");
                            temperature = jos.getString("temperature");
                            Log.i("temperature", temperature);
                            text = jos.getString("text");
                            Log.i("text", text);
                            code = Integer.parseInt(jos.getString("code"));
                            Log.i("code", "" + code);
                            if (temperature != null&& temperature.length() > 0
                                    && text != null && text.length() > 0) {
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = temperature;//传对象，还有arg1、arg2……
                                msg.obj = text;
                                msg.arg1 = code;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }
                    bufferedReader.close();
                    isr.close();
                    is.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            //使用api的数据接口
        }.execute(url);

    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(60000);
                    Message msg = new Message();
                    msg.what = 2;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    class WeatherThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                        Thread.sleep(3600000);
                        getWeather(IPUtils.getPubIp());
                        /*Message msg = new Message();
                        msg.what = 1;  //消息(一个整型值)
                        mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler*/
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }

    /**
     * 天气的图片
     */
    private void weatherIcon(int weatherCode) {
        try {
            switch (weatherCode) {
                case 0:
                case 2:
                case 38:
                    mImageView.setImageDrawable(mActivity.getDrawable(R.mipmap.weather_sunny));
                    break;
                case 1:
                case 3:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_clear));
                    break;
                case 4:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_cloudy));
                    break;
                case 5:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_partly_cloudy));
                    break;
                case 6:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_partly_cloudy_night));
                    break;
                case 7:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_mostly_cloudy));
                    break;
                case 8:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_mostly_cloudy_night));
                    break;
                case 9:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_overcast));
                    break;
                case 10:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_shower));
                    break;
                case 11:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_thundershower));
                    break;
                case 12:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_thundershower_with_hail));
                    break;
                case 13:
                case 19:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_light_rain));
                    break;
                case 14:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_moderaterain));
                    break;
                case 15:
                case 16:
                case 17:
                case 18:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_heavy_rain));
                    break;
                case 20:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_sleet));
                    break;
                case 21:
                case 22:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_light_snow));
                    break;
                case 23:
                case 24:
                case 25:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_moderate_snow));
                    break;
                case 26:
                case 27:
                case 28:
                case 29:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_dusty));
                    break;
                case 30:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_foggy));
                    break;
                case 31:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_haze));
                    break;
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_windy));
                    break;
                case 99:
                    mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity,R.mipmap.weather_unknown));
                    break;
                default:
            }

        } catch (NullPointerException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}