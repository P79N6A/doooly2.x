package com.doooly.dao.reachad;

import com.doooly.business.touristCard.datacontract.entity.SctcdAccount;
import com.doooly.business.touristCard.datacontract.entity.SctcdRechargeDetail;
import com.doooly.business.touristCard.datacontract.request.CountRechargeNumRequest;
import com.doooly.business.touristCard.datacontract.request.FindRechargeHistoryRequest;
import com.doooly.business.touristCard.datacontract.request.FindSctcdAccountRequest;
import com.doooly.entity.reachad.AdUserBusinessExpansion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 王晨宇 on 2018/1/18.
 */
public interface TouristCardDao {
	List<SctcdRechargeDetail> findRechargeHistory(FindRechargeHistoryRequest request);
	int countRechargeNum(CountRechargeNumRequest request);
	int addSctcdAccount(AdUserBusinessExpansion adUserBusinessExpansion);
	int updateSctcdAccountDelFlag(AdUserBusinessExpansion adUserBusinessExpansion);
	List<SctcdAccount> findSctcdAccount(FindSctcdAccountRequest request);
	List<SctcdAccount> findOneSctcdAccount(FindSctcdAccountRequest request);
	SctcdAccount findByUidBid(AdUserBusinessExpansion adUserBusinessExpansion);
	int updatePrivilege(@Param("id") Integer id, @Param("f15")String f15);

}
