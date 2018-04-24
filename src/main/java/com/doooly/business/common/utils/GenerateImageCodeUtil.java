package com.doooly.business.common.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.doooly.common.util.ProjectRootPathUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 生成二维码或条形码工具类
 * @author 赵清江
 * @date 2016年7月22日
 * @version 1.0
 */
public class GenerateImageCodeUtil {
	
	private static final int BLACK = 0xFF000000;  
    private static final int WHITE = 0xFFFFFFFF;   

	/**
	 * 图片默认宽度
	 */
	public static final int DEFAULT_BAR_WIDTH = 650;
	/**
	 * 图片默认高度
	 */
	public static final int DEFAULT_BAR_HEIGHT = 154;
	/**
	 * 图片默认宽度
	 */
	public static final int DEFAULT_QR_WIDTH = 500;
	/**
	 * 图片默认高度
	 */
	public static final int DEFAULT_QR_HEIGHT = 500;
	/**
	 * 图片默认格式
	 */
	public static final String DEFAULT_PIC_FORMAT = "png";
	/**
	 * 图片默认存储位置
	 */
	public static final String DEFAULT_SAVE_PATH = ProjectRootPathUtils.getRootPath() + "/code";
	/**
	 * logo默认存储位置
	 */
	public static final String DEFAULT_LOGO_PATH = ProjectRootPathUtils.getRootPath() + "/code/logo.png";
	
	/**
	 * 生成二维码(默认配置)
	 * @param content
	 * @param storePath
	 * @return 该二维码图片地址URL
	 * @throws Exception
	 */
	public static String generateQRCode(String content,String storePath) throws Exception{
		String absolutePath = generateQRCode(content, DEFAULT_PIC_FORMAT, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT, DEFAULT_SAVE_PATH);
		return absolutePath.substring(absolutePath.indexOf("code"));
	}
	/**
	 * 生成二维码(默认配置)
	 * @param content
	 * @return 返回二维码码图片输出流
	 * @throws Exception
	 */
	public static OutputStream generateQRCode(String content) throws Exception{
		return generateQRCode(content, DEFAULT_PIC_FORMAT, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT);
	}
	/**
	 * 生成二维码
	 * @param content
	 * @param format
	 * @param width
	 * @param height
	 * @param storePath
	 * @return 该二维码图片地址URL
	 * @throws Exception
	 */
	public static String generateQRCode(String content,String format,int width,int height,String storePath) throws Exception{
		//生成二维码
		BitMatrix bitMatrix = generateQRCode(content, width, height);
		//保存图片
		String absolutePath = storePath+"/"+getQRCodeName(format);
		Path path = (new File(absolutePath)).toPath();
		MatrixToImageWriter.writeToPath(bitMatrix, format, path);
		return absolutePath;
	}
	/**
	 * 生成二维码(不存储在本地)
	 * @param content
	 * @param format
	 * @param width
	 * @param height
	 * @return 返回二维码码图片输出流
	 * @throws Exception
	 */
	public static OutputStream generateQRCode(String content,String format,int width,int height) throws Exception{
		//生成二维码
		BitMatrix bitMatrix = generateQRCode(content, width, height);
				
		OutputStream stream = new FileOutputStream(new File(""));
		MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
		return stream;
	}
	/**
	 * 生成该logo的二维码图片
	 * @param content 二维码内容
	 * @param logoPath logo图片路径
	 * @param storePath 二维码存储路径
	 * @return 生成后的二维码路径(包含二维码图片名称)
	 * @throws Exception
	 */
	public static String generateQRCodeWithLogo(String content) throws Exception{
		return generateQRCodeWithLogo(content, DEFAULT_PIC_FORMAT, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT, DEFAULT_LOGO_PATH, DEFAULT_SAVE_PATH);
	}
	/**
	 * 生成该logo的二维码图片
	 * @param content 二维码内容
	 * @param format 图片格式(如png)
	 * @param width 二维码图片高度
	 * @param height 二维码图片宽度
	 * @param logoPath logo图片路径
	 * @param storePath 二维码存储路径
	 * @return 生成后的二维码路径(包含二维码图片名称)
	 * @throws Exception
	 */
	public static String generateQRCodeWithLogo(String content,String format,int width,int height,String logoPath,String storePath) throws Exception{
		//生成二维码
		BitMatrix bitMatrix = generateQRCode(content, width, height);
		//保存图片
		String absolutePath = storePath+"/"+getQRCodeName(format);
		File file = new File(absolutePath);
		writeToFile(bitMatrix, format, file, logoPath);
		return absolutePath;
	}
	
