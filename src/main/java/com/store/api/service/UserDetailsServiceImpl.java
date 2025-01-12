package com.store.api.service;

import com.store.api.exceptions.UserAlreadyExistException;
import com.store.api.exceptions.UserNotFoundException;
import com.store.api.model.user.AppUser;
import com.store.api.model.user.UserDTO;
import com.store.api.model.user.UserRole;
import com.store.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final ConfirmationCodeService confirmationCodeService;

    public UserDetailsServiceImpl(UserRepository userRepository,UserRoleService userRoleService,
                                  ConfirmationCodeService confirmationCodeService){
        this.userRepository = userRepository;
        this.userRoleService  =  userRoleService;
        this.confirmationCodeService = confirmationCodeService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       AppUser  appUser = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Could  not find "+username));
       return  new User(appUser.getUsername(),appUser.getPassword(),appUser.getAuthorities());
    }




    public void save(String firstName,String lastName,String username,String password)throws  UserAlreadyExistException{
        boolean userAlreadyExist =checkIfUserAlreadyExist(username);
        if(userAlreadyExist){
            throw new UserAlreadyExistException("Error: Account with this email already exists");
        }
        UserRole  role = userRoleService.getRole("USER");
        AppUser user = new AppUser(firstName,lastName,username,password);
        user.setUserDefaultSettings();
        user.setUserRoles(Set.of(role));
        userRepository.save(user);
    }

    private boolean checkIfUserAlreadyExist(String username){
       return userRepository.findByUsername(username).isPresent();
    }

    private boolean userExist(String username){
       return userRepository.findByUsername(username).isPresent();
    }

    public AppUser getUserByUsername(String username){
        return   userRepository.findByUsername(username)
                   .orElseThrow(()->new UsernameNotFoundException(username+" does not exist"));
    }

    public AppUser getUserById(Long id){
        return   userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("User does not exist"));
    }

    public List<AppUser> getAllUsers(){
        return userRepository.findAll();
    }

    public UserDTO editFirstAndLastName(Long id, String firstName, String lastName) {
        AppUser user = userRepository.findById(id).orElse(null);
        if(user!=null){
            user.setFirstName(firstName);
            user.setLastName(lastName);
          AppUser appUser =  userRepository.save(user);
            return UserDTO.builder()
                    .firstName(appUser.getFirstName())
                    .lastName(appUser.getLastName())
                    .id(appUser.getId())
                    .telephone(appUser.getTelephone())
                    .username(appUser.getUsername())
                    .build();
        }
        return null;
    }


    public UserDTO editUsername(Long id,String username){
        AppUser user = userRepository.findById(id).orElse(null);
      if(user!=null){
          user.setUsername(username);
          AppUser appUser =userRepository.save(user);
         return UserDTO.builder()
                 .firstName(appUser.getFirstName())
                 .lastName(appUser.getLastName())
                 .id(appUser.getId())
                 .telephone(appUser.getTelephone())
                 .username(appUser.getUsername())
                 .build();
      }
      return null;
    }


    public boolean changeUserPassword(Long id,String newPassword,boolean isAuthenticated){
       AppUser appUser= userRepository.findById(id)
               .orElseThrow(()->new UsernameNotFoundException("Invalid user id"));
           if(isAuthenticated){
               appUser.setPassword(newPassword);
               userRepository.save(appUser);
               return true;
           }

       return false;
    }

    public boolean addTelephone(Long id,String telephone){
      AppUser appUser= userRepository.findById(id)
               .orElseThrow(()->new UserNotFoundException("Invalid user"));

       if(appUser !=null){
           appUser.setTelephone(telephone);
           userRepository.save(appUser);
           confirmationCodeService.save(telephone);
           return true;
       }
       return false;
    }
}


