package com.Ecommerce.store.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ImageResponse {
    private String imageName;
    private String message;
    private boolean success;
    private HttpStatus httpStatus;
    private LocalDate localDate;
}
