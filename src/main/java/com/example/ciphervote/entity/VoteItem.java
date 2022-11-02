package com.example.ciphervote.entity;

import javax.persistence.*;

@Entity
@Table(name = "vote_item")
public class VoteItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "public_key")
    public String publicKey;
}
