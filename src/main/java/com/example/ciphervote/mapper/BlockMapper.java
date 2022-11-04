package com.example.ciphervote.mapper;

import com.example.ciphervote.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;

public interface BlockMapper extends JpaRepository<Block,String> {
    @Query("FROM Block b WHERE b.optionID=:optionID ORDER BY b.createdAt DESC")
    List<Block> getLatestByOptionID(Long optionID);

    List<Block> getByOptionID(Long optionID);
}
