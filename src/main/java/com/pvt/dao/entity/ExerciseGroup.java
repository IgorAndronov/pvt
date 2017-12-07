package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@Entity
@Table(name="exercise_group")
public class ExerciseGroup extends BaseEntity<Long> {

    private String name;

    private String description;

    @OneToMany(fetch = FetchType.LAZY)
    private Collection<Exercise> exercises = new ArrayList<>();


}
