package com.Ecommerce.store.dtos;

import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RefreshTokenDto {
    private int id;
    private String token;
    private Instant expiryDate;

}
