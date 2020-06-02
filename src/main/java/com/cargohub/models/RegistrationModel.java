package com.cargohub.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;

    private String cardNumber;
    private String nameOnCard;
    private String expirationMonth;
    private String expirationYear;
    private String billingAddress;
}
