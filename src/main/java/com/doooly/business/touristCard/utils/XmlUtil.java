package com.doooly.business.touristCard.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.doooly.business.touristCard.datacontract.base.ResponseStatus;
import com.doooly.business.touristCard.datacontract.response.QueryCardBalanceResponse;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 王晨宇 on 2018/1/17.
 */
public class XmlUtil {
	private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);

	public static Document getDocument(String xml) throws DocumentException {
		return DocumentHelper.parseText(xml);
	}

	public static String getSingleNodeXML(String xml, String xpath) throws DocumentException {
		return getDocument(xml).selectSingleNode(xpath).asXML().toString();
	}

	public static String getSingleNodeXML(Document document, String xpath) {
		return document.selectSingleNode(xpath).asXML().toString();
	}

	public static void main(String[] args) throws DocumentException {
		String resXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sctcd><request><requestType>sctcd.service.cardBal</requestType><merchantNum>320009967</merchantNum><requestDate>20180116174758</requestDate><cardno>30870706712</cardno></request><response><orderResult>00</orderResult><resultDesc>查询成功</resultDesc><cardno>30870706712</cardno><balance>0.0</balance><deposit>20.0</deposit><validate>20091019</validate></response></sctcd>";
		resXML = getSingleNodeXML(resXML, "//sctcd/response");
		XStream xstream = new XStream();
		xstream.alias("response", QueryCardBalanceResponse.class);
		QueryCardBalanceResponse response = (QueryCardBalanceResponse) xstream.fromXML(resXML);
		response.setStatus(ResponseStatus.SUCCESS);
		System.out.println(JSONObject.toJSONString(response, SerializerFeature.WriteMapNullValue));
	}
}
