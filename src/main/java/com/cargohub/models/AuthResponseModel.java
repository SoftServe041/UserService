package com.cargohub.models;

import com.cargohub.entities.RoleEntity;
import lombok.Data;

import java.util.Collection;

@Data
public class AuthResponseModel {
    private long id;
    private String email;
    private String token;
    private boolean isAdmin;
}
