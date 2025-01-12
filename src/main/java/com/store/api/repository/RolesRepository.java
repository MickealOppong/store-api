package com.store.api.repository;

import com.store.api.model.user.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends CrudRepository<UserRole,Long> {

    Optional<UserRole>  findByRole(String role);
}
