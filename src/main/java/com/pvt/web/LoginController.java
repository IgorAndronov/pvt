package com.pvt.web;


import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import static com.pvt.commons.Constants.*;
import com.pvt.dao.interfaces.user.UserService;


import static com.pvt.web.utils.WebConstants.*;

/**
 * Created by igor on 11.07.15.
 */

@Scope(value = "session")
@Controller
public class LoginController {
   private boolean loggedIn;
   private String nickName;
   private Integer userId;
   final static Logger logger = Logger.getLogger(LoginController.class);
   final static String ERROR_LOGIN_MSG_RU ="! Логин или пароль не верны.";
   final static String ERROR_LOGIN_MSG_EN ="! Login or password is incorrect.";

    @Resource(name = "UserServiceImpl")
    UserService userService;


    @RequestMapping(value = "/"+LOGIN_CONTROLLER, method = RequestMethod.GET)
    public String doLogin(){
        logger.info("do login");
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();
        logger.debug("!!! controller instance=" + this);
        logger.debug("!!! session=" + session);
        session.setAttribute(LOGIN_CONTROLLER, this);

   return "public/login";
    }

    @RequestMapping(value = "/"+LOGIN_CONTROLLER, method= RequestMethod.POST)
    public ModelAndView querrySubmit(@RequestParam Map<String,String> params, HttpServletRequest req) throws IOException {
        //getting login, password
        String login = params.get("login").toLowerCase();
        String password = params.get("password");

        logger.info(login + " is attempting to login");

        ModelAndView model = new ModelAndView();
        //check data and setup userId
        if(userService.isUserCanLogin(login, password, CLIENT, this)){
            model.setViewName("public/login_tmp");
            setLoggedIn(true);
            setNickName(login.substring(0,login.indexOf("@")) );
            logger.info("login of "+login +" is permitted. Nickname ="+getNickName());

        }else{
            model.setViewName("public/login");
            model.addObject("errorMsg", ERROR_LOGIN_MSG_RU);
            setLoggedIn(false);
            logger.info("login of " + login + " is not allowed");
        }

      return model;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
