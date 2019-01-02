package com.doooly.entity.activity;

import java.util.Date;

/**
 * 运营活动表(ActActivityRecord)实体类
 *
 * @author Mr_Wu
 * @since 2018-12-30 11:29:19
 */
public class ActActivityRecord{
    
    private Long id;
    //活动唯一标识，用于H5传入-确定唯一活动
    private String actKey;
    //活动名称
    private String actName;
    //1:抽奖活动 
    private Integer actType;
    //活动开始时间
    private Date beginDate;
    //活动结束时间
    private Date endDate;
    //0:下架 2：上架
    private Integer state;
    //库存总数量
    private Integer stockTotal;
    //剩余数量
    private Integer stockAvail;
    //更新时间
    private Date updateDate;
    
    private Date createDate;
    //创建人
    private String createBy;
    //修改人
    private String updateBy;

    private String groupId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActKey() {
        return actKey;
    }

    public void setActKey(String actKey) {
        this.actKey = actKey;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public Integer getActType() {
        return actType;
    }

    public void setActType(Integer actType) {
        this.actType = actType;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(Integer stockTotal) {
        this.stockTotal = stockTotal;
    }

    public Integer getStockAvail() {
        return stockAvail;
    }

    public void setStockAvail(Integer stockAvail) {
        this.stockAvail = stockAvail;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}