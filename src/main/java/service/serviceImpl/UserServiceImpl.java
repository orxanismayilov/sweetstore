package service.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.ResponseObject;
import model.User;
import model.UserSession;
import org.apache.log4j.Logger;
import service.UserService;
import utils.LoadPropertyUtil;
import utils.RestClientUtil;

import javax.ws.rs.core.Response;
import java.util.Properties;

public class UserServiceImpl implements UserService {
    private Logger logger=Logger.getLogger(UserServiceImpl.class);
    private Properties uriProperties;
    private UserSession userSession;
    private String URI_PROPERTIS="/resources/properties/resource-uri.properties";

    public UserServiceImpl() {
        uriProperties= LoadPropertyUtil.loadPropertiesFile(URI_PROPERTIS);
        userSession=UserSession.getInstance();
    }

    public boolean validateLogin(User user) {
        String uri=uriProperties.getProperty("useruri")+"/login";
        Response response=RestClientUtil.login(user,uri);
        if (response.getStatus()==Response.Status.OK.getStatusCode()){
            ResponseObject responseObject=response.readEntity(ResponseObject.class);
            ObjectMapper mapper=new ObjectMapper();
            user= mapper.convertValue(responseObject.getData(),new TypeReference<User>(){});
            userSession.setUser(user);
            return true;
        }
       logger.info("Login failed.");
       return false;
    }
}
