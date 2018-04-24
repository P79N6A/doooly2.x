package com.doooly.common.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ComputedRangeUtil {
	private static String getLonLat(String key,String address){
        //返回输入地址address的经纬度信息, 格式是 经度,纬度
        String queryUrl = "http://restapi.amap.com/v3/geocode/geo?key="+key+"&address="+address;
        String queryResult = getResponse(queryUrl);  //高德接品返回的是JSON格式的字符串

        JSONObject jo = JSON.parseObject(queryResult);
        JSONArray ja = jo.getJSONArray("geocodes");
        return JSON.parseObject(ja.getString(0)).get("location").toString();
    }

    private static Long getDistance(String key,String startLonLat, String endLonLat){
        //返回起始地startAddr与目的地endAddr之间的距离，单位：米
        Long result = new Long(0);
        String queryUrl = "http://restapi.amap.com/v3/distance?key="+key+"&origins="+startLonLat+"&destination="+endLonLat;
        String queryResult = getResponse(queryUrl);
        JSONObject jo = JSON.parseObject(queryResult);
        JSONArray ja = jo.getJSONArray("results");

        result = Long.parseLong(JSON.parseObject(ja.getString(0)).get("distance").toString());
        return result;
    }

    private static String getResponse(String serverUrl){
        //用JAVA发起http请求，并返回json格式的结果
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while((line = in.readLine()) != null){
                result.append(line);
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
//	public static void main(String[] args) {
//		System.out.println(getDistance("ff5428b094993a25c82dd63e1a0f01f9", "上海市浦东新区浦电路280号", "上海市闵行区浦江镇人才公寓"));
//	}
	/**
	* 计算两个经纬度直接的距离
	* 
	* @param lng1
	* @param lat1
	* @param lng2
	* @param lat2
	* @return
	*/
	private static final double EARTH_RADIUS = 6378.137;  
	  
	private static double rad(double d) {  
	    return d * Math.PI / 180.0;  
	}  
	  
	public static double GetDistance(double lat1, double lng1, double lat2,  
	     double lng2) {  
	 double radLat1 = rad(lat1);  
	    double radLat2 = rad(lat2);  
	    double a = radLat1 - radLat2;  
	    double b = rad(lng1) - rad(lng2);  
	    double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
	            + Math.cos(radLat1) * Math.cos(radLat2)  
	            * Math.pow(Math.sin(b / 2), 2)));  

	    s = s * EARTH_RADIUS;  
	    BigDecimal bg = new BigDecimal(s);
	    
	    s = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
	    return s;  
	}  
	public static void main(String[] args) {
		double getDistance = GetDistance( 31.221613,121.53116, 31.224639, 121.522227);
		System.out.println(getDistance);
	}
}