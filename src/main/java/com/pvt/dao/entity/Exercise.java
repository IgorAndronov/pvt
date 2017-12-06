package com.pvt.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Getter
@Setter
@ToString
@Entity
public class Exercise extends BaseEntity<Long> {

    private String name;

    private String description;

    private ExerciseGroup group;


}
