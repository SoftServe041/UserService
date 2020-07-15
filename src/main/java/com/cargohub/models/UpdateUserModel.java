package com.cargohub.models;

import com.cargohub.dto.RoleDto;
import lombok.Data;

import java.util.Collection;

@Data
public class UpdateUserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    //private Collection<String> roles;
    private Collection<RoleDto> roles;
}
