package com.cargohub.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "jwtblacklist")
@Data
public class JwtTokenBlackListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    private String token;

    @Column(nullable = false)
    private LocalDateTime dateOfAdding;
}
