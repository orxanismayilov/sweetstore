package sample.service.serviceImpl;

import org.apache.log4j.Logger;
import sample.model.User;
import sample.repository.UserDao;
import sample.service.UserService;

public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private Logger logger=Logger.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserDao userDao) {
        this.userDao=userDao;
    }

    public boolean validateLogin(User user) {

       if(userDao.validateLogin(user)) {
           logger.info("validation user:"+user.toString());
           return true;
       }
       logger.info("Login failed.");
       return false;
    }
}