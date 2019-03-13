package com.doooly.business.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MD5Util {
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	public static String MD5Psw(String psw) {
		String ret = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(psw.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			ret = buf.toString();
			// System.out.println("MD5(" + psw + ",32) = " + ret);
			// System.out.println("MD5(" + psw + ",16) = "+
			// buf.toString().substring(8, 24));
		} catch (Exception e) {
			e.printStackTrace();
			return ret;
		}
		return ret;
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	//字典排序参数
    public static String getSortSignContent(JSONObject req) {
        TreeMap<String, Object> parameters = new TreeMap<>();
        StringBuffer sb = new StringBuffer();
        String result = "";
        Iterator it = req.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            Object val = entry.getValue();
            if (null == val ) {
                continue;
            }
            if (val instanceof String) {
                String value = (String) val;
                if (StringUtils.isBlank(value)) {
                    continue;  // 获取所有返回参数，剔除sign字段以及内容为空或者等于null的字段。
                }
                parameters.put(key, value);
            } else if (val instanceof Integer) {
                int intParam = (int) val;
                parameters.put(key, intParam);
            } else {
                parameters.put(key, entry.getValue());
            }
        }

        TreeMap<String, Object> treeMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        treeMap = (TreeMap<String, Object>) parameters;
        //System.out.println("升序排序结果：" + treeMap);
        //把map中的集合拼接成字符串
        for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sb.append(key).append("=").append(value).append("&");
        }
        if (sb.length() > 0) {
            result = sb.toString().substring(0, sb.toString().length() - 1);
        }
        return result;
    }


}
