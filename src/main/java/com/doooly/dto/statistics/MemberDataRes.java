package com.doooly.dto.statistics;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseRes;
/**
 * 会员数量传输DTO
 * @author 杨汶蔚
 * @date 2016年9月20日
 * @version 1.0
 */
public class MemberDataRes extends BaseRes{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1182021164596774962L;
	//会员数据
    private MemberBean member;
    //激活数据
    private ActiveBean active;

    public ActiveBean getActive() {
        return active;
    }

    public void setActive(ActiveBean active) {
        this.active = active;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    /**
     * 激活数据实体
     */
    public static class ActiveBean {
        //员工数据
        private StaffBean staff;
        //家属数据
        private FamilyBean family;
        //社会会员
        private SocietyBean society;

        public StaffBean getStaff() {
            return staff;
        }

        public void setStaff(StaffBean staff) {
            this.staff = staff;
        }

        public FamilyBean getFamily() {
            return family;
        }

        public void setFamily(FamilyBean family) {
            this.family = family;
        }

        public SocietyBean getSociety() {
            return society;
        }

        public void setSociety(SocietyBean society) {
            this.society = society;
        }
    }

    /**
     * 会员数据实体
     */
    public static class MemberBean {
        //员工数据
        private StaffBean staff;
        //家属数据
        private FamilyBean family;
        //社会会员
        private SocietyBean society;

        public StaffBean getStaff() {
            return staff;
        }

        public void setStaff(StaffBean staff) {
            this.staff = staff;
        }

        public FamilyBean getFamily() {
            return family;
        }

        public void setFamily(FamilyBean family) {
            this.family = family;
        }

        public SocietyBean getSociety() {
            return society;
        }

        public void setSociety(SocietyBean society) {
            this.society = society;
        }
    }

    /**
     * 员工数据实体
     */
    public static class StaffBean {
        //近2周数据集合
        private List<WeekBean> week;
        //近6个月数据集合
        private List<MonthBean> month;

        public List<WeekBean> getWeek() {
            return week;
        }

        public void setWeek(List<WeekBean> week) {
            this.week = week;
        }

        public List<MonthBean> getMonth() {
            return month;
        }

        public void setMonth(List<MonthBean> month) {
            this.month = month;
        }
    }

    /**
     * 家属数据实体
     */
    public static class FamilyBean {
        //近2周数据集合
        private List<WeekBean> week;
        //近6个月数据集合
        private List<MonthBean> month;

        public List<WeekBean> getWeek() {
            return week;
        }

        public void setWeek(List<WeekBean> week) {
            this.week = week;
        }

        public List<MonthBean> getMonth() {
            return month;
        }

        public void setMonth(List<MonthBean> month) {
            this.month = month;
        }
    }

    public static class SocietyBean {
        //近2周数据集合
        private List<WeekBean> week;
        //近6个月数据集合
        private List<MonthBean> month;

        public List<WeekBean> getWeek() {
            return week;
        }

        public void setWeek(List<WeekBean> week) {
            this.week = week;
        }

        public List<MonthBean> getMonth() {
            return month;
        }

        public void setMonth(List<MonthBean> month) {
            this.month = month;
        }
    }

    public static class WeekBean {
    	/**日期*/
        private String date;
        /**数量*/
        private String count;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

    public static class MonthBean {
    	/**日期*/
        private String date;
        /**数量*/
        private String count;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
    @Override
	public String toJsonString() {
		JSONObject json = new JSONObject();
		json.put("code", this.getCode());
		json.put("msg", this.getMsg());
		json.put("active", active);
		json.put("member", member);
		return json.toJSONString();
	}
}
