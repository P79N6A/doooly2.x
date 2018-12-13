package com.doooly.entity.meituan;

import com.doooly.common.util.RandomUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wanghai on 2018/12/13.
 */
public class EasyLogin {

    private String productType = "mt_waimai";

    private String nounce;

    private String appKey = "3Q2NCPEYQW";

    private long requestTime;

    private String signature;

    //商企通id
    private int entId = 27898;

    //员工工号
    private String staffNo;

    //员工手机号
    private String staffPhoneNo;

    //商户登录token
    private String entToken;

    public EasyLogin() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = sdf.format(new Date());
        requestTime = Long.parseLong(timeStr);
        nounce = RandomUtil.getRandomStr(36);
    }


    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getNounce() {
        return nounce;
    }

    public void setNounce(String nounce) {
        this.nounce = nounce;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getEntId() {
        return entId;
    }

    public void setEntId(int entId) {
        this.entId = entId;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getStaffPhoneNo() {
        return staffPhoneNo;
    }

    public void setStaffPhoneNo(String staffPhoneNo) {
        this.staffPhoneNo = staffPhoneNo;
    }

    public String getEntToken() {
        return entToken;
    }

    public void setEntToken(String entToken) {
        this.entToken = entToken;
    }
}
