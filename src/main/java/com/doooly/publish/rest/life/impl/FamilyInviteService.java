package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.common.service.impl.AdUserService;
import com.doooly.business.familyInviteApp.AdInvitationService;
import com.doooly.business.lightenBusiness.AdLightenBusinessServiceI;
import com.doooly.business.myaccount.service.MyAccountServiceI;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.common.context.SpringContextHolder;
import com.doooly.common.util.WechatUtil;
import com.doooly.dao.reachlife.LifeWechatBindingDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.InvitationRes;
import com.doooly.entity.reachlife.LifeWechatBinding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Created by john on 18/5/11.
 */
@Component
@Path("/wechat/familyInviteApp")
public class FamilyInviteService {

    private static final Logger logger = LoggerFactory.getLogger(FamilyInviteService.class);

    private static String FAM_FLAG_APP = "2";
    private static Short MEMBER_TYPE = 1;// 家属会员
//    public final static  String FAMILY_PHONE_LIST = null;// PropertiesHolder.getProperty("swing.link");
//    public final static  String FAMILY_INVITATION_MAX_NUM = null;//PropertiesHolder.getProperty("swing.link");
    private static String FAMILY_PHONE_LIST = PropertiesHolder.getProperty("family.phone.list");
    private static String FAMILY_INVITATION_MAX_NUM = PropertiesHolder.getProperty("family.invitation.max.num");

    public static Integer FAM_INVITATION_TYPE = 1;
    public static Integer FAM_GIFT_MIN_NUM = 1;
    public static Integer FAM_GIFT_MAX_NUM = 3;
    public static Integer STAFF_GIFT_MAX_NUM = 1000;

