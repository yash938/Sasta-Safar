package com.Ecommerce.store.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler(ResourceNotFoundException.class)
   public    ResponseEntity<AllException> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){

        logger.info("Exception Handler Invoked");

        AllException build = AllException.builder().message(ex.getMessage()).httpStatus(HttpStatus.NOT_FOUND).success(true).build();
        return new ResponseEntity<>(build,HttpStatus.OK);

    }

}
