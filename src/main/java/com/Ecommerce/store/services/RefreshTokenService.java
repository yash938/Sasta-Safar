package com.Ecommerce.store.services;

import com.Ecommerce.store.dtos.RefreshToken;
import com.Ecommerce.store.dtos.RefreshTokenDto;
import com.Ecommerce.store.dtos.UserDto;

public interface RefreshTokenService {

     public RefreshTokenDto createRefreshToken(String username);

     public RefreshTokenDto findByToken(String token);

     public RefreshTokenDto verifyToken(RefreshTokenDto refreshTokenDto);

     UserDto getUser(RefreshTokenDto tokenDto);

}
