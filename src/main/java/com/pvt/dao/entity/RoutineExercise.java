package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "routine_exercise")
public class RoutineExercise extends BaseEntity<Long>{


    @ManyToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column
    private int exerciseNo;

    @Column
    private int countOfSets;

    @Column
    private int countOfRepsPerSet;

    @Column
    private int intervalBetweenApproaches;



}
