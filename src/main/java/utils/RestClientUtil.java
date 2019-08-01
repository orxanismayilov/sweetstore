package utils;

import model.Product;
import model.ResponseObject;
import model.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClientUtil {



    public static Response getSingleResource(String uri, String path) {
        Client client = ClientBuilder.newClient();
        Response response=client
                .target(uri)
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .get();
        return response;
    }

    public static Response getResourceList(String uri){
        Client client = ClientBuilder.newClient();
        Response response=client
                .target(uri)
                .request(MediaType.APPLICATION_JSON)
                .get();
        return response;
    }

    public static void updateResource(String uri, int id, Object resource){
        Client client=ClientBuilder.newClient();
        client
                .target(uri)
                .path(String.valueOf(id))
                .request()
                .put(Entity.entity(resource,MediaType.APPLICATION_JSON));
    }

    public static Response addNewResource(Object resource,String uri){
        Client client = ClientBuilder.newClient();
        Response response=client
                .target(uri)
                .request(MediaType.APPLICATION_JSON)
                .post((Entity.entity(resource,MediaType.APPLICATION_JSON)));
        return response;
    }

    public static void deleteResource(String uri, int id) {
        Client client = ClientBuilder.newClient();
        client
                .target(uri)
                .path(String.valueOf(id))
                .request()
                .delete();
    }
}
