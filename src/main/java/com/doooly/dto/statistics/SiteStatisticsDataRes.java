package com.doooly.dto.statistics;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseRes;

/**
 * 站点统计数量传输DTO
 * @author 杨汶蔚
 * @date 2016年9月20日
 * @version 1.0
 */
public class SiteStatisticsDataRes extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2466913768944898628L;
	/**独立IP访问信息*/
	private UvBean uv;
	/**客户点击访问信息*/
    private PvBean pv;

    public UvBean getUv() {
        return uv;
    }

    public void setUv(UvBean uv) {
        this.uv = uv;
    }

    public PvBean getPv() {
        return pv;
    }

    public void setPv(PvBean pv) {
        this.pv = pv;
    }

    public static class UvBean {
    	/**privilege：尊享特权*/
        private PrivilegeBean privilege;
        /**benefit：兜兜有礼*/
        private BenefitBean benefit;
        /**rob：员工疯抢*/
        private RobBean rob;
        /**gift：月月礼包*/
        private GiftBean gift;
        /**active：激活模块*/
        private ActiveBean active;
        /**user：个人中心*/
        private UserBean user;

        public PrivilegeBean getPrivilege() {
            return privilege;
        }

        public void setPrivilege(PrivilegeBean privilege) {
            this.privilege = privilege;
        }

        public BenefitBean getBenefit() {
            return benefit;
        }

        public void setBenefit(BenefitBean benefit) {
            this.benefit = benefit;
        }

        public RobBean getRob() {
            return rob;
        }

        public void setRob(RobBean rob) {
            this.rob = rob;
        }

        public GiftBean getGift() {
            return gift;
        }

        public void setGift(GiftBean gift) {
            this.gift = gift;
        }

        public ActiveBean getActive() {
            return active;
        }

        public void setActive(ActiveBean active) {
            this.active = active;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }
    }

    public static class PvBean {
    	/**privilege：尊享特权*/
        private PrivilegeBean privilege;
        /**benefit：兜兜有礼*/
        private BenefitBean benefit;
        /**rob：员工疯抢*/
        private RobBean rob;
        /**gift：月月礼包*/
        private GiftBean gift;
        /**active：激活模块*/
        private ActiveBean active;
        /**user：个人中心*/
        private UserBean user;

        public PrivilegeBean getPrivilege() {
            return privilege;
        }

        public void setPrivilege(PrivilegeBean privilege) {
            this.privilege = privilege;
        }

        public BenefitBean getBenefit() {
            return benefit;
        }

        public void setBenefit(BenefitBean benefit) {
            this.benefit = benefit;
        }

        public RobBean getRob() {
            return rob;
        }

        public void setRob(RobBean rob) {
            this.rob = rob;
        }

        public GiftBean getGift() {
            return gift;
        }

        public void setGift(GiftBean gift) {
            this.gift = gift;
        }

        public ActiveBean getActive() {
            return active;
        }

        public void setActive(ActiveBean active) {
            this.active = active;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }
    }

    public static class PrivilegeBean {
        /**
         * week:近两周的数据
         * date : 09-01
         * count : 2133
         */

        private List<WeekBean> week;

        public List<WeekBean> getWeek() {
            return week;
        }

        public void setWeek(List<WeekBean> week) {
            this.week = week;
        }
    }

    public static class BenefitBean {
        /**
         * week:近两周的数据
         * date : 09-01
         * count : 7456
         */

        private List<WeekBean> week;

        public List<WeekBean> getWeek() {
            return week;
        }

        public void setWeek(List<WeekBean> week) {
            this.week = week;
        }
    }

    public static class RobBean {
        /**
         * week:近两周的数据
         * date : 09-01
         * count : 2310
         */

        private List<WeekBean> week;

        public List<WeekBean> getWeek() {
            return week;
        }

        public void setWeek(List<WeekBean> week) {
            this.week = week;
        }
    }

    public static class GiftBean {
        /**
         * week:近两周的数据
         * date : 09-01
         * count : 1424
         */

        private List<WeekBean> week;

        public List<WeekBean> getWeek() {
            return week;
        }

        public void setWeek(List<WeekBean> week) {
            this.week = week;
        }
    }

    public static class ActiveBean {
        /**
         * week:近两周的数据
         * date : 09-01
         * count : 1450
         */

        private List<WeekBean> week;

        public List<WeekBean> getWeek() {
            return week;
        }

        public void setWeek(List<WeekBean> week) {
            this.week = week;
        }
    }

    public static class UserBean {
        /**
         * week:近两周的数据
         * date : 09-01
         * count : 7810
         */

        private List<WeekBean> week;

        public List<WeekBean> getWeek() {
            return week;
        }

        public void setWeek(List<WeekBean> week) {
            this.week = week;
        }
    }

    public static class WeekBean {
    	/**近两周每天当日日期*/
        private String date;
        /**近两周每天当日数量*/
        private int count;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
    @Override
   	public String toJsonString() {
   		JSONObject json = new JSONObject();
   		json.put("code", this.getCode());
   		json.put("msg", this.getMsg());
   		json.put("pv", pv);
   		json.put("uv", uv);
   		return json.toJSONString();
   	}
}
