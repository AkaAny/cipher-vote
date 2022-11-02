package com.example.ciphervote.entity;

import javax.persistence.*;

@Entity
@Table(name = "vote_option",indexes = {@Index(columnList = "vote_id")})
public class VoteOption {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "vote_id")
    public Long voteID;

    @Column(name = "description")
    public String description;
}
