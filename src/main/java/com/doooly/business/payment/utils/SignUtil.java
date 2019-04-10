package com.doooly.business.payment.utils;

import com.doooly.common.util.MD5Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Description: 加密工具类
 * @author: qing.zhang
 * @date: 2018-07-27
 */
public class SignUtil {

    /**
     * 创建签名
     * @param parameters
     * @return
     */
    public static String createSign(SortedMap<Object,Object> parameters,String client_secret){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v) && !"client_secret".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("client_secret="+client_secret);
        System.out.println(sb.toString());
        return MD5Utils.encode(sb.toString()).toLowerCase();
    }


    public static void main(String[] args) {
        SortedMap<Object,Object> parameters = new TreeMap<>();
        parameters.put("client_id", "test_doooly");
        long value = System.currentTimeMillis() / 1000;
        parameters.put("timestamp", "1111111111");
        String sign = createSign(parameters,"test_doooly");
        System.out.println(sign);
    }
}
