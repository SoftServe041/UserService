package com.cargohub.dto;

import lombok.Data;

@Data
public class BillingDetailsDto {
    private long id;
    private String cardNumber;
    private String nameOnCard;
    private String expirationMonth;
    private String expirationYear;
    private String billingAddress;
    private UserDto userDetails;
}
