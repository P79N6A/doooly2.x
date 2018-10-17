package com.doooly.business.myaccount.service.impl;

import com.doooly.business.myaccount.service.AdSystemNoticeServiceI;
import com.doooly.business.utils.Pagelab;
import com.doooly.common.constants.AppConstants;
import com.doooly.common.util.EmojiUtils;
import com.doooly.dao.reachad.AdSystemNoticeDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdSystemNotice;
import com.doooly.entity.reachad.AdSystemNoticeRead;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

@Service
@Transactional
public class AdSystemNoitceService implements AdSystemNoticeServiceI {
	@Autowired
	private AdSystemNoticeDao adSystemNoticeDao;
	@Autowired
    private AppMessageService appMessageService;

	private static Logger logger = Logger.getLogger(AdSystemNoitceService.class);

	@Override
	public MessageDataBean getSystemNoitceList(String userId, String target, Integer currentPage, Integer pageSize, String token, String versionCode) {
        logger.info(String.format("“消息中心”列表 userId=%s ", userId));
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Pagelab pagelab = new Pagelab(currentPage,pageSize);
        //查询总数
        int totalNum = adSystemNoticeDao.getSystemNoticeNum(userId,target);
        messageDataBean.setCode(MessageDataBean.success_code);
        if(totalNum>0){
            pagelab.setTotalNum(totalNum);//这里会计算总页码
            //查询详情
            List<AdSystemNotice> adSystemNoticeList =  adSystemNoticeDao.getSystemNoticeList(userId,target, pagelab.getStartIndex(), pagelab.getPageSize());
            //app2.0去掉token
          /*  for (AdSystemNotice adSystemNotice : adSystemNoticeList) {
                adSystemNotice.setTargetUrl(adSystemNotice.getTargetUrl()+"&token="+token);
            }*/
            //app2.0换新连接
            for (AdSystemNotice adSystemNotice : adSystemNoticeList) {
                if(versionCode!=null && "new".equals(versionCode)){
                    //将新连接赋值给旧链接
                    adSystemNotice.setTargetUrl(adSystemNotice.getNewTargetUrl());
                }
                adSystemNotice.setContent(EmojiUtils.emojiConverterUnicodeStr(adSystemNotice.getContent()));
                adSystemNotice.setTitle(EmojiUtils.emojiConverterUnicodeStr(adSystemNotice.getTitle()));
            }
            map.put("adSystemNoticeList",adSystemNoticeList);//数据
            map.put("countPage",pagelab.getCountPage());//总页码
        }
        messageDataBean.setData(map);
        return messageDataBean;
    }

	@Override
	public MessageDataBean getSystemNoitceByTypeList(String userId, String target, Integer currentPage, Integer pageSize, String token, String versionCode,String noticeType) {
        logger.info(String.format("“根据类型获取消息中心”列表 userId=%s ", userId));
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Pagelab pagelab = new Pagelab(currentPage,pageSize);
        //查询总数
        int totalNum = adSystemNoticeDao.getSystemNoticeByTypeNum(userId,target,noticeType);
        messageDataBean.setCode(MessageDataBean.success_code);
        if(totalNum>0){
            pagelab.setTotalNum(totalNum);//这里会计算总页码
            //查询详情
            List<AdSystemNotice> adSystemNoticeList =  adSystemNoticeDao.getSystemNoticeByTypeList(userId,target, pagelab.getStartIndex(), pagelab.getPageSize(),noticeType);
            //app2.0换新连接
            for (AdSystemNotice adSystemNotice : adSystemNoticeList) {
                if(versionCode!=null && "new".equals(versionCode)){
                    //将新连接赋值给旧链接
                    adSystemNotice.setTargetUrl(adSystemNotice.getNewTargetUrl());
                }
                adSystemNotice.setContent(EmojiUtils.emojiConverterUnicodeStr(adSystemNotice.getContent()));
                adSystemNotice.setTitle(EmojiUtils.emojiConverterUnicodeStr(adSystemNotice.getTitle()));
            }
            map.put("adSystemNoticeList",adSystemNoticeList);//数据
            map.put("countPage",pagelab.getCountPage());//总页码
        }
        messageDataBean.setData(map);
        return messageDataBean;
    }

