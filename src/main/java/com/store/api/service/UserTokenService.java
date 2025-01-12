package com.store.api.service;

import com.store.api.model.user.AppUser;
import com.store.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserTokenService {

    private JwtEncoder jwtEncoder;
    private UserRepository userRepository;
    private RefreshTokenService refreshTokenService;


    public Optional<String> token(Authentication authentication){

        // String scope = userRepository.findByUsername(authentication.getName())
        //       .map(AppUser::getAuthorities).get().toString();
       AppUser appUser =userRepository.findByUsername(authentication.getName()).orElse(null);
       if(appUser!=null){
           Set<String> roles = AuthorityUtils.authorityListToSet(appUser.getAuthorities())
                   .stream()
                   .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
           JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                   .issuer("local")
                   .issuedAt(Instant.now())
                   .expiresAt(Instant.now().plus(20, ChronoUnit.MINUTES))
                   .subject(authentication.getName())
                   .claim("scope",roles)
                   .build();
           return Optional.of(jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue());
       }
       return Optional.empty();
    }

    public Optional<String> token(String username){

        // String scope = userRepository.findByUsername(authentication.getName())
        //       .map(AppUser::getAuthorities).get().toString();
        AppUser appUser =userRepository.findByUsername(username).orElse(null);
        if(appUser!=null){
            Set<String> roles = AuthorityUtils.authorityListToSet(appUser.getAuthorities())
                    .stream()
                    .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
            JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                    .issuer("local")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plus(20, ChronoUnit.MINUTES))
                    .subject(appUser.getUsername())
                    .claim("scope",roles)
                    .build();
            return Optional.of(jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue());
        }
        return Optional.empty();
    }
}
