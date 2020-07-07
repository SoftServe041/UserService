package com.cargohub.repositories;

import com.cargohub.entities.JwtTokenBlackListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtTokenBlackListRepository extends JpaRepository<JwtTokenBlackListEntity, Long> {

    JwtTokenBlackListEntity findByToken(String token);
}
