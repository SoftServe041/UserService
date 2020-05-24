package com.cargohub.services.impl;

import com.cargohub.dto.UserDto;
import com.cargohub.entities.UserEntity;
import com.cargohub.exceptions.UserServiceException;
import com.cargohub.repositories.UserRepository;
import com.cargohub.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserServiceException("User already exists");
        }

        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

//        Collection<RoleEntity> roleEntities = new HashSet<>();
//        for (String role : user.getRoles()) {
//            RoleEntity roleEntity = roleRepository.findByName(role);
//            if (roleEntity != null) {
//                roleEntities.add(roleEntity);
//            }
//        }
//        userEntity.setRoles(roleEntities);

        UserEntity storedUser = userRepository.save(userEntity);

        return modelMapper.map(storedUser, UserDto.class);
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User with such email is not found: " + email);
        }

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserById(long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity == null) {
            throw new UserServiceException("User does not exist");
        }

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto updateUser(long id, UserDto user) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity == null) {
            throw new UserServiceException("User does not exist");
        }

        userEntity = modelMapper.map(user, UserEntity.class);
        UserEntity storedUser = userRepository.save(userEntity);

        return modelMapper.map(storedUser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        return null;
    }
}
