package com.Ecommerce.store.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllException {
    private String message;
    private boolean success;
    private HttpStatus httpStatus;
}
