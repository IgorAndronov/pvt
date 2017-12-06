package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoutineExercise extends BaseEntity<Long>{

    private Routine routine;

    private Exercise exercise;

    private int exerciseNo;

    private int countOfSets;

    private int countOfRepsPerSet;

    private int intervalBetweenApproaches;



}
