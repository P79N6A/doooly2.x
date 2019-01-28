package doooly.util.test;

import org.junit.Test;

import com.business.common.util.HttpClientUtil;

public class DaHuaTest {
	@Test
	public void test() {
		String json = "{\"jsonData\":\"E99450D302ED04128295F2E1DBF587E1099485F593B026390C2504D3F8B5CE548B0FF30BAF1CCCA67BD6A81A00968CFAE91E58C7D8D54B1CF9481ECD846AF447AE985A9D8B7C15266FA706C7464D922DF767BD37A7C123D854E49CA6EC12CA79C0912C1923E0D353C491BD3A24D3BEE3A3E58AD788089976100CDD38B2C1BEB8FAE8DEBB1933F186D8403D64C5007286\"}";
		String url = "https://app.dahuatech.com/getMobileOfficeDataService.svc/MarketingVadationNew";
		String result = HttpClientUtil.doPost(url, json);
		
		System.out.println(result);
	}
}
