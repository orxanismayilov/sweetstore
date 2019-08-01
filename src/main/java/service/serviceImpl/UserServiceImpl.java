package service.serviceImpl;

import model.ResponseObject;
import model.User;
import org.apache.log4j.Logger;
import service.UserService;
import utils.LoadPropertyUtil;
import utils.RestClientUtil;

import javax.ws.rs.core.Response;
import java.util.Properties;

public class UserServiceImpl implements UserService {
    private Logger logger=Logger.getLogger(UserServiceImpl.class);
    private Properties uriProperties;
    private String URI_PROPERTIS="/resources/properties/resource-uri.properties";

    public UserServiceImpl() {
        uriProperties= LoadPropertyUtil.loadPropertiesFile(URI_PROPERTIS);
    }

    public boolean validateLogin(User user) {
        String uri=uriProperties.getProperty("useruri")+"/login";
        Response response=RestClientUtil.addNewResource(user,uri);
        if (response.getStatus()==Response.Status.OK.getStatusCode()){
            ResponseObject<Boolean> responseObject=response.readEntity(ResponseObject.class);
            Boolean check= (Boolean) responseObject.getData();
            if(check) {
                logger.info("validation user:"+user.toString());
                return true;
            }
        }
       logger.info("Login failed.");
       return false;
    }
}
