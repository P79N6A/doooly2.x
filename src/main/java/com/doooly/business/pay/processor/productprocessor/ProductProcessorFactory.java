package com.doooly.business.pay.processor.productprocessor;

import java.util.HashMap;
import java.util.Map;

import com.doooly.common.context.SpringContextHolder;

/**
 * 处理指定类型的订单工厂类
 * 2017-10-21 16:14:00 WANG
 */
public class ProductProcessorFactory{

	private static Map<Integer, ProductProcessor> processorBeanMap;
	
	static{
		Map<String, ProductProcessor> map = SpringContextHolder.getBeansOfType(ProductProcessor.class);
		processorBeanMap = new HashMap<Integer, ProductProcessor>();
		for (Map.Entry<String, ProductProcessor> ele : map.entrySet()) {
			processorBeanMap.put(ele.getValue().getProcessCode(), ele.getValue());
		}
	}

	public static <T extends ProductProcessor> T getProcessor(int processCode) {
		return (T) processorBeanMap.get(processCode);
	}

}
