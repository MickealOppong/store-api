package com.store.api.controller;

import com.store.api.model.user.*;
import com.store.api.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final RefreshTokenService  refreshTokenService;
    private final UserTokenService userTokenService;
    private final AuthenticationManager authenticationManager;
    private final CartService cartService;
    private final FavouriteService  favouriteService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthController(UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder,
                          RefreshTokenService refreshTokenService, UserTokenService userTokenService,
                          AuthenticationManager authenticationManager,@Lazy CartService cartService,
                          @Lazy FavouriteService favouriteService
                ) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.refreshTokenService = refreshTokenService;
        this.userTokenService = userTokenService;
        this.authenticationManager = authenticationManager;
        this.cartService = cartService;
        this.favouriteService = favouriteService;
    }

    @GetMapping
    public String welcome(){
        return "Welcome,this endpoint is not secured";
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password()));
        if(authentication.isAuthenticated()){
            AppUser  appUser = userDetailsServiceImpl.getUserByUsername(loginRequest.username());

            RefreshToken token =refreshTokenService.createToken(appUser.getUsername());
            TokenDto tokenDto = TokenDto.builder()
                    .token(userTokenService.token(authentication).orElse(null))
                    .accessToken(token.getToken())
                    .expiredAt(token.getExpiredAt())
                    .issuedAt(token.getIssuedAt())
                    .build();
            UserDTO userDTO= UserDTO.builder()
                    .tokenDto(tokenDto)
                    .username(appUser.getUsername())
                    .id(appUser.getId())
                    .firstName(appUser.getFirstName())
                    .lastName(appUser.getLastName())
                    .telephone(appUser.getTelephone())
                    .build();

            //check whether user has created cart and update cart data
            cartService.updateUser(appUser,loginRequest.sessionId());
            favouriteService.updateUser(appUser);
            return  ResponseEntity.ok().body(userDTO);
        }
       return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login-postman")
    public ResponseEntity<UserDTO> loginPostman(String username,String  password,String sessionId){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username,password));
        if(authentication.isAuthenticated()){
            AppUser  appUser = userDetailsServiceImpl.getUserByUsername(username);
            RefreshToken token =refreshTokenService.createToken(username);
            TokenDto tokenDto = TokenDto.builder()
                    .token(userTokenService.token(authentication).orElse(null))
                    .accessToken(token.getToken())
                    .expiredAt(token.getExpiredAt())
                    .issuedAt(token.getIssuedAt())
                    .build();
            UserDTO userDTO= UserDTO.builder()
                    .tokenDto(tokenDto)
                    .username(appUser.getUsername())
                    .id(appUser.getId())
                    .firstName(appUser.getFirstName())
                    .lastName(appUser.getLastName())
                    .telephone(appUser.getTelephone())
                    .build();
            return  ResponseEntity.ok().body(userDTO);
        }
        else{
            return null;
        }

    }

    @PatchMapping("/change-password/{id}")
    public ResponseEntity<String> changePassword(Long id,String oldPassword,String newPassword){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(SecurityContextHolder
                        .getContext().getAuthentication().getName(),oldPassword));
        boolean isSuccessful =userDetailsServiceImpl
                .changeUserPassword(id,passwordEncoder.encode(newPassword),authentication.isAuthenticated());
        if(isSuccessful){
            return ResponseEntity.ok().body("Password changed");
        }
        return ResponseEntity.badRequest().body("Password change unsuccessful");
    }

    @DeleteMapping("/logout")
    public void logoutUser(@RequestParam String accessToken){
        log.info(accessToken+"");
        refreshTokenService.removeToken(accessToken);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<UserDTO> newToken(String accessToken){
       RefreshToken userToken= refreshTokenService.findToken(accessToken).orElse(null);
       if(userToken != null && !refreshTokenService.isTokenExpired(userToken)){
           String username = userToken.getAppUser().getUsername();

           RefreshToken newToken = refreshTokenService.newToken(accessToken,username);

           TokenDto tokenDto = TokenDto.builder()
                   .token(userTokenService.token(newToken.getAppUser().getUsername()).orElse(null))
                   .accessToken(newToken.getToken())
                   .expiredAt(newToken.getExpiredAt())
                   .issuedAt(newToken.getIssuedAt())
                   .build();
           UserDTO token = UserDTO.builder()
                   .tokenDto(tokenDto)
                   .username(newToken.getAppUser().getUsername())
                   .id(userToken.getAppUser().getId())
                   .firstName(newToken.getAppUser().getFirstName())
                   .lastName(newToken.getAppUser().getLastName())
                   .telephone(newToken.getAppUser().getTelephone())
                   .build();
           return ResponseEntity.ok(token);
       }
        return ResponseEntity.ok(null);
    }
}

