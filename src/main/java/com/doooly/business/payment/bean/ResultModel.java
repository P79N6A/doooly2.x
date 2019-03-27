


package com.doooly.business.payment.bean;


import com.alibaba.fastjson.JSONObject;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;

/**
 * 自定义返回结果
 * Created by liangjun on 2017/3/17.
 */
public class ResultModel {
    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String info;

    /**
     * 返回内容
     */
    private Object data;

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public Object getData() {
        return data;
    }

    public ResultModel() {
    }

    public ResultModel(int code, String info) {
        this.code = code;
        this.info = info;
        this.data = "";
    }

    public ResultModel(int code, String info, Object data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }

    public ResultModel(GlobalResultStatusEnum status) {
        this.code = status.getCode();
        this.info = status.getInfo();
        this.data = "";
    }

    public ResultModel(GlobalResultStatusEnum status, Object result) {
        this.code = status.getCode();
        this.info = status.getInfo();
        this.data = result;
    }

    public ResultModel(GlobalResultStatusEnum status, String errorMsg) {
        this.code = status.getCode();
        this.info = errorMsg != null ? errorMsg : status.getInfo();
        this.data = null;
    }

    public static ResultModel ok(Object result) {
        return new ResultModel(GlobalResultStatusEnum.SUCCESS, result);
    }

    public static ResultModel success_ok(Object result) {
        return new ResultModel(GlobalResultStatusEnum.SUCCESS_OK, result);
    }

    public static ResultModel ok() {
        return new ResultModel(GlobalResultStatusEnum.SUCCESS);
    }

    public static ResultModel error(GlobalResultStatusEnum error) {
        return new ResultModel(error);
    }

    public String toJsonString() {
        JSONObject json = new JSONObject();
        json.put("code", this.getCode());
        json.put("msg", this.getInfo());
        json.put("data", data);
        return json.toJSONString();
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * 饿了么对接返回结果
     * @return
     */
    public String toELMString() {
        JSONObject json = new JSONObject();
        json.put("code", this.getCode());
        json.put("message", this.getInfo());
        json.put("data", data);
        return json.toJSONString();
    }
}
