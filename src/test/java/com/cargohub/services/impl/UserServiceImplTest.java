package com.cargohub.services.impl;

import com.cargohub.dto.BillingDetailsDto;
import com.cargohub.dto.UserDto;
import com.cargohub.entities.BillingDetailsEntity;
import com.cargohub.entities.RoleEntity;
import com.cargohub.entities.UserEntity;
import com.cargohub.entities.extra.Roles;
import com.cargohub.exceptions.UserConflictException;
import com.cargohub.exceptions.UserNotFoundException;
import com.cargohub.exceptions.UserServiceException;
import com.cargohub.repositories.RoleRepository;
import com.cargohub.repositories.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

    private List<BillingDetailsEntity> getBillingDetailsEntity() {
        List<BillingDetailsDto> listBillingDetailsDto = getBillingDetailsDto();
        List<BillingDetailsEntity> detailsEntityList = new ArrayList<>();

        for (BillingDetailsDto bdd : listBillingDetailsDto) {
            detailsEntityList.add(new ModelMapper().map(bdd, BillingDetailsEntity.class));
        }

        return detailsEntityList;
    }

    private List<BillingDetailsDto> getBillingDetailsDto() {
        ArrayList<BillingDetailsDto> listBillingDetailsDto = new ArrayList<>();
        BillingDetailsDto billingDetailsDto1 = new BillingDetailsDto();
        billingDetailsDto1.setCardNumber("5523231357287208");
        billingDetailsDto1.setNameOnCard("Ivanov Ivan");
        billingDetailsDto1.setExpirationMonth("05");
        billingDetailsDto1.setExpirationYear("25");
        billingDetailsDto1.setBillingAddress("Pushkinska street 35B");
        listBillingDetailsDto.add(billingDetailsDto1);

        BillingDetailsDto billingDetailsDto2 = new BillingDetailsDto();
        billingDetailsDto2.setCardNumber("5523231357289999");
        billingDetailsDto2.setNameOnCard("Ivanov Ivan");
        billingDetailsDto2.setExpirationMonth("05");
        billingDetailsDto2.setExpirationYear("27");
        billingDetailsDto2.setBillingAddress("Pushkinska street 35B");
        listBillingDetailsDto.add(billingDetailsDto2);

        return listBillingDetailsDto;
    }

    private UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Ivan");
        userDto.setLastName("Ivanov");
        userDto.setEmail("ivanov@test.com");
        userDto.setPassword("12564");
        userDto.setAddress("Ivanova Street 4A");
        userDto.setEncryptedPassword(encryptedPassword);
        userDto.setBillingDetails(getBillingDetailsDto());

        return userDto;
    }

    @Test
    void createUser() {
        //given
        RoleEntity role = new RoleEntity();
        role.setName(Roles.ROLE_USER.name());

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
    void createUserThrowsUserConflictException() {
        //given
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(userEntity);

        //when, then
        assertThrows(UserConflictException.class, () -> userService.createUser(userDto));
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
        assertThrows(UsernameNotFoundException.class, () -> userService.getUser(userDto.getEmail()));
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
    void getUserByIdThrowsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    void updateUser() {
        //given
        userEntity.setId(id);

        when(userRepository.findById(id)).thenReturn(ofNullable(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(resultDto);

        //when
        UserDto storedUserDto = userService.updateUser(id, userDto);

        //then
        verify(userRepository).findById(id);
        verify(userRepository).save(userEntity);
        verify(modelMapper).map(userEntity, UserDto.class);

        assertThat(storedUserDto, is(resultDto));
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(id)).thenReturn(ofNullable(userEntity));
        doNothing().when(userRepository).delete(userEntity);

        userService.deleteUser(id);

        verify(userRepository).findById(id);
        verify(userRepository).delete(userEntity);
    }

    @Test
    void getUsers() {
        //given
        int page = 1;
        int limit = 1;
        UserEntity userEntity1 = new UserEntity();
        UserEntity userEntity2 = new UserEntity();
        List<UserEntity> users = Lists.newArrayList(userEntity1, userEntity2);
        Page<UserEntity> usersPage = new PageImpl<>(users);

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(usersPage);

        //when
        Page<UserEntity> resultUsersPage = userService.getUsers(page, limit);

        //then
        assertThat(resultUsersPage, is(usersPage));
        verify(userRepository).findAll(any(PageRequest.class));
    }

    @Test
    void updateUserPassword() {
        //given
        userEntity.setId(id);

        when(userRepository.findById(id)).thenReturn(ofNullable(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        //when
        boolean saved = userService.updateUserPassword(id, "Testme");

        //then
        verify(userRepository).findById(id);
        verify(userRepository).save(userEntity);

        assertTrue(saved);
    }

    @Test
    void addUsersBillingDetailsCard() {
        //given
        userEntity.setId(id);
        BillingDetailsDto billingDetailsDto = new BillingDetailsDto();
        BillingDetailsEntity billingDetailsEntity = new BillingDetailsEntity();

        when(userRepository.findById(id)).thenReturn(ofNullable(userEntity));
        when(modelMapper.map(billingDetailsDto, BillingDetailsEntity.class)).thenReturn(billingDetailsEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        //when
        boolean check = userService.addUsersBillingDetailsCard(id, billingDetailsDto);

        //then
        verify(modelMapper).map(billingDetailsDto, BillingDetailsEntity.class);
        verify(userRepository).save(userEntity);

        assertTrue(check);
    }

    @Test
    void removeUsersBillingDetailsCard() {
        userEntity.setId(id);
        int idToBeRemoved = 0;
        List<BillingDetailsEntity> billingDetailsEntityList = new ArrayList<>();
        billingDetailsEntityList.add(new BillingDetailsEntity());

        when(userRepository.findById(id)).thenReturn(ofNullable(userEntity));
        int countBeforeRemove = userEntity.getBillingDetails().size();
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        //when
        userService.removeUsersBillingDetailsCard(id, idToBeRemoved);
        int countAfterRemove = userEntity.getBillingDetails().size();
        //then

        assertThat(countBeforeRemove, is(countAfterRemove + 1));
    }


}