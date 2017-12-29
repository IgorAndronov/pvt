package com.pvt.web.websocket.chat;


import com.google.gson.Gson;
import com.pvt.dao.entity.Question;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.Objects;

public class QuestionEncoder implements Encoder.Text<Question> {

    @Override
    public String encode(Question question) throws EncodeException {
        return new Gson().toJson(question);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
