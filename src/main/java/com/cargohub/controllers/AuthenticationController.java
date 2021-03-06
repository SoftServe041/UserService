package com.cargohub.controllers;

import com.cargohub.dto.JwtTokenBlackListDto;
import com.cargohub.dto.UserDto;
import com.cargohub.entities.RoleEntity;
import com.cargohub.entities.UserEntity;
import com.cargohub.models.AuthRequestModel;
import com.cargohub.models.AuthResponseModel;
import com.cargohub.models.RegistrationModel;
import com.cargohub.models.RestUserModel;
import com.cargohub.security.jwt.JwtTokenProvider;
import com.cargohub.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(path = "/reset")
    public ResponseEntity logOut(@RequestHeader (value = "Authorization") String token){
        JwtTokenBlackListDto jwtTokenBlackListDto = userService.getToken(token);
        if(jwtTokenBlackListDto != null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        userService.saveTokenToBlackList(token);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity register(@RequestBody RegistrationModel registrationModel) {
        UserDto userDto = modelMapper.map(registrationModel, UserDto.class);
        userService.createUser(userDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/admin/batch/registration")
    public ResponseEntity registerBatch(@RequestBody RegistrationModel[] registrationModels) {
        for (RegistrationModel registrationModel : registrationModels) {
            UserDto userDto = modelMapper.map(registrationModel, UserDto.class);
            userService.createUser(userDto);
        }
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthRequestModel requestDto) {
        try {
            String email = requestDto.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
            UserDto user = userService.getUser(email);

            UserEntity foundUser = modelMapper.map(user, UserEntity.class);
            AuthResponseModel responseModel = new AuthResponseModel();
            responseModel.setEmail(foundUser.getEmail());
            responseModel.setToken(jwtTokenProvider.createToken(email, (List<RoleEntity>) foundUser.getRoles()));
            responseModel.setId(foundUser.getId());

            for(RoleEntity roleEntity : foundUser.getRoles()){
                if(roleEntity.getName().equals("ROLE_ADMIN")) {
                    responseModel.setAdmin(true);
                    break;
                }
            }
            return ResponseEntity.ok(responseModel);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
