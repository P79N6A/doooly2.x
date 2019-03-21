package com.doooly.entity.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *@description 支付订单记录表
 *@author liangjun
 *@data 2018/5/24
 */
public class PayRecordDomain implements Serializable {

    /**支付类型，积分支付*/
    public static final int PAY_TYPE_0 = 0;
    /**支付类型，微信支付*/
    public static final int PAY_TYPE_1 = 1;
    /**支付类型，积分微信支付*/
    public static final int PAY_TYPE_2 = 2;

    /**积分支付方式 1 短信验证码*/
    public static final int INTEGERAL_PAY_TYPE_0 = 1;
    /**积分支付方式 2 支付密码*/
    public static final int INTEGERAL_PAY_TYPE_1 = 2;

    /**积分支付状态，未支付*/
    public static final int INTEGRAL_PAY_STATUS_0 = 0;
    /**积分支付状态，支付成功*/
    public static final int INTEGRAL_PAY_STATUS_1 = 1;
    /**积分支付状态，支付失败*/
    public static final int INTEGRAL_PAY_STATUS_2 = 2;
    /**积分支付状态，退款*/
    public static final int INTEGRAL_PAY_STATUS_3 = 3;

    /**第三方支付状态，未支付*/
    public static final int PAY_STATUS_0 = 0;
    /**第三方支付状态，支付成功*/
    public static final int PAY_STATUS_1 = 1;
    /**第三方支付状态，支付失败*/
    public static final int PAY_STATUS_2 = 2;
    /**第三方支付状态，退款*/
    public static final int PAY_STATUS_3 = 3;

    /**交易类型 兜礼公众号*/
    public static final String TRADE_TYPE_DOOOLY_JS = "DOOOLY_JS";
    /**交易类型 APP*/
    public static final String TRADE_TYPE_DOOOLY_APP = "DOOOLY_APP";
    /**交易类型 武钢公众号*/
    public static final String TRADE_TYPE_WISCO_JS = "WISCO_JS";
    /**交易类型 APP*/
    public static final String TRADE_TYPE_WISCO_APP = "WISCO_APP";

    private Long id;
    private String businessId;
    private String mobile;
    private String merchantName;
    private String merchantOrderNo;
    private String outTradeNo;
    /**订单总价格*/
    private BigDecimal orderPrice;
    /**订单总金额*/
    private BigDecimal orderAmount;
    private String orderBody;
    /**支付类型 0积分支付 1微信支付 2 积分微信支付*/
    private Integer payType;
    /**积分支付金额*/
    private BigDecimal integralPayAmount;
    /**积分抵扣金额*/
    private BigDecimal integralRebateAmount;
    /**积分抵扣后支付金额*/
    private BigDecimal integralRebatePayAmount;
    /**第三方支付金额*/
    private BigDecimal payAmount;
    private String notifyUrl;
    private String clientIp;
    private String orderIp;
    /**积分支付状态 0 未支付，1支付成功 2支付失败 3退款*/
    private Integer integralPayStatus;
    /**支付状态 0 未支付，1支付成功 2支付失败 3退款*/
    private Integer payStatus;
    /**交易类型 APP,H5*/
    private String tradeType;
    /**订单有效期(单位分钟)*/
    private Integer orderPeriod;
    /**订单到期时间*/
    private Timestamp expireTime;
    private String remark;
    /**退还积分次数*/
    private Integer integralRefundCount;
    /**积分退还总金额*/
    private BigDecimal integralRefundAmount;
    /**退款次数*/
    private Integer refundCount;
    /**成功退款总金额*/
    private BigDecimal refundAmount;
    /**支付返回码*/
    @JsonIgnore
    private String returnCode;
    @JsonIgnore
    private String returnMsg;
    @JsonIgnore
    private String resultCode;
    @JsonIgnore
    private String errCode;
    @JsonIgnore
    private String errCodeDes;
    @JsonIgnore
    private String appid;
    @JsonIgnore
    private String mchId;
    @JsonIgnore
    private String transactionId;
    private String storesId;
    private String orderDate;
    private String orderDetail;
    private String redirectUrl;
    private String bigOrderNumber;
    private String dirIntegralSwitch;
    private Timestamp createTime;
    private Timestamp payEndTime;
    private Timestamp updateTime;
    /**兜礼支付id*/
    private String payId;
    public PayRecordDomain() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderBody() {
        return orderBody;
    }

    public void setOrderBody(String orderBody) {
        this.orderBody = orderBody;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public BigDecimal getIntegralPayAmount() {
        return integralPayAmount;
    }

    public void setIntegralPayAmount(BigDecimal integralPayAmount) {
        this.integralPayAmount = integralPayAmount;
    }

    public BigDecimal getIntegralRebateAmount() {
        return integralRebateAmount;
    }

    public void setIntegralRebateAmount(BigDecimal integralRebateAmount) {
        this.integralRebateAmount = integralRebateAmount;
    }

    public BigDecimal getIntegralRebatePayAmount() {
        return integralRebatePayAmount;
    }

    public void setIntegralRebatePayAmount(BigDecimal integralRebatePayAmount) {
        this.integralRebatePayAmount = integralRebatePayAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getOrderIp() {
        return orderIp;
    }

    public void setOrderIp(String orderIp) {
        this.orderIp = orderIp;
    }

    public Integer getIntegralPayStatus() {
        return integralPayStatus;
    }

    public void setIntegralPayStatus(Integer integralPayStatus) {
        this.integralPayStatus = integralPayStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getOrderPeriod() {
        return orderPeriod;
    }

    public void setOrderPeriod(Integer orderPeriod) {
        this.orderPeriod = orderPeriod;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIntegralRefundCount() {
        return integralRefundCount;
    }

    public void setIntegralRefundCount(Integer integralRefundCount) {
        this.integralRefundCount = integralRefundCount;
    }

    public BigDecimal getIntegralRefundAmount() {
        return integralRefundAmount;
    }

    public void setIntegralRefundAmount(BigDecimal integralRefundAmount) {
        this.integralRefundAmount = integralRefundAmount;
    }

    public Integer getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(Integer refundCount) {
        this.refundCount = refundCount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getStoresId() {
        return storesId;
    }

    public void setStoresId(String storesId) {
        this.storesId = storesId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(Timestamp payEndTime) {
        this.payEndTime = payEndTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getBigOrderNumber() {
        return bigOrderNumber;
    }

    public void setBigOrderNumber(String bigOrderNumber) {
        this.bigOrderNumber = bigOrderNumber;
    }

    public String getDirIntegralSwitch() {
        return dirIntegralSwitch;
    }

    public void setDirIntegralSwitch(String dirIntegralSwitch) {
        this.dirIntegralSwitch = dirIntegralSwitch;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
