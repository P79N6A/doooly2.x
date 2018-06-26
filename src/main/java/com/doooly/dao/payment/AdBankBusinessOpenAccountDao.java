package com.doooly.dao.payment;

import com.doooly.entity.payment.AdBankBusinessOpenAccount;

/**
 * 供应商开户表
 * @author qing.zhang
 * @version 2018-05-31
 */
public interface AdBankBusinessOpenAccountDao  {

    AdBankBusinessOpenAccount findByBusinessId(Integer businessId);
}