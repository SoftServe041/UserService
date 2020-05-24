package com.cargohub.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String addresses;
    private BillingDetailsDto billingDetails;
    private Collection<String> roles;
}
