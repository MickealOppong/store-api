package com.store.api.model.user;

import com.store.api.util.LogEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole extends LogEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String role;
    /*
    @ManyToMany(mappedBy = "userRoles",fetch = FetchType.EAGER)
    private List<AppUser>  appUserList = new ArrayList<>();

     */

    public UserRole(String role){
        this.role =  role;
    }
}
