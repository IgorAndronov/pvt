package com.pvt.logic.logic.core;

import com.pvt.dao.entity.Answer;
import com.pvt.dao.entity.Measurement;
import com.pvt.dao.entity.Question;
import com.pvt.dao.entity.UserSurvey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DialogState {

    Boolean canContinue;

    Boolean afterResume;

    int lastAnchorBeforeResume = 0;

    private boolean isLast ;

    private Question currentQuestion;

    private UserSurvey userSurvey = new UserSurvey();

    public int getLastAnchorBeforeResume() {
        return lastAnchorBeforeResume;
    }

    public void setLastAnchorBeforeResume(int lastAnchorBeforeResume) {
        this.lastAnchorBeforeResume = lastAnchorBeforeResume;
    }

    public DialogState(Boolean canContinue, Boolean afterResume) {
        this.canContinue = canContinue;
        this.afterResume = afterResume;
    }

    public Boolean getCanContinue() {
        return canContinue;
    }


    public void addAnswer(String answer) {
        getUserSurvey().getAnswers().add(createAnswer(getCurrentQuestion(), null, answer));
    }

    private Answer createAnswer(Question question, String userName, String value) {
        final Answer answer = new Answer();

        answer.setAnswer(value);
        answer.setQuestion(question);
        final Measurement measurement = new Measurement();
        measurement.setMeasurementOperationType(Measurement.MeasurementOperationType.EQUALS);
        measurement.setMeasurementUnit(question.getMeasurementUnit());
        measurement.setValueStart(value);
        measurement.setValueEnd(value);
        answer.setMeasurement(measurement);

        return answer;
    }


    public void setCanContinue(Boolean canContinue) {
        this.canContinue = canContinue;
    }

    public Boolean getAfterResume() {
        return afterResume;
    }

    public void setAfterResume(Boolean afterResume) {
        this.afterResume = afterResume;
    }
}
