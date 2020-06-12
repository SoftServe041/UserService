package com.cargohub.models;

import com.cargohub.dto.BillingDetailsDto;
import com.cargohub.dto.RoleDto;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class RequestUserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private List<BillingDetailsDto> billingDetails;
    private Collection<RoleDto> roles;
}
