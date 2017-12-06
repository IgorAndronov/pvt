package com.pvt.logic.service.impl;

import com.pvt.logic.model.SearchRoutineContext;
import com.pvt.logic.model.SearchRoutineResult;
import com.pvt.logic.service.RoutineService;


public class RoutineServiceImpl implements RoutineService {


    @Override
    public SearchRoutineResult search(SearchRoutineContext searchProgramContext) {
        final SearchRoutineResult result = new SearchRoutineResult();

        searchProgramContext.getAnswers().forEach(answer -> {
            if (answer.getMeasurement() != null) {
                //Find measurement in database
            }
        });
        return result;
    }
}
