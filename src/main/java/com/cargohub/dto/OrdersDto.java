package com.cargohub.dto;

import lombok.Data;

@Data
public class OrdersDto {
    private Integer id;
    private String trackingId;
    private Integer userId;
    private Double price;
    private Date estimatedDeliveryDate;
    private String departureHub;
    private String arrivalHub;
    private Cargo cargo;
    private PaymentStatus paymentStatus;
    private DeliveryStatus deliveryStatus;
}
