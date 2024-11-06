package com.Ecommerce.store.controllers;


import com.Ecommerce.store.Security.JwtHelper;
import com.Ecommerce.store.dtos.*;
import com.Ecommerce.store.entities.User;
import com.Ecommerce.store.services.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper helper;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RefreshTokenService refreshTokenService;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    public ResponseEntity<JwtResponse> generateRefreshToken(@RequestBody RefreshToken refreshToken){
        RefreshTokenDto byToken = refreshTokenService.findByToken(refreshToken.getRefreshToken());
        RefreshTokenDto refreshTokenDto = refreshTokenService.verifyToken(byToken);


        UserDto user = refreshTokenService.getUser(refreshTokenDto);
        String s = helper.generateToken(modelMapper.map(user,User.class));

        JwtResponse build = JwtResponse.builder().token(s).refreshTokenDto(byToken).user(user ).build();
        return ResponseEntity.ok(build);

    }

    @PostMapping("/generateToken")
    public ResponseEntity<JwtResponse> login(@RequestBody  JwtRequest jwtRequest){
        logger.info("Username {} , Password {} ",jwtRequest.getEmail(),jwtRequest.getPassword());

        this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());

        User user = (User)userDetailsService.loadUserByUsername(jwtRequest.getEmail());


        String token = helper.generateToken(user);

        RefreshTokenDto refreshToken = refreshTokenService.createRefreshToken(user.getEmail());


        JwtResponse build = JwtResponse.builder().token(token).refreshTokenDto(refreshToken).user(modelMapper.map(user, UserDto.class)).build();

        return ResponseEntity.ok(build);
    }

    private void doAuthenticate(String email, String password) {
    try{
        Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
    }catch (BadCredentialsException ex){
        ex.getMessage();
    }
    }
}
