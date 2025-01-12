package com.store.api.controller;

import com.store.api.exceptions.UserAlreadyExistException;
import com.store.api.exceptions.UserNotFoundException;
import com.store.api.model.user.*;
import com.store.api.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final SmsController smsController;

    public UserController(UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder,SmsController smsController) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.smsController = smsController;
    }

    @PostMapping("/user")
    public ResponseEntity<Boolean> addUser(@RequestBody UserRequest userRequest){
        try{
            userDetailsServiceImpl.save(userRequest.firstName(), userRequest.lastName(), userRequest.username(), passwordEncoder.encode(userRequest.password()));
               return ResponseEntity.ok(true);
        }catch (UserAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(false);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUser> > all(){
        return ResponseEntity.ok(userDetailsServiceImpl.getAllUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> user(@RequestParam String username){
        try{
            AppUser appUser = userDetailsServiceImpl.getUserByUsername(username);
            UserDTO userDTO = UserDTO.builder()
                    .id(appUser.getId())
                    .username(appUser.getUsername())
                    .firstName(appUser.getFirstName())
                    .lastName(appUser.getLastName())
                    .telephone(appUser.getTelephone())
                    .build();
            return ResponseEntity.ok(userDTO);
        }catch (UserNotFoundException e){
            return null;
        }
    }

    @PatchMapping("/edit-name/{id}")
    public ResponseEntity<UserDTO> editFirstAndLastName(@RequestParam Long id, @Valid @RequestBody NameEditDto nameEditDto){
       try{
         return  ResponseEntity.ok(userDetailsServiceImpl.editFirstAndLastName(id,nameEditDto.getFirstName(),nameEditDto.getLastName()));
       }catch (Exception e){
           return ResponseEntity.badRequest().body(null);
        }
    }
    @PatchMapping("/edit-username/{id}")
    public ResponseEntity<UserDTO> editUsername(@RequestParam Long id,@RequestBody UsernameDto usernameDto){
        try{
            return ResponseEntity.ok().body( userDetailsServiceImpl.editUsername(id, usernameDto.getUsername()));
        }catch (UserNotFoundException e){
            return null;
        }
    }

    /*
    @PatchMapping("/add-number/{id}")
    public ResponseEntity<String> addTelephoneNumber(@RequestParam Long id,@Valid @RequestBody UserTelephoneDto userTelephoneDto){
        log.info(userTelephoneDto.getTelephone());
        try{
           boolean successful= userDetailsServiceImpl.addTelephone(id,userTelephoneDto.getTelephone());
           if(successful){
              return smsController.sendSMS(userTelephoneDto.getTelephone());
           }
           return ResponseEntity.badRequest().body("Error performing operation");
        }catch (UserNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

     */



}
