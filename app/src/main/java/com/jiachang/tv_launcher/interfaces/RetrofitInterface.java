package com.jiachang.tv_launcher.interfaces;

import com.jiachang.tv_launcher.bean.BaseUrlBean;
import com.jiachang.tv_launcher.bean.FoodListRequestBean;
import com.jiachang.tv_launcher.bean.HotelInfoBean;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface RetrofitInterface {

    /**
     * 获取酒店和房间
     */
    @POST("/reservation/api/hic/serHotelInfo/get")
    Observable<HotelInfoBean> getHotel(
            @Query("cuid") String cuid
    );

    /**
     * 获取房间设备详情列表（用于查询设备信号和设备是否在线）
     */
    @FormUrlEncoded
    @POST("/reservation/api/hic/cuid/roomNum")
    Observable<ResponseBody> getRoomNum(
            @Field("cuid") String cuid
    );

    /**
     * 获取酒店食物列表
     */
    @POST("/reservation/api/hic/deliveryType/serDetail")
    Observable<FoodListRequestBean> getFoodBean(
            @Query("cuid") String cuid,
            @Query("deliveryType") int id
    );

    /**
     * 推送物品到前台
     * */
    @POST("/reservation/api/hic/service/mealDelivery")
    Observable<ResponseBody> sendGoods(
            @Query("cuid") String cuid,
            @Body RequestBody serviceRooms
    );

    @POST("/reservation/api/hic/cuid/token")
    Observable<ResponseBody> queryDevice(
            @Query("cuid") String cuid
    );

    /*
     * 获取BaseUrl
     * */
    @FormUrlEncoded
    @POST
    Observable<BaseUrlBean> getBaseUrl(
            @Url String url,
            @Field("rs") String getdturl
    );

    @FormUrlEncoded
    @POST("/App/c/sys/callb.php")
    Observable<ResponseBody> getAllDevice(
            @Query("url") String url,
            @Query("hictoken") String hictoken,
            @Field("rs") String getDevListJson
    );
}
