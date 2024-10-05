package com.Ecommerce.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private int id;


    private String name;

    private String email;

    private String password;
    private String gender;

    private String about;
    private String image;
}
