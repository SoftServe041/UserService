package com.cargohub.models;

import lombok.Data;

@Data
public class RequestUserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
}
