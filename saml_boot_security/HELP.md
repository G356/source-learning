# Getting Started
########### https set ##############
#keytool -genkey -alias tomcat -dname "CN=Andy,OU=kfit,O=kfit,L=HaiDian,ST=BeiJing,C=CN" -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 365
#https端口号.
server.port: 443
#证书的路径.
server.ssl.key-store: classpath:keystore.p12
#证书密码，请修改为您自己证书的密码.
server.ssl.key-store-password: https123
#秘钥库类型
server.ssl.keyStoreType: PKCS12
#证书别名
server.ssl.keyAlias: tomcat
########### import cert ##############
#keytool -importcert -alias some-alias -file key.cer -keystore samlKeystore.jks
##################### Generate a Self Signed Private/Public key pair in DER/PEM format #####################
# KEY AND CERT
openssl genrsa -out localhost.key 2048
openssl req -new -x509 -key localhost.key -out localhost.pem -days 3650 -subj /CN=localhost
# PEM KEY to DER
openssl pkcs8 -topk8 -inform PEM -outform DER -in  localhost.key -out  localhost.key.der -nocrypt
