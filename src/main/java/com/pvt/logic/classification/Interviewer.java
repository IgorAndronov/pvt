package com.pvt.logic.classification;

/**
 * Created by abe on 5/5/2017.
 */
public interface Interviewer {
    boolean hasMoreQuestions();
    Question provideQuestion();
    <QT extends QuestionType,AT extends AnswerType> void submitAnswer(Question<QT,AT> question, Question.AnswerOption<AT> answer);
    void skipQuestion(Question question);
    boolean isClassAssigned();
    AssignedClass assignedClass();
}
