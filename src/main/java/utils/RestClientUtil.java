package utils;

import model.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClientUtil {



    public static Response getSingleResource(String uri,String path,String username) {
        Client client = ClientBuilder.newClient();
        return client
                .target(uri)
                .queryParam("username",username)
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    public static Response getResourceList(String uri,String username){
        Client client = ClientBuilder.newClient();
        return client
                .target(uri)
                .queryParam("username",username)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    public static void updateResource(String uri, int id, Object resource,String username){
        Client client=ClientBuilder.newClient();
        client
                .target(uri)
                .path(String.valueOf(id))
                .queryParam("username",username)
                .request()
                .put(Entity.entity(resource,MediaType.APPLICATION_JSON));
    }

    public static Response addNewResource(Object resource,String uri,String username){
        Client client = ClientBuilder.newClient();
        return client
                .target(uri)
                .queryParam("username",username)
                .request(MediaType.APPLICATION_JSON)
                .post((Entity.entity(resource,MediaType.APPLICATION_JSON)));
    }

    public static Response deleteResource(String uri, int id,String username) {
        Client client = ClientBuilder.newClient();
        return client
                .target(uri)
                .path(String.valueOf(id))
                .queryParam("username",username)
                .request()
                .delete();
    }

    public static Response login(User user,String uri) {
        Client client = ClientBuilder.newClient();
        return client
                .target(uri)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user,MediaType.APPLICATION_JSON));
    }
}
