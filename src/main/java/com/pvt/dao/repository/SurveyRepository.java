package com.pvt.dao.repository;


import com.pvt.dao.entity.ExternalUser;
import com.pvt.dao.entity.Survey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SurveyRepository extends CrudRepository<Survey, Long> {

    List<Survey> findByUser(ExternalUser user);

}
