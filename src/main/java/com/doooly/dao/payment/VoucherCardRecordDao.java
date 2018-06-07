package com.doooly.dao.payment;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.persistence.annotation.MyBatisDao;
import com.doooly.entity.payment.VoucherCardRecord;
import com.doooly.entity.reachad.AdBusiness;

/**
 * @Auther: liangjun
 * @Date: 2018/5/29 10:46
 * @Description:
 */
@MyBatisDao
public interface VoucherCardRecordDao {

	VoucherCardRecord findByActivationCode(@Param("code")String code);

	void updateActiveData(VoucherCardRecord voucherCardRecord);

	List<VoucherCardRecord> findRechargeRecordsByUserId(@Param("userId")Long userId, @Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize);

	Integer getTotalCountByUserId(@Param("userId")Long userId);

	VoucherCardRecord checkCardPasswordData(@Param("cardPassword")String cardPassword);

	void updateRechargeData(VoucherCardRecord record);



}
