package com.example.ciphervote.worker;

import com.example.ciphervote.Block;
import com.example.ciphervote.model.SignaturePresentCounter;
import com.example.ciphervote.utils.MerkleTree;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultiNodeVoteWorker extends VoteWorker{
    public List<Block> gatherVerify(List<Block> localBlocks, Map<String, List<Block>> userIDBlockMap){
        //检验共识
        Map<String, SignaturePresentCounter> fullSigs=new HashMap<>();
        userIDBlockMap.forEach(new BiConsumer<String, List<Block>>() {
            @Override
            public void accept(String userID, List<Block> blocks) {
                List<String> sigs= blocks.stream().map(new Function<Block, String>() {
                    @Override
                    public String apply(Block block) {
                        return Base64.getEncoder().encodeToString(block.signature);
                    }
                }).collect(Collectors.toList());
                MerkleTree tree=new MerkleTree(sigs);
                String rootSigStr=new String(tree.getRoot().sig);
                SignaturePresentCounter counter=fullSigs.get(userID);
                if(counter==null){
                    counter=new SignaturePresentCounter();
                    counter.belongToUserID=userID;
                    counter.sigStr =rootSigStr;
                    counter.count=0;
                }
                counter.count++;
                fullSigs.put(userID,counter);
            }
        });
        SignaturePresentCounter maxPresentValidCounter= fullSigs.values().stream().max(new Comparator<SignaturePresentCounter>() {
            @Override
            public int compare(SignaturePresentCounter o1, SignaturePresentCounter o2) {
                return o1.count-o2.count;
            }
        }).orElseThrow();
        return userIDBlockMap.get(maxPresentValidCounter.belongToUserID);
        //更新本地账本
    }
}
