package com.click.cn.api;

import android.util.Log;
import android.widget.Toast;

import com.click.cn.MyApplication;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public  abstract  class ApiCallBack<T> implements Callback<T>{
    private String TAG = "ApiRequest";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (200 == response.code()){
            onSuccessful(call,response);
        }else {
            onFail(call,null,response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFail(call,t,null);
    }


    public abstract void onSuccessful(Call<T> call, Response<T> response);

    protected  void  onFail(Call<T> call , Throwable t, Response<T> response){
        if (null == response){
            Toast.makeText(MyApplication.getMainAppContext(),t.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e(TAG,"RESPONSE code is "+response.code()+": "+ response.raw().toString());
        if (null != response.errorBody()){
            //解析后台返回的错误信息
//            ErrorEntity errorEntity = new ErrorEntity();
//            try {
//                errorEntity = ErrorEntity.parse(response.errorBody().string());
//            } catch (IOException e) {
//                Log.e(TAG, "ErrorEntity解析错误:" + e.getMessage());
//            }
//            String message;
//            if (errorEntity.getErrorMessage() != null) {
//                message = errorEntity.getErrorMessage();
//            }else {
//                message ="账号已过期，请重新登录";
//            }
            // errorEntity.getErrorCode() 获取后台返回的 errorCode，根据 errorCode 前端做相应的处理
        }
    }
}

