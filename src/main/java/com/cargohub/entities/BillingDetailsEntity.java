package com.cargohub.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "billing_details")
@Data
public class BillingDetailsEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;
}
