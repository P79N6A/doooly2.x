package com.doooly.business.shanghaibank.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.shanghaibank.utils.SignUtil;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public abstract class Api {
    private String host;
    private int port;
    private String key;
    private String channels;
    private String pfxPath;
    private String pfxPwd;
    private ThreadLocal<CloseableHttpClient> httpClient;

    private final static Logger LOGGER = LoggerFactory.getLogger(Api.class);
    /**
     *
     * @param host  服务器地址
     * @param port  服务器端口号
     * @param key
     */
    public Api(String host, int port, String channels, String key, String pfxPath, String pfxPwd) {
        super();
        this.host = host;
        this.port = port;
        this.key=key;
        this.pfxPath=pfxPath;
        this.pfxPwd=pfxPwd;
        this.channels=channels;
        httpClient=new ThreadLocal<CloseableHttpClient>(){

            @Override
            protected CloseableHttpClient initialValue() {
                CloseableHttpClient client = null;
                client = getHttpsClient();
                return client;
            }

        };
    }

    public JSONObject post(String servletPath, String params) throws HttpException, IOException{
        LOGGER.info("------------------------------------------------------------------------");
        LOGGER.info("params："+ JSON.toJSONString(JSON.parseObject(params), true));
        LOGGER.info("------------------------------------------------------------------------");
        HttpPost post = new HttpPost( servletPath );
        StringEntity requestEntity = new StringEntity(params,"UTF-8");
//		RequestEntity requestEntity = new StringRequestEntity(params,"text/xml","UTF-8");
//		post.setRequestEntity(requestEntity);
        post.setEntity(requestEntity);
        String u=String.format("%1$s---%2$s", params,key);
        String [] flag= SignUtil.signData(URLEncoder.encode(u,"UTF-8"), pfxPath, pfxPwd);
//		post.setRequestHeader("channels", channels);
        post.setHeader("channels", channels);
//		post.setRequestHeader("signData", flag[0]);
        post.setHeader("signData", flag[0]);
//		post.setRequestHeader("signCert", flag[1]);
        post.setHeader("signCert", flag[1]);
//		post.setRequestHeader("Content-Type", "0");

        HttpHost httpHost = new HttpHost(host, port, "https");

        CloseableHttpResponse httpResponse = httpClient.get().execute(httpHost, post);
        if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            String ret = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            LOGGER.info(ret);
            JSONObject jsonObject= JSON.parseObject(ret);
            LOGGER.info("***********************************************");
            LOGGER.info("return:"+ JSON.toJSONString(jsonObject, true));
            LOGGER.info("***********************************************");
            if(StringUtils.equals(jsonObject.getString("resultCode"), "00000000")){
                return jsonObject;
            }else{
                throw new RuntimeException("业务功能错误："+jsonObject.getString("resultDesc"));
            }
        }
        throw new RuntimeException("服务器错误:"+ EntityUtils.toString(httpResponse.getEntity(), "UTF-8"));
    }

    public static CloseableHttpClient getHttpsClient() {
        CloseableHttpClient httpClient = null;
        SSLContext context;
        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

            } }, new SecureRandom());

            HostnameVerifier verifier = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(context, verifier);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpClient;
    }
}
