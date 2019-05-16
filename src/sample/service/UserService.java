package sample.service;

import org.apache.log4j.Logger;
import sample.model.User;
import sample.repository.UserDao;

public class UserService {
    private UserDao userDao;
    private Logger logger=Logger.getLogger(UserService.class);

    public UserService(UserDao userDao) {
        this.userDao=userDao;
    }

    public boolean validateLogin(User user) {

       if(userDao.validateLogin(user)) {
           logger.info("validationg user:"+user.toString());
           return true;
       }
       logger.info("Login failed.");
       return false;
    }
}
