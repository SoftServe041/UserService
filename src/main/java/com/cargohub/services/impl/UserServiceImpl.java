package com.cargohub.services.impl;

import com.cargohub.dto.BillingDetailsDto;
import com.cargohub.dto.UserDto;
import com.cargohub.entities.BillingDetailsEntity;
import com.cargohub.entities.RoleEntity;
import com.cargohub.entities.UserEntity;
import com.cargohub.entities.extra.Roles;
import com.cargohub.exceptions.ErrorMessages;
import com.cargohub.exceptions.UserConflictException;
import com.cargohub.exceptions.UserNotFoundException;
import com.cargohub.repositories.RoleRepository;
import com.cargohub.repositories.UserRepository;
import com.cargohub.security.jwt.JwtTokenProvider;
import com.cargohub.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new UserConflictException("User already exists");
        }

        for (int i = 0; i < userDto.getBillingDetails().size(); i++) {
            BillingDetailsDto billingDetails = userDto.getBillingDetails().get(i);
            billingDetails.setUserDetails(userDto);
            userDto.getBillingDetails().set(i, billingDetails);
        }

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        System.out.println("userDto.getPassword()" + userDto.getPassword() + " and "+
                userEntity.getEncryptedPassword());

        Collection<RoleEntity> roleEntities = new HashSet<>();
        RoleEntity roleEntity = roleRepository.findByName(Roles.ROLE_USER.name());
        if (roleEntity != null) {
            roleEntities.add(roleEntity);
        }
        userEntity.setRoles(roleEntities);

        UserEntity storedUser = userRepository.save(userEntity);

        return modelMapper.map(storedUser, UserDto.class);
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_USER_FOUND_BY_EMAIL + email);
        }

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserById(long id) {
        UserEntity userEntity = getUserEntityById(id);

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto updateUser(long id, UserDto user) {
        UserEntity userEntity = getUserEntityById(id);

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setAddress(user.getAddress());
        userEntity.setPhoneNumber(user.getPhoneNumber());

        UserEntity storedUser = userRepository.save(userEntity);

        return modelMapper.map(storedUser, UserDto.class);
    }

    //////////////in propgress??
    @Override
    public UserDto updateUserPassword(long id, String password) {
        UserEntity userEntity = getUserEntityById(id);
        
        System.out.println('\n' + "current pass " + userEntity.getEncryptedPassword() + '\n' +
                "password will be "+ password);
        //userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(password));
        userEntity.setEncryptedPassword("$2a$10$r0I1zYOy.j3mMQxrquUbUepzaJYSuqVmbk2YQJzJFbEVIZ902ua.a");

        System.out.println("new pass " + userEntity.getEncryptedPassword() );

        userRepository.save(userEntity);
        return modelMapper.map(userEntity,UserDto.class);
    }
    ////

    @Override
    public UserDto updateBillingDetails(long id, List<BillingDetailsDto> billingDetailsDtoList) {
        UserEntity userEntity = getUserEntityById(id);
        userEntity.getBillingDetails().clear();
        List<BillingDetailsEntity> billingDetailsEntityList = billingDetailsDtoList.stream().map(b -> {
            return modelMapper.map(b, BillingDetailsEntity.class);
        })
                .collect(Collectors.toList());

        for (int i = 0; i < billingDetailsEntityList.size(); i++) {
            billingDetailsEntityList.get(i).setUserDetails(userEntity);
            userEntity.getBillingDetails().add( billingDetailsEntityList.get(i));
        }
        userRepository.save(userEntity);
        return modelMapper.map(userEntity, UserDto.class);
    }


    @Override
    public void deleteUser(long id) {
        UserEntity userEntity = getUserEntityById(id);

        userRepository.delete(userEntity);
    }

    @Override
    public Page<UserEntity> getUsers(int page, int limit) {

        if (page > 0) {
            page -= 1;
        }

        Pageable pageableRequest = PageRequest.of(page, limit);

        return userRepository.findAll(pageableRequest);
    }


    private UserEntity getUserEntityById(long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity == null) {
            throw new UserNotFoundException(ErrorMessages.NO_USER_FOUND);
        }
        return userEntity;
    }
}
