package com.cargohub.services.impl;

import com.cargohub.dto.OrdersDto;
import com.cargohub.services.OrdersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final ModelMapper modelMapper;
    private final OrdersRepository ordersRepository;

    @Autowired
    public OrdersServiceImpl(OrdersRepository ordersRepository,
                             ModelMapper modelMapper) {
        this.ordersRepository = ordersRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrdersDto createOrder(OrdersDto ordersDto) {
        //saving to jar
        return null;
    }

    @Override
    public OrdersDto returnOrders(OrdersDto ordersDto) {
        //getting from jar
        return null;
    }
}
