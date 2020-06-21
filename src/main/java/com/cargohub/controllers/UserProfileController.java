package com.cargohub.controllers;

import com.cargohub.dto.BillingDetailsDto;
import com.cargohub.dto.UserDto;
import com.cargohub.entities.BillingDetailsEntity;
import com.cargohub.entities.UserEntity;
import com.cargohub.models.BillingDetailsModel;
import com.cargohub.models.RestUserModel;
import com.cargohub.models.UpdateUserModel;
import com.cargohub.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
@RequestMapping("/user/profile")
//@Secured({"ROLE_ADMIN","ROLE_USER"})
public class UserProfileController {

    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public UserProfileController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;

    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@PathVariable long id) {
        try {
            UserDto user = userService.getUserById(id);
            if (user == null) {
                throw new UsernameNotFoundException("User with id: " + id + " not found");
            }
            UpdateUserModel responseModel = modelMapper.map(user, UpdateUserModel.class);
            return ResponseEntity.ok(responseModel);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @CrossOrigin
    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateUser(@PathVariable long id,
                                     @RequestBody UpdateUserModel updateUserModel) {
        try {
            UserDto userDto = modelMapper.map(updateUserModel, UserDto.class);
            userService.updateUser(id, userDto);
            return new ResponseEntity(HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @CrossOrigin
    @PutMapping(path = "/reset-password/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateUserPassword(@PathVariable long id,
                                             @RequestBody String password) {
        try {
            userService.updateUserPassword(id, password);
            return new ResponseEntity(HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    ///BILLING
    @CrossOrigin
    @GetMapping(path = "/billing-details/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<BillingDetailsModel> getUserBillingDetails(@PathVariable long id) {
        try {
            UserDto user = userService.getUserById(id);
            List<BillingDetailsDto> pageBillingDetailsDto = user.getBillingDetails();
            List<BillingDetailsModel> pageBillingDetails = pageBillingDetailsDto.stream().map(b -> {
                return modelMapper.map(b, BillingDetailsModel.class);
            })
                    .collect(Collectors.toList());
            return pageBillingDetails;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }


    @CrossOrigin
    @PutMapping(path = "/billing-details/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUserBillingDetails(@PathVariable long id,
                                                   @RequestBody List<BillingDetailsModel> billingDetailsModels) {
        try {
            List<BillingDetailsDto> billingDetails = billingDetailsModels.stream().map(b -> {
                return modelMapper.map(b, BillingDetailsDto.class);
            })
                    .collect(Collectors.toList());

            userService.updateBillingDetails(id, billingDetails);
            return new ResponseEntity(HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }


}
