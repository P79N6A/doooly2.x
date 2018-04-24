package com.doooly.business.order;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.util.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by john on 17/12/22.
 */
public class OrderTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);

        long l = System.currentTimeMillis();
        for (int i = 0 ; i < 1000 ; i++){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    long l = System.currentTimeMillis();
                    String orderJson = createOrderJSON();
                    String responseContent = httpPost("http://admin.doooly.com/dooolytest/jersey/order/createOrder", orderJson);
                    System.out.println(responseContent);
                    System.out.println("111=====> " +  (System.currentTimeMillis() - l));
                }
            });
        }
        System.out.println("222=====> " +  (System.currentTimeMillis() - l));

    }

    /**
     * POST请求
     *
     * @return
     */
    public static String httpPost(String url, String json) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost method = new HttpPost(url);
        String responseContent = "";
        try {
            method.setEntity(new StringEntity(json, "UTF-8"));
            method.addHeader("Cache-Control", "no-cache");
            method.addHeader("Connection", "Keep-Alive");
            method.addHeader("Pragma", "no-cache");
            method.addHeader("Content-Type", "application/json; charset=UTF-8");
            HttpResponse response = httpClient.execute(method);
            System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity httpentity = response.getEntity();
            responseContent = EntityUtils.toString(httpentity, "UTF-8");
            return responseContent;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    public static String createOrderJSON() {
//        Member member = getCurrentMember(request, response);
//        String userId = member.getAdId();
//        String groupId = member.getGroup().getAdId();
//        String price = request.getParameter("price");
//        String consigneeMobile = request.getParameter("phoneNumber");
//        String merchantId = request.getParameter("merchantId");
//        String productId = request.getParameter("productId");
//        String skuId = request.getParameter("skuId");
//        String productType = request.getParameter("productType");
        //检查是否可以充值TODO


        //创建订单
        JSONObject order = new JSONObject();
        order.put("userId", "968707");
        order.put("groupId", "353");
        order.put("consigneeName", "陈新");
        order.put("consigneeMobile", "13127901307");
        order.put("consigneeAddr", null);
        order.put("productType", "2");
        List<JSONObject> productSkus = new ArrayList<JSONObject>();
        JSONObject productSku = new JSONObject();
        productSku.put("productId", "106");
        productSku.put("skuId", "500");
        productSku.put("buyNum", Integer.valueOf(1));
        productSkus.add(productSku);

        List<JSONObject> merchantProducts = new ArrayList<JSONObject>();
        JSONObject merchantProduct = new JSONObject();
        merchantProduct.put("merchantId", "9428");
        merchantProduct.put("remarks", "批量测试1000");
        merchantProduct.put("productSku", productSkus);
        merchantProducts.add(merchantProduct);
        order.put("merchantProduct", merchantProducts);
        System.out.println(order.toJSONString());

        return order.toJSONString();
    }
}
