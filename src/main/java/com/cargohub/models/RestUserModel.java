package com.cargohub.models;

import lombok.Data;

import java.util.List;

@Data
public class RestUserModel {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    // do we need ?:
    private List<BillingDetailsModel> billingDetails;
}
