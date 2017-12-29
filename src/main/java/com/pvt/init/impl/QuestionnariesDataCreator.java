package com.pvt.init.impl;


import com.google.inject.internal.util.ImmutableList;
import com.google.inject.internal.util.ImmutableMap;
import com.pvt.dao.entity.Exercise;
import com.pvt.dao.entity.ExerciseGroup;
import com.pvt.dao.entity.MeasurementUnit;
import com.pvt.dao.entity.Option;
import com.pvt.dao.entity.OptionGroup;
import com.pvt.dao.entity.Question;
import com.pvt.dao.entity.QuestionInputType;
import com.pvt.dao.entity.Routine;
import com.pvt.dao.entity.RoutineExercise;
import com.pvt.dao.entity.RoutineMeasurement;
import com.pvt.dao.entity.Survey;
import com.pvt.dao.repository.GenericRepository;
import com.pvt.dao.repository.MeasurementUnitRepository;
import com.pvt.dao.repository.QuestionRepository;
import com.pvt.init.DataCreator;
import com.pvt.util.Tuple;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static java.util.stream.StreamSupport.stream;

@Component
@Log4j
public class QuestionnariesDataCreator implements DataCreator {



    public static final String PLANNED_DESTINATION_WEIGHT_MEASUREMENT_UNIT = "PLANNED_DESTINATION_WEIGHT";

    public static final String COUNT_OF_TRAININS_PER_WEEK_MEASUREMENT_UNIT = "COUNT_OF_TRAININS_PER_WEEK";

    private static final String[] UNITS = {"WEIGHT", "HEIGHT", "TRAIN_TARGET"};


    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @PostConstruct
    @Override
    public void create() {
        log.info("Start creating questionnaires  data");
        createMeasurementUnits();
        createIntroductionSurveyQuestions();
        createFindProgramSurvey();
        createRoutines();

    }

    private void createRoutines() {
        //
        final Routine routine = new Routine();

        routine.setRoutineType(Routine.RoutineType.GYM);
        routine.setName("Программа набора мышечной массы");

        genericRepository.save(routine);

        final Routine routineCF = new Routine();
        routine.setRoutineType(Routine.RoutineType.GYM);
        routine.setName("Программа для похудения");

        genericRepository.save(routineCF);


        final Map<String,Tuple<String,String>> measurementMap =  ImmutableMap.of("WEIGHT", com.pvt.util.Tuple.create("100","200"),
                "HEIGHT", com.pvt.util.Tuple.create("150","200"),
                "TRAIN_TARGET", Tuple.create("Похудеть"));

        measurementMap.forEach((unit,value)->{
            final RoutineMeasurement routineMeasurement = new RoutineMeasurement();

            routineMeasurement.setMeasurementUnit(measurementUnitRepository.findByName(unit));
            routineMeasurement.setRoutine(routine);
            routineMeasurement.setValueStart(value.getFirst());
            routineMeasurement.setValueEnd(value.getSecond().orElse(value.getFirst()));
            routineMeasurement.setRoutine(routineCF);
        });

    }

    private void createMeasurementUnits() {
        of(UNITS).forEach(item -> {
            final MeasurementUnit unit = new MeasurementUnit();
            unit.setName(item);

            measurementUnitRepository.save(unit);
        });
    }

    private void createFindProgramSurvey() {
        final Survey survey = new Survey();

        survey.setName(Survey.INTRODUCTION_SURVEY);
        survey.setQuestions(stream(questionRepository
                .findAll()
                .spliterator(), false)
                .collect(Collectors.toList()));

        genericRepository.save(survey);
    }

    private void createIntroductionSurveyQuestions() {
        of("Привет.",
                "Я твой персональный тренер.",
                "Теперь твоя форма это моя забота.",
                "Мы с тобой составим индивидуальную для тебя программу и выведем твои тренировки на новый уровень.",
                "Немного обо мне.",
                "я имею способность к развитию и совершенствованию.",
                "Это значит что чем больше мы с тобой комуницируем, тем более индивидуальным я становлюсь.",
                "И так начнем",
                "Для того чтобы составить план наших с тобой занятий мне необходимо немного узнать о тебе")
                .forEach(introductionQuestion -> {
                    final Question question1 = new Question();
                    question1.setQuestion(introductionQuestion);
                    question1.setQuestionRequired(false);
                    question1.setAnswerRequired(false);
                    genericRepository.save(question1);
                });

        final Question heightQuestion = new Question();
        heightQuestion.setQuestion("1. Какой у тебя рост ?");
        heightQuestion.setMeasurementUnit(measurementUnitRepository.findByName("HEIGHT"));
        genericRepository.save(heightQuestion);
        final Question weightQuestion = new Question();
        weightQuestion.setQuestion("2. Какой у тебя вес ?");
        weightQuestion.setMeasurementUnit(measurementUnitRepository.findByName("WEIGHT"));
        genericRepository.save(weightQuestion);

        final Question trainTarget = new Question();
        trainTarget.setQuestion("3. Какая ваша цель занятия в зале");
        trainTarget.setMeasurementUnit(measurementUnitRepository.findByName("TRAIN_TARGET"));
        trainTarget.setQuestionInputType(QuestionInputType.SELECT);
        final OptionGroup trainTargetOptionGroup = new OptionGroup();
        genericRepository.save(trainTargetOptionGroup);
        trainTargetOptionGroup.setName("Targets");
        of("Набрать Вес", "Похудеть").forEach(og -> {
            final Option option = new Option();
            option.setOptionGroup(trainTargetOptionGroup);
            option.setValue(og);
            trainTargetOptionGroup.getOptions().add(option);
            genericRepository.save(option);
        });
        genericRepository.save(trainTargetOptionGroup);
        genericRepository.save(trainTarget);


    }

}
