package com.pvt.dao.repository;

import com.pvt.dao.entity.ExternalUser;
import com.pvt.dao.entity.UserSurvey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSurveyRepository extends CrudRepository<UserSurvey, Long> {

    List<UserSurvey> findByUser(ExternalUser user);


}
