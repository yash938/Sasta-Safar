package com.Ecommerce.store.dtos;

import com.Ecommerce.store.Validator.ImageValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private int userId;


    @Size(min = 3,max = 20)
    private String name;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "You entered wrong email")
    private String email;


    @NotBlank(message = "Password is required")
    private String password;

    @Size(min = 4,max = 6)
    private String gender;

    @NotBlank(message = "write about yourSelf")
    private String about;

    @ImageValid
    private String image;

    private Set<RoleDto> roles = new HashSet<>();
}
