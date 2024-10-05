package com.Ecommerce.store.Validator;

import jakarta.persistence.Table;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface  ImageValid {

    String message() default "Image name is Invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
