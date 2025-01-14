package com.store.api.repository;

import com.store.api.model.user.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByToken(String token);
}
