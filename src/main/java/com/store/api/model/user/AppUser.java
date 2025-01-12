package com.store.api.model.user;

import com.store.api.util.LogEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "Users")
@Data
@Builder
@AllArgsConstructor
public class AppUser extends LogEntity implements UserDetails{


    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private Long recId;
    private String username;
    private String firstName;
    private String lastName;
    private String telephone;
    private String password;

    //account status
    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "fk_role", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "fk_user", referencedColumnName = "id"))
    private Set<UserRole> userRoles  = new HashSet<>();


    //default constructor
    public AppUser(){}

    public AppUser(String firstName,String lastName,String username,String password) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void  setUserDefaultSettings(){
        this.isAccountNonExpired = true;
        this.isAccountNonLocked  = true;
        this.isCredentialsNonExpired   = true;
        this.isEnabled =true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(UserRole role: userRoles){
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
