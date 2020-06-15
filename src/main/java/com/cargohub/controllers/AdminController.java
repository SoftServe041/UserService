package com.cargohub.controllers;

import com.cargohub.dto.UserDto;
import com.cargohub.entities.UserEntity;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@Secured("ROLE_ADMIN")
public class AdminController {

    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public AdminController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<RestUserModel> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "limit", defaultValue = "5") int limit) {
        Page<UserEntity> entityPage = userService.getUsers(page, limit);
        return entityPage.map(entityObject -> modelMapper.map(entityObject, RestUserModel.class));
    }

    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable long id,
                                        @RequestBody UpdateUserModel updateUserModel) {
        UserDto userDto = modelMapper.map(updateUserModel, UserDto.class);
        userService.updateUser(id, userDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
