package com.cargohub.models;

import com.cargohub.entities.RoleEntity;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class RestUserModel {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    Collection<String> roles;
    // do we need ?:
    private List<BillingDetailsModel> billingDetails;
}
