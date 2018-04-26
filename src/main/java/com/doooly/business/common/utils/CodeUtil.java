package com.doooly.business.common.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class CodeUtil {
	//二维码颜色  
    private static final int BLACK = 0xFF000000;  
    //二维码颜色  
    private static final int WHITE = 0xFFFFFFFF;  
	
	public static BufferedImage newQRCode(String text, int width, int height, String outPutPath, String imageType) throws Exception {
		Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();  
        //设置编码字符集  
        his.put(EncodeHintType.CHARACTER_SET, "utf-8");  
        try {  
            //1、生成二维码  
            BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, his);  
              
            //2、获取二维码宽高  
            int[] rec = encode.getEnclosingRectangle();

    		int resWidth = rec[2] + 1;
    		int resHeight = rec[3] + 1;

    		BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
    		resMatrix.clear();
    		for (int i = 0; i < resWidth; i++) {
    			for (int j = 0; j < resHeight; j++) {
    				if (encode.get(i + rec[0], j + rec[1]))
    					resMatrix.set(i, j);
    			}
    		}
    		int codeWidth = resMatrix.getWidth();  
    		int codeHeight = resMatrix.getHeight();  
//    		int codeWidth = encode.getWidth();  
//    		int codeHeight = encode.getHeight();  
            //3、将二维码放入缓冲流  
            BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);  
            for (int i = 0; i < codeWidth; i++) {  
                for (int j = 0; j < codeHeight; j++) {  
                    //4、循环将二维码内容定入图片  
                    image.setRGB(i, j, resMatrix.get(i, j) ? BLACK : WHITE);  
                }  
            }  
            return image;
//            File outPutImage = new File(outPutPath);  
//            //如果图片不存在创建图片  
//            if(!outPutImage.exists())  
//                outPutImage.createNewFile();  
//            //5、将二维码写入图片  
//            ImageIO.write(image, imageType, outPutImage);  
        } catch (WriterException e) {  
            e.printStackTrace();  
            System.out.println("二维码生成失败");  
        } 
        return null;
	}
	
	/**  
     * 模拟form表单的形式 ，上传文件 以输出流的形式把文件写入到url中，然后用输入流来获取url的响应  
     * @param url  
     *            请求地址 微信接口地址比如http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=image 
     * @param filePath  
     *            文件在服务器保存路径比如localhost:8080/WeChat/file/xxx.jpg   
     * @return String url的响应信息返回值  
     * @throws IOException  
     */    
    public static String uploadFile(String url, BufferedImage image,String openId){    
    	 String result = null;    
         
         InputStream inputStream = null;  
         BufferedReader reader = null;    
         OutputStream out = null;
         try {    
//             File file = new File(filePath);  
//             inputStream = new FileInputStream(file);  
        	 ByteArrayOutputStream os = new ByteArrayOutputStream();  
        	 ImageIO.write(image, "gif", os);  
        	 inputStream = new ByteArrayInputStream(os.toByteArray());
             URL urlObj = new URL(url);    
             // 连接    
             HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();    
             /**  
              * 设置关键值  
              */    
             con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式    
             con.setDoInput(true);    
             con.setDoOutput(true);    
             con.setUseCaches(false); // post方式不能使用缓存    
             // 设置请求头信息    
             con.setRequestProperty("Connection", "Keep-Alive");    
             con.setRequestProperty("Charset", "UTF-8");    
             // 设置边界    
             String BOUNDARY = "---------------------------" + System.currentTimeMillis();    
             con.setRequestProperty("Content-Type", "multipart/form-data; boundary="    
                     + BOUNDARY);    
             // 请求正文信息    
             // 第一部分：    
             StringBuilder sb = new StringBuilder();    
             String regex = ".*/([^\\.]+)";  
             sb.append("--"); // 必须多两道线    
             sb.append(BOUNDARY);    
             sb.append("\r\n");    
             sb.append("Content-Disposition: form-data;name=\"media\";filename=\""    
                     + openId+".png" + "\"\r\n");    
             sb.append("Content-Type:application/octet-stream\r\n\r\n");    
             byte[] head = sb.toString().getBytes("utf-8");    
             // 获得输出流    
             out = new DataOutputStream(con.getOutputStream());    
             // 输出表头    
             out.write(head);    
             // 文件正文部分    
             // 把文件已流文件的方式 推入到url中    
             int bytes = 0;  
             byte[] bufferOut = new byte[1024];  
             while ((bytes = inputStream.read(bufferOut)) != -1) {  
                 out.write(bufferOut, 0, bytes);  
             }  
             // 结尾部分    
             byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线    
             out.write(foot);    
             out.flush();    
         
             StringBuffer buffer = new StringBuffer();    
             // 定义BufferedReader输入流来读取URL的响应    
             reader = new BufferedReader(new InputStreamReader(    
                     con.getInputStream()));    
             String line = null;    
             while ((line = reader.readLine()) != null) {    
                 buffer.append(line);    
             }    
             if (result == null) {    
                 result = buffer.toString();    
                 result = result.replaceAll("\\\\/", "/");
             }    
         } catch (IOException e) {    
             e.printStackTrace();    
             return null;    
         } finally {    
             if (reader != null) {    
                 try {  
                     reader.close();  
                 } catch (IOException e) {  
                     e.printStackTrace();  
                 }    
             }    
             if (inputStream != null) {    
             	try {  
             		inputStream.close();  
             	} catch (IOException e) {  
             		e.printStackTrace();  
             	}    
             }    
             if (out != null) {    
             	try {  
             		out.close();  
             	} catch (IOException e) {  
             		e.printStackTrace();  
             	}    
             }    
         }    
         return result;    
    }
    
}