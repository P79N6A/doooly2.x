package com.doooly.business.pay.processor.refundprocessor;

import com.doooly.common.context.SpringContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 退款后处理任务工程类
 *@author  2017-11-29 16:43:16 WANG
 */
public class AfterRefundProcessorFactory {

	private static List<AfterRefundProcessor> processorBeanList = new ArrayList<AfterRefundProcessor>();

	static{
		Map<String, AfterRefundProcessor> map = SpringContextHolder.getBeansOfType(AfterRefundProcessor.class);
		for (Map.Entry<String, AfterRefundProcessor> ele : map.entrySet()) {
			processorBeanList.add(ele.getValue());
		}
	}

	public static List<AfterRefundProcessor> getAllProcessors() {
		return processorBeanList;
	}
}
