package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_routine")
public class UserRoutine extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ExternalUser user;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @Column
    private boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "UserRotineSelectedExercise",
            joinColumns = { @JoinColumn(name = "user_routine_id") },
            inverseJoinColumns = { @JoinColumn(name = "selected_exercise_id") }
    )
    private Collection<SelectedExercise> exercises = new ArrayList<>();
}
