package com.pvt.dao.repository;

import com.pvt.dao.entity.ExternalUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<ExternalUser, Long> {
}
