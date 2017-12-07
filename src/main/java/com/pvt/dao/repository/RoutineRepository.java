package com.pvt.dao.repository;

import com.pvt.dao.entity.Routine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends CrudRepository<Routine,Long> {
}
