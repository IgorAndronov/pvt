package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Entity
@ToString
public class ExerciseGroup extends BaseEntity<Long> {

    private String name;

    private String description;

    private Collection<Exercise> exercises = new ArrayList<>();


}
