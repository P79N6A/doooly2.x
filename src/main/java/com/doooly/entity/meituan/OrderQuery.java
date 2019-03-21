package com.doooly.entity.meituan;


import com.doooly.common.meituan.ContentParams;

/**
 * Created by Administrator on 2018/12/19.
 */
public class OrderQuery extends ContentParams {

    private String orderSN;

    private long fromTime;

    private long toTime;

    private int status;

    //渠道订单号
    private String channelOrderId;

    public String getChannelOrderId() {
        return channelOrderId;
    }

    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    public String getOrderSN() {
        return orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public long getFromTime() {
        return fromTime;
    }

    public void setFromTime(long fromTime) {
        this.fromTime = fromTime;
    }

    public long getToTime() {
        return toTime;
    }

    public void setToTime(long toTime) {
        this.toTime = toTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
