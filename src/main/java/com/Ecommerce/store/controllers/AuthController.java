package com.Ecommerce.store.controllers;


import com.Ecommerce.store.dtos.JwtRequest;
import com.Ecommerce.store.dtos.JwtResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/generateToken")
    public ResponseEntity<JwtResponse> login(JwtRequest jwtRequest){
        logger.info("Username {} , Password {} ",jwtRequest.getUsername(),jwtRequest.getPassword());


        return ResponseEntity.ok(null);
    }
}
