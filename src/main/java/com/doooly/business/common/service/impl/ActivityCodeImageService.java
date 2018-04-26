package com.doooly.business.common.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.business.common.constant.ConnectorConstants.WechatConstants;
import com.doooly.business.common.service.ActivityCodeImageServiceI;
import com.doooly.business.common.utils.CodeUtil;
import com.doooly.common.constants.ConstantsV2.SystemCode;
import com.doooly.common.util.WechatUtil;
import com.doooly.common.webservice.WebService;
import com.doooly.dao.reachad.AdConfigDictDao;
import com.doooly.dao.reachlife.LifeWechatBindingDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachlife.LifeWechatBinding;
import com.wechat.ThirdPartyWechatUtil;
import com.wechat.vo.ActionInfoQRCode;
import com.wechat.vo.QRCode;
import com.wechat.vo.QRCodeRes;
import com.wechat.vo.SceneQRCode;

/**
 * @version 1.0
 * 推送图片和文字
 */
@Service
public class ActivityCodeImageService implements ActivityCodeImageServiceI {
	
	private static Logger logger = Logger.getLogger(ActivityCodeImageService.class);
//	private static StringRedisTemplate redisTemplate = (StringRedisTemplate)SpringContextHolder.getBean("redisTemplate");

	@Autowired
	private AdConfigDictDao dictDao;
	@Autowired
	private LifeWechatBindingDao wechatBindingDao;
	@Autowired
	private StringRedisTemplate redisTemplate;
	/**
	 * 画布合并图片
	 * */
	 @Override
	 public BufferedImage overlapImage(BufferedImage big, BufferedImage small,int height) {
         try {
           Graphics2D g = big.createGraphics();
           int x = (big.getWidth() - small.getWidth()) /2;
           int y = 0;
           if (height == 0 ) {
        	   y = (big.getHeight() - small.getHeight())/2;
           }else if (height == 1){
        	   y = (big.getHeight() - small.getHeight()) -130;
           }else if (height == 2) {
        	   x = 25;
        	   y = 25;
		}
           g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
           g.dispose();
//           ImageIO.write((RenderedImage)big, "png", new File("C:/Users/80418/Desktop/datas/10.png"));
         } catch (Exception e) {
           e.printStackTrace();
         }
         return big;
       }
	 	@Override
		public String sendText(String accessToken,String openId,JSONObject linkUrl, String sourceOpenId){
//			String accessToken = ThirdPartyWechatUtil.getAccessToken();
//			String accessToken = "8_McIFN7SumjwCT2_8xheg8fW1Bjzzc5lzyNMqidytgHIZq8m3twl1_hoqBd9cp1O6-SWfhgUjg-6Y5WzAV0DzXcCNEhbHQcRwEYlE-w-lVPBgjfCI9oesVWu3njJjM9mvL2x2Lng3jI-jWZcfDMMeAJAVEE";
			logger.info("推文获取的accessToken为:"+accessToken);
			JSONObject textMsg = new JSONObject();
			textMsg.put("touser", openId);
			textMsg.put("msgtype", "text");
			JSONObject context = new JSONObject();
			//todo...按实际业务需要进行替换
//			String contextStr = "心灵鸡汤\n\n<a href=\"https://baidu.com\">点此进入</a>";
//			                                          心灵鸡汤\n\n<a href=\"https://baidu.com\">点此进入</a>
			String phoneFee = linkUrl.getString("phoneFee");
			String phoneFeeUrl = linkUrl.getString("phoneFeeUrl");
			String contextStr = phoneFee.replace("URL", phoneFeeUrl+sourceOpenId);
			context.put("content", contextStr);
			textMsg.put("text", context);
			String result = ThirdPartyWechatUtil.sendCustomMessage(textMsg.toJSONString(), accessToken);
			logger.info("调用微信客服接口推文返回结果"+result);
			return result;
		}
	 	@Override
		public String sendImage(String accessToken,String openId,String mediaId){
//			String accessToken = ThirdPartyWechatUtil.getAccessToken();
//			String accessToken = "8_fIxLCM95-WZ9funEFT0kUUvpKTfBuBmBLAphfN8j7iMG4SlP6gUj5L1ExeRb8SJYmQDW9pmGQqTgEVgqiW8MEmWx-6FiRB69P-f3oDdWIFYnlYUadpePJ5vZYGFimXjCsrV9qkWPf1vNT3tWOHEiAJAKDA";
//			logger.info(accessToken);
			JSONObject imageMsg = new JSONObject();
			imageMsg.put("touser", openId);
			imageMsg.put("msgtype", WechatConstants.MESSAGE_TYPE_IMAGE);
			JSONObject image = new JSONObject();
			image.put("media_id", mediaId);
			imageMsg.put("image", image);
			logger.info(imageMsg.toJSONString());
			String result = ThirdPartyWechatUtil.sendCustomMessage(imageMsg.toJSONString(), accessToken);
			logger.info("调用微信客服接口推图片返回结果"+result);
			return result;
		}
		@Override
		public String sendActivityText(String accessToken, String openId,String sourceOpenId) {
			return this.sendText(accessToken, openId, this.getPhoneFee(),sourceOpenId);
			
		}
		@Override
		public String sendActivityImage(String accessToken, String openId, String mediaId) {
			// TODO Auto-generated method stub
			return this.sendImage(accessToken, openId, mediaId);
		}
		@Override
		public String getQRCodeUrl(String accessToken, String openId, String channel, String activityId) {
			QRCode qrCode = new QRCode();
			qrCode.setAction_name("QR_STR_SCENE");
			qrCode.setExpire_seconds(2592000l);
			ActionInfoQRCode actionInfoQRCode = new ActionInfoQRCode();
			SceneQRCode sceneQRCode = new SceneQRCode();
			sceneQRCode.setScene_str(channel+"~"+activityId+"~"+openId);
//			sceneQRCode.setScene_id(scene_id);
			actionInfoQRCode.setScene(sceneQRCode);
			qrCode.setAction_info(actionInfoQRCode);
			QRCodeRes result = ThirdPartyWechatUtil.createQRCode(qrCode, accessToken);
			logger.info("调用微信生成二维码参数返回结果"+result.toString());
			return result.getUrl();
		}
		@Override
		public MessageDataBean pushImageAndText(String openId, String sourceOpenId,String channel,String activityId){
			MessageDataBean messageDataBean = new MessageDataBean();
			HashMap<String, Object> data = new HashMap<String, Object>();
//			logger.info(new File(".").getAbsolutePath());
//			logger.info(WebService.getRootPath());
			JSONObject token = WechatUtil.getAccessTokenTicketRedisByChannel(channel);
			String accessToken = token.getString("accessToken");
			BufferedImage big = null;
			BufferedImage small = null;
			BufferedImage headImageAndNiceName = null;
			try {
				big = ImageIO.read(new FileInputStream(WebService.getRootPath()+"/image/activityImage.jpg"));
				String qrCodeUrl = this.getQRCodeUrl(accessToken, openId,channel,activityId);
				small = CodeUtil.newQRCode(qrCodeUrl,270,270, "", "png");
				big= this.overlapImage(big, small,1);
				//进行微信头像和昵称的插入
//			LifeWechatBinding wechatData = this.getWechatData(openId);
				Map<String, String> wechatUserByOpenId = WechatUtil.getWechatUserByOpenId(openId, channel);
				if (wechatUserByOpenId != null) {
					//下载headurl到本地
					headImageAndNiceName = this.downLoadHeadImage(wechatUserByOpenId.get("headimgurl"));
					//将头像和昵称拼到big的左上角
					big= this.overlapImage(big, headImageAndNiceName,2);
					Graphics2D g = big.createGraphics();
					Font f = new Font("宋体",Font.BOLD,25);  
					Color mycolor = Color.white;//new Color(0, 0, 255);  
					g.setColor(mycolor);  
					g.setFont(f); 
					g.drawString(wechatUserByOpenId.get("nickname"), 90, 58);
					g.dispose();
//        		ImageIO.write((RenderedImage)big, "png", new File("C:/Users/80418/Desktop/datas/wechat.png"));
				}
				String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token="+accessToken+"&type=image";
				String mediaIdJson = CodeUtil.uploadFile(url, big,openId);
				data.put("uploadCode", mediaIdJson);
				JSONObject mJsonObject=JSON.parseObject(mediaIdJson);
				String mediaId = mJsonObject.getString("media_id");
				logger.info("上传微信服务器素材返回mediaId为:"+mediaId);
				sourceOpenId = channel+"~"+activityId+"~"+sourceOpenId;
				//执行微信推文发送
				String sendActivityText = this.sendActivityText(accessToken,openId,sourceOpenId);
				data.put("textCode", sendActivityText);
				//执行微信图片发送
				String sendActivityImage = this.sendActivityImage(accessToken,openId,mediaId);
				data.put("imageCode", sendActivityImage);
				messageDataBean.setData(data);
				messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
				messageDataBean.setMess(SystemCode.SUCCESS.getMsg());
				logger.info("调用微信推送图片和文字接口返回结果"+messageDataBean.toJsonString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				big = null;
				small = null;
				headImageAndNiceName=null;
			}
			return messageDataBean;
		}
		
		private BufferedImage downLoadHeadImage(String url) {
			// TODO Auto-generated method stub
			byte[] btImg = getImageFromNetByUrl(url);    
			BufferedImage big;
			BufferedImage convertImage = null;
			InputStream in;
	        if(null != btImg && btImg.length > 0){  
	        	in = new ByteArrayInputStream(btImg);
				try {
					big = ImageIO.read(in);
//					ImageIO.write((RenderedImage)big, "png", new File("C:/Users/80418/Desktop/datas/wechat0.png"));
					convertImage= scaleByPercentage(big, 50,50); 
//					ImageIO.write((RenderedImage)convertImage, "png", new File("C:/Users/80418/Desktop/datas/wechat1.png"));
					convertImage = convertCircular(convertImage);  
//					ImageIO.write((RenderedImage)convertImage, "png", new File("C:/Users/80418/Desktop/datas/wechat2.png"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					IOUtils.closeQuietly(in);
				}
				return convertImage;
				}
			return null;
		}
		/**
		 * 从缓存获得phoneFee
		 * 
		 * @return
		 */
		public JSONObject getPhoneFee(){
			String phoneFee = redisTemplate.opsForValue().get("PHONE_FEE");
			if (StringUtils.isBlank(phoneFee)) {
				JSONObject wehcatJson = new JSONObject();
				phoneFee = dictDao.getValueByKey("PHONE_FEE");
				String phoneFeeUrl = dictDao.getValueByKey("PHONE_FEE_URL");
				wehcatJson.put("phoneFee", phoneFee);
				wehcatJson.put("phoneFeeUrl",phoneFeeUrl);
				redisTemplate.opsForValue().set("PHONE_FEE", wehcatJson.toJSONString(), 24*60*60L,TimeUnit.SECONDS);
				return wehcatJson;
			} else {
				return JSON.parseObject(phoneFee);
			}
			
		}
		@Override
		public void deleteTextInRedis() {
			redisTemplate.delete("PHONE_FEE");
		}
		@Override
		public LifeWechatBinding getWechatData(String openId) {
			// TODO Auto-generated method stub
			LifeWechatBinding wechatBinding = wechatBindingDao.getDataByOpenId(openId);
			if (wechatBinding != null) {
				if (StringUtils.isNotBlank(wechatBinding.getHeadImgurl())) {
					if (wechatBinding.getNickName1() == null) {
						wechatBinding.setNickName("***");
					} else {
						try {
							String s = new String(wechatBinding.getNickName1(), "UTF-8");
							char[] c = s.toCharArray();
							wechatBinding.setNickName(new String(c));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					return wechatBinding;
				}
			}
			return null;
		}
		
		/**  
	     * 将图片写入到磁盘  
	     * @param img 图片数据流  
	     * @param fileName 文件保存时的名称  
		 * @return 
	     */    
	    public static File writeImageToDisk(byte[] img, String fileName){    
	        try {    
	            File file = new File(WebService.getRootPath()+"/image" + fileName+".jpg");    
	            FileOutputStream fops = new FileOutputStream(file);    
	            fops.write(img);    
	            fops.flush();    
	            fops.close();    
//	            System.out.println("图片已经写入到D盘");   
	            return file;
	        } catch (Exception e) {    
	            e.printStackTrace();    
	        }    
	        return null;
	    }    
	    /**  
	     * 根据地址获得数据的字节流  
	     * @param strUrl 网络连接地址  
	     * @return  
	     */    
	    public static byte[] getImageFromNetByUrl(String strUrl){    
	    	InputStream inStream = null ;//通过输入流获取图片数据    
	        try {    
	            URL url = new URL(strUrl);    
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
	            conn.setRequestMethod("GET");    
	            conn.setConnectTimeout(1000);    
	            inStream = conn.getInputStream();//通过输入流获取图片数据    
	            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据    
	            return btImg;    
	        } catch (Exception e) {    
	            e.printStackTrace();    
	        } finally {
				IOUtils.closeQuietly(inStream);
			}  
	        return null;    
	    }    
	    /**  
	     * 从输入流中获取数据  
	     * @param inStream 输入流  
	     * @return  
	     * @throws Exception  
	     */    
	    public static byte[] readInputStream(InputStream inStream){    
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();    
	        byte[] buffer = new byte[1024];    
	        int len = 0;    
	        try {
				while( (len=inStream.read(buffer)) != -1 ){    
				    outStream.write(buffer, 0, len);    
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				IOUtils.closeQuietly(inStream);
			}    
	        return outStream.toByteArray();    
	    }    
	    /** 
	     * 缩小Image，此方法返回源图像按给定宽度、高度限制下缩放后的图像 
	     *  
	     * @param inputImage 
	     * @param maxWidth 
	     *            ：压缩后宽度 
	     * @param maxHeight 
	     *            ：压缩后高度 
	     * @throws java.io.IOException 
	     *             return 
	     */  
	    public static BufferedImage scaleByPercentage(BufferedImage inputImage, int newWidth, int newHeight) throws Exception {  
	        // 获取原始图像透明度类型  
	        int type = inputImage.getColorModel().getTransparency();  
	        int width = inputImage.getWidth();  
	        int height = inputImage.getHeight();  
	        // 开启抗锯齿  
//	        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
	        // 使用高质量压缩  
//	        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);  
	        BufferedImage img = new BufferedImage(newWidth, newHeight, type);  
	        Graphics2D graphics2d = img.createGraphics();  
//	        graphics2d.setRenderingHints(renderingHints);  
	        graphics2d.drawImage(inputImage, 0, 0, newWidth, newHeight, 0, 0, width, height, null);  
//	        graphics2d.drawImage(inputImage, 0, 0, newWidth, newHeight,  null);  
	        graphics2d.dispose();  
	        return img;  
	    } 
	    /** 
	     * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的 
	     *  
	     * @param url 
	     *            用户头像地址 
	     * @return 
	     * @throws IOException 
	     */  
	    public BufferedImage convertCircular(BufferedImage bi1) throws Exception {  
	        // 透明底的图片  
	        BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);  
	        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());  
	        Graphics2D g2 = bi2.createGraphics();  
	        g2.setClip(shape);  
	        // 使用 setRenderingHint 设置抗锯齿  
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
	        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  
	        g2.drawImage(bi1, 0, 0, null);  
	        // 设置颜色  
	        g2.setBackground(Color.green);  
	        g2.dispose();  
	        return bi2;  
	    }  
}
