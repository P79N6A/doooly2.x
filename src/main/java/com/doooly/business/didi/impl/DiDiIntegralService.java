package com.doooly.business.didi.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.didi.DiDiConnector;
import com.doooly.business.didi.constants.DiDiConstants;
import com.doooly.business.didi.service.DiDiIntegralServiceI;
import com.doooly.business.didi.utils.DidiEnterpriseApiUtil;
import com.doooly.common.constants.ThirdPartySMSConstatns;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.util.IdGeneratorUtil;
import com.doooly.common.util.ThirdPartySMSUtil;
import com.doooly.common.webservice.WebService;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.BusinessUserIntegralDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.BusinessUserIntegral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @Description: 滴滴数据接口
 * @author: qing.zhang
 * @date: 2018-03-06
 */
@Service
public class DiDiIntegralService implements DiDiIntegralServiceI {

    private static final Logger logger = LoggerFactory.getLogger(DiDiIntegralService.class);

    @Autowired
    private BusinessUserIntegralDao businessUserIntegralDao;
    @Autowired
    private DidiEnterpriseApiUtil didiEnterpriseApiUtil;
    @Autowired
    private AdUserDao adUserDao;
    @Autowired
    private AdBusinessDao adBusinessDao;

    /**
     * 查询滴滴权限
     * @param businessId 商户id
     * @param userId 用户id
     * @return
     */
    @Override
    public MessageDataBean getDiDiIntegral(Long businessId, Long userId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        AdUser adUser = adUserDao.selectByPrimaryKey(userId);
        //请求滴滴接口查询用户数据
        String result = DiDiConnector.didiMemberGet(adUser.getTelephone(),didiEnterpriseApiUtil.getDidiAccessToken());
        if(result == null){
            //说明出现异常
            //用户信息查询失败
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("不符合滴滴分配");
            logger.error("滴滴用户信息查询失败");
        }else{
            JSONObject jsonObject = JSONObject.parseObject(result);
            if(jsonObject.getInteger("errno")==0){
                //说明接口请求成功
                BusinessUserIntegral businessUserIntegral = businessUserIntegralDao.getDiDiIntegral(businessId,userId);
                if(jsonObject.getJSONObject("data").getInteger("total")>0){
                    //说明返还成功有数据
                    JSONObject object = jsonObject.getJSONObject("data").getJSONArray("records").getJSONObject(0);
                    String totalQuotaStr = object.getString("total_quota");
                    if(businessUserIntegral == null){
                        //插入兜礼记录表
                        businessUserIntegral = new BusinessUserIntegral();
                        businessUserIntegral.setBusinessId(businessId);
                        businessUserIntegral.setUserId(userId);
                        businessUserIntegral.setBusinessMemberId(object.getLong("id"));
                        businessUserIntegral.setBusinessIntegral(new BigDecimal(totalQuotaStr));
                        businessUserIntegralDao.insert(businessUserIntegral);
                        //返回数据 可用积分
                        map.put("businessIntegral",String.valueOf(new BigDecimal(totalQuotaStr).setScale(2,BigDecimal.ROUND_HALF_UP)));
                        messageDataBean.setCode(MessageDataBean.success_code);
                    }else {
                        //返回数据 可用积分
                        map.put("businessIntegral",String.valueOf(businessUserIntegral.getBusinessIntegral().setScale(2, BigDecimal.ROUND_HALF_UP)));
                        messageDataBean.setCode(MessageDataBean.success_code);
                    }
                }else {
                    //没数据调用新增接口
                    if(businessUserIntegral != null){
                        //新增前先删掉本地的
                        businessUserIntegralDao.delete(businessUserIntegral);
                    }
                    //1,查询用车制度接口
                    String regulationGet = DiDiConnector.didiRegulationGet(didiEnterpriseApiUtil.getDidiAccessToken());
                    if(regulationGet !=null && JSONObject.parseObject(regulationGet).getInteger("errno")==0){
                        JSONArray data = JSONObject.parseObject(regulationGet).getJSONArray("data");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < data.size(); i++) {
                            stringBuilder.append(data.getJSONObject(i).getString("regulation_id"));
                            stringBuilder.append("_");
                        }
                        //构建新增用户参数
                        JSONObject params = new JSONObject();
                        params.put("phone",adUser.getTelephone());
                        params.put("realname",adUser.getName());
                            /*params.put("use_company_money",1);
                            params.put("total_quota","0");
                            params.put("use_car_config", stringBuilder.substring(0, stringBuilder.length() - 1));*/
                        String memberAdd = DiDiConnector.didiMemberAdd(params.toJSONString(),didiEnterpriseApiUtil.getDidiAccessToken());
                        if(memberAdd !=null && JSONObject.parseObject(memberAdd).getInteger("errno")==0) {
                            //插入兜礼记录表
                            BusinessUserIntegral businessUserIntegral1 = new BusinessUserIntegral();
                            businessUserIntegral1.setBusinessId(businessId);
                            businessUserIntegral1.setUserId(userId);
                            businessUserIntegral1.setBusinessMemberId(JSONObject.parseObject(memberAdd).getJSONObject("data").getLong("id"));
                            businessUserIntegral1.setBusinessIntegral(new BigDecimal("0.00"));
                            businessUserIntegralDao.insert(businessUserIntegral1);
                            //返回数据 可用积分
                            map.put("businessIntegral","0.00");
                            messageDataBean.setCode(MessageDataBean.success_code);
                        }else {
                            //新增失败
                            messageDataBean.setCode(MessageDataBean.failure_code);
                            messageDataBean.setMess("不符合滴滴分配");
                            logger.error("滴滴新增接口失败");
                        }
                    }else {
                        //用车制度查询失败
                        messageDataBean.setCode(MessageDataBean.failure_code);
                        messageDataBean.setMess("不符合滴滴分配");
                        logger.error("滴滴用车制度查询失败");
                    }
                }
            }else{
                //用户信息查询失败
                messageDataBean.setCode(MessageDataBean.failure_code);
                messageDataBean.setMess("不符合滴滴分配");
                logger.error("滴滴用户信息查询失败");
            }
        }
        messageDataBean.setData(map);
        return messageDataBean;
    }

    /**
     * 积分兑换
     * @param businessId 商户id
     * @param userId 用户id
     * @param amount 积分数量
     * @param code 兜礼消费验证码
     * @param orderNumber
     * @return
     */
    @Override
    public MessageDataBean exchangeIntegral(Long businessId, Long userId, BigDecimal amount, Integer code, String orderNumber) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        AdUser adUser = adUserDao.selectByPrimaryKey(userId);
        BusinessUserIntegral businessUserIntegral = businessUserIntegralDao.getDiDiIntegral(businessId,userId);
        //积分消费
        JSONObject params = new JSONObject();
        params.put("businessId", DiDiConstants.DIDI_BUSINESS_ID);
        params.put("storesId", WebService.STOREID);
        params.put("cardNumber", adUser.getTelephone());
        params.put("verificationCode",code);
        params.put("amount", amount);
        String serialNumber = IdGeneratorUtil.getOrderNumber(3);
        params.put("orderNumber", orderNumber);
        params.put("serialNumber", serialNumber);
        params.put("orderDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String responseJSON = HTTPSClientUtils.sendPost(params, WebService.PAY_POINT_URL);
        JSONObject res = JSONObject.parseObject(responseJSON);
        if(res.getInteger("code")==0){
            //积分消费成功
            //2,滴滴成本中心 待定
            /*if(businessUserIntegral.getRemark() == null){
                //说明是第一次
                String result = DiDiConnector.didiBudgetCenterAdd(adUser.getName() + adUser.getTelephone(), amount,didiEnterpriseApiUtil.getDidiAccessToken());
                if(result != null && JSONObject.parseObject(result).getInteger("errno")==0){
                    businessUserIntegral.setRemark(JSONObject.parseObject(result).getJSONObject("data").getString("id"));
                }
            }else{
                //先查询成本中心
                String result = DiDiConnector.didiBudgetCenterGet(businessUserIntegral.getRemark(),adUser.getName() + adUser.getTelephone(),didiEnterpriseApiUtil.getDidiAccessToken());
            }*/

            //3,修改每月配额
            //3.1,查询用车制度接口
            String regulationGet = DiDiConnector.didiRegulationGet(didiEnterpriseApiUtil.getDidiAccessToken());
            if(regulationGet!=null && JSONObject.parseObject(regulationGet).getInteger("errno")==0){
                JSONArray data = JSONObject.parseObject(regulationGet).getJSONArray("data");
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < data.size(); i++) {
                    stringBuilder.append(data.getJSONObject(i).getString("regulation_id"));
                    stringBuilder.append("_");
                }
                //请求滴滴接口查询用户数据
                String totalQuotaStr;
                String result = DiDiConnector.didiMemberGet(adUser.getTelephone(),didiEnterpriseApiUtil.getDidiAccessToken());
                JSONObject jsonObject = JSONObject.parseObject(result);
                //说明接口请求成功
                if (jsonObject.getJSONObject("data").getInteger("total") > 0) {
                    //说明返还成功有数据
                    JSONObject object = jsonObject.getJSONObject("data").getJSONArray("records").getJSONObject(0);
                    totalQuotaStr = object.getString("total_quota");
                    //构建修改用户参数 滴滴用滴滴接口配额添加额度
                    BigDecimal oldBusinessIntegral = businessUserIntegral.getBusinessIntegral();
                    BigDecimal businessIntegral = oldBusinessIntegral.add(amount);
                    BigDecimal totalQuota = new BigDecimal(totalQuotaStr).add(amount);
                    Integer userCompanyMoney ;
                    JSONObject params1 = new JSONObject();
                    if(businessIntegral.compareTo(BigDecimal.ONE)<0){
                        //最后积分小于0 关闭企业积分支付
                        userCompanyMoney=0;
                    }else {
                        userCompanyMoney=1;
                        if(oldBusinessIntegral.compareTo(BigDecimal.ZERO)<0){
                            //说明之前是负数了, 先加上负的配额
                            totalQuota = totalQuota.add(oldBusinessIntegral);
                        }
                        params1.put("total_quota", String.valueOf(totalQuota));
                    }
                    params1.put("phone", adUser.getTelephone());
                    params1.put("realname", adUser.getName());
                    params1.put("use_company_money", userCompanyMoney);
                    params1.put("regulation_id", stringBuilder.substring(0, stringBuilder.length() - 1));
                    String memberEdit = DiDiConnector.didiMemberEdit(params1.toJSONString(), businessUserIntegral.getBusinessMemberId(),didiEnterpriseApiUtil.getDidiAccessToken());
                    if (memberEdit != null && JSONObject.parseObject(memberEdit).getInteger("errno") == 0) {
                        //修改兜礼记录表 自己记录
                        businessUserIntegral.setBusinessIntegral(businessIntegral);
                        businessUserIntegralDao.update(businessUserIntegral);
                        //1,插入积分兑换记录表
                        BusinessUserIntegral businessUserIntegral1 = new BusinessUserIntegral();
                        businessUserIntegral1.setBusinessId(businessId);
                        businessUserIntegral1.setUserId(userId);
                        businessUserIntegral1.setAmount(amount);
                        businessUserIntegral1.setType(1);
                        businessUserIntegralDao.insertRecord(businessUserIntegral1);
                        messageDataBean.setCode(MessageDataBean.success_code);
                    } else {
                        //积分退款
                        refundIntegral(amount, orderNumber, serialNumber, adUser);
                        messageDataBean.setCode(JSONObject.parseObject(memberEdit).getString("errno"));
                        messageDataBean.setMess("可能存在未支付的订单,请至“我的行程”中查看行程状态或联系滴滴客服：400-000-0999");
                        logger.error("滴滴修改用户接口失败");
                    }
                }else {
                    //滴滴接口没有查询到数据
                    //积分退款
                    refundIntegral(amount,orderNumber,serialNumber,adUser);
                    messageDataBean.setCode(MessageDataBean.failure_code);
                    messageDataBean.setMess("修改配额失败");
                    logger.error("滴滴用户查询数据为空");
                }
            }else {
                //用车制度查询失败
                //积分退款
                refundIntegral(amount,orderNumber,serialNumber,adUser);
                messageDataBean.setCode(MessageDataBean.failure_code);
                messageDataBean.setMess("滴滴用车制度查询失败");
                logger.error("滴滴用车制度查询失败");
            }
        }else {
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(res.getString("info"));
        }
        messageDataBean.setData(map);
        return messageDataBean;
    }


    /**
     * 获取积分验证码
     * @param businessId 商户id
     * @param userId 用户id
     * @param amount 积分数量
     * @param orderNumber
     * @return
     */
    @Override
    public MessageDataBean getCode(Long businessId, Long userId, BigDecimal amount, String orderNumber) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        AdUser adUser = adUserDao.selectByPrimaryKey(userId);
        //积分消费验证码
        JSONObject params = new JSONObject();
        params.put("businessId", DiDiConstants.DIDI_BUSINESS_ID);
        params.put("storesId", WebService.STOREID);
        params.put("cardNumber", adUser.getTelephone());
        params.put("amount", amount);
        String serialNumber = IdGeneratorUtil.getOrderNumber(3);
        params.put("orderNumber", orderNumber);
        params.put("serialNumber", serialNumber);
        params.put("orderDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        params.put("price",amount);
        String responseJSON = HTTPSClientUtils.sendPost(params, WebService.PAY_CODE_URL);
        JSONObject res = JSONObject.parseObject(responseJSON);
        if(res.getInteger("code")==0){
            messageDataBean.setCode(MessageDataBean.success_code);
        }else {
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(res.getString("info"));
        }
        messageDataBean.setData(map);
        return messageDataBean;
    }

    /**
     * 积分退款
     * @param integral 积分
     * @param orderNumber 订单号
     * @param serialNumber 流水号
     * @param adUser 用户信息
     */
    private void refundIntegral(BigDecimal integral,String orderNumber,String serialNumber,AdUser adUser) {
        JSONObject params = new JSONObject();
        params.put("orderNumber", orderNumber);
        params.put("serialNumber",serialNumber);
        params.put("cardNumber", adUser.getCardNumber());
        params.put("integral", integral);
        params.put("orderType", "5");
        params.put("businessId", WebService.BUSINESSID);
        params.put("storesId", WebService.STOREID);
        params.put("username", WebService.USERNAME);
        params.put("password", WebService.PASSWORD);
        JSONArray array = new JSONArray();
        params.put("orderDetail", array.toString());

        String ret = HTTPSClientUtils.sendPostNew(params.toJSONString(), WebService.ADDINTEGRALAUTHORIZATION);
        logger.info("result = {}",ret);
        if (ret != null) {
            JSONObject json = JSON.parseObject(ret);
            if (json.getInteger("code") == 0) {
                try {
                    // 积分退成功发送短信
                    String mobiles = adUser.getTelephone();
                    String alidayuSmsCode = ThirdPartySMSConstatns.SMSTemplateConfig.refund_success_template_code;
                    JSONObject paramSMSJSON = new JSONObject();
                    paramSMSJSON.put("product", "滴滴积分兑换");
                    paramSMSJSON.put("integral", integral);
                    int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
                    logger.info("sendMsg orderNum = {},i = {}", orderNumber, i);
                } catch (Exception e) {
                    logger.error("sendMsg has an error. e = {}", e);
                }
            } else {
                logger.error("退款出现异常");
            }
        } else {
            logger.error("退款出现异常");
        }

    }

    /**
     * 积分兑换
     * @param businessId 商户id
     * @param userId 用户id
     * @return
     */
    @Override
    public MessageDataBean toExchangeIntegral(Long businessId, Long userId) {
        MessageDataBean messageDataBean = new MessageDataBean();
        HashMap<String, Object> map = new HashMap<>();
        AdUser adUser = adUserDao.selectByPrimaryKey(userId);
        AdBusiness adBusiness = adBusinessDao.getById(String.valueOf(businessId));
        BusinessUserIntegral businessUserIntegral = businessUserIntegralDao.getDiDiIntegral(businessId,userId);
        map.put("telephone",adUser.getTelephone());
        map.put("integral",String.valueOf(adUser.getIntegral().setScale(2, BigDecimal.ROUND_HALF_UP)));
        map.put("businessLogo",adBusiness.getMiniLogo());
        map.put("businessIntegral",String.valueOf(businessUserIntegral.getBusinessIntegral().setScale(2, BigDecimal.ROUND_HALF_UP)));
        map.put("orderNumber",IdGeneratorUtil.getOrderNumber(3));
        map.put("businessNumber",adBusiness.getBusinessId());
        messageDataBean.setData(map);
        messageDataBean.setCode(MessageDataBean.success_code);
        return messageDataBean;
    }
}
