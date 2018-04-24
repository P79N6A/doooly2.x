package com.doooly.business.myaccount.service.impl;

import java.io.File;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myaccount.service.ComplaintBusinessServiceI;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.FileUtils;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachlife.LifeComplaintDao;
import com.doooly.entity.reachad.AdUserConn;
import com.doooly.entity.reachlife.LifeComplaint;

/**
 * 联系客服保存申述业务Service实现
 * 
 * @author sunzilei
 * @version 2017年7月20日
 */
@Service
@Transactional
public class ComplaintBusinessService implements ComplaintBusinessServiceI {
	
	private static Logger log = Logger.getLogger(ComplaintBusinessService.class);
	// 兜礼配置文件
	private static ResourceBundle dooolyBundle = PropertiesConstants.dooolyBundle;
	// 图片上传路径
	private static String IMAGE_UPLOAD_URL = dooolyBundle.getString("image_upload_url");
	// 图片查看路径
	private static String IMAGE_VIEW_URL = dooolyBundle.getString("image_view_url");

	@Autowired
	private LifeComplaintDao LifeComplaintDao;
	@Autowired
	private AdUserDao adUserDao;
	
	@Override
	public void complaintSave(HttpServletRequest request) {

		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");
				List<FileItem> items = null;
				try {
					//解析request
					items = upload.parseRequest(request);
					log.info("解析的字段条数：" + items.size());
				} catch (FileUploadException e) {
					e.printStackTrace();
					log.info(e);
				}
				StringBuffer imagePaths = new StringBuffer(); 
				JSONObject jsonData = new JSONObject();
				if (items != null && items.size() > 0) {
					for (FileItem fileItem : items) {
						//普通字段
						if (fileItem.isFormField()) {
							String fieldName = fileItem.getFieldName();
							String value = fileItem.getString("UTF-8");
							//value = URLDecoder.decode(value, "utf-8");
							log.info(fieldName + "==" + value);
							jsonData.put(fieldName, value);
						} else {
							//非普通字段
							log.info(fileItem.getFieldName() + "==图片" );
							String date = DateUtil.formatDate(new Date(), "yyyyMM");
							String uploadPath = IMAGE_UPLOAD_URL + date + "/";
							FileUtils.createFolder(uploadPath);
							String fileName = FileUtils.create_nonce_str() + ".jpg";
							uploadPath = uploadPath + fileName;
							File f = new File(uploadPath);
							fileItem.write(f);
							imagePaths.append(IMAGE_VIEW_URL + date + "/" + fileName + ";");
						}
					}
				}
				
				LifeComplaint complaint = JSONObject.toJavaObject(jsonData, LifeComplaint.class);
				
				complaint.setCreateIp(request.getRemoteAddr());
				
				if(StringUtils.isNotBlank(complaint.getUserId()) ){
					//获取用户信息
					AdUserConn user = adUserDao.findAdUserConnByUserId(complaint.getUserId());
					if(user != null){
						complaint.setGroupName(user.getGroupName());
						complaint.setGroupNum(user.getGroupNum());
						complaint.setMemberNum(user.getCardNumber());
						complaint.setCreateBy(user.getName());
						complaint.setUpdateBy(user.getName());
					}else{
						complaint.setCreateBy("");
						complaint.setUpdateBy("");
					}
				}
				
				if(StringUtils.isNotBlank(imagePaths) && imagePaths.length() > 0){
					String imagePath = imagePaths.substring(0, imagePaths.lastIndexOf(";"));
					log.info("图片保存路径  imagePath:" + imagePath);
					complaint.setImagePaths(imagePath);
				}
				
				complaint.setCreateDate(new Date());
				complaint.setUpdateDate(new Date());
				complaint.setModifyDate(new Date());
				complaint.setStatus(LifeComplaint.COMPLAINT_STATUS_COMPLAINTING);
				complaint.setDelFlag(LifeComplaint.DEL_FLAG_NORMAL);
				
				Random rand = new Random();
				// 产生一个 10到99之间（均包含）（两位整数）的随机数
				int randNum = rand.nextInt(90) + 10;
				complaint.setComplaintSn(new Date().getTime() + "" + randNum);
				
				LifeComplaintDao.insert(complaint);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("保存申诉错误" + e);
		}
	}

}
