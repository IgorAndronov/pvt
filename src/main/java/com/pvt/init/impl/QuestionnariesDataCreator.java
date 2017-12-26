package com.pvt.init.impl;


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
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static java.util.stream.StreamSupport.stream;

@Component
@Log4j
public class QuestionnariesDataCreator implements DataCreator {


    public static final String PLANNED_DESTINATION_WEIGHT_MEASUREMENT_UNIT = "PLANNED_DESTINATION_WEIGHT";

    public static final String COUNT_OF_TRAININS_PER_WEEK_MEASUREMENT_UNIT = "COUNT_OF_TRAININS_PER_WEEK";


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

    }

    private void createMeasurementUnits() {
        of("WEIGHT", "HEIGHT", "TRAIN_TARGET").forEach(item -> {
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
                "Немного обо мне",
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
        genericRepository.save(weightQuestion);


    }

    private void setUpRoutine() {
        final Routine routine = new Routine();

        routine.setName("Mass gain program");
        routine.setName("Program for mass gaining");
        routine.setRoutineType(Routine.RoutineType.GYM);
        genericRepository.save(routine);


        final ExerciseGroup category = new ExerciseGroup();
        category.setName("Chest");

        genericRepository.save(category);

        final Exercise ex1 = new Exercise();
        ex1.setName("Bench Press");
        ex1.setGroup(category);
        genericRepository.save(ex1);

        final Exercise ex2 = new Exercise();
        ex2.setGroup(category);
        ex2.setName("Pullover");
        genericRepository.save(ex2);

        final RoutineExercise routineExercise1 = new RoutineExercise();
        routineExercise1.setCountOfSets(4);
        routineExercise1.setCountOfRepsPerSet(6);
        routineExercise1.setExercise(ex1);
        routineExercise1.setExerciseNo(1);
        routineExercise1.setIntervalBetweenApproaches(2); // 2 mins
        routineExercise1.setRoutine(routine);

        final RoutineExercise routineExercise2 = new RoutineExercise();
        routineExercise2.setCountOfSets(4);
        routineExercise2.setCountOfRepsPerSet(6);
        routineExercise2.setExercise(ex2);
        routineExercise2.setExerciseNo(2);
        routineExercise2.setIntervalBetweenApproaches(2); // 2 mins
        routineExercise2.setRoutine(routine);

        genericRepository.save(routineExercise1);
        genericRepository.save(routineExercise2);


        final MeasurementUnit measurementUnit = new MeasurementUnit();
        measurementUnit.setMeasurementUnitType(MeasurementUnit.MeasurementUnitType.SINGLE);
        measurementUnit.setName(PLANNED_DESTINATION_WEIGHT_MEASUREMENT_UNIT);
        genericRepository.save(measurementUnit);

        final MeasurementUnit measurementUnit2 = new MeasurementUnit();
        measurementUnit2.setMeasurementUnitType(MeasurementUnit.MeasurementUnitType.RANGE);
        measurementUnit2.setName(COUNT_OF_TRAININS_PER_WEEK_MEASUREMENT_UNIT);


        genericRepository.save(measurementUnit2);


        final RoutineMeasurement measurement1 = new RoutineMeasurement();
        measurement1.setRoutine(routine);
        measurement1.setMeasurementUnit(measurementUnit);
        measurement1.setValueStart("100");
        genericRepository.save(measurement1);

        final RoutineMeasurement measurement2 = new RoutineMeasurement();
        measurement2.setRoutine(routine);
        measurement2.setMeasurementUnit(measurementUnit2);
        measurement2.setValueStart("2");
        measurement2.setValueEnd("5");
        genericRepository.save(measurement2);
    }

}
