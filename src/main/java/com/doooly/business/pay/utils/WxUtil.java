package com.doooly.business.pay.utils;


import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doooly.common.util.MD5Utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class WxUtil { 
	
	protected static Logger logger = LoggerFactory.getLogger(WxUtil.class);

	//转为xml
	public static String toXml(Object data,Class type){
		XStream xs = new XStream(new XppDriver(new NoNameCoder()) {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    // 对所有xml节点的转换都增加CDATA标记
                    boolean cdata = true;
                    @Override
                    @SuppressWarnings("rawtypes")
                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                    }
                    @Override
                    public String encodeNode(String name) {
                        return name;
                    }
                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        });
		xs.alias("xml", type);
		return xs.toXML(data);
	}
	
	//转为object
	public static Object fromXML( String data,Class type) {
		XStream xs = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xs.alias("xml", type);
		return xs.fromXML(data);
	}
	
	@SuppressWarnings("rawtypes")   
	public static String createSign(SortedMap<String,Object> parameters,String key){  
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		// 所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		logger.info("deeplink = {}" , sb.toString());
		String sign = MD5Utils.encode(sb.toString()).toUpperCase();
		return sign;
	}
	
	/**
	 * 微信签名验证
	 */
	public static boolean verify(SortedMap<String, Object> map, String sign, String key) {
		if (!StringUtils.isEmpty(sign) && map != null) {
			StringBuffer sb = new StringBuffer();
			Set es = map.entrySet();
			Iterator it = es.iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String k = (String) entry.getKey();
				Object v = entry.getValue();
				if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k) && !"class".equals(k)) {
					sb.append(k + "=" + v + "&");
				}
			}
			sb.append("key=" + key);
			String md5 = MD5Utils.encode(sb.toString()).toUpperCase();
			return sign.equalsIgnoreCase(md5);
		}
		return false;
	}
	
		
	public static String getNonceStr() { 
	    Random random = new Random(); 
	    return MD5Utils.encode(String.valueOf(random.nextInt(10000))); 
	  } 
	   
	  public static String getTimeStamp() { 
	    return String.valueOf(System.currentTimeMillis() / 1000); 
	  } 

}

