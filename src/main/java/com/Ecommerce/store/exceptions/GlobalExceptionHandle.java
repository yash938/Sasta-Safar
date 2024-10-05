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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandle {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler(ResourceNotFoundException.class)
   public    ResponseEntity<AllException> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){

        logger.info("Exception Handler Invoked");

        AllException build = AllException.builder().message(ex.getMessage()).httpStatus(HttpStatus.NOT_FOUND).success(true).build();
        return new ResponseEntity<>(build,HttpStatus.OK);

    }




    //handle api exception handling
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleApiReponseException(MethodArgumentNotValidException ex){

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();

        Map<String, Object> objectObjectHashMap = new HashMap<>();

        allErrors.stream().forEach(objectError -> {

            String defaultMessage = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();

            objectObjectHashMap.put(field,defaultMessage);

        });
        return new ResponseEntity<>(objectObjectHashMap,HttpStatus.BAD_REQUEST);
    }
}
