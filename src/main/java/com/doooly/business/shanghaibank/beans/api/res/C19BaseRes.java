package com.doooly.business.shanghaibank.beans.api.res;

/**
 * @Description: 上海银行接口返回基础类
 * @author: qing.zhang
 * @date: 2018-05-28
 */
public class C19BaseRes<T> {

    private String resultCode;//返回状态码
    private String resultDesc;//返回描述
    private T data;//返回结果数据

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public C19BaseRes() {
        super();
    }


    public C19BaseRes(String resultCode, String resultDesc, T data) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.data = data;
    }
}
