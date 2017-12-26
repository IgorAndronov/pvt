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
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "survey")
public class Survey extends BaseEntity<Long> {

    public static final String INTRODUCTION_SURVEY = "Introduction Survey";

    @Column
    private String name;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "SurveyQuestion",
            joinColumns = { @JoinColumn(name = "survey_id") },
            inverseJoinColumns = { @JoinColumn(name = "question_id") }
    )
    private Collection<Question> questions = new ArrayList<>();


}
