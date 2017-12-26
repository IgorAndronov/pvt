package com.pvt.logic.logic.core;

import com.pvt.dao.entity.Question;
import com.pvt.dao.interfaces.user.UserService;
import com.pvt.logic.service.QuestionarieService;
import com.pvt.web.websocket.chat.ChatAnnotation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.pvt.dao.entity.Question.createAnswerNotRequired;

/**
 * Created by admin on 13.05.2017.
 */

@Component(value = "CentralAI")
@Log4j
public class CentralAI {

    @Resource(name = "UserServiceImpl")
    private UserService userService;

    @Autowired
    private QuestionarieService questionarieService;


    public enum StateHolder {

        INSTANCE;
        private Map<String, DialogState> dialogStates = new ConcurrentHashMap<>();

        public DialogState getState(String userName){
            dialogStates.computeIfAbsent(userName,(k)->new DialogState(true,false));
            return dialogStates.get(userName);
        }

        public void setState(String userName,DialogState state){
            dialogStates.put(userName, state);
        }
    }



    public void doSomeWork() {
        final List<Question> questions = questionarieService.getIntroductionSurveyQuestions();

        try {
            ChatAnnotation connection = ChatAnnotation.getConnections().get("Fresher");
            if (connection == null) {
                return;
            }
            final DialogState state = StateHolder.INSTANCE.getState(connection.getNickname());

            for (int i = 0; i < questions.size(); i++) {
                Thread.currentThread().sleep(700);
                log.info("!!! continue");

                if (state.getAfterResume()) {
                    sendAndWait(createAnswerNotRequired("Вижу ты снова вернулся. "), connection);
                    sendAndWait(createAnswerNotRequired("Хорошо, тогда продолжим. "), connection);
                    sendAndWait(createAnswerNotRequired("Напомню, на чем мы остановились:"), connection);

                    int lastAnchorIndex = state.getLastAnchorBeforeResume();
                    i = lastAnchorIndex + 1;
                    state.setAfterResume(false);
                }
                final Question currentQuestion = questions.get(i);
                log.info("!!! next message = " + currentQuestion.getQuestion());


                addLastAnchor(connection.getNickname(), currentQuestion, i);
                sendAndWait(currentQuestion, connection);

            }


        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }

    }

    private void addLastAnchor(String nickname, Question currentQuestion, int index) {
        StateHolder.INSTANCE.getState(nickname).setLastAnchorBeforeResume(index);
        StateHolder.INSTANCE.getState(nickname).setCurrentQuestion(currentQuestion);

    }

    private void sendAndWait(Question question, ChatAnnotation connection) {
        System.out.println("sending..." + question.getQuestion());
        ChatAnnotation.broadcast(question);
        StateHolder.INSTANCE.getState(connection.getNickname()).setCanContinue(false);
        try {
            while (!StateHolder.INSTANCE.getState(connection.getNickname()).getCanContinue()) {
                Thread.currentThread().sleep(200);
                //  System.out.println(connection.getNickname() + " "+ dialogStates.get(connection.getNickname()).getCanContinue());
            }
        } catch (Exception e) {

        }

    }
}
