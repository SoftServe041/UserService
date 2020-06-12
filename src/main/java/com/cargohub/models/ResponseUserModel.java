package com.cargohub.models;

import lombok.Data;

@Data
public class ResponseUserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
}
