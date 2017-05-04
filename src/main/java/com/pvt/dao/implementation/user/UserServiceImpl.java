package com.pvt.dao.implementation.user;

import com.pvt.dao.interfaces.user.UserService;
import com.pvt.web.LoginController;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;



/**
 * Created by igor on 01.08.15.
 */
@Repository(value = "UserServiceImpl")
@Transactional
public class UserServiceImpl implements UserService {
    final static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Resource(name = "mySessionFactory")
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean isUserCanLogin(String login, String password, String userType, LoginController loginController) {
       return true;

    }
}
