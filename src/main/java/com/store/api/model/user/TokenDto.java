package com.store.api.model.user;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class TokenDto {

    private Instant expiredAt;
    private Instant issuedAt;
    private String accessToken;
    private String token;
}
