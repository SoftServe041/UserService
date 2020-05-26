package com.cargohub.services.impl;

import com.cargohub.dto.BillingDetailsDto;
import com.cargohub.dto.UserDto;
import com.cargohub.entities.BillingDetailsEntity;
import com.cargohub.entities.RoleEntity;
import com.cargohub.entities.UserEntity;
import com.cargohub.entities.extra.Roles;
import com.cargohub.exceptions.UserServiceException;
import com.cargohub.repositories.RoleRepository;
import com.cargohub.repositories.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    ModelMapper modelMapper;

    UserEntity userEntity;
    UserDto userDto;
    UserDto resultDto;
    String encryptedPassword = "abracadabra4323sdfs";
    long id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setFirstName("Ivan");
        userEntity.setLastName("Ivanov");
        userEntity.setEmail("ivanov@test.com");
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setAddress("Ivanova Street 4A");
        userEntity.setBillingDetails(getBillingDetailsEntity());

        userDto = getUserDto();
        resultDto = new UserDto();
        resultDto.setAddress("test address");

        id = 1;
    }

    private BillingDetailsEntity getBillingDetailsEntity() {
        BillingDetailsDto billingDetailsDto = getBillingDetailsDto();

        return new ModelMapper().map(billingDetailsDto, BillingDetailsEntity.class);
    }

    private BillingDetailsDto getBillingDetailsDto() {
        BillingDetailsDto billingDetailsDto = new BillingDetailsDto();
        billingDetailsDto.setCardNumber("5523231357287208");
        billingDetailsDto.setNameOnCard("Ivanov Ivan");
        billingDetailsDto.setExpirationMonth("05");
        billingDetailsDto.setExpirationYear("25");
        billingDetailsDto.setBillingAddress("Pushkinska street 35B");

        return billingDetailsDto;
    }

    private UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Ivan");
        userDto.setLastName("Ivanov");
        userDto.setEmail("ivanov@test.com");
        userDto.setPassword("12564");
        userDto.setAddress("Ivanova Street 4A");
        userDto.setBillingDetails(getBillingDetailsDto());

        return userDto;
    }

    @Test
    void createUser() {
        //given
        RoleEntity role = new RoleEntity();
        role.setId(1);
        role.setName(Roles.ROLE_USER);

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(null);
        when(modelMapper.map(userDto, UserEntity.class)).thenReturn(userEntity);
        when(bCryptPasswordEncoder.encode(userDto.getPassword())).thenReturn(encryptedPassword);
        when(roleRepository.findByName(Roles.ROLE_USER.name())).thenReturn(role);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(resultDto);

        //when
        UserDto storedUserDto = userService.createUser(userDto);

        //then
        verify(userRepository).findByEmail(userDto.getEmail());
        verify(modelMapper).map(userDto, UserEntity.class);
        verify(modelMapper).map(userEntity, UserDto.class);
        verify(bCryptPasswordEncoder).encode(userDto.getPassword());
        verify(roleRepository).findByName(Roles.ROLE_USER.name());
        verify(userRepository).save(userEntity);

        assertThat(storedUserDto, is(resultDto));
        assertTrue(userEntity.getRoles().contains(role));
    }

    @Test
    void createUserThrowsUserServiceException() {
        //given
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(userEntity);

        //when, then
        assertThrows(UserServiceException.class, () -> {
            userService.createUser(userDto);
        });
    }

    @Test
    void getUser() {
        //given
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(resultDto);

        //when
        UserDto userResponseDto = userService.getUser(userDto.getEmail());

        //then
        verify(userRepository).findByEmail(userDto.getEmail());
        verify(modelMapper).map(userEntity, UserDto.class);
        assertThat(userResponseDto, is(resultDto));
    }

    @Test
    void getUserThrowsUsernameNotFoundException() {
        //given
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(null);

        //when, then
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUser(userDto.getEmail());
        });
    }

    @Test
    void getUserById() {
        //given
        when(userRepository.findById(id)).thenReturn(ofNullable(userEntity));
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(resultDto);

        //when
        UserDto userResponseDto = userService.getUserById(id);

        //then
        verify(userRepository).findById(id);
        verify(modelMapper).map(userEntity, UserDto.class);
        assertThat(userResponseDto, is(resultDto));
    }

    @Test
    void getUserByIdThrowsUserServiceException() {
        assertThrows(UserServiceException.class, () -> {
            userService.getUserById(1);
        });
    }

    @Test
    void updateUser() {
        //given
        userEntity.setId(id);

        when(userRepository.findById(id)).thenReturn(ofNullable(userEntity));
        when(modelMapper.map(userDto, UserEntity.class)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(resultDto);

        //when
        UserDto storedUserDto = userService.updateUser(id, userDto);

        //then
        verify(userRepository).findById(id);
        verify(userRepository).save(userEntity);
        verify(modelMapper).map(userDto, UserEntity.class);
        verify(modelMapper).map(userEntity, UserDto.class);

        assertThat(storedUserDto, is(resultDto));
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(id)).thenReturn(ofNullable(userEntity));
        doNothing().when(userRepository).delete(userEntity);

        userService.deleteUser(id);
    }

    @Test
    void getUsers() {
//        //given
//        int page = 1;
//        int limit = 1;
//        Pageable pageableRequest = PageRequest.of(page, limit);
//        Page<UserEntity> usersPage = mock(Page.class);
//        UserEntity userEntity1 = new UserEntity();
//        UserEntity userEntity2 = new UserEntity();
//        List<UserEntity> users = Lists.newArrayList(userEntity1, userEntity2);
//
//        when(userRepository.findAll(pageableRequest)).thenReturn(usersPage);
//        when(usersPage.getContent()).thenReturn(users);
//        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(userDto);
//
//        //when
//        List<UserDto> result = userService.getUsers(page, limit);
//
//        //then
//        assertEquals(result.size(), users.size());
    }
}