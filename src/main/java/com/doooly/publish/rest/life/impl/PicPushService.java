package com.doooly.publish.rest.life.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.ActivityCodeImageServiceI;
import com.doooly.business.common.utils.CodeUtil;
import com.doooly.common.constants.ConstantsV2.SystemCode;
import com.doooly.common.util.WechatUtil;
import com.doooly.common.webservice.WebService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.PicPushServiceI;

/**
 * @Description: 推送图片和文字
 * @author: 杨汶蔚
 * @date: 2018-04-18
 */
@Component
@Path("/picPush")
public class PicPushService implements PicPushServiceI {

    private static final Logger logger = LoggerFactory.getLogger(PicPushService.class);

    @Autowired
    private ActivityCodeImageServiceI activityCodeImageServiceI;
    /**
    *	根据openId发送推文和图片
    */
    @POST
	@Path(value="/getPicPush")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String picPush(JSONObject json) {
    	//推文用父openId,推图用子openId
    	String openId = json.getString("openId");
    	String sourceOpenId = json.getString("sourceOpenId");
    	String channel = json.getString("channel");
    	String activityId = json.getString("activityId");
    	MessageDataBean messageDataBean= new MessageDataBean();
    	try {
    		messageDataBean = activityCodeImageServiceI.pushImageAndText(openId,sourceOpenId,channel,activityId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    		logger.error("发送推文和图片出错", e);
			messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
			messageDataBean.setMess(SystemCode.SUCCESS.getMsg());
		}
		return messageDataBean.toJsonString();
	}
    /**
     *	根据openId发送推文
     */
    @POST
	@Path(value="/getTextPush")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String textPush(JSONObject json) {
    	String openId = json.getString("openId");
    	String sourceOpenId = json.getString("sourceOpenId");
    	MessageDataBean messageDataBean= new MessageDataBean();
    	try {
//    		System.out.println(new File(".").getAbsolutePath());
//    		System.out.println(WebService.getRootPath());
			String accessToken = WechatUtil.getCacheAccessToken();
			//执行微信推文发送
			activityCodeImageServiceI.sendActivityText(accessToken,openId,sourceOpenId);
			messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("发送推文出错", e);
			messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
		}
    	return messageDataBean.toJsonString();
	}
    /**
     *	根据openId发送图片
     */
    @POST
    @Path(value="/getImagePush")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String imagePush(JSONObject json) {
    	String openId = json.getString("openId");
    	MessageDataBean messageDataBean= new MessageDataBean();
    	try {
			BufferedImage big =  ImageIO.read(new FileInputStream(WebService.getRootPath()+"/image/activityImage.jpg"));
			BufferedImage small = CodeUtil.newQRCode(openId,270,270, "", "png");
			big= activityCodeImageServiceI.overlapImage(big, small,1);
			String accessToken = WechatUtil.getCacheAccessToken();

			String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token="+accessToken+"&type=image";
			String mediaIdJson = CodeUtil.uploadFile(url, big,openId);
			JSONObject mJsonObject=JSON.parseObject(mediaIdJson);
			String mediaId = mJsonObject.getString("media_id");
			logger.info("上传微信服务器素材返回mediaId为:"+mediaId);
			//执行微信图片发送
			activityCodeImageServiceI.sendActivityImage(accessToken,openId,mediaId);
			messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("发送图片出错", e);
			messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
		}
    	return messageDataBean.toJsonString();
    }
    /**
     *	根据openId发送图片
     */
    @POST
    @Path(value="/deleteTextInRedis")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteTextInRedis(JSONObject json) {
    	MessageDataBean messageDataBean= new MessageDataBean();
    	try {
    		activityCodeImageServiceI.deleteTextInRedis();
    		messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    		messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
    	}
    	return messageDataBean.toJsonString();
    }

    

}
