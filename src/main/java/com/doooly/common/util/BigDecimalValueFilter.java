package com.doooly.common.util;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.math.BigDecimal;

/**
 * @Description: fastJson 数字格式化filter
 * @author: qing.zhang
 * @date: 2018-11-02
 */
public class BigDecimalValueFilter implements ValueFilter {

    public Object process(Object object, String name, Object value) {

        if (null != value && value instanceof BigDecimal) {

            String formatvalue = String.format("%.2f", ((BigDecimal) value).doubleValue());
            return formatvalue;
        }
        return value;
    }
}
