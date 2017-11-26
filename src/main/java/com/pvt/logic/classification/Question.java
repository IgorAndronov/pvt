package com.pvt.logic.classification;

import java.util.Collection;

/**
 * Created by abe on 5/5/2017.
 */
public interface Question<QT extends QuestionType,AT extends AnswerType> {
    QT subject();
    Collection<AnswerOption<AT>> answerOptions();
    interface AnswerOption<AT> {
        AT detail();
    }
}
