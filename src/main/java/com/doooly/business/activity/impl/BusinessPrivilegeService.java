package com.doooly.business.activity.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.BusinessPrivilegeServiceI;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdBusinessPrivilegeActivityDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdBusinessPrivilegeActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description: 商户特权开通
 * @author: qing.zhang
 * @date: 2017-09-12
 */
@Service
public class BusinessPrivilegeService implements BusinessPrivilegeServiceI {

    @Autowired
    private AdBusinessPrivilegeActivityDao adBusinessPrivilegeActivityDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AdBusinessDao adBusinessDao;

    @Override
    public MessageDataBean getActivityDetail(Integer businessId,Integer userId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        AdBusinessPrivilegeActivity adBusinessPrivilegeActivity = adBusinessPrivilegeActivityDao.get(businessId, userId);
        if(null != adBusinessPrivilegeActivity){
            //说明已经开通 返回失败错误码 跳转到开通成功页
            messageDataBean.setCode(MessageDataBean.failure_code);
        }else {
            //没有哀痛 返回正确码 跳转到活动首页
            messageDataBean.setCode(MessageDataBean.success_code);
        }
        return messageDataBean;
    }
    @Override
    public MessageDataBean apply(Integer businessId,Integer userId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        AdBusiness adBusiness = adBusinessDao.get(String.valueOf(businessId));
        if(adBusiness==null){
            //不需要开通
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("商户编号有误，请核对链接。");
            return messageDataBean;
        }
        if(adBusiness.getIsRebateApply()==0){
            //不需要开通
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("该商家未给员工开通返利标识，请核对商家数据。");
            return messageDataBean;
        }
        AdBusinessPrivilegeActivity adBusinessPrivilegeActivity = new AdBusinessPrivilegeActivity();
        adBusinessPrivilegeActivity.setBusinessId(businessId);
        adBusinessPrivilegeActivity.setUserId(userId);
        adBusinessPrivilegeActivityDao.insert(adBusinessPrivilegeActivity);
        //开通成功后将用户的信息放入redis队列-->微信端发送商户权限开通成功通知
        JSONObject data = new JSONObject();
        data.put("userId", userId);
        redisTemplate.convertAndSend("BUSINESS_CHANNEL", data.toString());
        messageDataBean.setCode(MessageDataBean.success_code);
        return messageDataBean;
    }
}
