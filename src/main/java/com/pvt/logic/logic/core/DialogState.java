package com.pvt.domain.logic.core;

/**
 * Created by admin on 14.05.2017.
 */
public class DialogState {

    Boolean canContinue;
    Boolean afterResume;

    public int getLastAnchorBeforeResume() {
        return lastAnchorBeforeResume;
    }

    public void setLastAnchorBeforeResume(int lastAnchorBeforeResume) {
        this.lastAnchorBeforeResume = lastAnchorBeforeResume;
    }

    int lastAnchorBeforeResume = 0;

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
