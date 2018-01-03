package com.pvt.logic.service.impl;

import com.pvt.dao.entity.Answer;
import com.pvt.dao.entity.Measurement;
import com.pvt.dao.entity.MeasurementUnit;
import com.pvt.dao.entity.RoutineMeasurement;
import com.pvt.dao.repository.MeasurementUnitRepository;
import com.pvt.dao.repository.RoutineMeasurementRepository;
import com.pvt.dao.repository.RoutineRepository;
import com.pvt.logic.model.SearchRoutineContext;
import com.pvt.logic.model.SearchRoutineResult;
import com.pvt.logic.service.RoutineSearchService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.inject.internal.util.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

@Service
@Log4j
public class RoutineSearchServiceImpl implements RoutineSearchService {

    private final MeasurementUnitRepository measurementUnitRepository;

    private final RoutineMeasurementRepository routineMeasurementRepository;

    private final RoutineRepository routineRepository;


    @Autowired
    public RoutineSearchServiceImpl(MeasurementUnitRepository measurementUnitRepository, RoutineMeasurementRepository routineMeasurementRepository
            , RoutineRepository routineRepository) {
        this.measurementUnitRepository = measurementUnitRepository;
        this.routineMeasurementRepository = routineMeasurementRepository;
        this.routineRepository = routineRepository;
    }

    @Override
    public SearchRoutineResult search(SearchRoutineContext searchProgramContext) {
        final SearchRoutineResult result = new SearchRoutineResult();
        final List<MeasurementUnit> measurementUnits = searchProgramContext.getSurvey()
                .getAnswers()
                .stream()
                .filter(item -> item.getAnswer() != null)
                .map(Answer::getMeasurement)
                .map(Measurement::getMeasurementUnit)
                .collect(toList());
        final List<String> measurementUnitNames = measurementUnits
                .stream()
                .map(MeasurementUnit::getName)
                .collect(toList());
        final Map<MeasurementUnit, List<RoutineMeasurement>>
                routineMeasurements =
                newArrayList(routineMeasurementRepository.findByMeasurementUnitNameIsIn(measurementUnitNames))
                        .stream()
                        .collect(Collectors.groupingBy(RoutineMeasurement::getMeasurementUnit));
        searchProgramContext.getSurvey()
                .getAnswers()
                .stream()
                .filter(item -> item.getAnswer() != null)
                .forEach(answer -> {
                    routineMeasurements.getOrDefault(answer.getMeasurement().getMeasurementUnit(), new ArrayList<>())
                            .forEach(rm -> {
                                if (answer.getMeasurement().match(rm)) {
                                    result.addRoutine(rm.getRoutine());
                                }
                            });


                });
        return result;
    }

}
