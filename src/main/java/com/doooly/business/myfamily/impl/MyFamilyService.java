package com.doooly.business.myfamily.impl;

import com.doooly.business.myaccount.service.impl.AppMessageService;
import com.doooly.business.myfamily.MyFamilyServiceI;
import com.doooly.dao.reachad.AdFamilyDao;
import com.doooly.dao.reachad.AdFamilyUserDao;
import com.doooly.dao.reachad.AdSystemNoticeDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdFamily;
import com.doooly.entity.reachad.AdFamilyUser;
import com.doooly.entity.reachad.AdSystemNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 我的家属
 * @author: qing.zhang
 * @date: 2017-07-25
 */
@Service
public class MyFamilyService implements MyFamilyServiceI {

    private static final Logger LOG = LoggerFactory.getLogger(MyFamilyService.class);

    private static final int POINT_SHARE_SWITCH_CONFIRM = 2;//积分共享开关 2 待确认
    private static final int POINT_SHARE_SWITCH_CLOSE = 1;//积分共享开关 1 关
    private static final int POINT_SHARE_SWITCH_OPEN = 0;//积分共享开关 0开


    @Autowired
    private AdFamilyDao adFamilyDao;
    @Autowired
    private AdFamilyUserDao adFamilyUserDao;
    @Autowired
    private AppMessageService appMessageService;
    @Autowired
    private AdSystemNoticeDao adSystemNoticeDao;


    @Override
    public MessageDataBean getMyFamily(Integer userId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        AdFamilyUser adFamilyUser = adFamilyUserDao.getMyFamily(userId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("adFamilyUser", adFamilyUser);
        messageDataBean.setData(map);
        messageDataBean.setCode(MessageDataBean.success_code);
        return messageDataBean;
    }

    @Override
    public MessageDataBean initMyFamily(Integer userId, List<HashMap<String, Object>> invitationFamilyList) {
        MessageDataBean messageDataBean = new MessageDataBean();
        //插入家庭表
        AdFamily adFamily = adFamilyDao.getMyFamily(userId);
        if(adFamily == null){
            adFamily = new AdFamily();
            adFamily.setPointShareSwitch(POINT_SHARE_SWITCH_CLOSE);
            adFamilyDao.insert(adFamily);
        }
        //插入家庭关系表
        AdFamilyUser adFamilyUser = new AdFamilyUser();
        adFamilyUser.setDelFlag(0);
        adFamilyUser.setFamilyId(adFamily.getId());
        adFamilyUser.setUserId(Long.valueOf(userId));
        adFamilyUser.setIsPointShare(POINT_SHARE_SWITCH_CONFIRM);
        AdFamilyUser user = adFamilyUserDao.getMyFamily(userId);
        if(user == null){
            //为空插入数据
            adFamilyUserDao.insert(adFamilyUser);
        }
        //循环插入家属
        for (HashMap<String, Object> map : invitationFamilyList) {
            AdFamilyUser user1 = adFamilyUserDao.getMyFamily(Integer.valueOf(map.get("id").toString()));
            //为空插入
            if(user1==null){
                adFamilyUser.setUserId(Long.parseLong(map.get("id").toString()));
                adFamilyUserDao.insert(adFamilyUser);
            }
        }
        messageDataBean.setData(new HashMap<String, Object>());
        messageDataBean.setCode(MessageDataBean.success_code);
        return messageDataBean;
    }

    @Override
    public MessageDataBean getMyFamilyList(Integer userId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        List<Map> adFamilyUserList = adFamilyUserDao.getMyFamilyList(userId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("adFamilyUserList", adFamilyUserList);
        messageDataBean.setData(map);
        messageDataBean.setCode(MessageDataBean.success_code);
        return messageDataBean;
    }


    @Override
    public MessageDataBean confirmSharePoint(Integer userId, Integer isPointShare) {
        MessageDataBean messageDataBean = new MessageDataBean();
        messageDataBean.setData(new HashMap<String, Object>());
        messageDataBean.setCode(MessageDataBean.success_code);
        if(userId ==null){
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("用户id不能为空");
        }
        adFamilyUserDao.updateFamilyUser(userId, isPointShare);
        if(isPointShare.equals(POINT_SHARE_SWITCH_CLOSE)){
            //传关闭直接将总开关关闭
            adFamilyDao.updateFamily(userId, POINT_SHARE_SWITCH_CLOSE);
        }
        //查询一下家庭状态
        List<Map> myFamilyList = adFamilyUserDao.getMyFamilyList(userId);
        Boolean flag = true;
        //如果所有的都同意就将家庭表设为同意
        for (Map map : myFamilyList) {
            if (!map.get("isPointShare").equals(POINT_SHARE_SWITCH_OPEN)) {
                flag = false;
                break;
            }
        }
        if (flag && myFamilyList.size()>=4) {
            //说明所有人同意并且有3个家属 修改家庭表
            adFamilyDao.updateFamily(userId, POINT_SHARE_SWITCH_OPEN);
            //向所有人发送消息
            StringBuilder familyName = new StringBuilder();
            for (Map map1 : myFamilyList) {
                familyName.append(map1.get("userName")).append("、");
            }
            String content = "您的家庭" + familyName.substring(0, familyName.length() - 1) + "已经开启家庭共享积分。";
            for (Map m : myFamilyList) {
                {
                    String userId1 = String.valueOf(m.get("userId"));
                    AdSystemNotice adSystemNotice = new AdSystemNotice();
                    adSystemNotice.setReceiveUser(userId1);
                    adSystemNotice.setContent(content);
                    adSystemNotice.setTarget("2");//app端消息
                    adSystemNotice.setNoticeType("3");//共享积分消息
                    int i = appMessageService.sendMessage(content, userId1);
                    if (i == 1) {
                        adSystemNoticeDao.insert(adSystemNotice);
                    } else {
                        LOG.error("app共享积分消息推送失败，失败用户id为{}", userId1);
                        messageDataBean.setCode(MessageDataBean.failure_code);
                        messageDataBean.setMess("共享积分消息推送家属失败,请稍后再试");
                        return messageDataBean;
                    }
                }
            }
        }
        return messageDataBean;
    }
}
