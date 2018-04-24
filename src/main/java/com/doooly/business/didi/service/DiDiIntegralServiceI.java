package com.doooly.business.didi.service;

import com.doooly.dto.common.MessageDataBean;

import java.math.BigDecimal;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-03-06
 */
public interface DiDiIntegralServiceI {
    MessageDataBean getDiDiIntegral(Long businessId, Long userId);

    MessageDataBean exchangeIntegral(Long businessId, Long userId, BigDecimal amount, Integer code, String orderNumber);

    MessageDataBean toExchangeIntegral(Long businessId, Long userId);

    MessageDataBean getCode(Long businessId, Long userId, BigDecimal amount, String orderNumber);
}
