package com.cargohub.services;

import com.cargohub.dto.OrdersDto;

public interface OrdersService {

    OrdersDto createOrder(OrdersDto ordersDto);
    OrdersDto returnOrders(OrdersDto ordersDto);

}
