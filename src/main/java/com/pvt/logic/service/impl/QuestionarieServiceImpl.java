package com.pvt.logic.service.impl;

import com.google.inject.internal.util.Lists;
import com.pvt.dao.entity.Question;
import com.pvt.dao.entity.Survey;
import com.pvt.dao.repository.SurveyRepository;

import com.pvt.logic.service.QuestionarieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class QuestionarieServiceImpl implements QuestionarieService {

    private final SurveyRepository surveyRepository;


    @Autowired
    public QuestionarieServiceImpl(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Override
    public List<Question> getIntroductionSurveyQuestions() {
        return Lists.newArrayList(surveyRepository.findByName(Survey.INTRODUCTION_SURVEY)
                .getQuestions());
    }
}
