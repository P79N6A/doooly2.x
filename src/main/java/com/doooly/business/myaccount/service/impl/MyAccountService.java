package com.doooly.business.myaccount.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.business.common.util.AESUtils;
import com.business.common.util.EncryptDecryptUtil;
import com.doooly.business.myaccount.service.MyAccountServiceI;
import com.doooly.business.mypoint.service.impl.MyPoinitService;
import com.doooly.common.constants.KeyConstants;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.FileUtils;
import com.doooly.common.util.MD5Util;
import com.doooly.dao.reachad.*;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 个人中心Service实现
 *
 * @author yuelou.zhang
 * @version 2017年7月19日
 */
@Service
@Transactional
public class MyAccountService implements MyAccountServiceI {

	private static Logger logger = Logger.getLogger(MyAccountService.class);
	// 兜礼配置文件
	private static ResourceBundle dooolyBundle = PropertiesConstants.dooolyBundle;
	// 图片上传路径
	private static String IMAGE_UPLOAD_URL = dooolyBundle.getString("image_upload_url");
	// 图片查看路径
	private static String IMAGE_VIEW_URL = dooolyBundle.getString("image_view_url");
	// 积分共享开关
	private static final int POINT_SHARE_SWITCH_CONFIRM = 2;// 2:待确认
	private static final int POINT_SHARE_SWITCH_CLOSE = 1;// 1:关

	@Autowired
	private AdUserDao adUserDao;

	@Autowired
	private AdUserPersonalInfoDao adUserPersonalInfoDao;

	@Autowired
	private AdFamilyUserDao adFamilyUserDao;

	@Autowired
	private AdFamilyDao adFamilyDao;

	@Autowired
	private MyPoinitService myPoinitService;

	@Autowired
	private AdAvailablePointsDao adAvailablePointsDao;
	
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private UserIntegralMapper userIntegralMapper;

	@Override
	public HashMap<String, Object> getAccountListById(String userId) {
		logger.info(String.format("获取用户信息 userId=%s ", userId));
		HashMap<String, Object> map = new HashMap<String, Object>();
		AdUserConn adUserConn = null;
		try {
			adUserConn = adUserDao.findAdUserConnByUserId(userId);
			// 会员类型 0:员工 1:家属  | 如果是家属则获取邀请人姓名
			if (AdUser.TYPE_FAMILY == Integer.valueOf(adUserConn.getUserType())) {
				String inviterName = adUserDao.findInviterNameByInviteeId(userId);
				adUserConn.setInviterName(inviterName);
			}
		} catch (Exception e) {
			logger.error(userId + "||获取用户信息异常！！！");
		}
		map.put("adUserConn", adUserConn);
		return map;
	}

