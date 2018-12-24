package com.doooly.business.guide.service;

import com.doooly.dto.common.MessageDataBean;

/**
 * @Description: 导购
 * @author: qing.zhang
 * @date: 2018-02-26
 */
public interface AdArticleServiceI {

    MessageDataBean getGuideProductList(String orderType, Integer currentPage, Integer pageSize, String userId);

    MessageDataBean getGuideProductListv2(String userId, String guideCategoryId, Integer currentPage, Integer pageSize, String recommendHomepage);

    MessageDataBean getArticleProductList(String articleId);

    MessageDataBean getArticleList();

    MessageDataBean getGuideCategaryList();

    MessageDataBean addSellCount(String productId);

}
