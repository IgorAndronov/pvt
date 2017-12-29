package com.pvt.logic.service.impl;

import com.pvt.dao.entity.Answer;
import com.pvt.dao.entity.Measurement;
import com.pvt.dao.entity.MeasurementUnit;
import com.pvt.dao.entity.RoutineMeasurement;
import com.pvt.dao.repository.MeasurementUnitRepository;
import com.pvt.dao.repository.RoutineMeasurementRepository;
import com.pvt.logic.model.SearchRoutineContext;
import com.pvt.logic.model.SearchRoutineResult;
import com.pvt.logic.service.RoutineSearchService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j
public class RoutineSearchServiceImpl implements RoutineSearchService {

    private final MeasurementUnitRepository measurementUnitRepository;

    private final RoutineMeasurementRepository routineMeasurementRepository;


    @Autowired
    public RoutineSearchServiceImpl(MeasurementUnitRepository measurementUnitRepository, RoutineMeasurementRepository routineMeasurementRepository) {
        this.measurementUnitRepository = measurementUnitRepository;
        this.routineMeasurementRepository = routineMeasurementRepository;
    }

    @Override
    public SearchRoutineResult search(SearchRoutineContext searchProgramContext) {
        final SearchRoutineResult result = new SearchRoutineResult();
        final List<String> measurementUnitNames = searchProgramContext.getSurvey()
                .getAnswers()
                .stream()
                .filter(item->item.getAnswer()!=null)
                .map(Answer::getMeasurement)
                .map(Measurement::getMeasurementUnit)
                .map(MeasurementUnit::getName)
                .collect(Collectors.toList());

        final Iterable<RoutineMeasurement> routineMeasurements = routineMeasurementRepository.findByMeasurementUnitNameIsIn(measurementUnitNames);
        routineMeasurements.forEach(routineMeasurement -> {

            if(match(searchProgramContext,routineMeasurement) && !result.getRoutines().contains(routineMeasurement.getRoutine())) {
                result.getRoutines().add(routineMeasurement.getRoutine());
            }
        });



        return result;
    }

    private boolean match(SearchRoutineContext searchProgramContext, RoutineMeasurement routineMeasurement) {
        //TODO implement it.
        return true;
    }
}
