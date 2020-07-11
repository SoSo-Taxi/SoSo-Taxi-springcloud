package com.apicaller.sosotaxi.entity;

/**
 * 返回的消息
 * @param <T> 返回数据的类型
 */
public class Result<T> {
    //返回信息
    private String msg;
    //是否是正常返回
    private Boolean success;
    //返回的数据
    private T detail;

    public String getMsg(){
        return msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getDetail() {
        return detail;
    }

    public void setDetail(T detail) {
        this.detail = detail;
    }
}
