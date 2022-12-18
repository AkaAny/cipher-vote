package com.example.ciphervote.service;

import com.example.ciphervote.entity.Block;
import com.example.ciphervote.entity.VoteOption;
import com.example.ciphervote.mapper.BlockMapper;
import com.example.ciphervote.mapper.VoteOptionMapper;
import com.example.ciphervote.model.UserModel;
import com.example.ciphervote.worker.VoteWorker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class VoteService extends VoteWorker {
    @Resource
    VoteOptionMapper voteOptionMapper;
    @Resource
    BlockMapper blockMapper;

    VoteService(){

    }

    public VoteService(VoteOptionMapper voteOptionMapper,BlockMapper blockMapper){
        this.voteOptionMapper=voteOptionMapper;
        this.blockMapper = blockMapper;
    }

    public void voteLocked(UserModel userModel,Long optionID) throws Throwable {
        //TODO: 连接同一个数据库（统一账本），需要分布式锁
        Block lastBlock= blockMapper.getLatestByOptionID(optionID).stream().findFirst().orElse(null);
        Block block=null;
        if(lastBlock==null){
            block=newVoteBlock(userModel, optionID);
        }else{
            block= add(userModel,lastBlock);
        }
        blockMapper.save(block);
    }

    public Map<Long,Integer> summary(Long voteID){
        List<VoteOption> optionList= voteOptionMapper.getByVoteID(voteID);
        Map<Long,Integer> optionNumberMap=new HashMap<>();
        optionList.forEach(new Consumer<VoteOption>() {
            @Override
            public void accept(VoteOption voteOption) {
                List<Block> relatedBlockList= blockMapper.getByOptionID(voteOption.id);
                if(!verify(relatedBlockList)){
                    return;
                }
                optionNumberMap.put(voteOption.id,relatedBlockList.size());
            }
        });
        return optionNumberMap;
    }
}
