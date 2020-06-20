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
        ordersDto.setTrackingId(generateTrackingId(
                ordersDto.getDeparture(),
                ordersDto.getArrival(),
                ordersDto.getUserid()));
        ordersService.createOrder(ordersDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(path = "/PROFILE_PAGE", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOrders(@RequestParam(value = "usedId", defaultValue = "a") int id) {

    }

    private String generateTrackingId(String firstCity, String secondCity, long id) {

        byte[] byteCity1 = firstCity.getBytes();
        byte[] byteCity2 = secondCity.getBytes();

        StringBuffer returnStr = new StringBuffer();
        for (byte a : byteCity1) {
            returnStr.append(a);
        }
        for (byte a : byteCity2) {
            returnStr.append(a);
        }
        returnStr.append(id);

        return returnStr.toString();
    }
}
