=====================������֤=========================
1��ͨ��keytools����keystore
keytool -genkey -alias tomcat -keyalg RSA -keypass changeit -storepass changeit -keystore server.keystore 

ע��CN�������� 
�����Ժ�ͨ��https://localhost:8443/path/ ������վ 
��ʱ��CN = localhost 
2.����x509֤��
keytool -export -alias tomcat -file d:\server.cer -keystore d:\server.keystore

3.�½�client���ε�keystore
keytool -genkey -alias trust -keyalg RSA -keypass changeit -storepass changeit -keystore d:\trust.keystore


4.��ӷ����֤�鵽�ͻ���
keytool -import -v -alias tomcat -file d:\server.cer -keystore d:\trust.keystore 

5.tomcat1.7����
<Connector port="8443" protocol="org.apache.coyote.http11.Http11Protocol" SSLEnabled="true"
           enableLookups="false"
           acceptCount="100" disableUploadTimeout="true"
           maxThreads="150" scheme="https" secure="true"
           clientAuth="false" sslProtocol="TLS"
           keystoreFile="conf\jks\server.keystore"
           keystorePass="changeit" />

tomcat1.6����
<Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true" enableLookups="false"
           acceptCount="100" disableUploadTimeout="true"
           maxThreads="150" scheme="https" secure="true"
           clientAuth="false" sslProtocol="TLS"
           keystoreFile="Users\loiane/.keystore"
           keystorePass="password" />

======================˫����֤===================================

5.ͨ��keytools����clientkeystore 
keytool -genkey -alias client -keyalg RSA -keypass changeit -storepass changeit -keystore d:\client.keystore

6.����x509֤��
keytool -export -alias client -file d:\client.cer -keystore d:\client.keystore. 

7���½�server���ε�trustserverkeystore
keytool -genkey -alias trustserver -keyalg RSA -keypass changeit -storepass changeit -keystore d:\trustserver.keystore 
8.��ӱ���֤��������������trustserverkeystore
keytool -import -v -alias client -file d:\client.cer -keystore d:\trustserver.keystore

9.��ӿͻ�����client.java�����Գɹ�
Request:POST https://localhost:8443 HTTP/1.1
HTTP/1.1 200 OK