	@Override
	public MessageDataBean getNoReadNum(String userId, String target) {
        logger.info(String.format("“消息中心”未读消息数量 userId=%s ", userId));
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<String, Object>();
        //查询总数
        int totalNum = adSystemNoticeDao.getNoReadNum(userId,target);
        map.put("totalNum",totalNum);
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setData(map);
        return messageDataBean;
    }

	@Override
	public MessageDataBean getNoReadNumByType(String userId, String target,String noticeType) {
        logger.info(String.format("根据类型获取“消息中心”未读消息数量 userId=%s ", userId));
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<String, Object>();
        //查询总数
        int totalNum = adSystemNoticeDao.getNoReadNumByType(userId,target,noticeType);
        map.put("totalNum",totalNum);
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setData(map);
        return messageDataBean;
    }

	@Override
	public MessageDataBean getListByType(String userId, String target) {
        logger.info(String.format("根据类型获取“消息中心”未读消息数量 userId=%s ", userId));
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        //查询总数
        List<AdSystemNotice> adSystemNoticeList = new ArrayList<>();
        AdSystemNotice.NoticeTypeEnum[] values = AdSystemNotice.NoticeTypeEnum.values();
        for (AdSystemNotice.NoticeTypeEnum value : values) {
            AdSystemNotice adSystemNotice = new AdSystemNotice();
            String noticeType = value.getNoticeType();
            adSystemNotice.setNoticeType(noticeType);
            adSystemNotice.setNoticeTypeStr(value.getNoticeTypeStr());
            adSystemNotice.setNoReadNum(adSystemNoticeDao.getNoReadNumByType(userId,target,noticeType));
            adSystemNoticeList.add(adSystemNotice);
        }
        //Collections.sort(adSystemNoticeList, (o1, o2) -> o1.getNoticeType().compareTo(o2.getNoticeType()));

        map.put("adSystemNoticeList",adSystemNoticeList);
        //查询总数
        int totalNum = adSystemNoticeDao.getNoReadNum(userId,target);
        map.put("totalNum",totalNum);
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setData(map);
        return messageDataBean;
    }

	@Override
	public MessageDataBean updateReadType(String userId, String target) {
        logger.info(String.format("“消息中心”更新读取状态 userId=%s ", userId));
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<String, Object>();
        //查询该用户所有未读消息
        List<AdSystemNotice> adSystemNoticeList = adSystemNoticeDao.getNoReadList(userId,target);
        if(CollectionUtils.isNotEmpty(adSystemNoticeList)){
            List<AdSystemNoticeRead> adSystemNoticeReads = new ArrayList<>();
            for (AdSystemNotice adSystemNotice : adSystemNoticeList) {
                AdSystemNoticeRead adSystemNoticeRead = new AdSystemNoticeRead();
                adSystemNoticeRead.setNoticeId(adSystemNotice.getId());
                adSystemNoticeRead.setReadType(1);
                adSystemNoticeRead.setReceiveUser(userId);
                adSystemNoticeReads.add(adSystemNoticeRead);
            }
            //批量插入未读信息到已读信息表
            adSystemNoticeDao.batchInsert(adSystemNoticeReads);
        }
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setData(map);
        return messageDataBean;
    }

	@Override
	public MessageDataBean updateReadNoticeType(String userId, String target, String noticeType) {
        logger.info(String.format("“消息中心”根据类型更新读取状态 userId=%s ", userId));
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        //查询该用户所有未读消息
        List<AdSystemNotice> adSystemNoticeList = adSystemNoticeDao.getNoReadListByType(userId,target,noticeType);
        if(CollectionUtils.isNotEmpty(adSystemNoticeList)){
            List<AdSystemNoticeRead> adSystemNoticeReads = new ArrayList<>();
            for (AdSystemNotice adSystemNotice : adSystemNoticeList) {
                AdSystemNoticeRead adSystemNoticeRead = new AdSystemNoticeRead();
                adSystemNoticeRead.setNoticeId(adSystemNotice.getId());
                adSystemNoticeRead.setReadType(1);
                adSystemNoticeRead.setReceiveUser(userId);
                adSystemNoticeReads.add(adSystemNoticeRead);
            }
            //批量插入未读信息到已读信息表
            adSystemNoticeDao.batchInsert(adSystemNoticeReads);
        }
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setData(map);
        return messageDataBean;
    }

