/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;

import com.doooly.business.pay.bean.AdOrderFlow;

import java.util.List;

public interface AdOrderFlowDao {
	
	int insert(AdOrderFlow adOrderFlow);

    List<AdOrderFlow> findListByAdOrderReport(AdOrderFlow adOrderFlowQuery);

}