package com.pvt.dao.entity;


import com.pvt.util.MeasurementUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;


@Getter
@Setter
@ToString(exclude = {"children","parent"})
@Entity
@Table(name = "question")
public class Question extends BaseEntity<Long> {


    public static final Question createAnswerNotRequired(String text){
        final Question q = new Question();
        q.setQuestion(text);
        q.setAnswerRequired(false);

        return q;
    }
    @Column
    private QuestionInputType questionInputType = QuestionInputType.TEXT;

    @Column(name = "question")
    private String question;

    @Column
    private boolean questionRequired =  true;

    @Column
    private boolean answerRequired = true;


    @Column
    private boolean last = false;


    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Question parent;


    @OneToMany(fetch = FetchType.EAGER)
    private Collection<Question> children = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "measurement_unit_id")
    private MeasurementUnit measurementUnit;

    @ManyToOne
    @JoinColumn(name = "option_group_id", nullable = true)
    private OptionGroup optionGroup;




}
