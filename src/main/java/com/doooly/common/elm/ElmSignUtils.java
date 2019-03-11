package com.doooly.common.elm;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.payment.constants.PayConstants;
import com.doooly.business.utils.DateUtils;
import com.doooly.business.utils.MD5Util;
import com.doooly.common.util.RandomUtil;
import com.github.pagehelper.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description: 饿了么加密验证工具类
 * @author: qing.zhang
 * @date: 2019-01-03
 */
public class ElmSignUtils {

    //RSA2 标准签名算法
    private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    //public static final String ELM_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCX8NSttpbTyrv+zcnNQMTNIrOnn00l4wK13nS0Hj5qtLTyNErNlZ1j0VRny4zNbe/d2Q5vMLw2PvzTRm2TVfNSJOZwMCISpkQWPRp3GuFM4kHJjDvt/4t+YSZ9d1yCY+/TQiAv2T357ues/repK2o4UOSjEW6hqKQyh7niTuObkCGfZIbVU7MN3Ac+W827JtB+tcEfGRCMRALlbYjEzcbkznvvG+cdnBzrIf6DjIl0oDOobQUJDJ8tvKSLSkqZtix0ijN7p6MFPJqP20FO72EjoLhwbxBIyz4i9btU+vuAHR6rNwq0zCYoQ9Q/9TTpKUaebymdjtohz7QzobIUjAqNAgMBAAECggEANiASgzdSD2xe/+vrAXUbJkBAY2v6HY76vjLau/cyIOzysH00UHwFrzM4sCjwpZWOJydVXwKraV+SzOh/zEaIATEn7bsXyDD2fD2UGe23aJ4I9XVBPA0WchTFk1hb2g8jtcwOjz0COPCEEzpBzr+qfRlD+VsSYAAlTeLPsvCLYDSs+NzNSWrEeDfPEZ0UmreCYsfVcmmf5ODuJF102JH3NZuzjDwEs7apeHtV/3jZyq3QWs/T4w/ksmmoDaMX3CQE0tgmwVYMTdkcwGLjVgPj1HgmO9gBvzrGWhsZPCaNJfhK2HS11Lmi6J+ejXBsKGo7DBtUwZW9Q+kuVMoaTgsFeQKBgQD9NuzZqefQ/agep0K+T/D1vuC2qrhSdNYKuVo3NWCsYw3iD8vvXjmzobyvbalHbMbVDFJEpAEbBCPFM/42H+c6ppDR0/N+uTJKTAi9XSWA5vLRjEn4ahMn/RXg8DFogKYoLt5OMuCd4eEleUq5frU/l7YcHGhC+WkjP6LC+t5FswKBgQCZnLWhNwAX+w7rOnAfq/4/V+vCQVU0GLFJR9KQrV/9wpH8MIFpay0xnQgTP76ekAAyrNuVLlRlPIqn83+wZC+PzsDCmHPukT0klX9EwGL6Q991Sfe4+68krbLm4XH9W3ie4qMwVL+FWvNvoA6JF3C4xd5vgP3mM2e0N+lGsODOvwKBgQDfBRNTGryca+05ImQ8rrgyGa5d9LtnlJQ42cvFuHOvIYdGxlb+OuVje15uvI1N5VayqqmC31hB7UF8HTEXhr3oAHCo/p8FzCOpIJLKBlJT2BWhzogQDY1VuSkd9y/tNPIjjjQqd9Ex/ZKJqOzRt/H3Uq4I0FRcOMQLUa8PQFmo1QKBgBp71Si1VdPMmhbeHWSof44mN2RnZSw1MaOjuV7uNUWH3Slwp28oZkNOgoznCZNG2nHiMX133l8R4AnyxkoAC98sUzl5lUtFSRhcmfOdg7MILqeHGCmRYvHc7KI3rjJzKcmi42W6xsZUFvJg0wC5xNpmauIoiB2/MRr2JMbmSev7AoGAQAT9MZcQWGJQagfqNjYw4V1h9gqMCdWfRR9ncv3dEU7QCSTcIZY01GPI9ie+auELUqTgMryo+wCXHAbX87Xt2wOBcBO6QMSy8uBgNTTiiP+fTzJKwBmBnJc/T3SG9hMeO1Mc7NtSc8qflcEBq4kSxK7D+mbDNiI6S+uRZjVjqxs=";
    //public static final String ELM_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl/DUrbaW08q7/s3JzUDEzSKzp59NJeMCtd50tB4+arS08jRKzZWdY9FUZ8uMzW3v3dkObzC8Nj7800Ztk1XzUiTmcDAiEqZEFj0adxrhTOJByYw77f+LfmEmfXdcgmPv00IgL9k9+e7nrP63qStqOFDkoxFuoaikMoe54k7jm5Ahn2SG1VOzDdwHPlvNuybQfrXBHxkQjEQC5W2IxM3G5M577xvnHZwc6yH+g4yJdKAzqG0FCQyfLbyki0pKmbYsdIoze6ejBTyaj9tBTu9hI6C4cG8QSMs+IvW7VPr7gB0eqzcKtMwmKEPUP/U06SlGnm8pnY7aIc+0M6GyFIwKjQIDAQAB";
    //
    ////饿了么给的公钥
    //public static final String ELM_GIAVE_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwKStAwppUmmNzHyoBcWn8DvDy4P0jJyAo5kt3ShAddRjlB1m6oDh8Nv1h2qYLK0yJb0PjpSIHdP+g88R32d8EvMq0QW+oIi7WoE4Ycpd31cit5s2dX72GMzT4i95D1ATF2WfI1KOjSDr2p+Oip2KhY6siV50fEoxdRCQu1XlcLjg60Spmul6JD3AsOBtLYfn1oWSq2U9bKaK5YvGbZwDdRZef5R//NI7fVdgPh3xRrIAzCqbP09mY1hnaLcHSKwsGrQeZg6Sj2CMwxlnIGkrD6UQuyFhEzRDmU5SdzZ+ZUySFLZ/7bIgZHxL1QtN4kjd5zZzd4lVhFEj3nvqrJzTWQIDAQAB";

