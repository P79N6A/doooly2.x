package com.doooly.business.forum.invitation.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.forum.invitation.AdForumInvitationServiceI;
import com.doooly.dao.reachad.AdForumInvitationActivityDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.forum.invitation.AdForumInvitationActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 论坛邀请service接口实现类
 * @Author: Mr.Wu
 * @Date: 2018/11/20
 */
@Service
public class AdForumInvitationService implements AdForumInvitationServiceI {

    @Autowired
    AdForumInvitationActivityDao adForumInvitationActivityDao;

    @Override
    public MessageDataBean registerForum(JSONObject jsonReq) {
        MessageDataBean res = new MessageDataBean();
        Date now = new Date();

        try {
            String phone = jsonReq.getString("phone");

            if (phone != null && !"".equals(phone)) {
                String id = adForumInvitationActivityDao.getByPhone(phone);

                if (id != null) {
                    res.setCode("10001");
                    res.setMess("该手机号已报名");
                } else {
                    AdForumInvitationActivity adForumInvitation = new AdForumInvitationActivity();
                    adForumInvitation.setName(jsonReq.getString("name"));
                    adForumInvitation.setPhone(jsonReq.getString("phone"));
                    adForumInvitation.setCompanyName(jsonReq.getString("companyName"));
                    adForumInvitation.setInviter(jsonReq.getString("inviter"));
                    adForumInvitation.setRegisterDate(now);
                    adForumInvitation.setCreateDate(now);
                    adForumInvitationActivityDao.insert(adForumInvitation);

                    res.setCode("10000");
                    res.setMess("报名成功");
                }
            } else {
                res.setCode("10003");
                res.setMess("参数错误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setCode("10002");
            res.setMess("服务异常，请稍后再试");
        }

        return res;
    }
}
