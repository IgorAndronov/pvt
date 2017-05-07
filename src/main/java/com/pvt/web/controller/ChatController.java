package com.pvt.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by admin on 07.05.2017.
 */

@Controller
public class ChatController {

    @RequestMapping(value = "/chat.html", method = RequestMethod.GET)
    public String getChatPage(){

        return "public/chat";

    }

    @RequestMapping(value = "/getPvtAnswer", method = RequestMethod.POST)
    public @ResponseBody
    String getPvtAnswer(@RequestBody String params, HttpServletRequest req){
        System.out.println("!!!"+params);
        return "{\"name\":\"hello\"}";

    }

}