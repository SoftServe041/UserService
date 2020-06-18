package com.cargohub.controllers;

import com.cargohub.dto.OrdersDto;
import com.cargohub.models.OrdersModel;
import com.cargohub.services.OrdersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrdersController {

    private ModelMapper modelMapper;
    private OrdersService ordersService;

    @Autowired
    public OrdersController(ModelMapper modelMapper, OrdersService ordersService) {
        this.modelMapper = modelMapper;
    }
    @PostMapping("/")
    public ResponseEntity makeOrder(@RequestBody OrdersModel ordersModel) {
        OrdersDto ordersDto = modelMapper.map(ordersModel, OrdersDto.class);
        //adding id
        ordersService.createOrder(ordersDto);
        //subbmiting to repo
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @GetMapping(path = "/PROFILE_PAGE", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOrders(@RequestParam(value = "usedId", defaultValue = "a") int id) {
        OrdersDto ordersDto = modelMapper.map(ordersModel, OrdersDto.class);
        //adding id
        ordersService.createOrder(ordersDto);
        //subbmiting to repo
        return new ResponseEntity(HttpStatus.CREATED);
    }
    private String generateOrderId(){

    }

}
