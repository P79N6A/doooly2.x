package com.doooly.business.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 分页封装类
 * @author: qing.zhang
 * @date: 2017-05-18
 */
public class Pagelab implements Serializable {

    private static final long serialVersionUID = 5299705971683723875L;

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_CURRENT_PAGE = 1;

    public Pagelab() {
        this.setPageSize(DEFAULT_PAGE_SIZE);
        this.setCurrentPage(DEFAULT_CURRENT_PAGE);
    }

    public Pagelab(Integer currentPage, Integer pageSize) {
        currentPage = currentPage == null ? DEFAULT_CURRENT_PAGE : currentPage;
        pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        this.setPageSize(pageSize);
        this.setCurrentPage(currentPage);
    }

    /**
     * 开始索引(默认值0)
     */
    private Integer startIndex;
    /**
     * 当前页面
     */
    private Integer currentPage;
    /**
     * 分页大小(默认20条)
     * 分页查询最多只能50条数据
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer countPage = 0;
    /**
     * 总条数
     */
    private Integer totalNum;

    /**
     * 页内容
     */
    private transient List<? extends Object> dataList;

    public Integer getStartIndex() {
        return null == startIndex ? 0 : startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = null == currentPage ? 1 : currentPage;
        this.setStartIndex((this.currentPage - 1) * this.getPageSize());
    }

    public Integer getPageSize() {
        return pageSize;
    }

    //分页查询最多只能50条数据
    public void setPageSize(Integer pageSize) {
        pageSize = (null == pageSize || pageSize > 50 || pageSize < 0) ? DEFAULT_PAGE_SIZE : pageSize;
        this.pageSize = pageSize;
    }

    public Integer getCountPage() {
        return countPage;
    }

    public void setCountPage(Integer countPage) {
        this.countPage = countPage;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
        this.setCountPage((totalNum - 1) / this.getPageSize() + 1);
        this.currentPage = this.currentPage > this.countPage ? this.countPage : this.currentPage;
    }

    public List<? extends Object> getDataList() {
        return dataList;
    }

    public void setDataList(List<? extends Object> dataList) {
        this.dataList = dataList;
    }
}