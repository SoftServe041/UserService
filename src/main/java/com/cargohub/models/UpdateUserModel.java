package com.cargohub.models;

import lombok.Data;

@Data
public class UpdateUserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
}
