package doooly;

import com.doooly.common.meituan.MeituanConstants;
import com.doooly.common.meituan.MeituanProductTypeEnum;
import com.doooly.common.meituan.RsaUtil;
import com.doooly.common.util.BeanMapUtil;
import com.doooly.entity.meituan.EasyLogin;
import com.google.gson.Gson;
import com.reach.redis.utils.GsonUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghai on 2018/12/13.
 */
public class TestMain {


    private static String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI7GfP9Zfqai1UFcEP/9t0CZj4wO47aU7kkkSE+uL69CDb83m+kX1/Nn0W3AFuaLrXx/Ny6ZbO1Gu9ziP+9IwMR/pPkwhh6FpFY2domp0h84Wpp7XZxB6xcYAUkiE2XygkEuuDqyqHBhxSUyfXPznmDDC3l+FvQgt5TxPTh7O3lnAgMBAAECgYA6NWQ6uuLuzw5Aomdv5qGynaivglaGVru7aCZvDeX0/uoZ3nMbGhR58QaqRxlPDv1A96Cox/Zn2mG3ESrdxHyKQL90g+r4gNWZONc/KPLsG14nvjAlxhQ558p5N9x5IRpmq8r2r9XJoGJDk5rqO3dSyEUGuCHP9Aqqu2TD9Za8SQJBAOznLQ7nmEKR6R5xqe9uuQR1KoI12gboU8K4X3j3S2WD2CBJPEf0luuRSsBPokdtZdKbEr7c6IZxlDSHvBhZVT0CQQCaSNs6S7XL0swAPlxx1D777hGpiV9P42J+7j7roo6Gj0oxiHJLMeyLu4hzjdCJ0+zeS0sh6HEC7ER+qnlE29tzAkB6SeNCfF5mjrdNldLo27j6ChlFWdMQGcGTFGWEJfNvlZ1tHSDW6/Uz6K4zk2frgxc6nf4RNCt7qwmcDC0WTJbpAkEAiwllJx/bcRdCaGXKgXo4WGiu2g3GKwRLWv/xDACuWG0A+6pu9XzEIxiZWylN6Sdmqt1HlAMY9P1erJeMOZW4KQJBAJsipPM/Y/YUhRTnl4sZazamtEriOtu1B6JzWKJ1frYU3ZSBkeYMekadalz/ANVz1FYHhfTcQ7Ti7gIGlcBr48w=";

    @Test
    public void test1() throws Exception{
        EasyLogin easyLogin = new EasyLogin();
        easyLogin.setEntToken("11");
        easyLogin.setStaffNo("22");
        easyLogin.setStaffPhoneNo("33");
        Map<String,Object> paramMap = BeanMapUtil.transBean2Map(easyLogin);
        paramMap =  BeanMapUtil.sortMapByKey(paramMap);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,Object> entry : paramMap.entrySet()) {
            if (sb.length() == 0) {
                sb.append(entry.getKey() + "=" + entry.getValue());
            } else {
                sb.append("&").append(entry.getKey() + "=" + entry.getValue());
            }

        }
        System.out.println(new Gson().toJson(paramMap));
        System.out.println(sb.toString());
        RSAPrivateKey rsaPrivateKey = RsaUtil.loadPrivateKey(private_key);
        String signature = RsaUtil.sign(sb.toString().getBytes(),rsaPrivateKey);
        signature = URLEncoder.encode(signature,"utf-8");
        System.out.println(signature);
    }


    @Test
    public void test2() {
       /* String orderNum = "3LNVWQWLTBwanghaicc";
        String[] a = orderNum.split(MeituanConstants.app_id);
        orderNum = a[1];
        System.out.println(GsonUtils.toString(a) +"|"+ orderNum);*/
       int pageNum = 1;
       int pageSize = 5;
       //int offset = (pageNum - 1) * pageSize;  //0,5   6,11  12,17  2,2

        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        if (start < list.size()) {
          if (end < list.size()) {
              System.out.println(new Gson().toJson(list.subList(start,end)));
          } else {
              System.out.println(new Gson().toJson(list.subList(start,list.size())));
          }
        }
        System.out.println(new Gson().toJson(list.subList(list.size() - 1,list.size())));

    }

    @Test
    public void test3() {
        System.out.println(getStrEncode("%E6%B5%8B%E8%AF%95%E4%BA%A7%E5%93%81"));
        try {
            String s = URLEncoder.encode("测试产品","UTF-8");
            System.out.println(s);
            System.out.println(URLDecoder.decode(s,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test4() {
        MeituanProductTypeEnum  meituanProductTypeEnum =  MeituanProductTypeEnum.getMeituanProductTypeByCode("sqt_home");
        System.out.println(meituanProductTypeEnum.getCode() + "|" + meituanProductTypeEnum.getDesc());
    }


    private String getStrEncode(String s) {
        List<String> encodes = Arrays.asList("GB2312","ISO-8859-1","UTF-8","GBK");
        for (int i = 0; i < encodes.size(); i++) {
            try {
                if (s.equals(new String(s.getBytes(encodes.get(i)),encodes.get(i)))) {
                    return encodes.get(i);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    @Test
    public void test5() {
        String s = "40LDXMG0HX_407915805307626";
        String[] a = s.split("_");
        System.out.println(a[1]);
    }

}
