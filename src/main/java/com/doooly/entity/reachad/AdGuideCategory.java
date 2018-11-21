/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 导购类目
 * @author qing.zhang
 * @version 2018-02-26
 */
public class AdGuideCategory {

    private String id;
    private String guideCategoryId;//类目主键id ，防止ios关键字冲突
    private String categoryNum;//类目编号
    private String categoryName;//类目名称
    private String state;//类目状态(0:关闭，1：开启)
    private String sort;//类目排序
    private Date createDate;
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuideCategoryId() {
        return guideCategoryId;
    }

    public void setGuideCategoryId(String guideCategoryId) {
        this.guideCategoryId = guideCategoryId;
    }

    public String getCategoryNum() {
        return categoryNum;
    }

    public void setCategoryNum(String categoryNum) {
        this.categoryNum = categoryNum;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}