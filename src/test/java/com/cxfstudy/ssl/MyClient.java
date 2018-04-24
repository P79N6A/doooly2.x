/*package com.cxfstudy.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;

public class MyClient {

	private static final String address = "https://localhost:12345/ws/ssl/userService";

	public static void main(String[] args) throws Exception {
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
		factoryBean.setAddress(address);
		factoryBean.setServiceClass(UserService.class);
		Object obj = factoryBean.create();
		UserService userService = (UserService) obj;
		
		configureSSLOnTheClient(userService);

		System.out.println(userService.list());
	}

	private static void configureSSLOnTheClient(Object obj) {
//		File file = new File(MyServer.class.getResource("/com/googlecode/garbagecan/cxfstudy/ssl/test.jks").getFile());
		File file = new File("D:/workspace/CXFHttps Maven Webapp/src/main/resources/jks/server.keystore");
		
		Client client = ClientProxy.getClient(obj);
		HTTPConduit httpConduit = (HTTPConduit) client.getConduit();

		try {
			TLSClientParameters tlsParams = new TLSClientParameters();
			tlsParams.setDisableCNCheck(true);

			KeyStore keyStore = KeyStore.getInstance("JKS");
			String password = "changeit";
			String storePassword = "changeit";
			
			keyStore.load(new FileInputStream(file), storePassword.toCharArray());
			TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustFactory.init(keyStore);
			TrustManager[] trustManagers = trustFactory.getTrustManagers();
			tlsParams.setTrustManagers(trustManagers);

			keyStore.load(new FileInputStream(file), storePassword.toCharArray());
			KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyFactory.init(keyStore, password.toCharArray());
			KeyManager[] keyManagers = keyFactory.getKeyManagers();
			tlsParams.setKeyManagers(keyManagers);
			
			FiltersType filtersTypes = new FiltersType();
			filtersTypes.getInclude().add(".*_EXPORT_.*");
			filtersTypes.getInclude().add(".*_EXPORT1024_.*");
			filtersTypes.getInclude().add(".*_WITH_DES_.*");
			filtersTypes.getInclude().add(".*_WITH_NULL_.*");
			filtersTypes.getExclude().add(".*_DH_anon_.*");
			tlsParams.setCipherSuitesFilter(filtersTypes);

			httpConduit.setTlsClientParameters(tlsParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
*/