package com.Ecommerce.store.Utility;

import com.Ecommerce.store.dtos.UserDto;
import com.Ecommerce.store.entities.User;

public class Utility {
    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImage(userDto.getImage());
        return user;
    }

    // Map Entity to DTO (if needed)
    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setGender(user.getGender());
        userDto.setAbout(user.getAbout());
        userDto.setImage(user.getImage());
        return userDto;
    }
}
