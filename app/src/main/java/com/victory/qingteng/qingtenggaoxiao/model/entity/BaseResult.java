package com.victory.qingteng.qingtenggaoxiao.model.entity;

import com.victory.qingteng.qingtenggaoxiao.model.api.CommonApi;

public class BaseResult<T> {

    private String msg;

    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess(){
        return CommonApi.RequestSuccess.equals(msg);
    }
}
