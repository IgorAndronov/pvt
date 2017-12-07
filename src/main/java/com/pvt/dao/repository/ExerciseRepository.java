package com.pvt.dao.repository;

import com.pvt.dao.entity.Exercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
}
