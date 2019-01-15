package com.doooly.business.thirdpart.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdGroupServiceI;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.myaccount.service.MyAccountServiceI;
import com.doooly.business.thirdpart.ThirdPartServiceI;
import com.doooly.business.thirdpart.constant.ThirdPartConstant;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.DaHuaConstants;
import com.doooly.common.token.TokenUtil;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.dao.reachad.AdUserPersonalInfoDao;
import com.doooly.dto.common.ConstantsLogin;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdUserConn;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.doooly.common.constants.Constants.TOKEN_KEY;
import static com.doooly.common.constants.Constants.TOKEN_NAME;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2019-01-15
 */
@Service
public class ThirdPartServiceImpl implements ThirdPartServiceI{

    private final static Logger logger = LoggerFactory.getLogger(ThirdPartServiceImpl.class);
    @Autowired
    private ConfigDictServiceI configDictServiceI;
    @Autowired
    private AdGroupServiceI adGroupServiceI;
    @Autowired
    private AdUserPersonalInfoDao adUserPersonalInfoDao;
    @Autowired
    private MyAccountServiceI myAccountService;
    @Autowired
    protected StringRedisTemplate redisTemplate;


    @Override
    public MessageDataBean getGroupInfo(JSONObject json, HttpServletRequest request) {
        MessageDataBean  messageDataBean = new MessageDataBean();
        HashMap<String,Object> map = new HashMap<>();
        //获取渠道来源第三方
        String thirdPartyChannel = request.getHeader(Constants.THIRDPARTYCHANNEL);
        String groupId = configDictServiceI.getValueByTypeAndKey(ThirdPartConstant.THIRD_PART_DICT_KEY, thirdPartyChannel);
        AdGroup adGroup = adGroupServiceI.getGroupById(groupId);
        map.put("adGroup",adGroup);
        messageDataBean.setData(map);
        messageDataBean.setCode(MessageDataBean.success_code);
        messageDataBean.setMess(MessageDataBean.success_mess);
        return messageDataBean;
    }

    @Override
    public MessageDataBean thirdLogin(JSONObject json, HttpServletRequest request) {
        MessageDataBean  messageDataBean = new MessageDataBean();
        HashMap<String,Object> map = new HashMap<>();
        //获取渠道来源第三方
        String thirdPartyChannel = request.getHeader(Constants.THIRDPARTYCHANNEL);
        if(DaHuaConstants.THIRDPARTYCHANNEL_DAHUA.equals(thirdPartyChannel)){
            //大华渠道去绑定的手机号和用户信息
            String url = configDictServiceI.getValueByTypeAndKey(ThirdPartConstant.THIRD_PART_DICT_KEY, DaHuaConstants.THIRD_DAHUA_USER_INFO_URL);
            String groupId = configDictServiceI.getValueByTypeAndKey(ThirdPartConstant.THIRD_PART_DICT_KEY, DaHuaConstants.THIRDPARTYCHANNEL_DAHUA);
            String thirdUserToken = json.getString("thirdUserToken");
            JSONObject param = new JSONObject();
            param.put("jsonData",thirdUserToken);
            JSONObject jsonObject = HttpClientUtil.httpPost(url + DaHuaConstants.USER_INFO_URL, param);
            if(jsonObject!= null && "200".equals(jsonObject.getString("ResultCode")) && "true".equals(jsonObject.getString("IsSuccess"))){
                //说明请求成功绑定用户信息
                JSONObject result = JSONObject.parseObject(jsonObject.getString("Result"));
                String FItemNumber = result.getString("FItemNumber");//大华工号
                Map<String,Object> params = new HashMap<>();
                params.put("workNumber",FItemNumber);
                params.put("adGroupId",groupId);
                //根据工号和企业查询
                AdUserConn isUser = adUserPersonalInfoDao.getIsUser(params);
                if(isUser != null){
                    String userId = String.valueOf(isUser.getId());
                    // 终端渠道
                    String channel = request.getHeader(Constants.CHANNEL);
                    // 用户当前使用token
                    String token = "";
                    // 验证是否已存在token,如果存在则刷新
                    String userToken = redisTemplate.opsForValue().get(channel + ":" + String.format(TOKEN_KEY, userId));
                    logger.info("====【userValidateLogin】用户已存在的token-userToken：" + userToken);
                    if (StringUtils.isNotBlank(userToken)) {
                        // 删除原token用户ID
                        redisTemplate.delete(userToken);
                        // 刷新token
                        token = TokenUtil.refreshUserToken(channel, userId);
                    } else {
                        token = TokenUtil.getUserToken(channel, userId);
                    }
                    // 获取用户个人信息
                    HashMap<String, Object> userInfomMap = myAccountService.getAccountListById(String.valueOf(isUser.getId()));
                    if (userInfomMap != null) {
                        userInfomMap.put(TOKEN_NAME, token);
                        map.put("userInfo", new Gson().toJson(userInfomMap));
                    } else {
                        map.put("userInfo", "");
                    }
                    messageDataBean.setData(map);
                    messageDataBean.setCode(ConstantsLogin.Login.SUCCESS.getCode());
                    messageDataBean.setMess(ConstantsLogin.Login.SUCCESS.getMsg());
                }else {
                    logger.error("用户信息不存在,返回前端跳登录页面");
                    messageDataBean.setCode(ConstantsLogin.Login.USER_NOT_EXIST.getCode());
                    messageDataBean.setMess(ConstantsLogin.Login.USER_NOT_EXIST.getMsg());
                }
            }else {
                logger.error("调用大华接口校验token异常，返回结果{}",jsonObject);
                messageDataBean.setCode(ConstantsLogin.ValidCode.VALID_ERROR.getCode());
                messageDataBean.setMess("身份验证失败，请退出重新登录或联系企业管理员，谢谢！");
            }
        }else {
            messageDataBean.setCode(ConstantsLogin.Login.SUCCESS.getCode());
            messageDataBean.setMess(ConstantsLogin.Login.SUCCESS.getMsg());
        }
        return messageDataBean;
    }
}
