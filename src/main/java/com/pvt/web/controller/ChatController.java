package com.pvt.web.controller;

import com.pvt.dao.entity.Answer;
import com.pvt.dao.entity.Measurement;
import com.pvt.dao.entity.Question;
import com.pvt.logic.logic.core.CentralAI;
import com.pvt.logic.logic.core.DialogState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by admin on 07.05.2017.
 */

@RestController
public class ChatController {

    private static final String CHAT_PAGE = "public/chat";

    @RequestMapping(value = "/chat.html", method = RequestMethod.GET)
    public ModelAndView getChatPage(){

        ModelAndView model = new ModelAndView(CHAT_PAGE);

        model.addObject("nickname", "Fresher");

        return model;


    }

    @RequestMapping(value = "/getPvtAnswer", method = RequestMethod.POST)
    public @ResponseBody
    String getPvtAnswer(@RequestBody String params, HttpServletRequest req){
        final String userName = "Fresher";
        final DialogState state = CentralAI.StateHolder.INSTANCE.getState("Fresher");
        state.addAnswer(params);
        return "{\"name\":\"hello\"}";

    }


}
