package com.pvt.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name="exercise")
public class Exercise extends BaseEntity<Long> {

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "exercise_group_id")
    private ExerciseGroup group;


}
