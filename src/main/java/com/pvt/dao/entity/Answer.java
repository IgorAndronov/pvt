package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Getter
@Setter
@Entity()
@Table(name="answer")
public class Answer extends BaseEntity<Long> {


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="measurement_id")
    private Measurement measurement;

    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name="user_id")
    private ExternalUser user;

    @Column
    private String answer;


}
