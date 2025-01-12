package com.store.api.service;

import com.store.api.model.user.AppUser;
import com.store.api.model.user.RefreshToken;
import com.store.api.repository.RefreshTokenRepository;
import com.store.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .appUser(userRepository.findByUsername(username)
                        .orElseThrow(()->new UsernameNotFoundException("Could not find "+username)))
                .expiredAt(Instant.now().plus(2, ChronoUnit.MINUTES))
                .token(UUID.randomUUID().toString())
                .issuedAt(Instant.now())
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken newToken(String token,String username){
        //delete old token
        removeToken(token);
        //create new token
        RefreshToken refreshToken = RefreshToken.builder()
                .appUser(userRepository.findByUsername(username)
                        .orElseThrow(()->new UsernameNotFoundException("Could not find "+username)))
                .issuedAt(Instant.now())
                .expiredAt(Instant.now().plus(2, ChronoUnit.MINUTES))
                .token(UUID.randomUUID().toString())
                .build();
        //return new token to user

        return refreshTokenRepository.save(refreshToken);
    }


    public boolean isTokenExpired(RefreshToken refreshToken){
       return refreshToken.getExpiredAt().isBefore(Instant.now());
    }

    public  void removeToken(String refreshToken){
        Optional<RefreshToken> token = refreshTokenRepository.findByToken(refreshToken);
        token.ifPresent(refreshTokenRepository::delete);
    }

    public Optional<RefreshToken> getByUsername(String username){
        Optional<AppUser> appUser = userRepository.findByUsername(username);
        return appUser.flatMap(user -> refreshTokenRepository.findById(user.getId()));
    }
}
