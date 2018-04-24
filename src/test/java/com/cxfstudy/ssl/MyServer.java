/*package com.cxfstudy.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.cxf.configuration.jsse.TLSServerParameters;
import org.apache.cxf.configuration.security.ClientAuthentication;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngineFactory;

public class MyServer {

	private static final int port = 12345;
	
	private static final String address = "https://0.0.0.0:"+port+"/ws/ssl/userService";

	public static void main(String[] args) throws Exception {
		System.out.println("Starting Server");
		
		configureSSLOnTheServer();
		
		JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
		factoryBean.setServiceClass(UserServiceImpl.class);
		factoryBean.setAddress(address);
		
		Server server = factoryBean.create();
		String endpoint = server.getEndpoint().getEndpointInfo().getAddress();

		System.out.println("Server started at " + endpoint);
	}

	public static void configureSSLOnTheServer() {
		File file = new File("D:/workspace/CXFHttps Maven Webapp/src/main/resources/jks/server.keystore");
		
		try {
			TLSServerParameters tlsParams = new TLSServerParameters();
			KeyStore keyStore = KeyStore.getInstance("JKS");
			String password = "changeit";
			String storePassword = "changeit";
			
			keyStore.load(new FileInputStream(file), storePassword.toCharArray());
			KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyFactory.init(keyStore, password.toCharArray());
			KeyManager[] keyManagers = keyFactory.getKeyManagers();
			tlsParams.setKeyManagers(keyManagers);

			keyStore.load(new FileInputStream(file), storePassword.toCharArray());
			TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustFactory.init(keyStore);
			TrustManager[] trustManagers = trustFactory.getTrustManagers();
			tlsParams.setTrustManagers(trustManagers);
			
			FiltersType filtersTypes = new FiltersType();
			filtersTypes.getInclude().add(".*_EXPORT_.*");
			filtersTypes.getInclude().add(".*_EXPORT1024_.*");
			filtersTypes.getInclude().add(".*_WITH_DES_.*");
			filtersTypes.getInclude().add(".*_WITH_NULL_.*");
			filtersTypes.getExclude().add(".*_DH_anon_.*");
			tlsParams.setCipherSuitesFilter(filtersTypes);
			
			ClientAuthentication ca = new ClientAuthentication();
			ca.setRequired(true);
			ca.setWant(true);
			tlsParams.setClientAuthentication(ca);
			
			JettyHTTPServerEngineFactory factory = new JettyHTTPServerEngineFactory();
			factory.setTLSServerParametersForPort(port, tlsParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
*/