package com.cargohub.controllers;

import com.cargohub.dto.UserDto;
import com.cargohub.entities.UserEntity;
import com.cargohub.models.RequestUserModel;
import com.cargohub.models.ResponseUserModel;
import com.cargohub.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public ResponseEntity<ResponseUserModel> addUser(@RequestBody RequestUserModel requestUserModel) {

        UserDto userDto = modelMapper.map(requestUserModel, UserDto.class);

        UserDto createdUserDto = userService.createUser(userDto);

        return new ResponseEntity<>(HttpStatus.CREATED)/*(modelMapper.map(createdUserDto, ResponseUserModel.class))*/;
    }

    @GetMapping(path = "/ok", consumes = { MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity<?> ok() {
        UserDto userDto = userService.getUser("tom2jones@gmail.com");

        return ResponseEntity.ok(modelMapper.map(userDto, UserEntity.class)) ;


        /*(modelMapper.map(createdUserDto, ResponseUserModel.class))*/
    }
    @GetMapping(path = "/get", consumes = { MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity<?> getUser() {
        UserDto userDto = userService.getUser("tom2jones@gmail.com");

        return ResponseEntity.ok(modelMapper.map(userDto, UserEntity.class)) ;/*(modelMapper.map(createdUserDto, ResponseUserModel.class))*/
    }
}
