package com.pvt.logic.service;


import com.pvt.config.AppConfig;
import com.pvt.dao.entity.Exercise;
import com.pvt.dao.entity.ExerciseGroup;
import com.pvt.dao.entity.Routine;
import com.pvt.dao.entity.RoutineExercise;
import com.pvt.dao.repository.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class RoutineServiceTest {

    @Autowired
    private QuestionRepository questionRepository;


    @Before
    public void setUp() {

        assertEquals(0, questionRepository.count());
        final Routine routine = new Routine();
        routine.setName("Mass gain program");
        routine.setName("Program for mass gaining");
        routine.setRoutineType(Routine.RoutineType.GYM);
        final ExerciseGroup category = new ExerciseGroup();
        category.setName("Chest");

        final Exercise ex1 = new Exercise();
        ex1.setName("Bench Press");
        ex1.setGroup(category);

        final Exercise ex2 = new Exercise();
        ex2.setGroup(category);
        ex2.setName("Pullover");

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


    }


    @Test
    public void testSearchRoutines() {

    }


}
