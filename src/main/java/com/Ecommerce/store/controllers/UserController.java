package com.Ecommerce.store.controllers;


import com.Ecommerce.store.dtos.UserDto;
import com.Ecommerce.store.exceptions.AllException;
import com.Ecommerce.store.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable int id){
        UserDto userDto1 = userService.updateUser(userDto, id);
        return new ResponseEntity<>(userDto1,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AllException> deleteUser(@PathVariable int id){
       userService.deleteUser(id);
       AllException.builder().success(true).message("User is deleted").httpStatus(HttpStatus.OK);
       return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAll(){
        List<UserDto> allUser = userService.getAllUser();
        return new ResponseEntity<>(allUser,HttpStatus.OK);
    }

    @GetMapping("/single/{id}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable int id){
        UserDto singleUser = userService.getSingleUser(id);
        return new ResponseEntity<>(singleUser,HttpStatus.OK);
    }

    @GetMapping("/userEmail/{email}")
    public ResponseEntity<UserDto> byEmail(@PathVariable String email){
        UserDto userEmail = userService.getUserEmail(email);
       return new ResponseEntity<>(userEmail,HttpStatus.OK);
    }

    @GetMapping("/key/{search}")
    public ResponseEntity<List<UserDto>> getByKeyword(@PathVariable String search){
        List<UserDto> userDtos = userService.searchUser(search);
        return new ResponseEntity<>(userDtos,HttpStatus.OK);

    }
}
