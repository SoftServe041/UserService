package com.cargohub.controllers;

import com.cargohub.dto.BillingDetailsDto;
import com.cargohub.dto.UserDto;
import com.cargohub.models.*;
import com.cargohub.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserProfileController {

    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public UserProfileController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;

    }

    @CrossOrigin
    @GetMapping(path = "/profile/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@PathVariable long id) {

        UserDto user = userService.getUserById(id);
        UpdateUserModel responseModel = modelMapper.map(user, UpdateUserModel.class);
        return ResponseEntity.ok(responseModel);
    }

    @CrossOrigin
    @PutMapping(path = "/profile/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateUser(@PathVariable long id,
                                     @RequestBody UpdateUserModel updateUserModel) {
        UserDto userDto = modelMapper.map(updateUserModel, UserDto.class);
        userService.updateUser(id, userDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping(path = "/profile/reset-password/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateUserPassword(@PathVariable long id,
                                             @RequestBody ResetPasswordModel password) {

        userService.updateUserPassword(id, password.getPassword());
        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(path = "/{id}/billing-details",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BillingDetailsModel> getUserBillingDetails(@PathVariable long id) {
            UserDto user = userService.getUserById(id);
            List<BillingDetailsDto> pageBillingDetailsDto = user.getBillingDetails();
            List<BillingDetailsModel> pageBillingDetails = pageBillingDetailsDto.stream().map(b -> {
                return modelMapper.map(b, BillingDetailsModel.class);
            })
                    .collect(Collectors.toList());
            return pageBillingDetails;
    }

    @CrossOrigin
    @PostMapping(path = "/{id}/billing-details",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addUserBillingDetails(@PathVariable long id,
                                                @RequestBody SaveBillingDetailsModel saveBillingDetailsModel) {

        BillingDetailsDto billingDetails = modelMapper.map(saveBillingDetailsModel, BillingDetailsDto.class);
        userService.addUsersBillingDetailsCard(id, billingDetails);
        return new ResponseEntity(HttpStatus.OK);

    }

    @CrossOrigin
    @DeleteMapping(path = "/{id}/billing-details/{cardId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteUserBillingDetails(@PathVariable long id,
                                                   @PathVariable long cardId) {
        System.out.println("id and card" + id + " " + cardId);
        if (userService.removeUsersBillingDetailsCard(id, cardId)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);

    }


}
