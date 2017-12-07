package com.pvt.logic.service;


import com.pvt.logic.model.SearchRoutineContext;
import com.pvt.logic.model.SearchRoutineResult;

public interface RoutineSearchService {

    SearchRoutineResult search(SearchRoutineContext searchProgramContext);
}