    //饿了么生产私钥
    public static final String ELM_PRD_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCyYpGPWKPYW7G4k8LNUyl4mjgf9kca5b7FIqJr73mjq+aKkywlIf/9wND08T40F5X6VXukE4ZWhoJ+wK0n8PbzxwwTjTwwn5XhG3r4e1WUixNBy/yr4o6IsP3U5J06a0tAY3McKefPIzGuPNCHhexnl65zSxWJ8k+4SWB657We8Jz85L7LDndl3Jfg09sbrnkjMgXn/u+elz+BQIgNfLNyEzneEEAgUf1I2VS4dB8SfkgIh3K+VyZXYLEo9UoOVplu7HT4Vjm37gDVMXoXZFWxWsNW58aJ2kCVDii0hDmoDCrIH/dUM7gD5eQbA/g9GUQv+/ECHwm5J6LH8aRasA8xAgMBAAECggEAQ97a8QZRuJUSpl6Z5JM6mSdcKVUDssks70aBM732sv5Jhfn+9tXb10lQd5F+EEXVC10Q4NmI6EzfQ6i9eQ/mddQK1TmMaBef5/kTrY0Hf6/2kLczGJbnkjx9J04gQEToacQs3M9Y4mfKlDeRQ68BxlxgxH8R391w1XYdnkWgxaq1vggIHS9OjRCXnexnD25tavvOnqV801ZYCLBEer6werCpiYQjzPJzVK2hGd7Se5nQAf6Yb88Wl/KrYeM+GY1xpEYd5hdLIfL6hZwN7q+lo5GfqSNSAAfw9yYzkUCD0z6j65fXjZ9o3jXI93N85GNbnNmTJK5chmZFjlcvLIDYAQKBgQDiXCQUQZ/wvBFE9XyLFWyM+XngocB/70lGmEUqhKw3ONxQSKpmIy0IFY7UoTga+VYKHBNqOATYvpBT3F7bbhL2sLXv3eBAkVrDANBQ/GUg9wbl3BfiNiX2KrySfCM5lSSjY0MrWzBIFAPKGgFCCl3/GzvmQIP6ttLV26A0IJ87sQKBgQDJvkBKOQdo+nqMf/jG7LrAtckW993qC4dO14vDwoBPclD/beUam0T3bUm47AsSMKo2RESzDOG0Q3Q1DEqKdg1fzpyB6yZiL83EYPx61LxoU3aAq9iTe8FW9dTByZR0xx8Ex1pzBw8DVwFkhGYpYahzdB2U+WEkYGWTo5IsxIBrgQKBgQCBCeXHZmG26n8twIPvBZeMBevkklEanV44UDWdt2Q1VsTBP6tj7kmNgjyaGg3R0rGpsbefpa+dn4THSakJjgZhevs8ck+Uf0guQhh/EmLUF/5mqsEyw4uAeXxmRT4PZWYtfjicTq+HZ2GBpLFy5FYRR4UuDTkvg2lNYJZDIRWlAQKBgE7rKrEeUATD/06Y/ROsJX4fXrOqcNgOfHbeIOGKY7EwcfkJ31aiKL91tCIvzEDoYp1p5Un5nw1qWboqvboeZir1Ywn7l/9O3fqVSg8uyykwhSnej5FaFw7SidDo7zcjccHnzj0zJK9UyMmudVx4xHGtmajeSR72yqWPli4HDImBAoGAe2UvUOIZb4hWqtKhDE6C2GmcB/gMHYKq+bwWSdfE+SBf0qUJTlrPlz6o52QyT3enhwUuFEbi1tFEylsdX4eog+vSWa6YzLjBu9ux4Kn8G4Q7ZRJ0rdDk2DHYIl1ACezeKogmtK81WIcrDsLl5gdkOOfsGyJX/gNko9UDnaEsIok=";
    //饿了么生产公钥
    public static final String ELM_PRD_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsmKRj1ij2FuxuJPCzVMpeJo4H/ZHGuW+xSKia+95o6vmipMsJSH//cDQ9PE+NBeV+lV7pBOGVoaCfsCtJ/D288cME408MJ+V4Rt6+HtVlIsTQcv8q+KOiLD91OSdOmtLQGNzHCnnzyMxrjzQh4XsZ5euc0sVifJPuElgeue1nvCc/OS+yw53ZdyX4NPbG655IzIF5/7vnpc/gUCIDXyzchM53hBAIFH9SNlUuHQfEn5ICIdyvlcmV2CxKPVKDlaZbux0+FY5t+4A1TF6F2RVsVrDVufGidpAlQ4otIQ5qAwqyB/3VDO4A+XkGwP4PRlEL/vxAh8JuSeix/GkWrAPMQIDAQAB";

