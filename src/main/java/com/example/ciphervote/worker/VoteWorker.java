package com.example.ciphervote.worker;

import com.example.ciphervote.entity.Block;
import com.example.ciphervote.algorithm.Paillier;
import com.example.ciphervote.model.UserModel;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.*;
import java.util.function.Predicate;

public class VoteWorker {
    public Block newVoteBlock(UserModel userModel,Long optionID) throws Throwable {
        Paillier paillier = new Paillier();
        BigInteger n=new BigInteger(userModel.n);
        paillier.setPublicKey(n);
        //make cipher
        BigInteger plain = new BigInteger("0", 10);
        BigInteger cipherBI = paillier.Encryption(plain);
        byte[] cipher = cipherBI.toByteArray();
        Block block=new Block(optionID,cipher, userModel.sigPublicKey, userModel.sigPrivateKey);
        return block;
    }
    public Block add(UserModel userModel,Block lastBlock) throws Throwable {
        byte[] cipher= lastBlock.mCipher;
        {
            Paillier paillier = new Paillier();
            BigInteger n=new BigInteger(userModel.n);
            paillier.setPublicKey(n);
            BigInteger oneBI = paillier.Encryption(new BigInteger("1", 10));
            BigInteger cipherBI = new BigInteger(cipher);
            cipherBI= paillier.cipher_add(cipherBI, oneBI);
            cipher = cipherBI.toByteArray();
        }
        Block block=new Block(lastBlock,cipher,userModel.sigPublicKey, userModel.sigPrivateKey);
        return block;
    }

    public List<Block> getLocalBlocks(){
        return new ArrayList<>();
    }

    public boolean verify(List<Block> voteBlockList){
        return voteBlockList.stream().allMatch(new Predicate<Block>() {
            @Override
            public boolean test(Block block) {
                try {
                    return block.verify();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public UserModel newUser() throws Throwable{
        UserModel userModel=new UserModel();
        Paillier paillier=new Paillier();
        userModel.p=paillier.getP().toByteArray();
        userModel.q=paillier.getQ().toByteArray();
        userModel.n=paillier.n.toByteArray();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        userModel.sigPublicKey=keyPair.getPublic().getEncoded();
        userModel.sigPrivateKey=keyPair.getPrivate().getEncoded();
        return userModel;
    }
}
