package com.Ecommerce.store.exceptions;

import org.apache.coyote.BadRequestException;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(){
        super("Resource Not Found");
    }

    public ResourceNotFoundException(String message){
        super(message);
    }


}
