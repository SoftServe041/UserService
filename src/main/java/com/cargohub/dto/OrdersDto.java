package com.cargohub.dto;

import lombok.Data;

@Data
public class OrdersDto {
    private int id;
    private String departure;
    private String arrival;
    private String load;
    private int price;
    private int userid;
}
