package com.pvt.logic.service;

import com.pvt.logic.classification.Question;

import java.util.Collection;
import java.util.List;

public interface QuestionarieService {

    List<com.pvt.dao.entity.Question> getIntroductionSurveyQuestions();
}
