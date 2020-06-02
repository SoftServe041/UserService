package com.cargohub.controllers;

import com.cargohub.dto.BillingDetailsDto;
import com.cargohub.dto.UserDto;
import com.cargohub.models.RegistrationModel;
import com.cargohub.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/registration")
public class AuthenticationController {

    private final ModelMapper modelMapper;
    private final UserService userService;

    public AuthenticationController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @PostMapping
    public HttpStatus register(@RequestBody RegistrationModel registrationModel) {
        UserDto userDto = modelMapper.map(registrationModel, UserDto.class);
        BillingDetailsDto billingDetailsDto = modelMapper.map(registrationModel, BillingDetailsDto.class);
        List<BillingDetailsDto> billingDetails = new ArrayList<>();
        billingDetails.add(billingDetailsDto);
        userDto.setBillingDetails(billingDetails);
        billingDetailsDto.setUserDetails(userDto);
        userService.createUser(userDto);

        return HttpStatus.CREATED;
    }

}
