package com.example.ciphervote.mapper;

import com.example.ciphervote.entity.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteOptionMapper extends JpaRepository<VoteOption,Long> {
    List<VoteOption> getByVoteID(Long voteID);
}
