package com.Ecommerce.store.serviceImplementation;

import com.Ecommerce.store.Configurations.UserMapperConfig;
import com.Ecommerce.store.Utility.Utility;
import com.Ecommerce.store.dtos.UserDto;
import com.Ecommerce.store.entities.User;
import com.Ecommerce.store.exceptions.ResourceNotFoundException;
import com.Ecommerce.store.repository.UserRepo;
import com.Ecommerce.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    @Autowired
  private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDto createUser(UserDto userDto) {
        User map = modelMapper.map(userDto, User.class);
        User save = userRepo.save(map);
        UserDto map1 = modelMapper.map(save, UserDto.class);
        return map1;
    }

    @Override
    public UserDto updateUser(UserDto userDto, int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user id is not found"));


      modelMapper.map(userDto,user);

        User save = userRepo.save(user);
        return modelMapper.map(save,UserDto.class);

    }

    @Override
    public void deleteUser(int id) {

        User userNotFound = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        userRepo.delete(userNotFound);

    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userRepo.findAll();
        List<UserDto> collect = users.stream().map(user -> Utility.mapToUserDto(user)).collect(Collectors.toList());
        return collect;


    }

    @Override
    public UserDto getSingleUser(int id) {
        User userIdIsNotFound = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user id is not found"));
        return Utility.mapToUserDto(userIdIsNotFound);
    }

    @Override
    public UserDto getUserEmail(String email) {
        User emailIsNotFound = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email is not found"));
        return Utility.mapToUserDto(emailIsNotFound);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> byNameContaining = userRepo.findByNameContaining(keyword);
        List<UserDto> collect = byNameContaining.stream().map(byNameContainings -> Utility.mapToUserDto(byNameContainings)).collect(Collectors.toList());
        return collect;
    }
}
