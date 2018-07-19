package com.doooly.business.myorder.dto;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 订单返回结果封装
 * @author: qing.zhang
 * @date: 2018-07-11
 */
public class OrderResult {
    /**
     * 结果数据。map／array
     */
    private List<Map> data;

    /**
     * 订单结果数据。map／array
     */
    private List<Map> orderData;

    /**
     * 如果data＝map，page＝null
     */
    private Map iPage;

    private Map status;

    public List<Map> getData() {
        return data;
    }

    public void setData(List<Map> data) {
        this.data = data;
    }

    public List<Map> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<Map> orderData) {
        this.orderData = orderData;
    }

    public Map getiPage() {
        return iPage;
    }

    public void setiPage(Map iPage) {
        this.iPage = iPage;
    }

    public Map getStatus() {
        return status;
    }

    public void setStatus(Map status) {
        this.status = status;
    }

    public void error(OrderResult orderResult) {
        Map status = new HashMap();
        status.put("code",0);// 返回码
        status.put("msg", "ok");// 返回信息
        orderResult.setStatus(status);
    }


    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("iPage",iPage);
        jsonObject.put("status",status);
        jsonObject.put("data",data);
        jsonObject.put("orderData",orderData);
        return jsonObject.toJSONString();
    }

    public static void main(String[] args) {
        OrderResult orderResult = new OrderResult();
        Map status = new HashMap();
        status.put("code",0);// 返回码
        status.put("msg", "ok");// 返回信息
        orderResult.setStatus(status);
        List<Map> orderData = new ArrayList<>();
        Map map = new HashMap();
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map.put("id",1);
        map1.put("id2",1);
        map2.put("id3",1);
        orderData.add(map);
        orderData.add(map1);
        orderData.add(map2);
        orderResult.setOrderData(orderData);
        String json = orderResult.toJsonString();
        System.out.println(json);
    }
}
