package com.cargohub.services;

import com.cargohub.dto.BillingDetailsDto;
import com.cargohub.dto.UserDto;
import com.cargohub.entities.BillingDetailsEntity;
import com.cargohub.entities.UserEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto user);
    UserDto getUser(String email);
    UserDto getUserById(long id);

    //need to decide who will be responsible for this operation (user or admin)
    UserDto updateUser(long id, UserDto user);
    UserDto updateUserPassword(long id, String password);
    UserDto updateBillingDetails(long id, List<BillingDetailsDto> billingDetailsDtoList);

    void deleteUser(long id);
    Page<UserEntity> getUsers(int page, int limit);
}
