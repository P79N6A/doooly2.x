package com.doooly.dto.statistics;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseRes;
/**
 * 订单数量传输DTO
 * @author 杨汶蔚
 * @date 2016年9月20日
 * @version 1.0
 */
public class OrderDataRes extends BaseRes{
	 /**
	 * 
	 */
	private static final long serialVersionUID = -1948275846604097256L;
	/**
     * date : 09-01
     * count : 1000
     */

    private List<WeekBean> week;
    /**
     * date : 9月
     * count : 1000
     */

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

    public static class WeekBean {
        private String date;
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

    public static class MonthBean {
        private String date;
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
   		json.put("month", month);
   		json.put("week", week);
   		return json.toJSONString();
   	}
}
