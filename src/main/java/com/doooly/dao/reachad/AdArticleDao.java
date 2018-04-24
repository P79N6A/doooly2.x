/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdArticle;
import com.doooly.entity.reachad.AdProduct;

import java.util.List;
import java.util.Map;

/**
 * 导购文章DAO接口
 * @author qing.zhang
 * @version 2018-02-26
 */
public interface AdArticleDao  {

    List<AdProduct> getArticleProductList(String articleId);//查询导购文章商品列表

    AdArticle getNewArticle();//查询最新上架文章

    List<Map> getArticleList(String articleId);//首页数据
}