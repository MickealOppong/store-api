package com.store.api.repository;

import com.store.api.model.util.ConfirmationCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository extends CrudRepository<ConfirmationCode,Long> {

    Optional<ConfirmationCode> findByCode(String code);
    Optional<ConfirmationCode> findByTelephone(String telephone);
    List<ConfirmationCode> findAll();
}
