package com.cargohub.controllers;

import com.cargohub.dto.UserDto;
import com.cargohub.models.RequestUserModel;
import com.cargohub.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

//    @GetMapping("/registration")
//    public String registration(Model model) {
//        model.addAttribute("userForm", new UserDto());
//
//        return "registration";
//    }

    @PostMapping(path = "/registration", consumes = { MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity addUser( @RequestBody RequestUserModel requestUserModel) {

        UserDto userDto = modelMapper.map(requestUserModel, UserDto.class);

        UserDto createdUserDto = userService.createUser(userDto);

        return ResponseEntity.ok(modelMapper.map(createdUserDto, RequestUserModel.class));
    }
}
