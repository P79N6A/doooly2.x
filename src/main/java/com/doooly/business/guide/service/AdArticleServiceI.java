package com.doooly.business.guide.service;

import com.doooly.dto.common.MessageDataBean;

/**
 * @Description: 导购
 * @author: qing.zhang
 * @date: 2018-02-26
 */
public interface AdArticleServiceI {

    MessageDataBean getGuideProductList(String orderType, Integer currentPage, Integer pageSize, String userId);

    MessageDataBean getArticleProductList(String articleId);

    MessageDataBean getArticleList();

    MessageDataBean addSellCount(String productId);

}
