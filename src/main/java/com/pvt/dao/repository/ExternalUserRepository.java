package com.pvt.dao.repository;

import com.pvt.dao.entity.ExternalUser;
import org.springframework.data.repository.CrudRepository;


public interface ExternalUserRepository extends CrudRepository<ExternalUser, Long> {
}
