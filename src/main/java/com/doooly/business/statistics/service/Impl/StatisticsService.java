package com.doooly.business.statistics.service.Impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.statistics.service.StatisticsServiceI;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dao.reachlife.SiteStatisticsDao;
import com.doooly.dto.statistics.MemberDataRes;
import com.doooly.dto.statistics.MemberDataRes.ActiveBean;
import com.doooly.dto.statistics.MemberDataRes.FamilyBean;
import com.doooly.dto.statistics.MemberDataRes.MemberBean;
import com.doooly.dto.statistics.MemberDataRes.MonthBean;
import com.doooly.dto.statistics.MemberDataRes.SocietyBean;
import com.doooly.dto.statistics.MemberDataRes.StaffBean;
import com.doooly.dto.statistics.MemberDataRes.WeekBean;
import com.doooly.dto.statistics.MemberTodayRes;
import com.doooly.dto.statistics.OrderDataRes;
import com.doooly.dto.statistics.SiteStatisticsDataRes;
import com.doooly.dto.statistics.SiteStatisticsDataRes.BenefitBean;
import com.doooly.dto.statistics.SiteStatisticsDataRes.GiftBean;
import com.doooly.dto.statistics.SiteStatisticsDataRes.PrivilegeBean;
import com.doooly.dto.statistics.SiteStatisticsDataRes.PvBean;
import com.doooly.dto.statistics.SiteStatisticsDataRes.RobBean;
import com.doooly.dto.statistics.SiteStatisticsDataRes.UserBean;
import com.doooly.dto.statistics.SiteStatisticsDataRes.UvBean;

@Service
public class StatisticsService implements StatisticsServiceI{
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private SiteStatisticsDao siteStatisticsDao;
	
	private int STAFF = 0;
	
	private int FAMILY = 1;
	
	private int SOCIETY = 4;
	
