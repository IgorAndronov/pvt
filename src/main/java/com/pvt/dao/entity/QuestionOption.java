package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Getter
@Setter
@ToString
public class QuestionOption extends BaseEntity<Long> {

    private Question question;

    private Option option;

}
