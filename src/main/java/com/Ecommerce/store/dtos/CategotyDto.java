package com.Ecommerce.store.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategotyDto {
    private int categoryId;

    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @NotBlank(message = "Title must be required")
    private String title;


    @NotBlank(message = "Description must be required")
    private String description;


    private String coverImage;
}
