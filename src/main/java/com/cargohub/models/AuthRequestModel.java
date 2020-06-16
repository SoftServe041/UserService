package com.cargohub.models;

import lombok.Data;

@Data
public class AuthRequestModel {
    private String email;
    private String password;
}

