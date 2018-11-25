package com.doooly.business.forum.invitation.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.forum.invitation.AdForumInvitationServiceI;
import com.doooly.business.guide.impl.AdArticleService;
import com.doooly.dao.reachad.AdForumInvitationActivityDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdForumInvitationActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AdArticleService.class);

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
                    res.setCode("1001");
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

                    res.setCode("1000");
                    res.setMess("报名成功");

//                    try {
//                        //报名成功发送短信
//                        String mobiles = adForumInvitation.getPhone();
//                        String alidayuSmsCode = ThirdPartySMSConstatns.SMSTemplateConfig.sign_up_success_template_code;
//                        JSONObject paramSMSJSON = new JSONObject();
//                        paramSMSJSON.put("name", adForumInvitation.getName());
//                        paramSMSJSON.put("activityName", "《新费税的政治解读及应对方案》");
//                        paramSMSJSON.put("date", "2018-12-12 13:30");
//                        paramSMSJSON.put("address", "上海南苏州路1247号3层八号桥艺术空间1908粮仓");
//                        int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
//                        logger.info("报名成功发送短信. i = {}", i);
//                    } catch (Exception e) {
//                        logger.error("sendMsg has an error. e = {}", e);
//                    }
                }
            } else {
                res.setCode("1003");
//                res.setMess("参数错误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setCode("1002");
//            res.setMess("服务异常，请稍后再试");
        }

        return res;
    }
}
