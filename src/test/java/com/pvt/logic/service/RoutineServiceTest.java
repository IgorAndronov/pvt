package com.pvt.logic.service;


import com.pvt.config.AppConfig;
import com.pvt.dao.entity.Answer;
import com.pvt.dao.entity.Exercise;
import com.pvt.dao.entity.ExerciseGroup;
import com.pvt.dao.entity.ExternalUser;
import com.pvt.dao.entity.Measurement;
import com.pvt.dao.entity.MeasurementUnit;
import com.pvt.dao.entity.Question;
import com.pvt.dao.entity.QuestionInputType;
import com.pvt.dao.entity.Routine;
import com.pvt.dao.entity.RoutineExercise;
import com.pvt.dao.entity.RoutineMeasurement;
import com.pvt.dao.entity.Survey;
import com.pvt.dao.entity.UserSurvey;
import com.pvt.dao.repository.ExerciseRepository;
import com.pvt.dao.repository.ExternalUserRepository;
import com.pvt.dao.repository.GenericRepository;
import com.pvt.dao.repository.MeasurementUnitRepository;
import com.pvt.dao.repository.QuestionRepository;
import com.pvt.dao.repository.RoutineRepository;
import com.pvt.dao.repository.SurveyRepository;
import com.pvt.dao.repository.UserSurveyRepository;
import com.pvt.logic.model.SearchRoutineContext;
import com.pvt.logic.model.SearchRoutineResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class RoutineServiceTest {

    public static final String PLANNED_DESTINATION_WEIGHT_MEASUREMENT_UNIT = "PLANNED_DESTINATION_WEIGHT";

    public static final String COUNT_OF_TRAININS_PER_WEEK_MEASUREMENT_UNIT = "COUNT_OF_TRAININS_PER_WEEK";

    @Autowired
    private ExerciseRepository exerciseRepository;


    @Autowired
    private RoutineRepository routineRepository;


    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserSurveyRepository userSurveyRepository;


    @Autowired
    private ExternalUserRepository userRepository;


    @Autowired
    private RoutineSearchService routineSearchService;

    @Before
    public void setUp() {
        setUpRoutine();
        setUpQuestions();
        setUpUserSurwey();


    }

    private void setUpUserSurwey() {
        final ExternalUser user = new ExternalUser();
        user.setName("dude");

        genericRepository.save(user);

        final Survey survey = new Survey();
        final UserSurvey userSurvey = new UserSurvey();

        survey.setName("Basic questions survey");

        final List<Question> questions = StreamSupport.stream(questionRepository.findAll().spliterator(), false).collect(Collectors.toList());
        final Question weightQuestion = questions.stream().filter(q -> q.getMeasurementUnit().getName().equals(PLANNED_DESTINATION_WEIGHT_MEASUREMENT_UNIT)).findAny().get();
        final Question trainsPerWeekQuestion = questions.stream().filter(q -> q.getMeasurementUnit().getName().equals(COUNT_OF_TRAININS_PER_WEEK_MEASUREMENT_UNIT)).findAny().get();
        survey.setQuestions(questions);

        assertEquals(2, survey.getQuestions().size());

        final Answer answerToWeight = new Answer();

        answerToWeight.setAnswer("75");
        answerToWeight.setQuestion(weightQuestion);
        final Measurement measurement = new Measurement();
        measurement.setMeasurementUnit(weightQuestion.getMeasurementUnit());
        measurement.setValueEnd("75");
        measurement.setValueStart("75");
        answerToWeight.setMeasurement(measurement);

        genericRepository.save(answerToWeight);

        final Answer answerToCountOfTrainsPerWeek = new Answer();

        answerToCountOfTrainsPerWeek.setAnswer("3");
        answerToCountOfTrainsPerWeek.setQuestion(trainsPerWeekQuestion);
        final Measurement measurement2 = new Measurement();
        measurement2.setMeasurementUnit(trainsPerWeekQuestion.getMeasurementUnit());
        measurement2.setValueEnd("3");
        measurement2.setValueStart("3");
        answerToCountOfTrainsPerWeek.setMeasurement(measurement2);

        genericRepository.save(answerToCountOfTrainsPerWeek);



        genericRepository.save(survey);


        userSurvey.setUser(user);
        userSurvey.setSurvey(survey);


        userSurvey.getAnswers().add(answerToWeight);
        userSurvey.getAnswers().add(answerToCountOfTrainsPerWeek);

        genericRepository.save(userSurvey);
        assertEquals(2, userSurvey.getAnswers().size());


    }

    private void setUpQuestions() {
        final Question question1 = new Question();
        question1.setQuestion("How much do you want to weigh ?");
        question1.setMeasurementUnit(measurementUnitRepository.findByName(PLANNED_DESTINATION_WEIGHT_MEASUREMENT_UNIT));
        question1.setQuestionRequired(true);
        question1.setQuestionInputType(QuestionInputType.TEXT);
        genericRepository.save(question1);

        final Question question2 = new Question();
        question2.setQuestion("How many times do you plan to go train per week ?");
        question2.setMeasurementUnit(measurementUnitRepository.findByName(COUNT_OF_TRAININS_PER_WEEK_MEASUREMENT_UNIT));
        question2.setQuestionRequired(true);
        question2.setQuestionInputType(QuestionInputType.TEXT);

        genericRepository.save(question2);


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


    @Test
    public void testSearchRoutines() {
        final UserSurvey survey = userSurveyRepository.findByUser(userRepository.findByName("dude")).get(0);
        assertNotNull(survey);

        final SearchRoutineResult result = routineSearchService.search(SearchRoutineContext.of(survey));

        assertNotNull(result);

        assertEquals(1, result.getRoutines().size());


    }


}
