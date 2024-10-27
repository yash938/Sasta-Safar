package com.Ecommerce.store.controllers;

import com.Ecommerce.store.Security.JWTSecurityHelper;
import com.Ecommerce.store.dtos.JwtResponse;
import com.Ecommerce.store.dtos.UserDto;
import com.Ecommerce.store.entities.JwtRequest;
import com.Ecommerce.store.entities.User;
import com.Ecommerce.store.exceptions.BadApiRequest;
import com.Ecommerce.store.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController

@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JWTSecurityHelper jwtSecurityHelper;

    @Value("{googleId}")
    private String googleId;

    @Value("{newPassword}")
    private String newPassword;

    Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        this.doAuthenticate(request.getEmail(),request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtSecurityHelper.generateToken(userDetails);

        UserDto userDto = modelMapper.map(userDetails,UserDto.class);
        JwtResponse response = JwtResponse.builder().JwtToken(token)
                .user(userDto).build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);
        try{
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException ex){
            throw new BadApiRequest("Invalid username or password !!");
        }
    }


    @GetMapping("/current")
    public ResponseEntity<UserDto> currentUser(Principal principal){
        String name = principal.getName();
        return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name),UserDto.class), HttpStatus.OK);
    }


    //Login With Google
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws GeneralSecurityException, IOException {
        String idTokenString = data.get("idToken").toString();
        NetHttpTransport netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        // Set up Google ID Token Verifier
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory)
                .setAudience(Collections.singleton(googleId))
                .build();

        // Verify and parse the ID token
        GoogleIdToken idToken = verifier.verify(idTokenString);
        GoogleIdToken.Payload payload = idToken.getPayload();
        logger.info("id is {}" , payload);

        String email = payload.getEmail();
        User user=null;
        user = userService.findUserByEmailForGoogle(email).orElseThrow(null);

         if(user == null){
             //create new user
           user=  this.saveUser(data.get("name").toString(),data.get("email"),toString(),data.get("image").toString());
         }

        ResponseEntity<JwtResponse> login = this.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());
         return login;
    }
    private User saveUser(String name, Object o, String email, String image) {
        UserDto newUser = UserDto.builder()
                .name(name)
                .email(email)
                .password(newPassword)
                .image(image)
                .roles(new HashSet<>())
                .build();

        UserDto user = userService.createUser(newUser);
        return this.modelMapper.map(user,User.class);

        // Assuming saveUser is a method in userService to save a user
    }



}
