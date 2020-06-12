package com.cargohub.models;

import lombok.Data;

@Data
public class AuthResponseModel {
    private long id;
    private String email;
    private String token;
    private boolean isAdmin;
}
