package com.cargohub.models;

import lombok.Data;

@Data
public class OrdersModel {

    private String departure;
    private String arrival;
    private String load;
    private int price;
    private int userid;

}
