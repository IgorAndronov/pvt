package com.pvt.init.impl;


import com.pvt.dao.entity.Exercise;
import com.pvt.dao.entity.ExerciseGroup;
import com.pvt.dao.entity.Measurement;
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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static java.util.stream.StreamSupport.stream;

@Component
@Profile("!test")
@Log4j
public class QuestionnariesDataCreator implements DataCreator {


    private final GenericRepository genericRepository;


    private final MeasurementUnitRepository measurementUnitRepository;


    private final QuestionRepository questionRepository;


    @Autowired
    public QuestionnariesDataCreator(GenericRepository genericRepository, MeasurementUnitRepository measurementUnitRepository, QuestionRepository questionRepository) {
        this.genericRepository = genericRepository;
        this.measurementUnitRepository = measurementUnitRepository;
        this.questionRepository = questionRepository;
    }

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
        createMassGainRoutine();
        createLostWeightRoutine();

    }

    private void createLostWeightRoutine() {
        final Routine lostWeightRoutine = new Routine();

        lostWeightRoutine.setName("Программа тренировок для похудения");
        lostWeightRoutine.setRoutineType(Routine.RoutineType.GYM);
        genericRepository.save(lostWeightRoutine);
        final RoutineMeasurement trainTargetMeasurementRoutine = new RoutineMeasurement();
        trainTargetMeasurementRoutine.setMeasurementUnit(measurementUnitRepository.findByName("TRAIN_TARGET"));
        trainTargetMeasurementRoutine.setRoutine(lostWeightRoutine);
        trainTargetMeasurementRoutine.setMeasurementOperationType(Measurement.MeasurementOperationType.EQUALS);
        trainTargetMeasurementRoutine.setValueStart("Похудеть");
        genericRepository.save(trainTargetMeasurementRoutine);

        final RoutineMeasurement heightRoutineMeasurement = new RoutineMeasurement();
        heightRoutineMeasurement.setMeasurementUnit(measurementUnitRepository.findByName("HEIGHT"));
        heightRoutineMeasurement.setRoutine(lostWeightRoutine);
        heightRoutineMeasurement.setMeasurementOperationType(Measurement.MeasurementOperationType.GT);
        heightRoutineMeasurement.setValueStart("120");
        genericRepository.save(heightRoutineMeasurement);

        final RoutineMeasurement weightRoutineMeasurement = new RoutineMeasurement();
        weightRoutineMeasurement.setMeasurementUnit(measurementUnitRepository.findByName("WEIGHT"));
        weightRoutineMeasurement.setRoutine(lostWeightRoutine);
        weightRoutineMeasurement.setMeasurementOperationType(Measurement.MeasurementOperationType.EQUALS);
        weightRoutineMeasurement.setValueStart("90");
        weightRoutineMeasurement.setValueEnd("180");
        genericRepository.save(weightRoutineMeasurement);

    }

    private Routine createMassGainRoutine() {
        final Routine massGainRoutine = new Routine();

        massGainRoutine.setName("Программа набора мышечной массы");
        massGainRoutine.setRoutineType(Routine.RoutineType.GYM);
        genericRepository.save(massGainRoutine);


        final RoutineMeasurement trainTargetMeasurementRoutine = new RoutineMeasurement();
        trainTargetMeasurementRoutine.setMeasurementUnit(measurementUnitRepository.findByName("TRAIN_TARGET"));
        trainTargetMeasurementRoutine.setRoutine(massGainRoutine);
        trainTargetMeasurementRoutine.setMeasurementOperationType(Measurement.MeasurementOperationType.EQUALS);
        trainTargetMeasurementRoutine.setValueStart("Набрать Вес");
        genericRepository.save(trainTargetMeasurementRoutine);

        final RoutineMeasurement heightRoutineMeasurement = new RoutineMeasurement();
        heightRoutineMeasurement.setMeasurementUnit(measurementUnitRepository.findByName("HEIGHT"));
        heightRoutineMeasurement.setRoutine(massGainRoutine);
        heightRoutineMeasurement.setMeasurementOperationType(Measurement.MeasurementOperationType.GT);
        heightRoutineMeasurement.setValueStart("120");
        genericRepository.save(heightRoutineMeasurement);

        final RoutineMeasurement weightRoutineMeasurement = new RoutineMeasurement();
        weightRoutineMeasurement.setMeasurementUnit(measurementUnitRepository.findByName("WEIGHT"));
        weightRoutineMeasurement.setRoutine(massGainRoutine);
        weightRoutineMeasurement.setMeasurementOperationType(Measurement.MeasurementOperationType.EQUALS);
        weightRoutineMeasurement.setValueStart("65");
        weightRoutineMeasurement.setValueEnd("120");

        genericRepository.save(weightRoutineMeasurement);

        return massGainRoutine;
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
//        of("Привет.",
//                "Я твой персональный тренер.",
//                "Теперь твоя форма это моя забота.",
//                "Мы с тобой составим индивидуальную для тебя программу и выведем твои тренировки на новый уровень.",
//                "Немного обо мне",
//                "я имею способность к развитию и совершенствованию.",
//                "Это значит что чем больше мы с тобой комуницируем, тем более индивидуальным я становлюсь.",
//                "И так начнем",
//                "Для того чтобы составить план наших с тобой занятий мне необходимо немного узнать о тебе")
//                .forEach(introductionQuestion -> {
//                    final Question question1 = new Question();
//                    question1.setQuestion(introductionQuestion);
//                    question1.setQuestionRequired(false);
//                    question1.setAnswerRequired(false);
//                    genericRepository.save(question1);
//                });

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
//            option.setOptionGroup(trainTargetOptionGroup);
            option.setValue(og);
            trainTargetOptionGroup.getOptions().add(option);
            genericRepository.save(option);
        });
        trainTarget.setOptionGroup(trainTargetOptionGroup);
        genericRepository.save(trainTarget);
        genericRepository.save(trainTargetOptionGroup);
        genericRepository.save(weightQuestion);


    }

}
