package com.doooly.dao.payment;

import com.doooly.entity.payment.AdShanghaiBankAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 上海银行开户DAO接口
 * @author qing.zhang
 * @version 2018-05-28
 */
public interface AdShanghaiBankAccountDao{

    AdShanghaiBankAccount getAccount(@Param("cNName") String cNName);

    AdShanghaiBankAccount getBankAccount(AdShanghaiBankAccount adShanghaiBankAccount);

    void insert(AdShanghaiBankAccount adShanghaiBankAccount);

    void updateAmount(AdShanghaiBankAccount adShanghaiBankAccount);//虚账号查询余额后更新

    void updateAmountByNotice(AdShanghaiBankAccount adShanghaiBankAccount);//根据虚账号到账通知更新

    //根据虚账户账号类型金额更新，type值1代表增加 2代表减少
    void updateAmountByEAcctNo(@Param("eAcctNo") String eAcctNo, @Param("amount") BigDecimal amount, @Param("type") int type);
}