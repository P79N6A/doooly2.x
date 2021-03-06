package com.doooly.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * WANG
 */
public class HttpClientUtil {

    private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
    /**编码*/
    public static String CHARACTER_ENCODING = "UTF-8";
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 10000;
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_TYPE_JSON = "application/json";

    
    static HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            System.out.println("Warning: URL Host: " + urlHostName + " vs. "
                               + session.getPeerHost());
            return true;
        }
    };
    
    
    private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
				.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
	}
    

	static class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}
    
    /**
     * 发送https请求
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return 返回微信服务器响应的信息
     */
    public static String httpsRequest(String requestUrl,  String outputStr) {
        try {
            URL url = new URL(requestUrl);
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded"); 
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (Exception e) {
            log.info("doPost e = {} url = {},params = {}", e, requestUrl, outputStr);
        }
        return null;
    }

    public static String httsRequestCert(String url,String mchId, String certSslPath,String requestXML){
        SSLConnectionSocketFactory sslsf = null;
        try {
            InputStream  inputStream = new FileInputStream(certSslPath);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(inputStream, mchId.toCharArray());
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
            sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            log.info("httsRequestCert e = {}",e);
            e.printStackTrace();
        }

        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        CloseableHttpResponse response =  null;
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity reqEntity = new StringEntity(requestXML, "UTF-8");
            // 设置类型
            reqEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(reqEntity);
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    result += text;
                    System.out.println(text);
                }
            }
            EntityUtils.consume(entity);
        }catch (Exception e){
            log.info("httsRequestCert HttpPost.e = {}",e);
            e.printStackTrace();
        }
        finally {
            try {
                httpclient.close();
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


//    //请求方法
//    public static String httpsRequest(String requestUrl, String outputStr) {
//        try {
//            URL url = new URL(requestUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            // 设置请求方式（GET/POST）
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
//            // 当outputStr不为null时向输出流写数据
//            if (null != outputStr) {
//                OutputStream outputStream = conn.getOutputStream();
//                // 注意编码格式
//                outputStream.write(outputStr.getBytes("UTF-8"));
//                outputStream.close();
//            }
//            // 从输入流读取返回内容
//            InputStream inputStream = conn.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String str = null;
//            StringBuffer buffer = new StringBuffer();
//            while ((str = bufferedReader.readLine()) != null) {
//                buffer.append(str);
//            }
//            // 释放资源
//            bufferedReader.close();
//            inputStreamReader.close();
//            inputStream.close();
//            inputStream = null;
//            conn.disconnect();
//            return buffer.toString();
//        }catch (Exception e) {
//            log.info("doPost e = {} url = {},params = {}", e, requestUrl, outputStr);
//        }
//        return null;
//    }

    public static JSONObject httpPost(String httpUrl, JSONObject json) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(httpUrl);
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            log.info(String.format("statusCode=%s", response.getStatusLine()));
            String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
            return JSON.parseObject(responseJson);
        } catch (Exception ex) {
            log.error("httpPost() ex={}", ex);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            }
        }
        return null;
    }


    /**
     * 发送Post请求，并返回处理结果
     *
     * @param reqJson
     * @return
     */
    public static String sendPost(JSONObject reqJson, String httpUrl) {
        try {
            log.info("httpUrl = {}",httpUrl);
            CloseableHttpClient httpClient = createSSLClientDefault();
            HttpPost method = new HttpPost(httpUrl);
            StringEntity entity = new StringEntity(reqJson.toJSONString(), "UTF-8");
            method.setEntity(entity);
            method.addHeader("Cache-Control", "no-cache");
            method.addHeader("Connection", "Keep-Alive");
            method.addHeader("Pragma", "no-cache");
            method.addHeader("Content-Type", "application/json; charset=UTF-8");
            HttpResponse response = httpClient.execute(method);
            log.info(String.format("statusCode=%s", response.getStatusLine().getStatusCode()));
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 获取服务器响应字符串
                String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
                return responseJson;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("sendPost e = {} httpUrl = {},reqJson = {}", e, httpUrl, reqJson);
        }
        return MessageDataBean.failure_code;
    }


    public static String httpsGet(String url) {
        return httpsGet(url,null);
    }

    /**
     * https get请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String httpsGet(String url, List<NameValuePair> params) {
        String body = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            client = HttpClientUtil.createSSLClientDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
            HttpGet get = new HttpGet(url);
            get.setConfig(requestConfig);
            if(params != null && params.size() > 0){
                String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
                get.setURI(new URI(get.getURI().toString() + "?" + str));
            }
            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                body = EntityUtils.toString(entity,"UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("httpsGet e = {} url = {},params = {}", e, url, params);
        }finally{
            try {
                response.close();
            } catch (IOException e) {
            }
            try {
                client.close();
            } catch (IOException e) {
            }
        }
        return body;
    }

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     * @param url  请求地址
     * @param headerMap 头信息
     * @param paramMap 参数
     * @return
     */
    public static String doPost(String url, Map<String, String> headerMap, Map<String, String> paramMap, String bodyStr) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            httpPost.setConfig(requestConfig);
            // 头部请求信息
            if(null != headerMap) {
                Iterator iterator = headerMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    httpPost.addHeader(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            // 请求参数信息
            if(null != paramMap){
                String contentType = "";
                if(null != headerMap && headerMap.containsKey(HEADER_CONTENT_TYPE)){
                    contentType = headerMap.get(HEADER_CONTENT_TYPE).toLowerCase();
                }
                switch (contentType) {
                    case HEADER_CONTENT_TYPE_JSON:
                        String paramStr = JSON.toJSON(paramMap).toString();
                        StringEntity stringEntity = new StringEntity(paramStr, CHARACTER_ENCODING);//解决中文乱码问题
                        httpPost.setEntity(stringEntity);
                        break;
                    default:
                        List<NameValuePair> pairList = new ArrayList<NameValuePair>(paramMap.size());
                        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                            NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                            pairList.add(pair);
                        }
                        httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(CHARACTER_ENCODING)));
                        break;
                }
            }

            if (null != bodyStr) {
                httpPost.setEntity(new StringEntity(bodyStr, CHARACTER_ENCODING));
            }
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            httpStr = EntityUtils.toString(entity, CHARACTER_ENCODING);
            log.info("doPost响应状态码："+statusCode+"，结果："+httpStr+"");
        } catch (IOException e) {
            log.error("doPost执行异常："+e.getMessage()+"",e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    log.error("doPost执行异常："+e.getMessage()+"",e);
                }
            }
        }
        return httpStr;
    }

}