    @Override
    public MessageDataBean sendMessage(String userId, String target, String content) {
        logger.info(String.format("“发送消息”列表 userId=%s ", userId));
        MessageDataBean messageDataBean = new MessageDataBean();
        //先发送ios
        int num = appMessageService.sendMessage(content, userId,AppConstants.IOS_SEND, AppConstants.IOS_ENVIRONMENT);
            //ios 发送失败在发安卓
        int num1 = appMessageService.sendMessage(content, userId,AppConstants.ANDROID_SEND, AppConstants.IOS_ENVIRONMENT);
        //ios 和 android 都失败才算失败
        if(num == 0 && num1 ==0){
            messageDataBean.setCode(MessageDataBean.failure_code);
        }else {
            //家属邀请的推送要入库
            if("invitation".equals(target)){
                AdSystemNotice adSystemNotice = new AdSystemNotice();
                adSystemNotice.setTitle("账户开通提醒通知");
                adSystemNotice.setContent(content);
                adSystemNotice.setReceiveUser(userId);
                adSystemNotice.setNoticeType("0");
                adSystemNotice.setTarget("2");
                adSystemNotice.setTargetUrl(ResourceBundle.getBundle("doooly").getString("invite_url"));
                adSystemNotice.setNewTargetUrl(ResourceBundle.getBundle("doooly").getString("invite_url"));
                int i = adSystemNoticeDao.insert(adSystemNotice);
                logger.info("adSystemNoticeDao.insert rows = " + i);
            }
            messageDataBean.setCode(MessageDataBean.success_code);
        }
        return messageDataBean;
    }

    @Override
    public MessageDataBean sendMessage(String content) {
        logger.info(String.format("发送系统消息内容为"+content));
        MessageDataBean messageDataBean = new MessageDataBean();
        //先发送iso在发送安卓
        int num = appMessageService.sendMessage(content,AppConstants.IOS_SEND, AppConstants.IOS_ENVIRONMENT);
        appMessageService.sendMessage(content,AppConstants.ANDROID_SEND, AppConstants.IOS_ENVIRONMENT);
        if(num == 0){
            messageDataBean.setCode(MessageDataBean.failure_code);
        }else {
            messageDataBean.setCode(MessageDataBean.success_code);
        }
        return messageDataBean;
    }

    @Override
    public MessageDataBean sendUrlMessage(String content, String url, String target) {
        logger.info("发送系统消息内容为"+content);
        MessageDataBean messageDataBean = new MessageDataBean();
        //先发送iso在发送安卓
        int num = 0;
        if("2".equals(target)){
            //推送全平台
            num = appMessageService.sendUrlMessage(content,AppConstants.IOS_SEND, AppConstants.IOS_ENVIRONMENT,url);
            appMessageService.sendUrlMessage(content,AppConstants.ANDROID_SEND, AppConstants.IOS_ENVIRONMENT,url);
        }else if("3".equals(target)){
            //推送ios
            num = appMessageService.sendUrlMessage(content,AppConstants.IOS_SEND, AppConstants.IOS_ENVIRONMENT,url);
        }else if("4".equals(target)){
            //推送安卓
            num = appMessageService.sendUrlMessage(content,AppConstants.ANDROID_SEND, AppConstants.IOS_ENVIRONMENT,url);
        }
        if(num == 0){
            messageDataBean.setCode(MessageDataBean.failure_code);
        }else {
            messageDataBean.setCode(MessageDataBean.success_code);
        }
        return messageDataBean;
    }

}
