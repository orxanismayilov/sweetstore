package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClientUtil {



    public static Response getSingleResource(String uri,String path,ClientConfig clientConfig) {
        Client client = ClientBuilder.newClient(clientConfig);
        return client
                .target(uri)
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    public static Response getResourceList(String uri,ClientConfig clientConfig){
        Client client = ClientBuilder.newClient(clientConfig);
        return client
                .target(uri)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    public static void updateResource(String uri, Object resource,ClientConfig clientConfig){
        Client client=ClientBuilder.newClient(clientConfig);
        client
                .target(uri)
                .request()
                .put(Entity.entity(resource,MediaType.APPLICATION_JSON));
    }

    public static Response addNewResource(Object resource,String uri,ClientConfig clientConfig){
        Client client = ClientBuilder.newClient(clientConfig);
        return client
                .target(uri)
                .request(MediaType.APPLICATION_JSON)
                .post((Entity.entity(resource,MediaType.APPLICATION_JSON)));
    }

    public static Response deleteResource(String uri, int id,ClientConfig clientConfig) {
        Client client = ClientBuilder.newClient(clientConfig);
        return client
                .target(uri)
                .path(String.valueOf(id))
                .request()
                .delete();
    }

    public static Response login(ClientConfig clientConfig,String uri) {
        Client client = ClientBuilder.newClient(clientConfig);
        return client
                .target(uri)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }
}
