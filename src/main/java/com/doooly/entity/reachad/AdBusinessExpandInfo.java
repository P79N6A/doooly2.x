package com.doooly.entity.reachad;

/**
 * @Description: 商户拓展信息表
 * @author: qing.zhang
 * @date: 2017-08-28
 */
public class AdBusinessExpandInfo {

    private Long id ;
    private Long businessId;//商户主表编号
    private String shopId;//兜礼访问商家唯一标识
    private String shopKey;//兜礼访问商家密钥32位
    private String businessUrl;//商家接口地址
    private Integer type;//对接类型 默认0，我方提供对接方式 ，1内购网专属
    /**兜礼访问商家唯一标识*/
    private String clientId;
    /**兜礼访问商家密钥32位*/
    private String clientSecret;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopKey() {
        return shopKey;
    }

    public void setShopKey(String shopKey) {
        this.shopKey = shopKey;
    }


    public String getBusinessUrl() {
        return businessUrl;
    }

    public void setBusinessUrl(String businessUrl) {
        this.businessUrl = businessUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}