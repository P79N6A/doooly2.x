package com.doooly.common.util;

import com.doooly.business.pay.bean.WxPrePayParams;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class XmlUtil {
	// 转为xml
	public static String toXml(Object data) {
		XStream xs = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xs.alias("xml", WxPrePayParams.class);
		return xs.toXML(data);
	}

	// 转为object
	public static Object fromXML(Class type, String data) {
		XStream xs = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xs.alias("xml", type);
		return xs.fromXML(data);
	}

	// 转为object
	public static Object fromXML(Class type, String alias, String data) {
		XStream xs = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xs.alias(alias, type);
		return xs.fromXML(data);
	}
}
