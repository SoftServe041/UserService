package com.cargohub.controllers;

import com.cargohub.dto.UserDto;
import com.cargohub.entities.UserEntity;
import com.cargohub.models.UpdateUserModel;
import com.cargohub.security.jwt.JwtTokenProvider;
import com.cargohub.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/profile")
//@Secured({"ROLE_ADMIN","ROLE_USER"})
public class UserProfileController {

   // private AuthenticationManager authenticationManager;
   // private JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public UserProfileController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, ModelMapper modelMapper) {
      //  this.authenticationManager = authenticationManager;
      //  this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.modelMapper = modelMapper;

    }

    @GetMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@PathVariable long id){
        try {
            UserDto user = userService.getUserById(id);

            System.out.println(user.toString()+'\n');

            if (user == null) {
                throw new UsernameNotFoundException("User with id: " + id + " not found");
            }


            UserEntity foundUser = modelMapper.map(user, UserEntity.class);
            UpdateUserModel responseModel = new UpdateUserModel();
            responseModel.setEmail(foundUser.getEmail());
            responseModel.setFirstName(foundUser.getFirstName());
            responseModel.setLastName(foundUser.getLastName());
            responseModel.setAddress(foundUser.getAddress());
            responseModel.setPhoneNumber(foundUser.getPhoneNumber());
            System.out.println(responseModel.toString());


            return ResponseEntity.ok(responseModel);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@PathVariable long id,
                                     @RequestBody UpdateUserModel updateUserModel) {
                                      //  , String password ){
        try {
            UserDto userDto = modelMapper.map(updateUserModel, UserDto.class);
/*
            if (password.length() > 0)
                userDto.setPassword(password);
*/
            userService.updateUser(id, userDto);
            return new ResponseEntity(HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }



}