	@Override
	public void updatePersonalData(JSONObject obj) {
		logger.info("更新会员个人信息jsonStr:" + obj.toJSONString());
		try {
			// 0.jsonObj转JavaBean
			AdUserConn adUserConn = JSONObject.toJavaObject(obj, AdUserConn.class);
			// 1.修改ad_user(性别|姓名)
			if (!StringUtils.isEmpty(adUserConn.getSex()) || !StringUtils.isEmpty(adUserConn.getName())) {
				adUserDao.updateUserData(adUserConn);
			}
			// 2.修改ad_user_personal_info(生日|头像)
			if (!StringUtils.isEmpty(adUserConn.getBirthday())
					|| !StringUtils.isEmpty(adUserConn.getAppHeadImageUrl())) {
				int result = adUserDao.getPersonalInfoByUserId(adUserConn);
				if (result > 0) {
					adUserDao.updatePersonalData(adUserConn);
				} else {
					adUserDao.insertPersonalData(adUserConn);
				}
			}
			// 3.修改ad_family_user(家属关系)
			if (!StringUtils.isEmpty(adUserConn.getFamilyRelation())) {
				// 根据用户id获取家庭关系表记录
				AdFamilyUser user = adFamilyUserDao.getMyFamily(Integer.valueOf(adUserConn.getUserId()));
				if (user == null) {
					// 插入家庭表
					AdFamily adFamily = adFamilyDao.getMyFamily(Integer.valueOf(adUserConn.getUserId()));
					if (adFamily == null) {
						adFamily = new AdFamily();
						adFamily.setPointShareSwitch(POINT_SHARE_SWITCH_CLOSE);
						adFamilyDao.insert(adFamily);
					}
					// 插入家庭关系表
					AdFamilyUser adFamilyUser = new AdFamilyUser();
					adFamilyUser.setDelFlag(0);
					adFamilyUser.setFamilyId(adFamily.getId());
					adFamilyUser.setUserId(Long.valueOf(adUserConn.getUserId()));
					adFamilyUser.setIsPointShare(POINT_SHARE_SWITCH_CONFIRM);
					adFamilyUser.setFamilyRelationship(Integer.valueOf(adUserConn.getFamilyRelation()));
					adFamilyUserDao.insert(adFamilyUser);
				} else {
					// 更新家庭关系表
					adFamilyUserDao.updateFamilyRelation(adUserConn.getUserId(), adUserConn.getFamilyRelation());
				}
			}
		} catch (Exception e) {
			logger.error(obj.get("userId") + "||更新会员个人信息异常！！！" + e.getMessage());
		}
	}

