package com.cargohub.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "jwtblacklist")
@Data
public class JwtTokenBlackListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    private String token;
}
