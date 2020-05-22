package com.cargohub.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "billing_details")
@Data
public class BillingDetailsEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String nameOnCard;

    @Column(nullable = false)
    private String expirationMonth;

    @Column(nullable = false)
    private String expirationYear;

    @Column(nullable = false)
    private String billingAddress;

    @OneToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;
}