	/**获取会员数据*/
	@Override
	public MemberDataRes getMemberDataRes() {
		/****获取memberData对象start*****/
		MemberDataRes memberDataRes = new MemberDataRes();
		StaffBean staffActiveBean = new StaffBean();
		/**获取员工激活数据*/
		List<WeekBean> weekActiveStaff = getWeekData(STAFF);
		staffActiveBean.setWeek(weekActiveStaff);
		System.out.println(weekActiveStaff);
		
		List<MonthBean> monthActiveStaff = getMonthData(STAFF);
		staffActiveBean.setMonth(monthActiveStaff);
		
		ActiveBean activeBean = new ActiveBean();
		activeBean.setStaff(staffActiveBean);
		FamilyBean familyActiveBean = new FamilyBean();
		/**获取家属激活数据*/
		List<WeekBean> weekActiveFamily = getWeekData(FAMILY);
		familyActiveBean.setWeek(weekActiveFamily);
		
		List<MonthBean> monthActiveFamily = getMonthData(FAMILY);
		familyActiveBean.setMonth(monthActiveFamily);
		
		activeBean.setFamily(familyActiveBean);
		/**获取社会会员激活数据*/
		SocietyBean societyActiveBean = new SocietyBean();
		List<WeekBean> weekActiveSociety = getWeekData(SOCIETY);
		societyActiveBean.setWeek(weekActiveSociety);
		
		List<MonthBean> monthActiveSociety = getMonthData(SOCIETY);
		societyActiveBean.setMonth(monthActiveSociety);
		
		activeBean.setSociety(societyActiveBean);
		memberDataRes.setActive(activeBean);
		/**获取会员数目*/
		MemberBean memberBean = new MemberBean();
		StaffBean staffMemberBean = new StaffBean();
		
		List<WeekBean> weekMemberStaff = getWeekMemberData(STAFF);
		staffMemberBean.setWeek(weekMemberStaff);
		List<MonthBean> monthMemberStaff = getMonthMemberData(STAFF);
		staffMemberBean.setMonth(monthMemberStaff);
		/**完成员工会员数据*/
		memberBean.setStaff(staffMemberBean);
		/**获取家属会员数据*/
		FamilyBean familyMemberBean = new FamilyBean();
		List<WeekBean> weekMemberFamily = getWeekMemberData(FAMILY);
		familyMemberBean.setWeek(weekMemberFamily);
		List<MonthBean> monthMemberFamily = getMonthMemberData(FAMILY);
		familyMemberBean.setMonth(monthMemberFamily);
		/**完成家属会员数据*/
		memberBean.setFamily(familyMemberBean);
		/**获取社会会员数据*/
		SocietyBean societyMemberBean = new SocietyBean();
		List<WeekBean> weekMemberSociety = getWeekMemberData(SOCIETY);
		societyMemberBean.setWeek(weekMemberSociety);
		List<MonthBean> monthMemberSociety = getMonthMemberData(SOCIETY);
		societyMemberBean.setMonth(monthMemberSociety);
		/**完成社会会员数据*/
		memberBean.setSociety(societyMemberBean);
		memberDataRes.setMember(memberBean);
		/****获取memberData对象end*****/

		return memberDataRes;
	}
	/**获取当天数目 
	 * * type 1 家属   0 员工
     * */
	@Override
	public MemberTodayRes getMemberTodayRes() {
		MemberTodayRes memberTodayRes = new MemberTodayRes();
	    //今日激活员工数
	    int activeStaff=adUserDao.getCountByToday(STAFF);
	    //今日激活家属数
	    int activeFamily=adUserDao.getCountByToday(FAMILY);
	    //今日激活社会会员数
	    int activeSociety=adUserDao.getCountByToday(SOCIETY);
	    //今日激活总数
	    int activeTotal=activeStaff + activeFamily+activeSociety;
	    //目前的总员工会员数
	    int memberStaff=adUserDao.getCountByTotal(STAFF);
	    //目前的总家属会员数
	    int memberFamily=adUserDao.getCountByTotal(FAMILY);
	    //目前激活社会会员数
	    int memberSociety=adUserDao.getCountByTotal(SOCIETY);
	    //目前的总会员总数
	    int memberTotal=memberStaff + memberFamily+memberSociety;
	    memberTodayRes.setActiveTotal(activeTotal);
	    memberTodayRes.setActiveStaff(activeStaff);
	    memberTodayRes.setActiveFamily(activeFamily);
	    memberTodayRes.setActiveSociety(activeSociety);
	    memberTodayRes.setMemberTotal(memberTotal);
	    memberTodayRes.setMemberStaff(memberStaff);
	    memberTodayRes.setMemberFamily(memberFamily);
	    memberTodayRes.setMemberSociety(memberSociety);
		return memberTodayRes;
	}



	/**获取近六个月每个月数目 
	 * * type 1 家属   0 员工
     * */
	private List<MonthBean> getMonthData(int type) {
		List<MonthBean> monthlist = new ArrayList<>();
		Calendar cal=Calendar.getInstance();
		for (int i = 6; i > 0; i--) {
			cal.setTime(new Date());
			cal.add(cal.MONTH, -i);
			String date = cal.get(Calendar.MONTH)+1 +"月";
			String count = adUserDao.getCountByMonthActive(type,i)+"";
			MonthBean month = new MonthBean();
			month.setCount(count);
			month.setDate(date);
			monthlist.add(month);
		}
		return monthlist;
	}



