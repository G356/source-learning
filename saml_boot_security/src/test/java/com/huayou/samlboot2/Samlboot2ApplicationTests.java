package com.huayou.samlboot2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class Samlboot2ApplicationTests {

    @Test
    public void contextLoads() {
    }
    //@Test
    //public void test()throws Exception{
    //    String cerPath = "E:/key/kstore.cer";		//证书文件路径
    //    String storePath = "E:/key/kstore.keystore";	//证书库文件路径
    //    String alias = "kstore";		//证书别名
    //    String storePw = "init234";	//证书库密码
    //    String keyPw = "init123";	//证书密码
    //
    //    System.out.println("从证书获取的公钥为:" + getPublicKey(cerPath));
    //    System.out.println("从证书获取的私钥为:" + getPrivateKey(storePath, alias, storePw, keyPw));
    //
    //}
    //private static String getPublicKey(String cerPath) throws Exception {
    //    CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
    //    FileInputStream fis = new FileInputStream(cerPath);
    //    X509Certificate Cert = (X509Certificate) certificatefactory.generateCertificate(fis);
    //    PublicKey pk = Cert.getPublicKey();
    //    String publicKey = Base64.getEncoder().encodeToString(pk.getEncoded());
    //    return publicKey;
    //}
    //
    //private static String getPrivateKey(String storePath, String alias, String storePw, String keyPw) throws Exception {
    //    FileInputStream is = new FileInputStream(storePath);
    //    KeyStore ks = KeyStore.getInstance("JKS");
    //    ks.load(is, storePw.toCharArray());
    //    is.close();
    //    PrivateKey key = (PrivateKey) ks.getKey(alias, keyPw.toCharArray());
    //    System.out.println("privateKey:" + Base64.getEncoder().encodeToString(key.getEncoded()));
    //    String privateKey = Base64.getEncoder().encodeToString(key.getEncoded());
    //    return privateKey;
    //}



}
