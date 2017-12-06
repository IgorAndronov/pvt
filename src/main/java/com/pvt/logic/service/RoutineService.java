package com.pvt.logic.service;


import com.pvt.logic.model.SearchRoutineContext;
import com.pvt.logic.model.SearchRoutineResult;

public interface RoutineService {

    SearchRoutineResult search(SearchRoutineContext searchProgramContext);
}