	/**
	 * 生成条形码(默认配置)
	 * @param content
	 * @param storePath
	 * @return 该条形码图片地址URL
	 * @throws Exception
	 */
	public static String generateBarCode(String content,String storePath) throws Exception{
		String absolutePath = generateBarCode(content, DEFAULT_PIC_FORMAT, DEFAULT_BAR_WIDTH, DEFAULT_BAR_HEIGHT, DEFAULT_SAVE_PATH);
		return absolutePath.substring(absolutePath.indexOf("code"));
	}
	/**
	 * 生成条形码(默认配置)
	 * @param content
	 * @return 返回条形码图片输出流
	 * @throws Exception
	 */
	public static OutputStream generateBarCode(String content)throws Exception{
		return generateBarCode(content, DEFAULT_PIC_FORMAT, DEFAULT_BAR_WIDTH, DEFAULT_BAR_HEIGHT);
	}
	
	/**
	 * 生成条形码
	 * @param content
	 * @param format
	 * @param width
	 * @param height
	 * @param storePath
	 * @return 该条形码图片地址URL
	 * @throws Exception
	 */
	public static String generateBarCode(String content,String format,int width,int height,String storePath) throws Exception{
		//生成二维码
		BitMatrix bitMatrix = generateBarCode(content, width, height);
		//保存图片
		String absolutePath = storePath+"/"+getBarCodeName(format);
		Path path = (new File(absolutePath)).toPath();
		MatrixToImageWriter.writeToPath(bitMatrix, format, path);
		return absolutePath;
	}
	/**
	 * 生成条形码(不存储在本地)
	 * @param content
	 * @param format
	 * @param width
	 * @param height
	 * @return 返回条形码图片输出流
	 * @throws Exception
	 */
	public static OutputStream generateBarCode(String content,String format,int width,int height) throws Exception{
		//生成二维码
		BitMatrix bitMatrix = generateBarCode(content, width, height);
				
		OutputStream stream = new FileOutputStream(new File(""));
		MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
		return stream;
	}
	
	
	/**
	 * 生成条形码
	 * @param content
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	private static BitMatrix generateBarCode(String content,int width,int height) throws Exception{
		Map<EncodeHintType, Object> map = new HashMap<EncodeHintType,Object>();
		map.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODABAR, width, height,map);
		return deleteWhite(bitMatrix);
	}
	/**
	 * 生成二维码
	 * @param content
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	private static BitMatrix generateQRCode(String content,int width,int height) throws Exception{
		Map<EncodeHintType, Object> map = new HashMap<EncodeHintType,Object>();
		map.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,map);
		return deleteWhite(bitMatrix);
	}
	/**
	 * 生成条形码图片名称
	 * @return
	 */
	private static String getBarCodeName(String format){
		StringBuffer strBuffer = new StringBuffer("BR-");
		strBuffer.append(getTimestamp());
		strBuffer.append("."+format);
		return strBuffer.toString();
	}
	/**
	 * 生成二维码图片名称
	 * @return
	 */
	private static String getQRCodeName(String format){
		StringBuffer strBuffer = new StringBuffer("QR-");
		strBuffer.append(getTimestamp());
		strBuffer.append("."+format);
		return strBuffer.toString();
	}
	/**
	 * 获取时间戳
	 * @return
	 */
	private static String getTimestamp(){
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return formater.format(new Date());
	}
	/**
	 * 
	 * @param matrix
	 * @param format
	 * @param file
	 * @param logoPath
	 * @throws IOException
	 */
	private static void writeToFile(BitMatrix matrix,String format,File file,String logoPath) throws IOException {  
        BufferedImage image = toBufferedImage(matrix);  
        Graphics2D gs = image.createGraphics();  
          
        //载入logo  
        Image img = ImageIO.read(new File(logoPath));  
        gs.drawImage(img, 125, 125, null);  
        gs.dispose();  
        img.flush();  
        if(!ImageIO.write(image, format, file)){  
            throw new IOException("Could not write an image of format " + format + " to " + file);    
        }  
    }  
	/**
	 * 
	 * @param matrix
	 * @return
	 */
	private static BufferedImage toBufferedImage(BitMatrix matrix){  
        int width = matrix.getWidth();  
        int height = matrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
          
        for(int x=0;x<width;x++){  
            for(int y=0;y<height;y++){  
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);  
            }  
        }  
        return image;     
    }  
	/**
	 * 去白边
	 * @param matrix
	 * @return
	 */
	public static BitMatrix deleteWhite(BitMatrix matrix){  
	    int[] rec = matrix.getEnclosingRectangle();  
	    int resWidth = rec[2] + 1;  
	    int resHeight = rec[3] + 1;  
	  
	    BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);  
	    resMatrix.clear();  
	    for (int i = 0; i < resWidth; i++) {  
	        for (int j = 0; j < resHeight; j++) {  
	            if (matrix.get(i + rec[0], j + rec[1]))  
	                resMatrix.set(i, j);  
	        }  
	    }  
	    return resMatrix;  
	}  

}
