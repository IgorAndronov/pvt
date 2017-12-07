package com.pvt.dao.repository;

import com.pvt.dao.entity.BaseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericRepository extends CrudRepository<BaseEntity<Long>,Long> {

}
