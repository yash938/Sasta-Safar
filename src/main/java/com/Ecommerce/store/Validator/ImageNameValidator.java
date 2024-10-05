package com.Ecommerce.store.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator <ImageValid,String>{
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        //logic
        if(value.isBlank()){
            return false;
        }else {
            return true;
        }
    }
}
