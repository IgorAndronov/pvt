package com.pvt.web.websocket.chat;


import com.pvt.dao.entity.Question;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.Objects;

public class QuestionEncoder implements Encoder.Text<Question> {

    @Override
    public String encode(Question question) throws EncodeException {
        return Json.createObjectBuilder()
                .add("id", Objects.toString(question.getId()))
                .add("text", question.getQuestion())
                .add("answerRequired",question.isAnswerRequired())
                .add("questionRequired",question.isQuestionRequired())
                .add("inputType",question.getQuestionInputType().name())
                .build().toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
