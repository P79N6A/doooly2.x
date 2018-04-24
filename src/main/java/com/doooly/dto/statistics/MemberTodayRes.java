package com.doooly.dto.statistics;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseRes;

public class MemberTodayRes extends BaseRes{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3286631322372906484L;
	//今日激活总数
    private int activeTotal;
    //今日激活员工数
    private int activeStaff;
    //今日激活家属数
    private int activeFamily;
    //今日激活社会会员数
    private int activeSociety;

    //总会员数
    private int memberTotal;
    //员工会员总数
    private int memberStaff;
    //家属会员总数
    private int memberFamily;
    //社会会员总数
    private int memberSociety;

    public int getActiveTotal() {
        return activeTotal;
    }

    public void setActiveTotal(int activeTotal) {
        this.activeTotal = activeTotal;
    }

    public int getActiveStaff() {
        return activeStaff;
    }

    public void setActiveStaff(int activeStaff) {
        this.activeStaff = activeStaff;
    }

    public int getActiveFamily() {
        return activeFamily;
    }

    public void setActiveFamily(int activeFamily) {
        this.activeFamily = activeFamily;
    }

    public int getActiveSociety() {
        return activeSociety;
    }

    public void setActiveSociety(int activeSociety) {
        this.activeSociety = activeSociety;
    }

    public int getMemberTotal() {
        return memberTotal;
    }

    public void setMemberTotal(int memberTotal) {
        this.memberTotal = memberTotal;
    }

    public int getMemberStaff() {
        return memberStaff;
    }

    public void setMemberStaff(int memberStaff) {
        this.memberStaff = memberStaff;
    }

    public int getMemberFamily() {
        return memberFamily;
    }

    public void setMemberFamily(int memberFamily) {
        this.memberFamily = memberFamily;
    }

    public int getMemberSociety() {
        return memberSociety;
    }

    public void setMemberSociety(int memberSociety) {
        this.memberSociety = memberSociety;
    }
    @Override
   	public String toJsonString() {
   		JSONObject json = new JSONObject();
   		json.put("code", this.getCode());
   		json.put("msg", this.getMsg());
   		json.put("activeTotal", activeTotal);
   		json.put("activeStaff", activeStaff);
   		json.put("activeFamily", activeFamily);
   		json.put("activeSociety", activeSociety);
   		json.put("memberTotal", memberTotal);
   		json.put("memberStaff", memberStaff);
   		json.put("memberFamily", memberFamily);
   		json.put("memberSociety", memberSociety);
   		return json.toJSONString();
   	}
  
}