	/**获取近两周每天数目 
	 * * type 1 家属   0 员工
     * */
	private List<WeekBean> getWeekData(int type) {
		List<WeekBean> weeklist = new ArrayList<>();
		Calendar cal=Calendar.getInstance();
		for (int i = 14; i > 0; i--) {
			cal.setTime(new Date());
			cal.add(cal.DAY_OF_MONTH, -i);
			String date = cal.get(Calendar.MONTH)+1 +"-"+cal.get(Calendar.DAY_OF_MONTH);
			String count = adUserDao.getCountByWeekActive(type,i)+"";
			WeekBean week = new WeekBean();
			week.setCount(count);
			week.setDate(date);
			weeklist.add(week);
		}
		return weeklist;
	}
	/**获取截止至近六个月的每个月的数目 
	 * * type 1 家属   0 员工
     * */
	private List<MonthBean> getMonthMemberData(int type) {
		List<MonthBean> monthlist = new ArrayList<>();
		Calendar cal=Calendar.getInstance();
		for (int i = 6; i > 0; i--) {
			cal.setTime(new Date());
			cal.add(cal.MONTH, -i);
			String date = cal.get(Calendar.MONTH)+1 +"月";
			String count = adUserDao.getCountByMonthMember(type,i)+"";
			MonthBean month = new MonthBean();
			month.setCount(count);
			month.setDate(date);
			monthlist.add(month);
		}
		return monthlist;
	}
	/**获取截止至两周的每天的数目 
	 * * type 1 家属   0 员工
     * */
	private List<WeekBean> getWeekMemberData(int type) {
		List<WeekBean> weeklist = new ArrayList<>();
		Calendar cal=Calendar.getInstance();
		for (int i = 14; i > 0; i--) {
			cal.setTime(new Date());
			cal.add(cal.DAY_OF_MONTH, -i);
			String date = cal.get(Calendar.MONTH)+1 +"-"+cal.get(Calendar.DAY_OF_MONTH);
			String count = adUserDao.getCountByWeekMember(type,i)+"";
			WeekBean week = new WeekBean();
			week.setCount(count);
			week.setDate(date);
			weeklist.add(week);
		}
		return weeklist;
	}
	/**获取订单近两周（包括今天）和近六个月的数目
     * */
	@Override
	public OrderDataRes getOderData() {
		OrderDataRes orderDataRes = new OrderDataRes();
		List<com.doooly.dto.statistics.OrderDataRes.WeekBean> weeklist = new ArrayList<>();
		Calendar cal=Calendar.getInstance();
		for (int i = 13; i >= 0; i--) {
			String date = null;
			if (i == 0) {
				date = "今天";
			}else {
				cal.setTime(new Date());
				cal.add(cal.DAY_OF_MONTH, -i);
				date = cal.get(Calendar.MONTH)+1 +"-"+cal.get(Calendar.DAY_OF_MONTH);
			}
			int count = orderDao.getCountByWeek(i);
			com.doooly.dto.statistics.OrderDataRes.WeekBean week = new com.doooly.dto.statistics.OrderDataRes.WeekBean();
			week.setCount(count);
			week.setDate(date);
			weeklist.add(week);
		}
		List<com.doooly.dto.statistics.OrderDataRes.MonthBean> monthlist = new ArrayList<>();
		Calendar cale=Calendar.getInstance();
		for (int i = 6; i > 0; i--) {
			cale.setTime(new Date());
			cale.add(cale.MONTH, -i);
			String date = cale.get(Calendar.MONTH)+1 +"月";
			int count = orderDao.getCountByMonth(i);
			com.doooly.dto.statistics.OrderDataRes.MonthBean month = new com.doooly.dto.statistics.OrderDataRes.MonthBean();
			month.setCount(count);
			month.setDate(date);
			monthlist.add(month);
		}
		orderDataRes.setWeek(weeklist);
		orderDataRes.setMonth(monthlist);
		return orderDataRes;
	}
	
