package com.cargohub.dto;

import lombok.Data;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String address;
    private String phoneNumber;
    private List<BillingDetailsDto> billingDetails;
    private Collection<RoleDto> roles;
}
