package com.Ecommerce.store.serviceImplementation;

import com.Ecommerce.store.Configurations.UserMapperConfig;
import com.Ecommerce.store.Utility.Helper;
import com.Ecommerce.store.Utility.Utility;
import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.dtos.UserDto;
import com.Ecommerce.store.entities.User;
import com.Ecommerce.store.exceptions.ResourceNotFoundException;
import com.Ecommerce.store.repository.UserRepo;
import com.Ecommerce.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {


    Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
    @Autowired
  private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Value("${user.profile.image.path}")
    private String imagePath;

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

        String path = imagePath + userNotFound.getImage();

        try{
            Path path1 = Paths.get(path);
            Files.delete(path1);
        }catch (NoSuchFileException ex){
           logger.info("User image not found in folder {}");
           ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }


        userRepo.delete(userNotFound);

    }

    @Override
    public PaegableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepo.findAll(pageable);
        PaegableResponse<UserDto> paegable = Helper.getPaegable(page, UserDto.class);
        return paegable;
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
