package com.Ecommerce.store.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandle {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler(ResourceNotFoundException.class)
   public ResponseEntity<AllException> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){

        logger.info("Exception Handler Invoked");
        AllException allException = new AllException(ex.getMessage(), true, HttpStatus.NOT_FOUND, LocalDate.now());
        return new ResponseEntity<>(allException,HttpStatus.NOT_FOUND);
    }

    //handle api exception handling
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleApiResponseException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();  // Get the field name
            String errorMessage = error.getDefaultMessage();     // Get the default message from the validation
            errors.put(fieldName, errorMessage);                 // Add to the errors map
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<AllException> BadApiRequestExceptionHandler(BadApiRequest ex){
        logger.info("Bad Api Request");
        AllException allException = new AllException(ex.getMessage(), false, HttpStatus.BAD_REQUEST, LocalDate.now());
        return  new ResponseEntity<>(allException,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<AllException> UserNotFound(UserNotFoundException ex){
        AllException allException = new AllException(ex.getMessage(), false, HttpStatus.BAD_REQUEST, LocalDate.now());
        return new ResponseEntity<>(allException,HttpStatus.NOT_FOUND);
    }



}
