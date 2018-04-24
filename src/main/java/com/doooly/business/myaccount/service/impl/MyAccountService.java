package com.doooly.business.myaccount.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

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

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myaccount.service.MyAccountServiceI;
import com.doooly.business.mypoint.service.impl.MyPoinitService;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.FileUtils;
import com.doooly.dao.reachad.AdAvailablePointsDao;
import com.doooly.dao.reachad.AdFamilyDao;
import com.doooly.dao.reachad.AdFamilyUserDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdFamily;
import com.doooly.entity.reachad.AdFamilyUser;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdUserConn;

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
	private AdFamilyUserDao adFamilyUserDao;

	@Autowired
	private AdFamilyDao adFamilyDao;

	@Autowired
	private MyPoinitService myPoinitService;

	@Autowired
	private AdAvailablePointsDao adAvailablePointsDao;
	
	@Autowired
	private StringRedisTemplate redisTemplate;

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
            String totalIntegral = availablePoint.add(returnPoint).add(consumerPoints).toString();
            String appHeadImageUrl = adUserConn.getAppHeadImageUrl();
            if(StringUtils.isBlank(appHeadImageUrl)){
                appHeadImageUrl = "";
            }
            map.put("appHeadImageUrl", appHeadImageUrl);
            map.put("name", adUserConn.getName());
            map.put("totalIntegral",totalIntegral);
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
