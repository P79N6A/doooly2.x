package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doooly.business.statistics.service.StatisticsServiceI;
import com.doooly.dto.statistics.MemberDataRes;
import com.doooly.dto.statistics.MemberTodayRes;
import com.doooly.dto.statistics.OrderDataRes;
import com.doooly.dto.statistics.SiteStatisticsDataRes;
import com.doooly.publish.rest.life.TVStatisticsServiceI;
@Component
@Path("/tv")
public class TVStatisticsService implements TVStatisticsServiceI {
	@Autowired
	private StatisticsServiceI statisticsService;
	
	private static final Logger logger = LoggerFactory.getLogger(TVStatisticsService.class);  
	
	/**获取会员近两周和近六个月数据*/
	@POST
	@Path(value="/member/history")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String memberData(){
		MemberDataRes memberDataRes = statisticsService.getMemberDataRes();
		logger.info(memberDataRes.toJsonString());
		return memberDataRes.toJsonString();
		
	}

	/**获取会员近两周和近六个月数据*/
	@POST
	@Path(value="/member/today")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String memberTodayData() {
		MemberTodayRes memberTodayRes = statisticsService.getMemberTodayRes();
		logger.info(memberTodayRes.toJsonString());
		return memberTodayRes.toJsonString();
	}
	
	/**获取订单近两周和近六个月数据*/
	@POST
	@Path(value="/order/data")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String orderData() {
		OrderDataRes orderDataRes = statisticsService.getOderData();
		logger.info(orderDataRes.toJsonString());
		return orderDataRes.toJsonString();
	}
	
	/**获取站点统计近两周和近六个月数据*/
	@POST
	@Path(value="/module/data")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String SiteStatisticsData() {
		SiteStatisticsDataRes siteStatistics = statisticsService.SiteStatisticsData();
		logger.info(siteStatistics.toJsonString());
		return siteStatistics.toJsonString();
	}
}
