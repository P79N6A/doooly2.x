=====================单向认证=========================
1、通过keytools生成keystore
keytool -genkey -alias tomcat -keyalg RSA -keypass changeit -storepass changeit -keystore server.keystore 

注意CN必须域名 
比如以后通过https://localhost:8443/path/ 访问网站 
这时候CN = localhost 
2.导出x509证书
keytool -export -alias tomcat -file d:\server.cer -keystore d:\server.keystore

3.新建client信任的keystore
keytool -genkey -alias trust -keyalg RSA -keypass changeit -storepass changeit -keystore d:\trust.keystore


4.添加服务端证书到客户端
keytool -import -v -alias tomcat -file d:\server.cer -keystore d:\trust.keystore 

5.tomcat1.7配置
<Connector port="8443" protocol="org.apache.coyote.http11.Http11Protocol" SSLEnabled="true"
           enableLookups="false"
           acceptCount="100" disableUploadTimeout="true"
           maxThreads="150" scheme="https" secure="true"
           clientAuth="false" sslProtocol="TLS"
           keystoreFile="conf\jks\server.keystore"
           keystorePass="changeit" />

tomcat1.6配置
<Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true" enableLookups="false"
           acceptCount="100" disableUploadTimeout="true"
           maxThreads="150" scheme="https" secure="true"
           clientAuth="false" sslProtocol="TLS"
           keystoreFile="Users\loiane/.keystore"
           keystorePass="password" />

======================双向认证===================================

5.通过keytools生成clientkeystore 
keytool -genkey -alias client -keyalg RSA -keypass changeit -storepass changeit -keystore d:\client.keystore

6.导出x509证书
keytool -export -alias client -file d:\client.cer -keystore d:\client.keystore. 

7、新建server信任的trustserverkeystore
keytool -genkey -alias trustserver -keyalg RSA -keypass changeit -storepass changeit -keystore d:\trustserver.keystore 
8.添加本地证书进入服务器信任trustserverkeystore
keytool -import -v -alias client -file d:\client.cer -keystore d:\trustserver.keystore

9.添加客户端类client.java，测试成功
Request:POST https://localhost:8443 HTTP/1.1
HTTP/1.1 200 OK
