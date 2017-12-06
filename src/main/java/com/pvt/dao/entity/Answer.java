package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity(name = "answer")
public class Answer extends BaseEntity<Long> {

    private Measurement measurement;

    private Question question;

    private ExternalUser user;

    private String answer;


}
