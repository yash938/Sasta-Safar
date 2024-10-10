package com.Ecommerce.store.services;


import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.dtos.UserDto;

import java.util.List;


public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto,int id);

    void deleteUser(int id);

    PaegableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    UserDto getSingleUser(int id);

    UserDto getUserEmail(String email);

    List<UserDto> searchUser(String keyword);



}
