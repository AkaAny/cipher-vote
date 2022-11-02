package com.example.ciphervote.controller;

import com.example.ciphervote.model.UserModel;
import com.example.ciphervote.model.VoteRequest;
import com.example.ciphervote.model.VoteSummary;
import com.example.ciphervote.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/vote")
public class VoteController {
    final
    VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @RequestMapping(value = "/vote",method = RequestMethod.PUT)
    public void vote(@RequestBody VoteRequest body){
        try {
            voteService.voteLocked(body.userModel, body.optionID);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/summary",method = RequestMethod.GET)
    public ResponseEntity<VoteSummary> summary(@RequestParam("voteID") Long voteID){
        Map<Long,Integer> optionNumberMap= voteService.summary(voteID);
        VoteSummary data=new VoteSummary();
        data.optionNumberMap=optionNumberMap;
        return ResponseEntity.ok(data);
    }

    @RequestMapping(value = "/newUser",method = RequestMethod.POST)
    public ResponseEntity<UserModel> newUser(){
        try {
            UserModel userModel= voteService.newUser();
            return ResponseEntity.ok(userModel);
        } catch (Throwable e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
