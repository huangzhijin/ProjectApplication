package com.click.cn.api;


import com.click.cn.base.Result;
import com.click.cn.bean.LoginBean;


import org.green.greenlibrary.User;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public interface ApiService {

    @GET("users/{user}/repos")
    Call<String> getRank(@Path("user") String user);

    @GET(Api.LOGIN)
    Call<String> getRank2(@Query("key") String key , @Query("area") String area);


    @GET(Api.LOGIN)
    Call<String> getRank3(@QueryMap Map<String,String> params);


    @GET
    public Call<String> getRank4(@Url String url);

    //-------------------------------------------------------------------------

    @FormUrlEncoded
    @POST("login")
    Call<Result<LoginBean>> login(@Field("loginName") String username, @Field("verId") String verId);



    @FormUrlEncoded
    @POST("login")
    Call<LoginBean> login2(@FieldMap Map<String,String>params);


    @POST("/users/new")
    Call<LoginBean> createUser(@Body User user);

//---------------------------------------------------------------------------------------

//    @Multipart
//    @POST("user/imgUpLoad")
//    Call<String> uploadOne(@Part("sign") String sign,@Part("appKey") String appKey,@Part("osName") String osName,@Part("memberNo") String memberNo, @Part  MultipartBody.Part file);
//    Call<String> uploadOne(@PartMap Map<String,String> params, @Part  MultipartBody.Part file);

//    Call<String> uploadOne(@Query("sign") String sign, @Query("appKey") String appKey, @Query("osName") String osName, @Query("memberNo") String memberNo, @Part  MultipartBody.Part file);
//    Call<String> uploadOne(@QueryMap Map<String,String> params, @Part  MultipartBody.Part file);


    // 上传单个文件
    @Multipart
    @POST("upload")
    Call<LoginBean> uploadFile(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file);

    // 上传多个文件
    @Multipart
    @POST("upload")
    Call<LoginBean> uploadMultipleFiles(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2);

    /**
     * 登陆
     * @return
     */
//    @FormUrlEncoded
//    @POST(Api.LOGIN)
//    Call<LoginBean> login(@Field("username") String username, @Field("password") String password);



}
