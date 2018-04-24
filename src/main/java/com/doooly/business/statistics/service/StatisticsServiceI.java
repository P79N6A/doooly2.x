package com.doooly.business.statistics.service;

import com.doooly.dto.statistics.MemberDataRes;
import com.doooly.dto.statistics.MemberTodayRes;
import com.doooly.dto.statistics.OrderDataRes;
import com.doooly.dto.statistics.SiteStatisticsDataRes;

public interface StatisticsServiceI {

	
	/**获取会员近两周和近六个月数据*/
	public MemberDataRes getMemberDataRes();
	
	/**获取会员近两周和近六个月数据*/
	public MemberTodayRes getMemberTodayRes();
	
	/**获取订单近两周(包括今天)和近六个月数据*/
	public OrderDataRes getOderData();
	/**获取站点统计近两周(包括今天)和近六个月数据*/
	public SiteStatisticsDataRes SiteStatisticsData();
}
