package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name="routine")
public class Routine extends BaseEntity<Long> {

    public enum RoutineType {
        GYM,
        CROSSFIT,
    }

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private RoutineType routineType;

}