    public static Boolean validParam(String consumerNo, String timeStamp, String sign, String s, JSONObject eleConfig) {
        String elmConsumerNO = eleConfig.getString("consumerNo");
        String elmConsumerSecret = eleConfig.getString("consumerSecret");
        //1,客户编码校验
        if (!consumerNo.equals(elmConsumerNO)) {
            //传入和提供配置不一致
            return false;
        }
        //2,时间戳
        int nowTime = DateUtils.getNowTime();
        if (nowTime - Long.parseLong(timeStamp) > ELMConstants.REQUEST_EXPIRY_TIME) {
            return false;
        }
        //3,加密sign
        String sign1 = MD5Util.MD5Encode(s + elmConsumerSecret + timeStamp, ELMConstants.CHARSET);
        if (!sign.equals(sign1)) {
            return false;
        }
        return true;
    }


    /**
     * RSA2 生成签名
     *
     * @param privateKey
     * @param req
     * @return
     * @throws Exception
     */
    public static String rsaSign(String privateKey, JSONObject req) throws Exception {
        if (StringUtils.isBlank(privateKey) || null == req || req.isEmpty()) {
            return null;
        }
        String content = getSortSignContent(req);
        PrivateKey priKey = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
        Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
        signature.initSign(priKey);
        signature.update(content.getBytes("utf-8"));
        byte[] signed = signature.sign();
        return new String(Base64.encodeBase64(signed));
    }

    /**
     * 验签方法
     *
     * @param publicKey
     * @param contents
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean rsaCheck(String publicKey, JSONObject contents, String sign) throws Exception {
        //System.out.println(">>验证的签名为:" + sign);
        //System.out.println(">>生成签名的参数为:" + content);
        String content = getSortSignContent(contents);
        PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
        Signature signature = java.security.Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
        signature.initVerify(pubKey);
        signature.update(content.getBytes("utf-8"));
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins == null || StringUtil.isEmpty(algorithm)) {
            return null;
        }
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey = readText(ins).getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        StringWriter writer = new StringWriter();
        io(new InputStreamReader(ins), writer, -1);
        byte[] encodedKey = writer.toString().getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    /**
     * 将筛选的参数按照第一个字符的键值ASCII码递增排序（字母升序），
     * 如果遇到相同字符则按照第二个字符的键值ASCII码递增排序，以此类推。
     *
     * @param req
     * @return
     */
    public static String getSortSignContent(JSONObject req) {
        TreeMap<String, Object> parameters = new TreeMap<>();
        StringBuffer sb = new StringBuffer();
        String result = "";
        Iterator it = req.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            Object val = entry.getValue();
            if (null == val) {
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

    private static String readText(InputStream ins) throws IOException {
        Reader reader = new InputStreamReader(ins);
        StringWriter writer = new StringWriter();
        io(reader, writer, -1);
        return writer.toString();
    }

    private static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = DEFAULT_BUFFER_SIZE >> 1;
        }
        char[] buffer = new char[bufferSize];
        int amount;
        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }

    public static void main(String[] args) {
        try {
            JSONObject res = new JSONObject();
            res.put("returnCode", PayConstants.PAY_STATUS_2); //支付请求结果 1: 成功, 2: 失败
            res.put("returnMsg", PayConstants.PAY_STATUS_0); //支付请求结果或错误信息
            res.put("transactionId", null);                  //支付网关的订单号
            res.put("payAmount", "");                      //支付金额(单位:分)
            res.put("outTradeNo", "");                     //三方交易号
            res.put("payStatus", PayStatusEnum.PayTypeNotPay.getCode());  //支付状态
            res.put("nonceStr", RandomUtil.getRandomStr(32));   //随机串（长度32）

            // 根据私钥生成签名
            String signStr = rsaSign(ELMConstants.ELM_PRIVATE_KEY, res);
            System.out.println("----------------生成签名：" + signStr);

            // 根据公钥验签
            //String content = getSortSignContent(res);
            boolean flag = rsaCheck(ELMConstants.ELM_PUBLIC_KEY, res, signStr);
            System.out.println("----------------根据公钥验签结果：" + flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}