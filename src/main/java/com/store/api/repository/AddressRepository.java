package com.store.api.repository;

import com.store.api.model.Address.GlobalAddress;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<GlobalAddress,Long> {

    @Override
    List<GlobalAddress>  findAll();

    @Query(value = "SELECT * FROM address g where g.address_type=?1 AND g.userid=?2",nativeQuery = true)
    List<GlobalAddress>   findByAddressTypeAndUserid(String addressType,Long userid);

}
