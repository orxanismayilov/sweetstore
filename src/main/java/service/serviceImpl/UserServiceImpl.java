package service.serviceImpl;

import model.ResponseObject;
import model.User;
import model.UserSession;
import org.apache.log4j.Logger;
import repository.UserDao;
import service.UserService;
import utils.LoadPropertyUtil;
import utils.RestClientUtil;

import java.util.Properties;

public class UserServiceImpl implements UserService {
    private Logger logger=Logger.getLogger(UserServiceImpl.class);
    private Properties uriProperties;
    private String URI_PROPERTIS="/resources/properties/resource-uri.properties";

    public UserServiceImpl() {
        uriProperties= LoadPropertyUtil.loadPropertiesFile(URI_PROPERTIS);
    }

    public boolean validateLogin(User user) {
        String uri=uriProperties.getProperty("useruri");
        ResponseObject<Boolean> responseObject=RestClientUtil.addNewResource(user,uri);
        Boolean check= (Boolean) responseObject.getData();
        if(check) {
           logger.info("validation user:"+user.toString());
           return true;
       }
       logger.info("Login failed.");
       return false;
    }
}
