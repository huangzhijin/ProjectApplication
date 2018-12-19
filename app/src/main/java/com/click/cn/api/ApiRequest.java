package com.click.cn.api;





import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 网络请求配置
 */
public class ApiRequest {


    private static final Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static ApiService apiService;

    public static ApiService getServiceApi() {
        if (apiService == null) {
            apiService = getRetrofit(Api.ENDPOINT).create(ApiService.class);
        }
        return apiService;
    }


    //配置cookie
//    private static ClearableCookieJar cookieJar =
//            new PersistentCookieJar(new SetCookieCache(),
//                    new SharedPrefsCookiePersistor(BPApplication.getAppContext()));

    // 配置OKhttp日志
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            Log.i("RetrofitLog","retrofit log = "+ message);
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY);


    //配置client
    private static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
//            .cookieJar(cookieJar)
            .build();

    private static Retrofit getRetrofit(String baseUrl) {

        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build();
    }



    private static class LogInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.i("RetrofitLog--> ", "request:" + request.toString());
            okhttp3.Response response = chain.proceed(chain.request());
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Log.i("RetrofitLog--> ", "response body:" + content);
            if (response.body() != null) {
                ResponseBody body = ResponseBody.create(mediaType, content);
                return response.newBuilder().body(body).build();
            } else {
                return response;
            }
        }
    }
}
