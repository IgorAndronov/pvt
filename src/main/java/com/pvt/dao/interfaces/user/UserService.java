package com.pvt.dao.interfaces.user;


import com.pvt.web.LoginController;

/**
 * Created by igor on 01.08.15.
 */
public interface UserService {
    public boolean isUserCanLogin(String login, String password, String userType, LoginController loginController);
    public String getIntroMessage(String clientType);
}
