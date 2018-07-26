package com.doooly.publish.rest.payment;

import com.alibaba.fastjson.JSONObject;

import javax.ws.rs.FormParam;

/**
 * @Description: 兜礼1号通
 * @author: qing.zhang
 * @date: 2018-05-29
 */
public interface ShanghaiBankRestServiceI {
    // 虚账户开户
    String c19SCrVirSingleAccount(JSONObject obj);
    // 虚账户提款
    String c19VirWithDrawalsInq(JSONObject obj);
    // 虚账户代缴
    String c19SingleCharge(JSONObject obj);
    // 虚账户代发
    String c19VirSReTrigSer(JSONObject obj);
    // 虚账户提款通知
    String c19WithDrawalsNotice(@FormParam("checkvalue")String checkvalue, @FormParam("encmsg")String encmsg,
                                @FormParam("service")String service);
    // 虚账户余额查询
    String c19VirAcctBlanceInq(JSONObject obj);

    // 虚账户交易明细查询
    String c19VirAcctTranDtlQry(JSONObject obj);
}
