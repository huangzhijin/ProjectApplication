package com.click.cn.base;

public class Result<T> {

    private int statusCode;
    private String statusMessage;
    private T data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public T getData() {
        return data;
    }
}
