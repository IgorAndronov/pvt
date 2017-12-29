package com.pvt.dao.repository;


import com.pvt.dao.entity.Survey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyRepository extends CrudRepository<Survey, Long> {

    Survey findByName(String name);

}
