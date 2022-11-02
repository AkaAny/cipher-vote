package com.example.ciphervote.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SignatureUtils {
    public static byte[] getSignature(byte[] pcksPrivateKey,byte[] data) throws Throwable {
        KeySpec spec=new PKCS8EncodedKeySpec(pcksPrivateKey);
        KeyFactory keyFactory= KeyFactory.getInstance("RSA");
        PrivateKey privateKey= keyFactory.generatePrivate(spec);
        Signature signature=Signature.getInstance("MD5WithRSA");
        signature.initSign(privateKey);
        signature.update(data);
        byte[] signatureData= signature.sign();
        return signatureData;
    }

    public static boolean verifySignature(byte[] x509PublicKey,byte[] data,byte[] signatureData) throws Throwable{
        KeySpec keySpec = new X509EncodedKeySpec(x509PublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(data);
        return signature.verify(signatureData);
    }
}
