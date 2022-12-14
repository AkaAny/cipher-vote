package com.example.ciphervote.entity;

import com.example.ciphervote.utils.SignatureUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Base64;
import java.util.UUID;

@Entity
@Table(name = "block")
public class Block {
    @Id
    public String blockID;
    public String previousID;
    public Long optionID; //选取的选项

    public Long createdAt;

    @Lob
    public byte[] sigPublicKey;
    @Lob
    public byte[] mCipher; //同态加密的密文
    @Lob
    public byte[] signature;

    public boolean verify() throws Throwable{
        byte[] latestSignatureData= this.signature;
        return SignatureUtils.verifySignature(sigPublicKey,mCipher,latestSignatureData);
    }

    protected Block(){

    }

    public Block(Long optionID,byte[] cipher,byte[] sigPublicKey,byte[] sigPrivateKey) throws Throwable {
        this.blockID= UUID.randomUUID().toString();
        this.previousID= null;
        this.optionID=optionID;
        this.sigPublicKey=sigPublicKey;
        setCipher(cipher,sigPrivateKey);
        this.createdAt= System.currentTimeMillis();
    }

    public void setCipher(byte[] cipher,byte[] sigPrivateKey) throws Throwable {
        this.mCipher=cipher;
        //generate sign for current data
        byte[] signatureData= SignatureUtils.getSignature(sigPrivateKey, cipher);
        System.out.println(Base64.getEncoder().encodeToString(signatureData));
        this.signature=signatureData;
    }

    public Block(Block lastBlock,byte[] cipher,byte[] sigPublicKey,byte[] sigPrivateKey) throws Throwable {
        this(lastBlock.optionID,cipher,sigPublicKey,sigPrivateKey);
        this.previousID= lastBlock.blockID;
    }
}
