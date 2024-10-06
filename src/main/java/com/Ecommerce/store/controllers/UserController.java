package com.Ecommerce.store.controllers;


import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.dtos.UserDto;
import com.Ecommerce.store.exceptions.AllException;
import com.Ecommerce.store.exceptions.ImageResponse;
import com.Ecommerce.store.services.ImageFile;
import com.Ecommerce.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    Logger logger  = LoggerFactory.getLogger(UserController.class);

    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    @Autowired
    private UserService userService;

    @Autowired
    private ImageFile imageFile;
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
    public ResponseEntity<PaegableResponse<UserDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir
    ) {
        PaegableResponse<UserDto> response = userService.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
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


    //ImageUpload Controller
    @PostMapping("/image/{id}")
    public ResponseEntity<ImageResponse> uploadFile(@PathVariable int id, @RequestParam("image")MultipartFile image){
        String imageName = imageFile.UploadFile(image, imageUploadPath);

        UserDto singleUser = userService.getSingleUser(id);
        singleUser.setImage(imageName);

        UserDto userDto = userService.updateUser(singleUser, id);

        ImageResponse imageResponse = new ImageResponse(imageName,"Image upload successfully",true,HttpStatus.OK, LocalDate.now());
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }




    //Serve Image

    @GetMapping("/image/{id}")
    public void ServeImage(@PathVariable int id, HttpServletResponse response) throws IOException {
        UserDto singleUser = userService.getSingleUser(id);
        logger.info("user image name {}",singleUser.getImage());
        InputStream resource = imageFile.getResource(imageUploadPath, singleUser.getImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response. getOutputStream());
    }








}
