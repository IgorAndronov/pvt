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
@Table(name = "user_survey")
public class UserSurvey extends BaseEntity<Long> {


    public enum SurveyStatus {
        COMPLETED,
        IN_PROGRESS,
        NOT_STARTED;
    }


    @Column
    private SurveyStatus surveyStatus = SurveyStatus.NOT_STARTED;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private ExternalUser user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "SurveyAnswer",
            joinColumns = { @JoinColumn(name = "survey_id") },
            inverseJoinColumns = { @JoinColumn(name = "answer_id") }
    )
    private Collection<Answer> answers = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "survey_id")

    private Survey survey;

    @Column
    private Date startTime;

    @Column
    private Date endTime;

}
