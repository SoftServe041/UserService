package com.cargohub.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingDetailsModel {
    private String cardNumber;
    private String nameOnCard;
    private String expirationMonth;
    private String expirationYear;
    private String billingAddress;
}
