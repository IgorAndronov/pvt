package com.pvt.logic.logic.core;

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
