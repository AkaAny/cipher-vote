package com.example.ciphervote.entity;

import javax.persistence.*;

@Entity
@Table(name = "vote_record",indexes = {@Index(columnList = "vote_id")})
public class VoteRecord {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "vote_id")
    public Long voteID;

    public Long optionID;
}
