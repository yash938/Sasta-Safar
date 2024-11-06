package com.Ecommerce.store.dtos;

import com.Ecommerce.store.entities.User;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    UserDto user;

    private RefreshTokenDto refreshTokenDto;
}
