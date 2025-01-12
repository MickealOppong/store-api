package com.store.api.service;

import com.store.api.exceptions.RoleNotFoundException;
import com.store.api.model.user.UserRole;
import com.store.api.repository.RolesRepository;
import org.springframework.stereotype.Service;

@Service

public class UserRoleService {


    private RolesRepository  rolesRepository;

    public UserRoleService(RolesRepository rolesRepository){
        this.rolesRepository = rolesRepository;
    }

    public void save(UserRole  userRole){
        rolesRepository.save(userRole);
    }

    public UserRole getRole(String role)throws RoleNotFoundException{
        return rolesRepository.findByRole(role).orElseThrow(()->new RoleNotFoundException("Could not find  "+role));
    }
}
