package com.doooly.business.pay.processor.afterpayprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.doooly.common.context.SpringContextHolder;

/**
 * 交易成功后处理工厂类<br>
 * 2017-10-21 16:14:56 WANG
 */
public class AfterPayProcessorFactory{

	private static List<AfterPayProcessor> processorBeanList = new ArrayList<AfterPayProcessor>();
	
	static{
		Map<String, AfterPayProcessor> map = SpringContextHolder.getBeansOfType(AfterPayProcessor.class);
		for (Map.Entry<String, AfterPayProcessor> ele : map.entrySet()) {
			processorBeanList.add(ele.getValue());
		}
	}
	
	public static List<AfterPayProcessor> getAllProcessors() {
		return processorBeanList;
	}

}