	@Override
	public JSONObject uploadImage(HttpServletRequest request) {
		JSONObject jsonData = new JSONObject();
		try {
			// 1.判断提交数据是否是文件上传表单数据
			if (ServletFileUpload.isMultipartContent(request)) {
				// 2.创建文件上传解析器并解决上传文件名的中文乱码
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");
				List<FileItem> items = null;
				try {
					// 3.解析上传数据
					items = upload.parseRequest(request);
				} catch (FileUploadException e) {
					logger.error("解析上传数据异常！！！" + e.getMessage());
				}
				if (CollectionUtils.isNotEmpty(items)) {
					for (FileItem fileItem : items) {
						// A.如果fileItem中封装的是普通输入项数据
						if (fileItem.isFormField()) {
							String fieldName = fileItem.getFieldName();
							String value = fileItem.getString("UTF-8");
							jsonData.put(fieldName, value);// 此处获取userId
						} else {
							// B.如果fileItem中封装的是上传文件
							String date = DateUtil.formatDate(new Date(), "yyyyMM");
							String uploadPath = IMAGE_UPLOAD_URL + date;
							try {
								FileUtils.createFolder(uploadPath);
							} catch (Exception e) {
								logger.error("生成文件夹失败");
							}
							String fileName = FileUtils.create_nonce_str() + ".jpg";
							uploadPath = uploadPath + "/" + fileName;
							logger.info("图片上传路径  uploadPath:" + uploadPath);

							// ********************上传begin********************************
							InputStream is = fileItem.getInputStream();
							FileUtils.writeFile(is, uploadPath);
							// ********************上传end********************************

							String imageViewPath = IMAGE_VIEW_URL + date + "/" + fileName;
							logger.info("图片访问路径  imageViewPath:" + imageViewPath);
							jsonData.put("appHeadImageUrl", imageViewPath);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("上传图片并生成图片地址异常！！！" + e.getMessage());
		}
		return jsonData;
	}

	@Override
	public HashMap<String, Object> getUserInfoById(String userId, String businessId, String username, String password) {
		logger.info(String.format("个人中心页面获取个人资料(包括积分信息/可用卡券张数) userId=%s ", userId));
		HashMap<String, Object> map = new HashMap<String, Object>();
		AdUserConn adUserConn = null;
		try {
			long start = System.currentTimeMillis();
			// 1.获取用户基本信息(头像、姓名、部门、工号、企业名称等)
			adUserConn = adUserDao.findAdUserConnByUserId(userId);
			logger.info(String.format("个人中心 获取用户基本信息===> SQL耗时：%s", System.currentTimeMillis()-start));
			// 2.获取我的积分 可用积分|待返积分|累计积分
			MessageDataBean pointDataBean = myPoinitService.queryUserIntegral(businessId, username, password, userId);
			HashMap<String, Object> pointMap = (HashMap<String, Object>) pointDataBean.getData();
			BigDecimal availablePoint = (BigDecimal) pointMap.get("availablePoint");
			BigDecimal returnPoint = (BigDecimal) pointMap.get("returnPoint");
			adUserConn.setAvailableIntegral(availablePoint.toString());
			adUserConn.setReturnIntegral(returnPoint.toString());
			UserIntegral userIntegral = new UserIntegral();
			userIntegral.setAdUserId(Long.parseLong(userId));
			BigDecimal dirIntegal = userIntegralMapper.getAvailIntegal(userIntegral);
			adUserConn.setDirIntegal(dirIntegal);
			adUserConn.setAvailableIntegral(availablePoint.add(dirIntegal).toString());
			logger.info(String.format("个人中心 获取用户积分信息===> SQL耗时：%s", System.currentTimeMillis()-start));
			// 3.判断用户是否有不同状态下的新订单
			this.setNewOrdersFlag(adUserConn, userId);
			logger.info(String.format("个人中心 获取用户订单信息===> SQL耗时：%s", System.currentTimeMillis()-start));
		} catch (Exception e) {
			logger.error(userId + "||获取个人中心资料(包括积分信息/可用卡券张数)异常！！！",e);
		}
		map.put("adUserConn", adUserConn);
		return map;
	}

	@Override
	public HashMap<String, Object> getFamilyUserInfo(String userId) {
		logger.info(String.format("获取家属头像姓名 userId=%s ", userId));
		HashMap<String, Object> map = new HashMap<String, Object>();
		AdUserConn adUserConn = null;
		try {
			// 1 获取家属头像姓名
			adUserConn = adUserDao.getFamilyUserInfo(userId);
            // 查询可用积分
            BigDecimal availablePoint = adUserDao.getAvailablePoint(userId);
            // 查询待返积分
            BigDecimal returnPoint = adUserDao.getReturnPoint(userId);
            // 获取消费积分
            BigDecimal consumerPoints = adAvailablePointsDao.getConsumerPoints(userId);
            //总积分
			String totalIntegral = "0.0";
			if(availablePoint != null) {
				totalIntegral = availablePoint.add(returnPoint).add(consumerPoints).toString();
			}
			String appHeadImageUrl = adUserConn.getAppHeadImageUrl();
			if (StringUtils.isBlank(appHeadImageUrl)) {
				appHeadImageUrl = "";
			}
            map.put("appHeadImageUrl", appHeadImageUrl);
            map.put("name", adUserConn.getTelephone());
            map.put("totalIntegral",totalIntegral);
            map.put("delFlag",adUserConn.getDelFlag());
        } catch (Exception e) {
            logger.error("获取家属头像姓名累计积分出错userId为:"+userId,e);
        }
		return map;
	}

	@Override
	public HashMap<String, Object> batchGetFamilyUserInfo(List<Map> invitationFamilyList) {
        logger.info(String.format("获取家属头像姓名"));
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (Map map1 : invitationFamilyList) {
            AdUserConn adUserConn;
            String userId = String.valueOf(map1.get("id"));
            try {
                // 1 获取家属头像姓名
                adUserConn = adUserDao.getFamilyUserInfo(userId);
                // 查询可用积分
                BigDecimal availablePoint = adUserDao.getAvailablePoint(userId);
                // 查询待返积分
                BigDecimal returnPoint = adUserDao.getReturnPoint(userId);
                // 获取消费积分
                BigDecimal consumerPoints = adAvailablePointsDao.getConsumerPoints(userId);
                //总积分
                String totalIntegral = availablePoint.add(returnPoint).add(consumerPoints).toString();
                String appHeadImageUrl = adUserConn.getAppHeadImageUrl();
                //去掉企业头像判断
               /* if(StringUtils.isBlank(appHeadImageUrl)){
                    appHeadImageUrl = adUserConn.getLogoUrl();
                }*/
                map1.put("appHeadImageUrl", appHeadImageUrl);
                map1.put("name", adUserConn.getName());
                map1.put("totalIntegral",totalIntegral);
                map1.put("telephone",adUserConn.getTelephone());
                map1.put("cardNumber",adUserConn.getCardNumber());
                map1.put("isActive",adUserConn.getIsActive());
            } catch (Exception e) {
                logger.error("获取家属头像姓名累计积分出错userId为:"+userId,e);
            }
        }
        map.put("invitationFamilyList",invitationFamilyList);
		return map;
	}

    @Override
    public HashMap<String, Object> isSetPayPassword(String userId) {
        logger.info(String.format("获取设置支付密码信息"));
        HashMap<String, Object> map = new HashMap<String, Object>();
        Integer isSetPayPassword = adUserPersonalInfoDao.getIsSetPayPassword(userId);
        map.put("isSetPayPassword",isSetPayPassword==null?0:isSetPayPassword);
        return map;
    }

    @Override
    public MessageDataBean setPayPassword(String userId, String payPassword, String isPayPassword) {
        MessageDataBean messageDataBean = new MessageDataBean();
        logger.info(String.format("设置支付密码"));
        String decryptByRSAPrivateKey = EncryptDecryptUtil.decryptByRSAPrivateKey(payPassword, KeyConstants.RSA_PRIBKEY);
        String decryptByAES = AESUtils.deCode(decryptByRSAPrivateKey, KeyConstants.AES_KEY);
        if(StringUtils.isBlank(decryptByRSAPrivateKey)|| StringUtils.isBlank(decryptByAES)){
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("参数解密失败");
            return messageDataBean;
        }
        String encryptByMd5 = MD5Util.digest(decryptByAES,KeyConstants.CHARSET);
        AdUserConn adUserConn = new AdUserConn();
        adUserConn.setUserId(userId);
        adUserConn.setId(Long.parseLong(userId));
        int result = adUserDao.getPersonalInfoByUserId(adUserConn);
        if (result == 0) {
            //如果为空插入用户拓展信息
            adUserDao.insertPersonalData(adUserConn);
        }
        //更新支付方式和密码
        adUserPersonalInfoDao.updatePayPassword(userId,encryptByMd5,isPayPassword);
        messageDataBean.setCode(MessageDataBean.success_code);
        return messageDataBean;
    }

    @Override
    public MessageDataBean validPayPassword(String userId, String payPassword) {
        MessageDataBean messageDataBean = new MessageDataBean();
        logger.info(String.format("校验支付密码"));
        String decryptByRSAPrivateKey = EncryptDecryptUtil.decryptByRSAPrivateKey(payPassword, KeyConstants.RSA_PRIBKEY);
        //logger.info("rsa解密出来值======"+decryptByRSAPrivateKey);
        String decryptByAES = AESUtils.deCode(decryptByRSAPrivateKey, KeyConstants.AES_KEY);
        //logger.info("aes解密出来key==========="+decryptByAES);
        if(StringUtils.isBlank(decryptByRSAPrivateKey)|| StringUtils.isBlank(decryptByAES)){
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("参数解密失败");
            return messageDataBean;
        }
        String encryptByMd5 = MD5Util.digest(decryptByAES,KeyConstants.CHARSET);
        AdUser adUser = adUserDao.getById(Integer.valueOf(userId));
        if(adUser.getPayPassword().equals(encryptByMd5)){
            //校验通过
            messageDataBean.setCode(MessageDataBean.success_code);
            messageDataBean.setMess(decryptByAES);
        }else {
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("旧密码不正确，请重新输入");
        }
        return messageDataBean;
    }


    @Override
    public MessageDataBean openPayPassword(String userId, String isPayPassword) {
        MessageDataBean messageDataBean = new MessageDataBean();
        logger.info(String.format("开启支付密码支付"));
        Integer isSetPayPassword = adUserPersonalInfoDao.getIsSetPayPassword(userId);
        if(isSetPayPassword!=1){
            //直接返回错误信息
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess("请先设置支付密码后在开启");
        }else {
            AdUser aduser = new AdUser();
            aduser.setId(Long.valueOf(userId));
            aduser.setIsPayPassword(isPayPassword);
            try {
                adUserDao.updateByPrimaryKeySelective(aduser);
                messageDataBean.setCode(MessageDataBean.success_code);
            } catch (Exception e) {
                logger.error("更新用户支付方式异常",e);
                messageDataBean.setCode(MessageDataBean.failure_code);
            }
        }
        return messageDataBean;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //System.out.println(UUID.randomUUID().toString().replaceAll("-","").substring(0,16));
        String s4 = EncryptDecryptUtil.encryptByDES("111111", KeyConstants.AES_KEY);
        System.out.println("des加密前字符串========"+s4);
        String s5 = EncryptDecryptUtil.decryptByDES(s4, KeyConstants.AES_KEY);
        System.out.println("des解密后字符串=========="+s5);
        //1，先aes加密在rsa加密
        String s1 = AESUtils.enCode("111111", KeyConstants.AES_KEY);
        System.out.println("aeskey加密后的字符串----------"+s1);
        String s = EncryptDecryptUtil.encryptByRSAPublicKey(s1, KeyConstants.RSA_PUBKEY);
        System.out.println("rsa公钥加密后字符串---------"+s);
        //2，解密,先私钥解密在aes解密
        String s2 = EncryptDecryptUtil.decryptByRSAPrivateKey(s, KeyConstants.RSA_PRIBKEY);
        System.out.println("rsa私钥解密结果==========="+s2);
        String s3 = AESUtils.deCode(s2, KeyConstants.AES_KEY);
        System.out.println("aeskey解密后的字符串==========="+s3);

    }

    public void setNewOrdersFlag(AdUserConn adUserConn,String userId){
		// 1.Redis获取用户各个状态订单数
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String orderTotal = opsForValue.get("ordertotal:"+userId+":0");//已下单
		String finishTotal = opsForValue.get("ordertotal:"+userId+":1");//已完成
		String cancelTotal = opsForValue.get("ordertotal:"+userId+":2");//已取消
		logger.info(String.format("用户 %s redis中已下单数:%s,已完成数:%s,已取消数:%s", userId,orderTotal,finishTotal,cancelTotal));
		// 2.DB获取用户各个状态订单数
		AdUserConn adUser = adUserDao.getOrderTotal(userId);
		int orderTotalMap = adUser.getOrderTotal();
		int finishTotalMap = adUser.getFinishTotal();
		int cancelTotalMap = adUser.getCancelTotal();
		logger.info(String.format("用户 %s DB中已下单数:%s,已完成数:%s,已取消数:%s", userId,orderTotalMap,finishTotalMap,cancelTotalMap));
		// 3.有新订单 设置flag
		if(StringUtils.isNotEmpty(orderTotal) && orderTotalMap>Integer.valueOf(orderTotal)){
			adUserConn.setNewOrderFlag(true);
		}else{
			adUserConn.setNewOrderFlag(false);
		}
		if(StringUtils.isNotEmpty(finishTotal) && finishTotalMap>Integer.valueOf(finishTotal)){
			adUserConn.setNewFinishFlag(true);
		}else{
			adUserConn.setNewFinishFlag(false);
		}
		if(StringUtils.isNotEmpty(cancelTotal) && cancelTotalMap>Integer.valueOf(cancelTotal)){
			adUserConn.setNewCancelFlag(true);
		}else{
			adUserConn.setNewCancelFlag(false);
		}
	}

}
