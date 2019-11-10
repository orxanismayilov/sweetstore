package model;

import dtos.UserDTO;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class UserSession {
    private static UserSession instance=null;
    private ClientConfig clientConfig;
    private UserDTO userDTO;

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance==null){
           instance =new UserSession();
        }
        return instance;
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
