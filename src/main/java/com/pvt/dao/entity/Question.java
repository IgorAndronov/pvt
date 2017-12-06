package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
public class Question extends BaseEntity<Long> {

    private QuestionInputType questionInputType = QuestionInputType.TEXT;

    private String questionName;

    private boolean questionRequired =  true;

    private boolean answerRequired = true;

    private Question relatedQuestion;

    private String asnwer;


}