    @Autowired
    private AdUserServiceI adUserServiceI;
    @Autowired
    private AdInvitationService adInvitationService;
    @Autowired
    protected MyAccountServiceI myAccountService;
    @Autowired
    protected LifeWechatBindingDao lifeWechatBindingDao;
    @Autowired
    private AdUserService adUserService;
    @Autowired
    private AdLightenBusinessServiceI adLightenBusinessService;
    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 进入家属邀请
     *
     * @param json
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path(value = "/familyInviteApp")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String familyInviteAppRestFul(JSONObject json, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        JSONObject responseJson = new JSONObject();
        try {
            long start = System.currentTimeMillis();
            String userToken = request.getHeader(Constants.TOKEN_NAME);
            String channel = request.getHeader(Constants.CHANNEL);
            if(StringUtils.isEmpty(channel)){
                return new MessageDataBean("10001","token is null").toJsonString();
            }
            String userId = redisTemplate.opsForValue().get(userToken);
            AdUser user = adUserServiceI.getById(userId);
            logger.info("channel={},userToken={},userId={},user={}", channel, userToken, userId, user);

            //1.获取会员邀请详细信息
            InvitationRes invitationRes = getInvitationRes(userId, user.getTelephone());
            //2.获取被邀请家属的头像昵称
            List<HashMap<String, Object>> invitationFamilyList = invitationRes.getInvitationFamilyList();
            List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            if(invitationFamilyList!=null){
                for (int i = 0; i < (FAMILY_PHONE_LIST.contains(user.getTelephone()) ? invitationFamilyList.size() : 3) ; i++) {
                    HashMap<String, Object> inviteeMap = invitationFamilyList.get(i);
                    if(!StringUtils.isEmpty(channel) && channel.endsWith("app")){
                        //app
                        String inviteeId =inviteeMap.get("inviteeId").toString();
                        HashMap<String, String> map = new HashMap<String, String>();
                        HashMap<String, Object> infoMap = myAccountService.getFamilyUserInfo(inviteeId);
                        if (infoMap != null) {
                            map.put("isActive", getActiveStatus((String) inviteeMap.get("isActive"), (String) infoMap.get("delFlag")));
                            map.put("headImgurl", (String) infoMap.get("appHeadImageUrl"));
                            map.put("name", (String) infoMap.get("name"));
                        }else{
                            map.put("isActive", getActiveStatus((String) inviteeMap.get("isActive"), (String) infoMap.get("delFlag")));
                            map.put("headImgurl", "");
                            map.put("name", "");
                        }
                        list.add(map);
                    }else{
                        //wechat
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        String inviteeId =inviteeMap.get("inviteeId").toString();
                        AdUser invitee = adUserServiceI.getById(inviteeId);
                        String ivTelephone = invitee.getTelephone();
                        String cardNumber = invitee.getCardNumber();
                        LifeWechatBinding wechatHead = WechatUtil.getLifeWechatBinding(ivTelephone, cardNumber);
                        logger.info("===============wechatHead:" + wechatHead);
                        if(wechatHead!=null){
                            String headImgurl = wechatHead.getHeadImgurl();
                            hashMap.put("name", (String)inviteeMap.get("name"));
                            hashMap.put("headImgurl", headImgurl);
                            hashMap.put("isActive", getActiveStatus(invitee.getIsActive(), invitee.getDelFlag()));
                        } else {
                            hashMap.put("name", (String) inviteeMap.get("name"));
                            hashMap.put("headImgurl", "");
                            hashMap.put("isActive", getActiveStatus(invitee.getIsActive(), invitee.getDelFlag()));
                        }
                        list.add(hashMap);
                    }
                }
            }
            logger.info("===============invitationRes:" + invitationRes);
            logger.info("===============list:" + list);
            responseJson.put("invitation", invitationRes);
            responseJson.put("inviteeList", list == null ? null : list);
            responseJson.put("channel", channel);
            responseJson.put("code", MessageDataBean.success_code);
            logger.info("家属邀请加载页面调用接口耗时" + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put("code", MessageDataBean.failure_code);
        }
        return responseJson.toJSONString();
    }


    /**
     * 点击立即邀请
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @POST
    @Path(value = "/inviteFamily")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject inviteFamily(JSONObject json,@Context HttpServletRequest request,@Context HttpServletResponse response)throws Exception {
        JSONObject res = new JSONObject();
        try {
            long start = System.currentTimeMillis();
            String userToken = request.getHeader(Constants.TOKEN_NAME);
            String channel = request.getHeader(Constants.CHANNEL);
            res.put("channel", channel);
            String userId = redisTemplate.opsForValue().get(userToken);
            AdUser user = adUserServiceI.getById(userId);
            String telephone = json.getString("telephone");
            logger.info("channel={},userToken={},userId={},telephone={}", channel, userToken, userId, telephone);
            // 判断的登录人是否为会员还是家属
            if (user != null && MEMBER_TYPE == user.getType()) {
                res.put("code", "1003");
                res.put("msg", "您为家属,不可以邀请家属！");
                return res;
            }
            // 获取邀请人信息
            InvitationRes invitationRes = getInvitationRes(userId, user.getTelephone());
            // 判断邀请人数是否已满
            int maxNum = 0;
            if (StringUtils.isNotBlank(FAMILY_PHONE_LIST) && FAMILY_PHONE_LIST.contains(user.getTelephone())) {
                maxNum = Integer.valueOf(FAMILY_INVITATION_MAX_NUM);
            } else {
                maxNum = FAM_GIFT_MAX_NUM;
            }
            if (invitationRes.getInvitationAvailNum() <= 0 && invitationRes.getInvitationFamilyList() != null && invitationRes.getInvitationFamilyList().size() >= maxNum) {
                res.put("code", "1005");
                res.put("msg", "邀请名额已满！");
                return res;
            }
            // 通过手机号和邀请人去判断手机是否可以添加
            JSONObject data = new JSONObject();
            data.put("userId", userId);
            data.put("telephone", telephone);
            data.put("channel", channel);
            JSONObject result = adUserService.checkTelephone(data);

            if ("1000".equals(result.get("code"))) {
                // 1000表示手机号在库中不存在可以创建账号
                JSONObject saveRes = adInvitationService.saveUserNotActive(telephone, invitationRes.getInvitationCode(), FAM_INVITATION_TYPE, "111111", telephone,channel);
                logger.info("saveRes = {}", saveRes);
                if (saveRes.getString("code").equals("1000")) {
                    res.put("userId", userId);
                    res.put("code", "1000");
                    res.put("msg", saveRes.getString("desc"));
                } else {
                    res.put("code", saveRes.getString("code"));
                    res.put("msg", saveRes.getString("desc"));
                }
                // //获取微信初始化配置信息
                // String config = JSSDKUtil.getJSSDKConfig(WechatUtil.getMap(request));
                // String shareConfig = JSSDKUtil.getFamilyShareConfig(member.getAdId());
                // res.put("config", config);
                // res.put("shareConfig", shareConfig);
                // LogWriter.info("shareConfig================"+ shareConfig +" ===========");
                logger.info("家属邀请邀请手机调用接口耗时"+ (System.currentTimeMillis() - start) + " ms");
            }else if ("1003".equals(res.get("code")) || "1004".equals(res.get("code"))) {
                // 1003表示手机号在库中存在并且为家属，无需创建账号直接分享，让用户登录
                res.put("code", "1000");
                res.put("userId", userId);
                res.put("msg", result.get("msg"));
            } else {
                res.put("code", result.get("code"));
                res.put("userId", userId);
                res.put("msg", result.get("msg"));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            logger.info("服务器异常");
        }
        return res;
    }



    /**
     * 跳转到落地页
     *
     * @param request
     * @param model
     * @return
     */
    @POST
    @Path(value = "/share/getFamilyInviteInfoRestFul")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getFamilyInviteInfoRestFul(JSONObject json,@Context HttpServletRequest request) throws Exception{
        long start = System.currentTimeMillis();
        logger.info("getFamilyInviteInfoRestFul() start = {}", start);
        JSONObject req = new JSONObject();
        JSONObject responseJson = new JSONObject();
        try {
            String userId = json.getString("userId");
            String channel = json.getString("channel");
            AdUser user = adUserServiceI.getById(userId);
            String mobile = user.getTelephone();
            String cardNumber = user.getCardNumber();
            // 获取头像
            LifeWechatBinding wechatBind = WechatUtil.getLifeWechatBinding(mobile, cardNumber);
            HashMap<String, Object> headMap = myAccountService.getFamilyUserInfo(userId);
            logger.info("map = {}", headMap);
            if (wechatBind != null) {
                headMap.put("headImgurl", wechatBind.getHeadImgurl());
                headMap.put("name", (String) headMap.get("name"));
            } else {
                req.put("userId", userId);
                if (headMap != null) {
                    headMap.put("headImgurl", (String) headMap.get("appHeadImageUrl"));
                    headMap.put("name", (String) headMap.get("name"));
                } else {
                    headMap.put("headImgurl", "");
                    headMap.put("name", "");
                }
            }
            logger.info("headMap = {}", headMap);
            // 获取用户的企业信息
            JSONObject groupInfo = adInvitationService.getGroupInfo(userId);
            responseJson.put("adGroup", groupInfo.get("adGroup"));
            logger.info("groupInfo = {}", groupInfo);
            // 获取所有商户
            JSONObject pram = new JSONObject();
            pram.put("userId", userId);
            pram.put("flag", 1);
            JSONObject result = adLightenBusinessService.getAllBusiness(pram);
            if (MessageDataBean.success_code.equals(result.get("code"))) {
                responseJson.put("adBusinessList", result.get("adBusniessList"));
                responseJson.put("wechatBinding", headMap);
            } else {
                responseJson.put("adBusinessList", null);
                responseJson.put("wechatBinding", headMap);
            }
            responseJson.put("channel", channel);
            responseJson.put("code", MessageDataBean.success_code);
            logger.info("getFamilyInviteInfoRestFul() execution time = {}", System.currentTimeMillis() - start);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("服务器异常 e = {}",e);
            responseJson.put("code", MessageDataBean.failure_code);
        }
        return responseJson.toJSONString();
    }

    private InvitationRes getInvitationRes(String userId,  String telephone) {
        JSONObject reqJson = new JSONObject();
        if (StringUtils.isNotBlank(FAMILY_PHONE_LIST) && StringUtils.isNotBlank(telephone) && FAMILY_PHONE_LIST.contains(telephone)) {
            reqJson.put("invitationMaxNum", FAMILY_INVITATION_MAX_NUM);
        } else {
            reqJson.put("invitationMaxNum", FAM_GIFT_MAX_NUM);
        }
        reqJson.put("flag", FAM_FLAG_APP);
        reqJson.put("giftNum", FAM_GIFT_MIN_NUM);
        reqJson.put("invitationMinNum", FAM_GIFT_MIN_NUM);
        reqJson.put("invitationType", FAM_INVITATION_TYPE);
        reqJson.put("userId", userId);
        JSONObject invitationDetail = adInvitationService.getInvitationDetail(reqJson);
        logger.info("invitationDetail = {}",invitationDetail);
        InvitationRes invitationRes =JSONObject.parseObject(invitationDetail.toJSONString(), InvitationRes.class);
        if (invitationRes.getValidationTime() == null) {
            invitationRes.setValidationTime(0L);
        }
        return invitationRes;
    }

    private String getActiveStatus(String isActive,String delFlag){
        if("1".equals(delFlag)){
            //用户如果是删除状态，表示邀请失败
            return "3";
        }else{
            //前台展示 1">邀请成功 2">邀请中 3">邀请失败
            if(isActive.equals("1")){
                return "2";
            }else if(isActive.equals("2")){
                return "1";
            }else{
                return "3";
            }
        }
    }
}
