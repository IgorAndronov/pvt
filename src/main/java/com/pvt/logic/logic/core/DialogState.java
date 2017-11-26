package com.pvt.logic.logic.core;

/**
 * Created by admin on 14.05.2017.
 */
public class DialogState {

    Boolean canContinue;
    Boolean afterResume;

    int lastAnchorBeforeResume = 0;

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