	/**获取站点统计近两周（包括今天）的数目
	 * name 六个模块名
	 *  privilege：尊享特权
	 *  benefit：兜兜有礼
	 *  rob：员工疯抢
	 *  gift：月月礼包
	 *  active：激活模块
	 *  user：个人中心
     * */
	@Override
	public SiteStatisticsDataRes SiteStatisticsData() {
		SiteStatisticsDataRes siteStatisticsDataRes = new SiteStatisticsDataRes();
		PvBean pvBean = new PvBean();
		PrivilegeBean privilegePV = new PrivilegeBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> privilegePVWeek = getStatisticsPVDataForWeek("privilege");
		privilegePV.setWeek(privilegePVWeek);
		
		BenefitBean benefitPV = new BenefitBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> benefitPVWeek = getStatisticsPVDataForWeek("benefit");
		benefitPV.setWeek(benefitPVWeek);
		
		RobBean robPV =new RobBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> robPVWeek = getStatisticsPVDataForWeek("rob");
		robPV.setWeek(robPVWeek);
		
		GiftBean giftPV = new GiftBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> giftPVWeek = getStatisticsPVDataForWeek("gift");
		giftPV.setWeek(giftPVWeek);
		
		com.doooly.dto.statistics.SiteStatisticsDataRes.ActiveBean activePV = new com.doooly.dto.statistics.SiteStatisticsDataRes.ActiveBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> activePVWeek = getStatisticsPVDataForWeek("active");
		activePV.setWeek(activePVWeek);
		
		UserBean userPV = new UserBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> userPVWeek = getStatisticsPVDataForWeek("user");
		userPV.setWeek(userPVWeek);
		/**封装pv*/
		pvBean.setPrivilege(privilegePV);
		pvBean.setBenefit(benefitPV);
		pvBean.setRob(robPV);
		pvBean.setGift(giftPV);
		pvBean.setActive(activePV);
		pvBean.setUser(userPV);
		
		UvBean uvBean = new UvBean();
		PrivilegeBean privilegeUV = new PrivilegeBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> privilegeUVWeek = getStatisticsUVDataForWeek("privilege");
		privilegeUV.setWeek(privilegeUVWeek);
		
		BenefitBean benefitUV = new BenefitBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> benefitUVWeek = getStatisticsUVDataForWeek("benefit");
		benefitUV.setWeek(benefitUVWeek);
		
		RobBean robUV =new RobBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> robUVWeek = getStatisticsUVDataForWeek("rob");
		robUV.setWeek(robUVWeek);
		
		GiftBean giftUV = new GiftBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> giftUVWeek = getStatisticsUVDataForWeek("gift");
		giftUV.setWeek(giftUVWeek);
		
		com.doooly.dto.statistics.SiteStatisticsDataRes.ActiveBean activeUV = new com.doooly.dto.statistics.SiteStatisticsDataRes.ActiveBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> activeUVWeek = getStatisticsUVDataForWeek("active");
		activeUV.setWeek(activeUVWeek);
		
		UserBean userUV = new UserBean();
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> userUVWeek = getStatisticsUVDataForWeek("user");
		userUV.setWeek(userUVWeek);
		/**封装UV*/
		uvBean.setPrivilege(privilegeUV);
		uvBean.setBenefit(benefitUV);
		uvBean.setRob(robUV);
		uvBean.setGift(giftUV);
		uvBean.setActive(activeUV);
		uvBean.setUser(userUV);

		siteStatisticsDataRes.setPv(pvBean);
		siteStatisticsDataRes.setUv(uvBean);
		return siteStatisticsDataRes;
	}
	private List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> getStatisticsPVDataForWeek(String name) {
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> weekList = new ArrayList<>();
		Calendar cal=Calendar.getInstance();
		for (int i = 13; i >= 0; i--) {
			String date = null;
			if (i == 0) {
				date = "今天";
			}else {
				cal.setTime(new Date());
				cal.add(cal.DAY_OF_MONTH, -i);
				date = cal.get(Calendar.MONTH)+1 +"-"+cal.get(Calendar.DAY_OF_MONTH);
			}
			int count = siteStatisticsDao.getPVCountByWeek(name,i);
			com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean week = new com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean();
			week.setCount(count);
			week.setDate(date);
			weekList.add(week);
		}
		return weekList;
	}
	private List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> getStatisticsUVDataForWeek(String name) {
		List<com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean> weekList = new ArrayList<>();
		Calendar cal=Calendar.getInstance();
		for (int i = 13; i >= 0; i--) {
			String date = null;
			if (i == 0) {
				date = "今天";
			}else {
				cal.setTime(new Date());
				cal.add(cal.DAY_OF_MONTH, -i);
				date = cal.get(Calendar.MONTH)+1 +"-"+cal.get(Calendar.DAY_OF_MONTH);
			}
			int count = siteStatisticsDao.getUVCountByWeek(name,i);
			com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean week = new com.doooly.dto.statistics.SiteStatisticsDataRes.WeekBean();
			week.setCount(count);
			week.setDate(date);
			weekList.add(week);
		}
		return weekList;
	}

}
