package com.doooly.common.util;

import java.io.IOException;
import java.security.KeyStore;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.constants.SSLConfigConstants;

public class HTTPSClientUtils {
	private static Logger log = LoggerFactory.getLogger(HTTPSClientUtils.class);
	private static KeyStore trustStore = null;
	private static KeyStore keyStore = null;

	static {

		try {
			trustStore = KeyStore.getInstance(SSLConfigConstants.TRUSTSTORE_TYPE_JKS);
			keyStore = KeyStore.getInstance(SSLConfigConstants.P12_TYPE_PKCS12);
			// 加载证书
			trustStore.load(SSLConfigConstants.TRUSTSTORE_INPUTSTREAM,
					SSLConfigConstants.TRUSTSTORE_PASSWORD.toCharArray());
			keyStore.load(SSLConfigConstants.P12_INPUTSTREAM,
					SSLConfigConstants.P12_PASSWORD.toCharArray());
		} catch (Exception e) {
			log.debug("e = {}", e);
		} finally {
			try {
				// SSLConfigConstants.TRUSTSTORE_INPUTSTREAM.close();
				// SSLConfigConstants.P12_INPUTSTREAM.close();
			} catch (Exception ignore) {
			}
		}
	}

	/**
	 * 获取Httpclient对象
	 * 
	 * @return
	 * @throws java.security.cert.CertificateException
	 * @throws UnrecoverableKeyException
	 */
	public static HttpClient getHttpClient() throws java.security.cert.CertificateException, UnrecoverableKeyException {
		CloseableHttpClient httpclient = null;
		try {
			// 相信自己的CA和所有自签名的证书
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
					.loadKeyMaterial(keyStore, SSLConfigConstants.P12_PASSWORD.toCharArray()).build();

			// 允许使用的协议
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
					new String[] { "SSLv3", "TLSv1" }, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
			PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(10000).build();
			poolConnManager.setDefaultSocketConfig(socketConfig);
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(10000)
					.setConnectTimeout(30000).setSocketTimeout(60000).build();
			CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolConnManager)
					.setDefaultRequestConfig(requestConfig).build();
			return httpClient;
		} catch (Exception e) {
			e.printStackTrace();
		} finally

		{
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpclient;

	}

	/**
	 * 发送Post请求，并返回处理结果
	 * 
	 * @param reqJson
	 * @return
	 */
	public static String sendPost(JSONObject reqJson, String httpUrl) {
		try {
			HttpClient httpClient = getHttpClient();
			// HttpClient httpClient = getHttpClient();
			return sendPost(httpClient, reqJson, httpUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * 发送Post请求，并返回处理结果
	 * 
	 * @param reqJson
	 * @return
	 */
	public static String sendPostNew(String reqJson, String httpUrl) {
		try {
			HttpClient httpClient = getHttpClient();
			// HttpClient httpClient = getHttpClient();
			HttpPost method = null;
			try {
				method = new HttpPost(httpUrl);
				StringEntity entity = new StringEntity(reqJson, "UTF-8");
				method.setEntity(entity);
				method.addHeader("Cache-Control", "no-cache");
				method.addHeader("Connection", "Keep-Alive");
				method.addHeader("Pragma", "no-cache");
				method.addHeader("Content-Type", "application/json; charset=UTF-8");
				HttpResponse response = httpClient.execute(method);
				String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
				log.info(String.format("statusCode=%s", response.getStatusLine()));
				return responseJson;
			} catch (Exception e) {
				log.info("sendPost() httpUrl = {}, e = {}", httpUrl, e);
				e.printStackTrace();
			} finally {
				if (method != null)
					method.releaseConnection();
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 发送Post请求，并返回处理结果
	 * 
	 * @param reqJson
	 * @return
	 */
	public static String sendPost(HttpClient httpClient, JSONObject reqJson, String httpUrl) {
		HttpPost method = null;
		try {
			method = new HttpPost(httpUrl);
			StringEntity entity = new StringEntity(reqJson.toJSONString(), "UTF-8");
			method.setEntity(entity);
			method.addHeader("Cache-Control", "no-cache");
			method.addHeader("Connection", "Keep-Alive");
			method.addHeader("Pragma", "no-cache");
			method.addHeader("Content-Type", "application/json; charset=UTF-8");
			HttpResponse response = httpClient.execute(method);
			String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
			log.info(String.format("statusCode=%s", response.getStatusLine()));
			return responseJson;
		} catch (Exception e) {
			log.info("sendPost() httpUrl = {}, e = {}", httpUrl, e);
			e.printStackTrace();
		} finally {
			if (method != null)
				method.releaseConnection();
		}

		return null;
	}

	/**
	 * 发送Post请求，并返回处理结果
	 * 
	 * @param reqJson
	 * @return
	 */
	public static String sendHttpPost(JSONObject reqJson, String httpUrl) {
		HttpPost method = null;
		try {
			@SuppressWarnings("deprecation")
			HttpClient client = new DefaultHttpClient();
			method = new HttpPost(httpUrl);
			StringEntity entity = new StringEntity(reqJson.toJSONString(), "UTF-8");
			method.setEntity(entity);
			method.addHeader("Cache-Control", "no-cache");
			method.addHeader("Connection", "Keep-Alive");
			method.addHeader("Pragma", "no-cache");
			method.addHeader("Content-Type", "application/json; charset=UTF-8");
			HttpResponse response = client.execute(method);
			String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
			log.info(String.format("statusCode=%s", response.getStatusLine()));
			return responseJson;
		} catch (Exception e) {
			log.info(String.format("sendPost() httpUrl = %s, e = %s", httpUrl, e));
			e.printStackTrace();
		} finally {
			if (method != null)
				method.releaseConnection();
		}

		return null;
	}

}