package com.doooly.dao.payment;

import com.doooly.entity.payment.AdShanghaiBank;

/**
 * 上海银行支持银行DAO接口
 * @author qing.zhang
 * @version 2018-05-28
 */
public interface AdShanghaiBankDao {

    AdShanghaiBank findByAccountBank(String subBranchNo);
}