//package com.huayou.samlboot2;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileWriter;
//import java.security.*;
//import java.security.cert.Certificate;
//import java.util.Base64;
//public class ExportPrivateKey {
//    private File keystoreFile;
//    private String keyStoreType;
//    private char[] password;
//    private String alias;
//    private File exportedFile;
//    public static KeyPair getPrivateKey(KeyStore keystore, String alias, char[] password) {
//        try {
//            Key key = keystore.getKey(alias, password);
//            if (key instanceof PrivateKey) {
//                Certificate cert = keystore.getCertificate(alias);
//                PublicKey publicKey = cert.getPublicKey();
//                return new KeyPair(publicKey, (PrivateKey) key);
//            }
//        } catch (UnrecoverableKeyException e) {
//        } catch (NoSuchAlgorithmException e) {
//        } catch (KeyStoreException e) {
//        }
//        return null;
//    }
//
//    public void export() throws Exception {
//        KeyStore keystore = KeyStore.getInstance(keyStoreType);
//        keystore.load(new FileInputStream(keystoreFile), password);
//        KeyPair keyPair = getPrivateKey(keystore, alias, password);
//        PrivateKey privateKey = keyPair.getPrivate();
//        String encoded = Base64.getEncoder().encodeToString(privateKey.getEncoded());
//        FileWriter fw = new FileWriter(exportedFile);
//        fw.write("—–BEGIN PRIVATE KEY—–\n");
//        fw.write(encoded);
//        fw.write("\n");
//        fw.write("—–END PRIVATE KEY—–");
//        fw.close();
//    }
//
//    public static void main(String args[]) throws Exception {
//        ExportPrivateKey export = new ExportPrivateKey();
//        export.keystoreFile = new File("E:/key/kstore.keystore");
//        export.keyStoreType = "JKS";
//        export.password = "init234".toCharArray();
//        export.alias = "kstore";
//        export.exportedFile = new File("kstore");
//        export.export();
//    }
//
//}
