package service.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.UserDTO;
import model.ResponseObject;
import model.User;
import model.UserSession;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
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
    private ObjectMapper mapper;

    public UserServiceImpl() {
        uriProperties= LoadPropertyUtil.loadPropertiesFile(URI_PROPERTIS);
        userSession=UserSession.getInstance();
        mapper=new ObjectMapper();

    }

    public boolean validateLogin(User user) {
        String uri=uriProperties.getProperty("useruri");
        ClientConfig clientConfig=createClienteConfig(user);
        Response response=RestClientUtil.login(clientConfig,uri);
        if (response.getStatus()==Response.Status.OK.getStatusCode()){
            userSession.setClientConfig(clientConfig);
            userSession.setUserDTO(mapUserDto(response));
            return true;
        }
       logger.info("Login failed.");
       return false;
    }

    private ClientConfig createClienteConfig(User user) {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
                .nonPreemptive()
                .credentials(user.getName(), user.getPassword())
                .build();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(feature) ;
        return clientConfig;
    }

    private UserDTO mapUserDto(Response response) {
        ResponseObject<UserDTO> responseObject=response.readEntity(ResponseObject.class);
        return mapper.convertValue(responseObject.getData(),new TypeReference<UserDTO>(){});
    }
}
