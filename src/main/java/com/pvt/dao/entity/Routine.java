package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Getter
@Setter
@ToString
@Entity
public class Routine extends BaseEntity<Long> {

    public enum RoutineType {
        GYM,
        CROSSFIT,
    }

    private String name;

    private String description;

    private RoutineType routineType;

}
